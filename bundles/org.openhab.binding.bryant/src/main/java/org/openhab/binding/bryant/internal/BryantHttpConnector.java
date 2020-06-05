/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.bryant.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

/**
 * This class makes the connection to the thermostat and manages it.
 * It is also responsible for sending commands to the Infinitude proxy.
 * *
 *
 * @author Brad Lewis - Initial contribution
 */
public class BryantHttpConnector {

    private Logger logger = LoggerFactory.getLogger(BryantHttpConnector.class);

    private static final int REQUEST_TIMEOUT = 3; // 3 seconds

    private final String cmdUrl;

    private final String statusUrl;

    private final HttpClient httpClient;

    private ScheduledFuture<?> pollingJob;

    protected ScheduledExecutorService scheduler;
    protected BryantState state;
    protected BryantConfiguration config;

    public BryantHttpConnector(BryantConfiguration config, BryantState state, ScheduledExecutorService scheduler,
            HttpClient httpClient) {
        this.config = config;
        this.scheduler = scheduler;
        this.state = state;
        this.cmdUrl = String.format("http://%s:%d/api/status", config.getHost(), config.getPort());
        this.statusUrl = String.format("http://%s:%d/api/status", config.getHost(), config.getPort());
        this.httpClient = httpClient;
    }

    public BryantState getState() {
        return state;
    }

    /**
     * Set up the connection to the receiver by starting to poll the HTTP API.
     */
    public void connect() {
        if (!isPolling()) {
            logger.debug("HTTP polling started.");

            pollingJob = scheduler.scheduleWithFixedDelay(() -> {
                try {
                    refreshHttpProperties();
                } catch (IOException e) {
                    logger.debug("IO error while retrieving document", e);
                    state.connectionError("IO error while connecting to Thermostat: " + e.getMessage());
                    stopPolling();
                } catch (RuntimeException e) {
                    /**
                     * We need to catch this RuntimeException, as otherwise the polling stops.
                     * Log as error as it could be a user configuration error.
                     */
                    StringBuilder sb = new StringBuilder();
                    for (StackTraceElement s : e.getStackTrace()) {
                        sb.append(s.toString()).append("\n");
                    }
                    logger.error("Error while polling Http: \"{}\". Stacktrace: \n{}", e.getMessage(), sb.toString());
                }
            }, 0, config.httpPollingInterval, TimeUnit.SECONDS);

        }
    }

    private boolean isPolling() {
        return pollingJob != null && !pollingJob.isCancelled();
    }

    private void stopPolling() {
        if (isPolling()) {
            pollingJob.cancel(true);
            logger.debug("HTTP polling stopped.");
        }
    }

    /**
     * Shutdown the http client
     */
    public void dispose() {
        logger.debug("disposing connector");

        stopPolling();
    }

    protected void internalSendCommand(String command) {
        logger.debug("Sending command '{}'", command);
        if (StringUtils.isBlank(command)) {
            logger.warn("Trying to send empty command");
            return;
        }

        try {
            String url = cmdUrl + URLEncoder.encode(command, Charset.defaultCharset().displayName());
            logger.trace("Calling url {}", url);

            httpClient.newRequest(url).timeout(REQUEST_TIMEOUT, TimeUnit.SECONDS).send(new Response.CompleteListener() {
                @Override
                public void onComplete(Result result) {
                    if (result.getResponse().getStatus() != 200) {
                        logger.warn("Error {} while sending command", result.getResponse().getReason());
                    }
                }
            });

        } catch (UnsupportedEncodingException e) {
            logger.warn("Error sending command", e);
        }
    }

    private void refreshHttpProperties() throws IOException {
        logger.trace("Refreshing Thermostat status");

        ContentResponse response;

        try {
            String url = statusUrl;
            response = httpClient.newRequest(url).timeout(REQUEST_TIMEOUT, TimeUnit.SECONDS).send();
            if (response.getStatus() == HttpURLConnection.HTTP_OK) {

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(StringOrNull.class, new GsonDeserializers.StringOrNullDeserializer())
                        .create();
                ZoneStatus result = gson.fromJson(new JsonParser().parse(response.getContentAsString()),
                        ZoneStatus.class);

                state.setMode(result.getMode());
                state.setTemperature(result.getZones().get(config.getZone() - 1).getRt());
                state.setHumidity(result.getZones().get(config.getZone() - 1).getRh());
                state.setHeatSetPoint(result.getZones().get(config.getZone() - 1).getHtsp());
                state.setCoolSetPoint(result.getZones().get(config.getZone() - 1).getClsp());
                state.setActivity(result.getZones().get(config.getZone() - 1).getCurrentActivity());
                logger.debug("Thermostat Update Received.");

            }

        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            logger.debug("Unable to get temperature update.", e);

        }

    }
}
