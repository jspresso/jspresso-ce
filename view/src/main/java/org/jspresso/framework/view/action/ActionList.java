/*
 * Copyright (c) 2005-2007 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.action;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;


/**
 * A describeable list of actions.
 * <p>
 * Copyright (c) 2005-2007 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ActionList extends DefaultIconDescriptor {

  private List<IDisplayableAction> actions;

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
   * Sets the actions.
   * 
   * @param actions
   *            the actions to set.
   */
  public void setActions(List<IDisplayableAction> actions) {
    this.actions = actions;
  }

}
