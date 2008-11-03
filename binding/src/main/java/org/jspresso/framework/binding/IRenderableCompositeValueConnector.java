/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

/**
 * This is the interface implemented by displayable composite connectors.
 * (composite).
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IRenderableCompositeValueConnector extends
    ICompositeValueConnector {

  /**
   * Clones this connector.
   * 
   * @return the connector's clone.
   */
  IRenderableCompositeValueConnector clone();

  /**
   * Clones this connector.
   * 
   * @param newConnectorId
   *            the identifier of the clone connector
   * @return the connector's clone.
   */
  IRenderableCompositeValueConnector clone(String newConnectorId);

  /**
   * Gets the connector responsible for rendering the composite connector.
   * 
   * @return the rendering connector.
   */
  IValueConnector getRenderingConnector();
  
  /**
   * Gets the string used to display this connector.
   * 
   * @return the rendered string value.
   */
  String getDisplayValue();
  
  /**
   * Gets the icon used to display this connector.
   * 
   * @return the rendered icon image url.
   */
  String getDisplayIconImageUrl();

  /**
   * Gets the string used to display this connector description.
   * 
   * @return the rendered description.
   */
  String getDisplayDescription();
}
