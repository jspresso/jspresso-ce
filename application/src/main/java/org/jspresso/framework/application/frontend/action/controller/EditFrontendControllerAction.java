/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.controller;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.std.EditComponentAction;

/**
 * This is a frontend action to display a view backed by the session backend
 * controller itself. It is used, for instance, to display the running
 * asynchronous actions.
 * 
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class EditFrontendControllerAction<E, F, G> extends
    EditComponentAction<E, F, G> {

  /**
   * Constructs a new {@code ChangePasswordAction} instance.
   */
  public EditFrontendControllerAction() {
    // Empty a of now.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    setActionParameter(getFrontendController(context), context);
    return super.execute(actionHandler, context);
  }
}
