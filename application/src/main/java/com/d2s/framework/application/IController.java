/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application;

import java.util.Locale;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.model.entity.IEntity;

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
   * Merges an entity in the application session.
   * 
   * @param entity
   *          the entity to merge.
   * @param mergeMode
   *          the merge mmode to be used.
   * @return the merged entity.
   */
  IEntity merge(IEntity entity, MergeMode mergeMode);

  /**
   * Stops the controller. This method performs any necessary cleanup.
   * 
   * @return true if the stop was successful.
   */
  boolean stop();

  /**
   * Gets the current controller locale.
   * 
   * @return the current controller locale.
   */
  Locale getLocale();
}
