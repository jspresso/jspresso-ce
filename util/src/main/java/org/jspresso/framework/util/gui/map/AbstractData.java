/*
 * Copyright (c) 2005-2018 Maxime Hamm. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.util.gui.map;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract data
 *
 * @author Maxime HAMM Date: 27/01/2018
 */
class AbstractData implements Serializable {

    private Map<String, Object> options;

    /**
     * The Id.
     */
    String id;

    /**
     * Gets options
     *
     * @return The options
     */
    public Map<String, Object> getOptions() {
        return options;
    }

    /**
     * Add option
     *
     * @param option The option key
     * @param object The option value
     * @see <a href="https://openlayers.org/en/latest/apidoc/ol.style.Icon.html">Open layers (point's icon)</a>
     * @see <a href="https://openlayers.org/en/latest/apidoc/ol.style.Stroke.html">Open layers (route's lines)</a>
     */
    public void addOption(String option, Object object) {
        if (options == null)
            options = new HashMap<>();

        options.put(option, object);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }
}
