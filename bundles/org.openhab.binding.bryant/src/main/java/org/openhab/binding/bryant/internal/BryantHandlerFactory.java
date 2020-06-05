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

import static org.openhab.binding.bryant.internal.BryantBindingConstants.THING_TYPE_THERMOSTAT;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.eclipse.smarthome.io.net.http.HttpClientFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link BryantHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Brad Lewis - Initial contribution
 */
// @NonNullByDefault
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.bryant")
public class BryantHandlerFactory extends BaseThingHandlerFactory {

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_THERMOSTAT);
    private HttpClient httpClient;

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    /*
     * @Override
     * protected @Nullable ThingHandler createHandler(Thing thing) {
     * ThingTypeUID thingTypeUID = thing.getThingTypeUID();
     *
     * if (THING_TYPE_THERMOSTAT.equals(thingTypeUID)) {
     * return new BryantHandler(thing);
     * }
     *
     * return null;
     *
     * }
     */

    @Override
    protected ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_THERMOSTAT.equals(thingTypeUID)) {
            return new BryantHandler(thing, httpClient);
        }

        return null;
    }

    @Reference
    protected void setHttpClientFactory(HttpClientFactory httpClientFactory) {
        this.httpClient = httpClientFactory.getCommonHttpClient();
    }

    protected void unsetHttpClientFactory(HttpClientFactory httpClientFactory) {
        this.httpClient = null;
    }
}
