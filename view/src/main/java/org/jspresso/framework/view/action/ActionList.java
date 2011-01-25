/*
 * Copyright (c) 2005-2007 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.gui.ERenderingOptions;

/**
 * An action list is collection of actions tha can be described with a name, a
 * description and an icon. Wether these informations are visually leveraged
 * depends on the place where the action list is used. For instance, an action
 * list used to creae a menu in a menu bar will be able to leverage its name and
 * icon for menu representation. If it is used to define a toolbar part, none of
 * them will be leveraged. The name of the action list is also used to identify
 * the sibling action lists to be merged when inheriting action map together.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ActionList extends DefaultIconDescriptor implements ISecurable {

  private List<IDisplayableAction> actions;
  private ERenderingOptions        renderingOptions;
  private boolean                  collapsable;
  private Collection<String>       grantedRoles;

  /**
   * Constructs a new <code>ActionList</code> instance.
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
        .setActions(new ArrayList<IDisplayableAction>(getActions()));
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
   * <code>ERenderingOptions</code> enum or its equivalent string representation
   * :
   * <ul>
   * <li><code>LABEL_ICON</code> for label and icon</li>
   * <li><code>LABEL</code> for label only</li>
   * <li><code>ICON</code> for icon only.</li>
   * </ul>
   * <p>
   * Default value is <code>null</code>, i.e. determined from outside, e.g. the
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
   * Configures the actionlist so that it can be collapsed by view factories.
   * Collapsable action lists can typically be rendered as combo buttons in UI
   * channels that support it.
   * <p>
   * Default value is <code>false</code>.
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
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * Assigns the roles that are authorized to use this action list. It supports
   * &quot;<b>!</b>&quot; prefix to negate the role(s). Whenever the user is not
   * granted sufficient privileges, the action list is simply not displayed at
   * runtime. Setting the collection of granted roles to <code>null</code>
   * (default value) disables role based authorization, then access is granted
   * to anyone.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = grantedRoles;
  }

}
