/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend;

import java.util.Locale;

import com.d2s.framework.application.IController;
import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.util.descriptor.IIconDescriptor;

/**
 * General contract of frontend (view) application controllers.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IFrontendController extends IController, IIconDescriptor {

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
   */
  void start(IBackendController backendController, Locale locale);
}
