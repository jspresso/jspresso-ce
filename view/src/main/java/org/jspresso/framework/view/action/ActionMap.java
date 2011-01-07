/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.view.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.util.gui.ERenderingOptions;

/**
 * An action map is used to structure a set of actions. The actions are not
 * directly registered but are placed into action lists that in turn are
 * assigned to the action map. Action maps can be merged together, i.e. an
 * action map can have multiple parent action maps. In that case, Action lists
 * that have the same name are merged together. The way action maps are rendered
 * depends on the place where they are declared. For instance an action map that
 * is assigned to a view might be rendered as a toolbar with each action list
 * separated into its own group. On the other hand, an action map declared on
 * the frontend controller might be represented as a menu bar on the main
 * application frame.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ActionMap {

  private List<ActionList>  actionLists;
  private List<ActionMap>   parentActionMaps;

  private ERenderingOptions renderingOptions;

  private static void completeActionMap(
      Map<String, ActionList> bufferActionMap, List<ActionList> actionLists) {
    if (actionLists != null) {
      Map<String, ActionList> mapOfActionLists = new LinkedHashMap<String, ActionList>();
      for (ActionList al : actionLists) {
        mapOfActionLists.put(al.getName(), al);
      }
      for (Map.Entry<String, ActionList> actionListEntry : mapOfActionLists
          .entrySet()) {
        ActionList bufferActionList = bufferActionMap.get(actionListEntry
            .getKey());
        if (bufferActionList == null) {
          bufferActionList = actionListEntry.getValue().clone();
          bufferActionMap.put(actionListEntry.getKey(), bufferActionList);
        } else {
          for (IDisplayableAction localAction : actionListEntry.getValue()
              .getActions()) {
            int existingIndex = bufferActionList.getActions().indexOf(
                localAction);
            if (existingIndex >= 0) {
              bufferActionList.getActions().set(existingIndex, localAction);
            } else {
              bufferActionList.getActions().add(localAction);
            }
          }
        }
      }
    }
  }

  /**
   * Gets the list of action sets composing the parent action maps with the
   * local one.
   * 
   * @return the actions list.
   */
  public List<ActionList> getActionLists() {
    Map<String, ActionList> buffer = new LinkedHashMap<String, ActionList>();
    if (parentActionMaps != null) {
      for (ActionMap parentActionMap : parentActionMaps) {
        completeActionMap(buffer, parentActionMap.getActionLists());
      }
    }
    if (actionLists != null) {
      completeActionMap(buffer, actionLists);
    }
    return new ArrayList<ActionList>(buffer.values());
  }

  /**
   * Gets the renderingOptions.
   * 
   * @return the renderingOptions.
   */
  public ERenderingOptions getRenderingOptions() {
    return renderingOptions;
  }

  /**
   * Assigns the action lists that are directly owned by this action map. The
   * action lists that will actually be rendered will be the merge of the
   * directly owned action lists and of the parent action maps. Action lists
   * that have the same name are merged together and into a merged action list,
   * local actions will replace parent actions with the same name.
   * 
   * @param actionLists
   *          the action lists list to set.
   */
  public void setActionLists(List<ActionList> actionLists) {
    this.actionLists = actionLists;
  }

  /**
   * Assigns the parent action maps. The action lists that will actually be
   * rendered will be the merge of the directly owned action lists and of the
   * parent action maps. Action lists that have the same name are merged
   * together and into a merged action list, local actions will replace parent
   * actions with the same name.
   * 
   * @param parentActionMaps
   *          the parentActionMaps to set.
   */
  public void setParentActionMaps(List<ActionMap> parentActionMaps) {
    this.parentActionMaps = parentActionMaps;
  }

  /**
   * Indicates how the actions should be rendered. This is either a value of the
   * <code>ERenderingOptions</code> enum or its equivalent string representation
   * :
   * <ul>
   * <li><code>LABEL_ICON</code> for label and icon</li>
   * <li><code>LABEL</code> for label only</li>
   * <li><code>ICON</code> for icon only.</li>
   * </ul>
   * <p>
   * Default value is <code>null</code>, i.e. determined from outside, e.g. the
   * view factory.
   * 
   * @param renderingOptions
   *          the renderingOptions to set.
   */
  public void setRenderingOptions(ERenderingOptions renderingOptions) {
    this.renderingOptions = renderingOptions;
  }
}
