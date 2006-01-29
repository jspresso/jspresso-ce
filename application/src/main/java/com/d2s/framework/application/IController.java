/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application;

import java.util.Map;

import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.view.action.IActionHandler;

/**
 * This interface is implemented by the controllers of the application.
 * Controllers implement the interface since their main role is to execute
 * application actions.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IController extends IActionHandler {

  /**
   * Retrieves the initial action context from the controller. This context is
   * passed to the action chain and contains application-wide context key-value
   * pairs.
   * 
   * @return the map representing the initial context provided by this
   *         controller.
   */
  Map<String, Object> getInitialActionContext();

  /**
   * Merges an entity in the application session.
   * 
   * @param entity
   *          the entity to merge.
   * @param mergeMode
   *          the merge mmode to be used.
   * @return the merged entity.
   */
  IEntity merge(IEntity entity, MergeMode mergeMode);
}
