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

/**
 * The {@link BryantConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Brad Lewis - Initial contribution
 */
public class BryantConfiguration {

    /**
     * The hostname (or IP Address) of the Infinitude Proxy Server
     */
    public String host;

    /**
     * The HTTP port
     */
    public Integer port;

    /**
     * The Thermostat zone number
     */
    public Integer zone;

    /**
     * The interval to poll the Infinitude Server over HTTP for changes
     */
    public Integer httpPollingInterval;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getHttpPollingInterval() {
        return httpPollingInterval;
    }

    public void setHost(Integer httpPollingInterval) {
        this.httpPollingInterval = httpPollingInterval;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getZone() {
        return zone;
    }

    public void setZone(Integer zone) {
        this.zone = zone;
    }
}
