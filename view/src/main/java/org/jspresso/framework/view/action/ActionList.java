/*
 * Copyright (c) 2005-2007 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.action;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.gui.ERenderingOptions;

/**
 * A describeable list of actions.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ActionList extends DefaultIconDescriptor {

  private List<IDisplayableAction> actions;
  private ERenderingOptions        renderingOptions;

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
   * Sets the actions.
   * 
   * @param actions
   *          the actions to set.
   */
  public void setActions(List<IDisplayableAction> actions) {
    this.actions = actions;
  }

  /**
   * Sets the renderingOptions.
   * 
   * @param renderingOptions
   *          the renderingOptions to set.
   */
  public void setRenderingOptions(ERenderingOptions renderingOptions) {
    this.renderingOptions = renderingOptions;
  }

}
