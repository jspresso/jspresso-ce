/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a hierachical structure for holding action maps. An
 * action map is a map of action sets keyed by their grouping goal (like 'EDIT',
 * 'VIEW', ...).
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ActionMap {

  private Map<String, List<IDisplayableAction>> actionMap;
  private List<ActionMap>                       parentActionMaps;

  private static void completeActionMap(
      Map<String, List<IDisplayableAction>> globalActionMap,
      Map<String, List<IDisplayableAction>> localActionMap) {
    if (localActionMap != null) {
      for (Map.Entry<String, List<IDisplayableAction>> actionListEntry : localActionMap
          .entrySet()) {
        List<IDisplayableAction> globalActionList = globalActionMap
            .get(actionListEntry.getKey());
        if (globalActionList == null) {
          globalActionList = new ArrayList<IDisplayableAction>();
          globalActionMap.put(actionListEntry.getKey(), globalActionList);
        }
        for (IDisplayableAction localAction : actionListEntry.getValue()) {
          int existingIndex = globalActionList.indexOf(localAction);
          if (existingIndex >= 0) {
            globalActionList.set(existingIndex, localAction);
          } else {
            globalActionList.add(localAction);
          }
        }
      }
    }
  }

  /**
   * Gets the map of action sets composing the parent actionmaps with the local
   * one.
   * 
   * @return the actionMap.
   */
  public Map<String, List<IDisplayableAction>> getActionMap() {
    Map<String, List<IDisplayableAction>> returnedActionMap = new LinkedHashMap<String, List<IDisplayableAction>>();
    if (parentActionMaps != null) {
      for (ActionMap parentActionMap : parentActionMaps) {
        completeActionMap(returnedActionMap, parentActionMap.getActionMap());
      }
    }
    if (actionMap != null) {
      completeActionMap(returnedActionMap, actionMap);
    }
    return returnedActionMap;
  }

  /**
   * Sets the actionMap.
   * 
   * @param actionMap
   *            the actionMap to set.
   */
  public void setActionMap(Map<String, List<IDisplayableAction>> actionMap) {
    this.actionMap = actionMap;
  }

  /**
   * Sets the parentActionMaps.
   * 
   * @param parentActionMaps
   *            the parentActionMaps to set.
   */
  public void setParentActionMaps(List<ActionMap> parentActionMaps) {
    this.parentActionMaps = parentActionMaps;
  }

}
