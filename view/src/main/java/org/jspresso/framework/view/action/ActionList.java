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
package org.jspresso.framework.view.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.gui.ERenderingOptions;

/**
 * An action list is collection of actions tha can be described with a name, a
 * description and an icon. Whether these information are visually leveraged
 * depends on the place where the action list is used. For instance, an action
 * list used to create a menu in a menu bar will be able to leverage its name and
 * icon for menu representation. If it is used to define a toolbar part, none of
 * them will be leveraged. The name of the action list is also used to identify
 * the sibling action lists to be merged when inheriting action map together.
 *
 * @author Vincent Vandenschrick
 */
public class ActionList extends DefaultIconDescriptor implements ISecurable,
    IPermIdSource {

  private List<IDisplayableAction> actions;
  private ERenderingOptions        renderingOptions;
  private boolean                  collapsable;
  private Collection<String>       grantedRoles;
  private String                   permId;

  /**
   * Constructs a new {@code ActionList} instance.
   */
  public ActionList() {
    collapsable = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionList clone() {
    ActionList clonedActionList = (ActionList) super.clone();
    clonedActionList
        .setActions(new ArrayList<>(getActions()));
    return clonedActionList;
  }

  /**
   * Gets the actions.
   *
   * @return the actions.
   */
  public List<IDisplayableAction> getActions() {
    return actions;
  }

  /**
   * Gets the renderingOptions.
   *
   * @return the renderingOptions.
   */
  public ERenderingOptions getRenderingOptions() {
    return renderingOptions;
  }

  /**
   * Assigns the list of actions owned by this action list.
   *
   * @param actions
   *          the actions to set.
   */
  public void setActions(List<IDisplayableAction> actions) {
    this.actions = actions;
  }

  /**
   * Indicates how the actions should be rendered. This is either a value of the
   * {@code ERenderingOptions} enum or its equivalent string representation
   * :
   * <ul>
   * <li>{@code LABEL_ICON} for label and icon</li>
   * <li>{@code LABEL} for label only</li>
   * <li>{@code ICON} for icon only.</li>
   * </ul>
   * <p>
   * Default value is {@code null}, i.e. determined from outside, e.g. the
   * view factory or the owning action map.
   *
   * @param renderingOptions
   *          the renderingOptions to set.
   */
  public void setRenderingOptions(ERenderingOptions renderingOptions) {
    this.renderingOptions = renderingOptions;
  }

  /**
   * Gets the collapsable.
   *
   * @return the collapsable.
   */
  public boolean isCollapsable() {
    return collapsable;
  }

  /**
   * Configures the action list so that it can be collapsed by view factories.
   * Collapsable action lists can typically be rendered as combo buttons in UI
   * channels that support it.
   * <p>
   * Default value is {@code false}.
   *
   * @param collapsable
   *          the collapsable to set.
   */
  public void setCollapsable(boolean collapsable) {
    this.collapsable = collapsable;
  }

  /**
   * Gets the grantedRoles.
   *
   * @return the grantedRoles.
   */
  @Override
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * Assigns the roles that are authorized to use this action list. It supports
   * &quot;<b>!</b>&quot; prefix to negate the role(s). Whenever the user is not
   * granted sufficient privileges, the action list is simply not displayed at
   * runtime. Setting the collection of granted roles to {@code null}
   * (default value) disables role based authorization, then access is granted
   * to anyone.
   *
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = grantedRoles;
  }

  /**
   * Gets the permId.
   *
   * @return the permId.
   */
  @Override
  public String getPermId() {
    return permId;
  }

  /**
   * Sets the permanent identifier to this application element. Permanent
   * identifiers are used by different framework parts, like dynamic security or
   * record/replay controllers to uniquely identify an application element.
   * Permanent identifiers are generated by the SJS build based on the element
   * id but must be explicitly set if Spring XML is used.
   *
   * @param permId
   *          the permId to set.
   */
  @Override
  public void setPermId(String permId) {
    this.permId = permId;
  }
}
