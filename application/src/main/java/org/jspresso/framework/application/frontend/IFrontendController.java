/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend;

import java.util.Locale;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.IController;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.util.descriptor.IIconDescriptor;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IActionable;


/**
 * General contract of frontend (view) application controllers.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the actual gui component type used.
 * @param <F>
 *            the actual icon type used.
 * @param <G>
 *            the actual action type used.
 */
public interface IFrontendController<E, F, G> extends IController,
    IIconDescriptor, IActionable {

  /**
   * Retrieves a map of help action lists to be presented on this view.
   * 
   * @return the map of action lists handled by this view.
   */
  ActionMap getHelpActions();

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

  /**
   * Gets the view factory used by this controller.
   * 
   * @return the view factory used by this controller.
   */
  IViewFactory<E, F, G> getViewFactory();

  /**
   * Starts the controller. This method performs any necessary initializations
   * (such as binding to the backend controller) and shows the initial view to
   * the user. The initial view is generally built from the root view
   * descriptor.
   * 
   * @param backendController
   *            the backend controller to bind to.
   * @param locale
   *            the locale this controller should use to create the views and
   *            execute actions.
   * @return true if the controller succesfully started.
   */
  boolean start(IBackendController backendController, Locale locale);
}
