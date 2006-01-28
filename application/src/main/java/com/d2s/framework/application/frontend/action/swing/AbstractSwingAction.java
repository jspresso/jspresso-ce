/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing;

import javax.swing.Icon;
import javax.swing.JComponent;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IViewFactory;

/**
 * This class serves as base class for swing actions. It provides accessors on
 * commonly used artifacts.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractSwingAction extends AbstractChainedAction {

  /**
   * Retrieves the widget this action was triggered from. It may serve to
   * determine the root window or dialog for instance. It uses a well-known
   * action context key which is :
   * <li> <code>ActionContextConstants.SOURCE_COMPONENT</code>.
   * 
   * @return the source widget this action was triggered from.
   */
  public JComponent getSourceComponent() {
    return (JComponent) getContext().get(
        ActionContextConstants.SOURCE_COMPONENT);
  }

  /**
   * Refines return type.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected IIconFactory<Icon> getIconFactory() {
    return (IIconFactory<Icon>) super.getIconFactory();
  }

  /**
   * Refines return type.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected IViewFactory<JComponent> getViewFactory() {
    return (IViewFactory<JComponent>) super.getViewFactory();
  }

}
