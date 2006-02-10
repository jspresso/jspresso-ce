/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.action;

/**
 * well-known action context keys.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class ActionContextConstants {

  private ActionContextConstants() {
    // to prevent this class from being instanciated.
  }

  /**
   * The source widget.
   */
  public static final String SOURCE_COMPONENT                   = "SOURCE_COMPONENT";

  /**
   * The selected indices of the view connector in case of a collection
   * connector. It may serve as storage key for a return value whenever an
   * action must be chained with selection update on the view.
   */
  public static final String SELECTED_INDICES                   = "SELECTED_INDICES";

  /**
   * The the descriptor of the model domain object the action was triggered on.
   */
  public static final String MODEL_DESCRIPTOR                   = "MODEL_DESCRIPTOR";

  /**
   * The root connector of the application controller.
   */
  public static final String MODEL_CONNECTOR                    = "MODEL_CONNECTOR";

  /**
   * The locale the action has to use to execute.
   */
  public static final String LOCALE                             = "LOCALE";

  /**
   * The view connector the action was triggered on.
   */
  public static final String VIEW_CONNECTOR                     = "VIEW_CONNECTOR";

  /**
   * The module view connector the action was triggered on.
   */
  public static final String MODULE_VIEW_CONNECTOR          = "MODULE_VIEW_CONNECTOR";

  /**
   * The module model connector the action was triggered on.
   */
  public static final String MODULE_MODEL_CONNECTOR         = "MODULE_MODEL_CONNECTOR";

  /**
   * The selected indices in the parent module.
   */
  public static final String PARENT_MODULE_SELECTED_INDICES = "PARENT_MODULE_SELECTED_INDICES";

  /**
   * The parent module view connector the action was triggered on.
   */
  public static final String PARENT_MODULE_VIEW_CONNECTOR   = "PARENT_MODULE_VIEW_CONNECTOR";

  /**
   * The current application session.
   */
  public static final String APPLICATION_SESSION                = "APPLICATION_SESSION";

  /**
   * An arbitrary action result.
   */
  public static final String ACTION_RESULT                      = "ACTION_RESULT";

  /**
   * An arbitrary action param.
   */
  public static final String ACTION_PARAM                       = "ACTION_PARAM";

  /**
   * The view connector at the origin of the action chain.
   */
  public static final String SOURCE_VIEW_CONNECTOR              = "SOURCE_VIEW_CONNECTOR";
}
