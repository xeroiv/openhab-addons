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

import java.math.BigDecimal;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.smarthome.core.library.types.DecimalType;

/**
 * Abstract class containing common functionality for the connectors.
 *
 * @author Brad Lewis - Initial contribution
 */
public abstract class BryantConnector {

    private static final BigDecimal POINTFIVE = new BigDecimal("0.5");
    protected ScheduledExecutorService scheduler;
    protected BryantState state;
    protected BryantConfiguration config;

    public abstract void connect();

    public abstract void dispose();

    protected abstract void internalSendCommand(String command);

    protected String toDenonValue(DecimalType number) {
        String dbString = String.valueOf(number.intValue());
        BigDecimal num = number.toBigDecimal();
        if (num.compareTo(BigDecimal.TEN) == -1) {
            dbString = "0" + dbString;
        }
        if (num.remainder(BigDecimal.ONE).equals(POINTFIVE)) {
            dbString = dbString + "5";
        }
        return dbString;
    }
}
