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
package org.jspresso.framework.binding;

import org.jspresso.framework.util.gui.Icon;

/**
 * This is the interface implemented by displayable composite connectors.
 * (composite).
 *
 * @author Vincent Vandenschrick
 */
public interface IRenderableCompositeValueConnector extends
    ICompositeValueConnector {

  /**
   * Clones this connector.
   *
   * @return the connector's clone.
   */
  @Override
  IRenderableCompositeValueConnector clone();

  /**
   * Clones this connector.
   *
   * @param newConnectorId
   *          the identifier of the clone connector
   * @return the connector's clone.
   */
  @Override
  IRenderableCompositeValueConnector clone(String newConnectorId);

  /**
   * Gets the string used to display this connector description.
   *
   * @return the rendered description.
   */
  String getDisplayDescription();

  /**
   * Gets the icon used to display this connector.
   *
   * @return the rendered icon image url.
   */
  Icon getDisplayIcon();

  /**
   * Gets the string used to display this connector.
   *
   * @return the rendered string value.
   */
  String getDisplayValue();

  /**
   * Gets the connector responsible for rendering the composite connector.
   *
   * @return the rendering connector.
   */
  IValueConnector getRenderingConnector();
}
