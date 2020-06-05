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

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * This class deserializes the Infinitude status json response.
 * *
 *
 * @author Brad Lewis - Initial contribution
 *
 */

class GsonDeserializers {
    static class StringOrNullDeserializer implements JsonDeserializer<StringOrNull> {
        @Override
        public StringOrNull deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json.isJsonPrimitive()) {
                return new StringOrNull(json.getAsString());
            }
            if (json.isJsonArray()) {
                JsonElement element = ((JsonArray) json).get(0);
                if (element.isJsonPrimitive()) {
                    return new StringOrNull(element.getAsString());
                }
            }
            return new StringOrNull(null);
        }
    }
}

class ZoneStatus {
    private StringOrNull mode;
    private List<ZonesWrapper> zones;

    public String getMode() {
        return mode.getValue();
    }

    public void setMode(StringOrNull mode) {
        this.mode = mode;
    }

    public List<Zone> getZones() {
        return zones.get(0).getZone();
    }
}

class ZonesWrapper {
    private List<Zone> zone;

    public List<Zone> getZone() {
        return zone;
    }

    public void setZone(List<Zone> zone) {
        this.zone = zone;
    }
}

class Zone {
    private StringOrNull rt;
    private StringOrNull rh;
    private StringOrNull clsp;
    private StringOrNull htsp;
    private StringOrNull currentActivity;

    public String getRt() {
        return rt.getValue();
    }

    public void setRt(StringOrNull rt) {
        this.rt = rt;
    }

    public String getRh() {
        return rh.getValue();
    }

    public void setRh(StringOrNull rh) {
        this.rh = rh;
    }

    public String getClsp() {
        return clsp.getValue();
    }

    public void setClsp(StringOrNull clsp) {
        this.clsp = clsp;
    }

    public String getHtsp() {
        return htsp.getValue();
    }

    public void setHtsp(StringOrNull htsp) {
        this.htsp = htsp;
    }

    public String getCurrentActivity() {
        return currentActivity.getValue();
    }

    public void setCurrentActivity(StringOrNull currentActivity) {
        this.currentActivity = currentActivity;
    }
}

class StringOrNull {
    private String value;

    public StringOrNull(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
