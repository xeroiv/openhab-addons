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

import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jetty.client.HttpClient;

/**
 * Returns the connector based on the configuration.
 * Currently there is 1 types: HTTP
 *
 * @author Brad Lewis - Initial contribution
 */
public class BryantConnectorFactory {

    public @NonNull BryantHttpConnector getConnector(BryantConfiguration config, BryantState state,
            ScheduledExecutorService scheduler, HttpClient httpClient) {

        return new BryantHttpConnector(config, state, scheduler, httpClient);

    }
}
