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

import java.util.List;
import java.util.Map;

/**
 * This class is the generic server peer of a client GUI component.
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
public class RComponent {

  private String                  name;
  private String                  description;
  private RIcon                   icon;
  private List<RAction>           actions;
  private Map<String, String>     renderingTranslations;
  private Map<String, RIcon>      renderingIcons;

  /**
   * Sets the name.
   * 
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the name.
   * 
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the description.
   * 
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the description.
   * 
   * @return the description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the actions.
   * 
   * @param actions
   *          the actions to set.
   */
  public void setActions(List<RAction> actions) {
    this.actions = actions;
  }

  /**
   * Gets the actions.
   * 
   * @return the actions.
   */
  public List<RAction> getActions() {
    return actions;
  }

  /**
   * Sets the icon.
   * 
   * @param icon
   *          the icon to set.
   */
  public void setIcon(RIcon icon) {
    this.icon = icon;
  }

  /**
   * Gets the icon.
   * 
   * @return the icon.
   */
  public RIcon getIcon() {
    return icon;
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
