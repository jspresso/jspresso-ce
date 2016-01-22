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
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.util.gui.ERenderingOptions;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IActionViewDescriptor;

/**
 * This type of view allows to make an action available as a view and thus
 * participate in the UI composition as a visual component. An action view can
 * then be embedded in surrounding a composite view. It literally takes the
 * action away from the toolbar/context menu it is located when registered in an
 * action map and makes it a primary citizen of the UI.
 *
 * @author Vincent Vandenschrick
 */
public class BasicActionViewDescriptor extends BasicViewDescriptor implements
    IActionViewDescriptor {

  private IDisplayableAction action;
  private ERenderingOptions  renderingOptions = ERenderingOptions.ICON;

  /**
   * {@inheritDoc}
   */
  @Override
  public IDisplayableAction getAction() {
    return action;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ERenderingOptions getRenderingOptions() {
    return renderingOptions;
  }

  /**
   * Assigns the action to display as a view. The action will typically be
   * rendered as a button in the UI. whenever you want to size the icon used to
   * display the action (and thus the button peer), you might use the
   * {@code preferredWidth} / {@code preferredHeight} properties.
   *
   * @param action
   *          the action to set.
   */
  public void setAction(IDisplayableAction action) {
    this.action = action;
  }

  /**
   * Indicates how the action should be rendered. This is either a value of the
   * {@code ERenderingOptions} enum or its equivalent string representation
   * :
   * <ul>
   * <li>{@code LABEL_ICON} for label and icon</li>
   * <li>{@code LABEL} for label only</li>
   * <li>{@code ICON} for icon only.</li>
   * </ul>
   * <p>
   * Default value is {@code ERenderingOptions.ICON}, i.e. icon only.
   *
   * @param renderingOptions
   *          the renderingOptions to set.
   */
  public void setRenderingOptions(ERenderingOptions renderingOptions) {
    this.renderingOptions = renderingOptions;
  }

}
