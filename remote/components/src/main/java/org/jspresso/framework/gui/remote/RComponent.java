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

import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.remote.RemotePeer;

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
public abstract class RComponent extends RemotePeer {

  private String           label;
  private String           tooltip;
  private String           background;
  private String           borderType;
  private String           font;
  private String           foreground;
  private RIcon            icon;
  private RAction[]    actions;
  private RemoteValueState state;

  /**
   * Constructs a new <code>RComponent</code> instance.
   * 
   * @param guid
   *          the guid.
   */
  public RComponent(String guid) {
    super(guid);
  }

  /**
   * Sets the label.
   * 
   * @param label
   *          the label to set.
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Gets the name.
   * 
   * @return the name.
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the tooltip.
   * 
   * @param tooltip
   *          the tooltip to set.
   */
  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }

  /**
   * Gets the description.
   * 
   * @return the description.
   */
  public String getTooltip() {
    return tooltip;
  }

  /**
   * Sets the actions.
   * 
   * @param actions
   *          the actions to set.
   */
  public void setActions(RAction[] actions) {
    this.actions = actions;
  }

  /**
   * Gets the actions.
   * 
   * @return the actions.
   */
  public RAction[] getActions() {
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
   * Gets the state.
   * 
   * @return the state.
   */
  public RemoteValueState getState() {
    return state;
  }

  /**
   * Sets the state.
   * 
   * @param state
   *          the state to set.
   */
  public void setState(RemoteValueState state) {
    this.state = state;
  }

  
  /**
   * Gets the background.
   * 
   * @return the background.
   */
  public String getBackground() {
    return background;
  }

  
  /**
   * Sets the background.
   * 
   * @param background the background to set.
   */
  public void setBackground(String background) {
    this.background = background;
  }

  
  /**
   * Gets the borderType.
   * 
   * @return the borderType.
   */
  public String getBorderType() {
    return borderType;
  }

  
  /**
   * Sets the borderType.
   * 
   * @param borderType the borderType to set.
   */
  public void setBorderType(String borderType) {
    this.borderType = borderType;
  }

  
  /**
   * Gets the font.
   * 
   * @return the font.
   */
  public String getFont() {
    return font;
  }

  
  /**
   * Sets the font.
   * 
   * @param font the font to set.
   */
  public void setFont(String font) {
    this.font = font;
  }

  
  /**
   * Gets the foreground.
   * 
   * @return the foreground.
   */
  public String getForeground() {
    return foreground;
  }

  
  /**
   * Sets the foreground.
   * 
   * @param foreground the foreground to set.
   */
  public void setForeground(String foreground) {
    this.foreground = foreground;
  }
}
