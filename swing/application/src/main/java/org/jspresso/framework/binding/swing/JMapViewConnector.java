/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.swing;

import com.bbn.openmap.MapBean;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import org.jspresso.framework.binding.ConnectorBindingException;
import org.jspresso.framework.binding.basic.BasicCompositeConnector;
import org.jspresso.framework.binding.basic.BasicValueConnector;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.view.descriptor.IMapViewDescriptor;

/**
 * JMapViewConnector connector.
 *
 * @author Vincent Vandenschrick
 */
public class JMapViewConnector extends BasicCompositeConnector {

  private BasicValueConnector mapContentConnector;

  /**
   * Constructs a new {@code JMapViewConnector} instance.
   *
   * @param id
   *     the id of the connector.
   * @param mapView
   *     the connected MapBean.
   * @param mapContentId
   *     the map content id
   */
  public JMapViewConnector(String id, MapBean mapView, String mapContentId) {
    super(id);
    mapContentConnector = new BasicValueConnector(mapContentId);
    addChildConnector(mapContentConnector);

    bindMapView(mapView);
  }

  private void bindMapView(final MapBean mapView) {
    IValueChangeListener longLatValueChangeListener = new IValueChangeListener() {
      @Override
      public void valueChange(ValueChangeEvent evt) {
        String mapContent = mapContentConnector.getConnectorValue();
        if (mapContent != null) {
          try {
            JSONObject mapContentAsJson = new JSONObject(mapContent);
            JSONArray markers = mapContentAsJson.optJSONArray(IMapViewDescriptor.MARKERS_KEY);
            if (markers != null && markers.length() > 0) {
              JSONArray marker = markers.getJSONArray(0);
              mapView.setCenter(marker.getDouble(1), marker.getDouble(0));
              mapView.setVisible(true);
            } else {
              mapView.setVisible(false);
            }
          } catch (JSONException e) {
            throw new ConnectorBindingException(e, "Invalid Json map content");
          }
        } else {
          mapView.setVisible(false);
        }
      }
    };
    mapContentConnector.addValueChangeListener(longLatValueChangeListener);
  }
}
