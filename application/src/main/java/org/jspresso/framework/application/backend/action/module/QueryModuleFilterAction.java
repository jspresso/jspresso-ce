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
package org.jspresso.framework.application.backend.action.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.backend.action.AbstractQbeAction;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.entity.IEntity;

/**
 * Retrieves the filter of a module and queries the persistent store to populate
 * the module objects. The actual query is delegated to another backend action
 * (defaulted to {@code QueryEntitiesAction}) that can be configured
 * through the {@code queryAction} property.
 *
 * @author Vincent Vandenschrick
 */
public class QueryModuleFilterAction extends AbstractQbeAction {

  /**
   * Retrieves the query component out of the context.
   *
   * @param context
   *          the action context.
   * @return the query component.
   */
  @Override
  protected IQueryComponent getQueryComponent(Map<String, Object> context) {
    FilterableBeanCollectionModule module = (FilterableBeanCollectionModule) getModule(context);
    return module.getFilter();
  }

  /**
   * Extracts the result list out of the module.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected List<? extends IComponent> getExistingResultList(
      IQueryComponent queryComponent, Map<String, Object> context) {
    FilterableBeanCollectionModule module = (FilterableBeanCollectionModule) getModule(context);
    return (List<? extends IComponent>) module.getModuleObjects();
  }

  /**
   * Assigns the result list to the module.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void queryPerformed(IQueryComponent queryComponent,
      Map<String, Object> context) {
    FilterableBeanCollectionModule module = (FilterableBeanCollectionModule) getModule(context);
    List<?> currentModuleObjects = module.getModuleObjects();
    List<Object> targetModuleObjects = new ArrayList<>(
        queryComponent.getQueriedComponents());
    // We need to preserve transient entities from being lost.
    if (currentModuleObjects != null) {
      for (Object comp : currentModuleObjects) {
        if (comp instanceof IEntity && !((IEntity) comp).isPersistent()
            && !targetModuleObjects.contains(comp)) {
          targetModuleObjects.add(comp);
        }
      }
    }
    module.setModuleObjects(targetModuleObjects);
    //queryComponent.setQueriedComponents(null);
  }
}
