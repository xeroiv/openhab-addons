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

import static org.openhab.binding.bryant.internal.BryantBindingConstants.*;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link BryantHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Brad Lewis - Initial contribution
 *
 */

public class BryantHandler extends BaseThingHandler implements BryantStateChangedListener {

    private final Logger logger = LoggerFactory.getLogger(BryantHandler.class);
    private HttpClient httpClient;
    private BryantHttpConnector connector;
    private ScheduledFuture<?> retryJob;
    private static final int RETRY_TIME_SECONDS = 30;
    private BryantConnectorFactory connectorFactory = new BryantConnectorFactory();
    private BryantState bryantState;
    private @Nullable BryantConfiguration config;

    public BryantHandler(Thing thing, HttpClient httpClient) {
        super(thing);
        this.httpClient = httpClient;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

        if (connector == null) {
            return;
        }

        if (connector instanceof BryantHttpConnector && command instanceof RefreshType) {
            // Refreshing individual channels isn't supported by the Http connector.
            // The connector refreshes all channels together at the configured polling interval.
            return;
        }

        if (CHANNEL_TEMPERATURE.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            }
        }
        if (CHANNEL_HUMIDITY.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            }
        }
        if (CHANNEL_HEAT_SET_POINT.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            }
        }
        if (CHANNEL_COOL_SET_POINT.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            }
        }
        if (CHANNEL_MODE.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            }
        }
        if (CHANNEL_ACTIVITY.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            }
        }
    }

    @Override
    public void initialize() {

        cancelRetry();
        config = getConfigAs(BryantConfiguration.class);

        if (!checkConfiguration()) {
            return;
        }

        bryantState = new BryantState(this);
        updateStatus(ThingStatus.UNKNOWN);

        createConnection();
        logger.debug("Finished initializing!");

    }

    public boolean checkConfiguration() {
        // prevent too low values for polling interval
        if (config.httpPollingInterval < 5) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "The polling interval should be at least 5 seconds!");
            return false;
        }

        return true;
    }

    private void cancelRetry() {
        ScheduledFuture<?> localRetryJob = retryJob;
        if (localRetryJob != null && !localRetryJob.isDone()) {
            localRetryJob.cancel(false);
        }
    }

    private void createConnection() {
        if (connector != null) {
            connector.dispose();
        }
        connector = connectorFactory.getConnector(config, bryantState, scheduler, httpClient);
        connector.connect();
    }

    @Override
    public void stateChanged(String channelID, State state) {
        logger.debug("Received state {} for channelID {}", state, channelID);

        // Don't flood the log with thing 'updated: ONLINE' each time a single channel changed
        if (this.getThing().getStatus() != ThingStatus.ONLINE) {
            updateStatus(ThingStatus.ONLINE);
        }
        updateState(channelID, state);
    }

    @Override
    public void connectionError(String errorMessage) {
        if (this.getThing().getStatus() != ThingStatus.OFFLINE) {
            // Don't flood the log with thing 'updated: OFFLINE' when already offline
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, errorMessage);
        }
        connector.dispose();
        retryJob = scheduler.schedule(this::createConnection, RETRY_TIME_SECONDS, TimeUnit.SECONDS);
    }

    /*
     * // Example for background initialization:
     * scheduler.execute(() -> {
     *
     * The isTelnet parameter has no default.
     * When not set we will try to auto-detect the correct values
     * for isTelnet and zoneCount and update the Thing accordingly.
     *
     * boolean thingReachable = false; // <background task with long running initialization here>
     * logger.debug("Trying to auto-detect the connection.");
     * ContentResponse response;
     *
     * try {
     * // response = httpClient.newRequest("http://" + host + ":" + String.valueOf(httpPort) + "/api/status/")
     * // .timeout(3, TimeUnit.SECONDS).send();
     * response = httpClient
     * .newRequest(String.format("http://%s:%d/api/status/", config.getHost(), config.getPort()))
     * .timeout(3, TimeUnit.SECONDS).send();
     *
     * if (response.getStatus() == HttpURLConnection.HTTP_OK) {
     * logger.debug("We can access the HTTP API, disabling the Telnet mode by default.");
     * thingReachable = true; // <background task with long running initialization here>
     * }
     * } catch (InterruptedException | TimeoutException | ExecutionException e) {
     * logger.debug("Error when trying to access AVR using HTTP on port 80, reverting to Telnet mode.", e);
     *
     * }
     *
     * // when done do:
     * if (thingReachable) {
     * updateStatus(ThingStatus.ONLINE);
     * createConnection();
     * } else {
     * updateStatus(ThingStatus.OFFLINE);
     * }
     *
     * });
     */

}
