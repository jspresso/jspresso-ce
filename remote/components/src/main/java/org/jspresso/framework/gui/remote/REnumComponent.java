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
package org.jspresso.framework.gui.remote;

import java.util.Map;


/**
 * A component containing enumerated values.
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
public class REnumComponent extends RComponent {

  private static final long serialVersionUID = 4314490166693887426L;

  private Map<String, String> renderingTranslations;
  private Map<String, RIcon> renderingIcons;

  /**
   * Constructs a new <code>REnumComponent</code> instance.
   * 
   * @param guid the guid
   */
  public REnumComponent(String guid) {
    super(guid);
  }

  /**
   * Sets the renderingTranslations.
   * 
   * @param renderingTranslations
   *          the renderingTranslations to set.
   */
  public void setRenderingTranslations(Map<String, String> renderingTranslations) {
    this.renderingTranslations = renderingTranslations;
  }

  /**
   * Gets the renderingTranslations.
   * 
   * @return the renderingTranslations.
   */
  public Map<String, String> getRenderingTranslations() {
    return renderingTranslations;
  }

  /**
   * Sets the renderingIcons.
   * 
   * @param renderingIcons
   *          the renderingIcons to set.
   */
  public void setRenderingIcons(Map<String, RIcon> renderingIcons) {
    this.renderingIcons = renderingIcons;
  }

  /**
   * Gets the renderingIcons.
   * 
   * @return the renderingIcons.
   */
  public Map<String, RIcon> getRenderingIcons() {
    return renderingIcons;
  }

}
