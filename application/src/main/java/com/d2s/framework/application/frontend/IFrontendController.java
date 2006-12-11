/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend;

import java.util.Locale;

import com.d2s.framework.action.IAction;
import com.d2s.framework.application.IController;
import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.binding.IMvcBinder;
import com.d2s.framework.util.descriptor.IIconDescriptor;
import com.d2s.framework.view.IViewFactory;
import com.d2s.framework.view.action.IActionable;

/**
 * General contract of frontend (view) application controllers.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public interface IFrontendController<E, F, G> extends IController,
    IIconDescriptor, IActionable {

  /**
   * Starts the controller. This method performs any necessary initializations
   * (such as binding to the backend controller) and shows the initial view to
   * the user. The initial view is generally built from the root view
   * descriptor.
   *
   * @param backendController
   *          the backend controller to bind to.
   * @param locale
   *          the local this controller should use to create the views and
   *          execute actions.
   * @return true if the controller succesfully started.
   */
  boolean start(IBackendController backendController, Locale locale);

  /**
   * Gets the view factory used by this controller.
   *
   * @return the view factory used by this controller.
   */
  IViewFactory<E, F, G> getViewFactory();

  /**
   * Gets the mvc binder used by this controller.
   *
   * @return the mvc binder used by this controller.
   */
  IMvcBinder getMvcBinder();

  /**
   * Gets the action which is executed when the controller is started.
   *
   * @return the action which is executed when the controller is started.
   */
  IAction getStartupAction();
}
