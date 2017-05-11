/*
 * Copyright (c) 2005-2017 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.module;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.binding.CollectionConnectorHelper;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.IValueConnector;

/**
 * Navigates the module objects by selecting the next / previous element.
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 * @author Vincent Vandenschrick
 */
public class NavigateModuleObjectsAction<E, F, G> extends FrontendAction<E, F, G> {

  private int offset;

  /**
   * Execute boolean.
   *
   * @param actionHandler
   *     the action handler
   * @param context
   *     the context
   * @return the boolean
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    Module module = getModule(context);
    if (module instanceof BeanCollectionModule) {
      int moduleObjectsCount = ((BeanCollectionModule) module).getModuleObjects().size();
      if (moduleObjectsCount > 0) {
        IValueConnector moduleConnector = getController(context).getCurrentModuleView().getConnector();
        ICollectionConnector moduleCollectionConnector = CollectionConnectorHelper.extractMainCollectionConnector(
            moduleConnector);
        if (moduleCollectionConnector != null) {
          int[] selectedIndices = moduleCollectionConnector.getSelectedIndices();
          int selectedIndex = -1;
          if (selectedIndices == null || selectedIndices.length == 0) {
            if (offset > 0) {
              selectedIndex = 0;
            } else {
              selectedIndex = moduleObjectsCount - 1;
            }
          } else {
            selectedIndex = selectedIndices[0];
            selectedIndex += offset;
          }
          if (selectedIndex >= 0 && selectedIndex < moduleObjectsCount) {
            moduleCollectionConnector.setSelectedIndices(new int[]{selectedIndex}, selectedIndex);
          }
        }
      }
    }
    return true;
  }

  /**
   * Gets offset.
   *
   * @return the offset
   */
  public int getOffset() {
    return offset;
  }

  /**
   * Sets offset.
   *
   * @param offset
   *     the offset
   */
  public void setOffset(int offset) {
    this.offset = offset;
  }
}
