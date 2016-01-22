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

import org.jspresso.framework.binding.basic.BasicCompositeConnector;
import org.jspresso.framework.binding.basic.BasicValueConnector;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;

/**
 * JMapViewConnector connector.
 *
 * @author Vincent Vandenschrick
 */
public class JMapViewConnector extends BasicCompositeConnector {

  private BasicValueConnector longitudeConnector;
  private BasicValueConnector latitudeConnector;

  /**
   * Constructs a new {@code JMapViewConnector} instance.
   *
   * @param id           the id of the connector.
   * @param mapView           the connected MapBean.
   * @param longitudeId the longitude connector id
   * @param latitudeId the latitude connector id
   */
  public JMapViewConnector(String id, MapBean mapView, String longitudeId, String latitudeId) {
    super(id);
    longitudeConnector = new BasicValueConnector(longitudeId);
    latitudeConnector = new BasicValueConnector(latitudeId);
    addChildConnector(longitudeConnector);
    addChildConnector(latitudeConnector);

    bindMapView(mapView);
  }

  private void bindMapView(final MapBean mapView) {
    IValueChangeListener longLatValueChangeListener = new IValueChangeListener() {
      @Override
      public void valueChange(ValueChangeEvent evt) {
        Double latitude = latitudeConnector.getConnectorValue();
        Double longitude = longitudeConnector.getConnectorValue();
        if (longitude != null && latitude != null) {
          mapView.setCenter(latitude, longitude);
        }
      }
    };
    longitudeConnector.addValueChangeListener(longLatValueChangeListener);
    latitudeConnector.addValueChangeListener(longLatValueChangeListener);
  }
}
