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

import org.eclipse.smarthome.core.types.State;

/**
 * Interface to notify the {@link BryantHandler} about state changes.
 *
 * @author Brad Lewis - Initial contribution
 *
 */
public interface BryantStateChangedListener {
    /**
     * Update was received.
     *
     * @param channelID the channel for which its state changed
     * @param state the new state of the channel
     */
    void stateChanged(String channelID, State state);

    /**
     * A connection error occurred
     *
     * @param errorMessage the error message
     */
    void connectionError(String errorMessage);
}
