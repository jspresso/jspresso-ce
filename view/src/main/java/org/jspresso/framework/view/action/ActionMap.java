/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.ISecurityHandler;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.gui.ERenderingOptions;
import org.jspresso.framework.util.lang.ICloneable;

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
 * @author Vincent Vandenschrick
 */
public class ActionMap implements ISecurable, IPermIdSource, ICloneable {

  private List<ActionList>   actionLists;
  private List<ActionMap>    parentActionMaps;
  private Collection<String> grantedRoles;

  private ERenderingOptions  renderingOptions;

  private String             permId;

  private static void completeActionMap(
      Map<String, ActionList> bufferActionMap, List<ActionList> actionLists,
      ISecurityHandler securityHandler) {
    if (actionLists != null) {
      Map<String, ActionList> mapOfActionLists = new LinkedHashMap<>();
      for (ActionList al : actionLists) {
        if (al.getName() != null) {
          mapOfActionLists.put(al.getName(), al);
        } else {
          // to avoid duplicates
          mapOfActionLists.put(Integer.toHexString(al.hashCode()), al);
        }
      }
      for (Map.Entry<String, ActionList> actionListEntry : mapOfActionLists
          .entrySet()) {
        if (securityHandler.isAccessGranted(actionListEntry.getValue())) {
          try {
            securityHandler.pushToSecurityContext(actionListEntry.getValue());
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
          } finally {
            securityHandler.restoreLastSecurityContextSnapshot();
          }
        }
      }
    }
  }

  /**
   * Gets the list of action sets composing the parent action maps with the
   * local one.
   *
   * @param securityHandler
   *          the action handler used to handle role based security.
   * @return the actions list.
   */
  public List<ActionList> getActionLists(ISecurityHandler securityHandler) {
    Map<String, ActionList> buffer = new LinkedHashMap<>();
    if (parentActionMaps != null) {
      for (ActionMap parentActionMap : parentActionMaps) {
        if (securityHandler.isAccessGranted(parentActionMap)) {
          try {
            securityHandler.pushToSecurityContext(parentActionMap);
            completeActionMap(buffer,
                parentActionMap.getActionLists(securityHandler),
                securityHandler);
          } finally {
            securityHandler.restoreLastSecurityContextSnapshot();
          }
        }
      }
    }
    if (actionLists != null) {
      completeActionMap(buffer, actionLists, securityHandler);
    }
    return new ArrayList<>(buffer.values());
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
   * {@code ERenderingOptions} enum or its equivalent string representation
   * :
   * <ul>
   * <li>{@code LABEL_ICON} for label and icon</li>
   * <li>{@code LABEL} for label only</li>
   * <li>{@code ICON} for icon only.</li>
   * </ul>
   * <p>
   * Default value is {@code null}, i.e. determined from outside, e.g. the
   * view factory.
   *
   * @param renderingOptions
   *          the renderingOptions to set.
   */
  public void setRenderingOptions(ERenderingOptions renderingOptions) {
    this.renderingOptions = renderingOptions;
  }

  /**
   * Gets the grantedRoles.
   *
   * @return the grantedRoles.
   */
  @Override
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * Assigns the roles that are authorized to use this action map. It supports
   * &quot;<b>!</b>&quot; prefix to negate the role(s). Whenever the user is not
   * granted sufficient privileges, the action map is simply not displayed at
   * runtime. Setting the collection of granted roles to {@code null}
   * (default value) disables role based authorization, then access is granted
   * to anyone.
   *
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = grantedRoles;
  }

  /**
   * Gets the permId.
   *
   * @return the permId.
   */
  @Override
  public String getPermId() {
    return permId;
  }

  /**
   * Sets the permanent identifier to this application element. Permanent
   * identifiers are used by different framework parts, like dynamic security or
   * record/replay controllers to uniquely identify an application element.
   * Permanent identifiers are generated by the SJS build based on the element
   * id but must be explicitly set if Spring XML is used.
   *
   * @param permId
   *          the permId to set.
   */
  @Override
  public void setPermId(String permId) {
    this.permId = permId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionMap clone() {
    ActionMap clone = null;
    try {
      clone = (ActionMap) super.clone();
    } catch (CloneNotSupportedException e) {
      // Cannot happen;
    }
    return clone;
  }
}
