/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.action;

/**
 * An actionable object.
 * 
 * @author Vincent Vandenschrick
 */
public interface IActionable {

  /**
   * Retrieves a map of action lists to be presented on this view. Actions
   * should be grouped based on their kind (for instance a list of edit actions,
   * a list of business actions...) and will be presented accordingly. For
   * instance, each action list will be presented in a different menu in a menu
   * bar, will be separated from the others by a separator in a toolbar, ...
   * 
   * @return the map of action lists handled by this view.
   */
  ActionMap getActionMap();

  /**
   * Retrieves a secondary map of action lists to be presented on this view.
   * Actions in this map should be visually distinguished from the main action
   * map, e.g. placed in another toolbar.
   * 
   * @return the secondary map of action lists handled by this view.
   */
  ActionMap getSecondaryActionMap();
}
