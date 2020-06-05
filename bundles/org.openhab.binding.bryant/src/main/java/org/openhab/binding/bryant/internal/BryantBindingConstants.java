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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link BryantBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Brad Lewis - Initial contribution
 */
@NonNullByDefault
public class BryantBindingConstants {

    private static final String BINDING_ID = "bryant";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_THERMOSTAT = new ThingTypeUID(BINDING_ID, "thermostat");

    // List of all Channel ids
    public static final String CHANNEL_TEMPERATURE = "temperature";
    public static final String CHANNEL_HUMIDITY = "humidity";
    public static final String CHANNEL_HEAT_SET_POINT = "heatsetpoint";
    public static final String CHANNEL_COOL_SET_POINT = "coolsetpoint";
    public static final String CHANNEL_MODE = "mode";
    public static final String CHANNEL_ACTIVITY = "activity";
}
