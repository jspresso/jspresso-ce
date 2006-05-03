/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view;

import java.util.Locale;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.view.action.IDisplayableAction;

/**
 * A factory for actions.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual action class created.
 * @param <F>
 *          the actual component class the created actions are installed in.
 */
public interface IActionFactory<E, F> {

  /**
   * <code>TOOLTIP_ELLIPSIS</code> is "...".
   */
  String TOOLTIP_ELLIPSIS = "...";

  /**
   * Creates an action from its descriptor.
   * 
   * @param action
   *          the action descriptor.
   * @param actionHandler
   *          the handler responsible for executing the action.
   * @param view
   *          the view which the action is attached to.
   * @param locale
   *          the locale the action has to use.
   * @return the constructed action.
   */
  E createAction(IDisplayableAction action, IActionHandler actionHandler,
      IView<F> view, Locale locale);

  /**
   * Creates an action from its descriptor.
   * 
   * @param action
   *          the action descriptor.
   * @param actionHandler
   *          the handler responsible for executing the action.
   * @param sourceComponent
   *          the view component which the action is attached to.
   * @param modelDescriptor
   *          the model descriptor this action is triggered on.
   * @param viewConnector
   *          the view connector this action is created on.
   * @param locale
   *          the locale the action has to use.
   * @return the constructed action.
   */
  E createAction(IDisplayableAction action, IActionHandler actionHandler,
      F sourceComponent, IModelDescriptor modelDescriptor,
      IValueConnector viewConnector, Locale locale);
}
