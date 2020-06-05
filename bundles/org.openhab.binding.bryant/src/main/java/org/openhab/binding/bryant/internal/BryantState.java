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

import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.types.State;

/**
 * Represents the state of the handled Bryant Thermostat
 *
 * @author Brad Lewis - Initial contribution
 *
 */
public class BryantState {

    private State temperature;
    private State humidity;
    private State heatSetPoint;
    private State coolSetPoint;
    private State mode;
    private State activity;

    private BryantStateChangedListener handler;

    public BryantState(BryantStateChangedListener handler) {
        this.handler = handler;
    }

    public void connectionError(String errorMessage) {
        handler.connectionError(errorMessage);
    }

    public State getStateForChannelID(String channelID) {
        switch (channelID) {
            case BryantBindingConstants.CHANNEL_TEMPERATURE:
                return temperature;
            case BryantBindingConstants.CHANNEL_HUMIDITY:
                return humidity;
            case BryantBindingConstants.CHANNEL_HEAT_SET_POINT:
                return heatSetPoint;
            case BryantBindingConstants.CHANNEL_COOL_SET_POINT:
                return coolSetPoint;
            case BryantBindingConstants.CHANNEL_MODE:
                return mode;
            case BryantBindingConstants.CHANNEL_ACTIVITY:
                return activity;

            default:
                return null;
        }
    }

    public void setTemperature(String temperature) {
        StringType newVal = StringType.valueOf(temperature);
        if (!newVal.equals(this.temperature)) {
            this.temperature = newVal;
            handler.stateChanged(BryantBindingConstants.CHANNEL_TEMPERATURE, this.temperature);
        }
    }

    public void setHumidity(String humidity) {
        StringType newVal = StringType.valueOf(humidity);
        if (!newVal.equals(this.humidity)) {
            this.humidity = newVal;
            handler.stateChanged(BryantBindingConstants.CHANNEL_HUMIDITY, this.humidity);
        }
    }

    public void setHeatSetPoint(String heatSetPoint) {
        StringType newVal = StringType.valueOf(heatSetPoint);
        if (!newVal.equals(this.heatSetPoint)) {
            this.heatSetPoint = newVal;
            handler.stateChanged(BryantBindingConstants.CHANNEL_HEAT_SET_POINT, this.heatSetPoint);
        }
    }

    public void setCoolSetPoint(String coolSetPoint) {
        StringType newVal = StringType.valueOf(coolSetPoint);
        if (!newVal.equals(this.coolSetPoint)) {
            this.coolSetPoint = newVal;
            handler.stateChanged(BryantBindingConstants.CHANNEL_COOL_SET_POINT, this.coolSetPoint);
        }
    }

    public void setMode(String mode) {
        StringType newVal = StringType.valueOf(mode);
        if (!newVal.equals(this.mode)) {
            this.mode = newVal;
            handler.stateChanged(BryantBindingConstants.CHANNEL_MODE, this.mode);
        }
    }

    public void setActivity(String activity) {
        StringType newVal = StringType.valueOf(activity);
        if (!newVal.equals(this.activity)) {
            this.activity = newVal;
            handler.stateChanged(BryantBindingConstants.CHANNEL_ACTIVITY, this.activity);
        }
    }

}
