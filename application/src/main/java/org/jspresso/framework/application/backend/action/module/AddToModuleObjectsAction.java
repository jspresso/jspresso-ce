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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractCollectionAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;

/**
 * This action adds a new object in the projected collection.
 *
 * @author Vincent Vandenschrick
 * @deprecated use AddComponentCollectionToMasterAction instead
 */
@Deprecated
public class AddToModuleObjectsAction extends AbstractCollectionAction {

  /**
   * Adds a new object in the projected collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    BeanCollectionModule module = (BeanCollectionModule) getModule(context);

    List<Object> projectedCollection;
    if (module.getModuleObjects() == null) {
      projectedCollection = new ArrayList<>();
    } else {
      projectedCollection = new ArrayList<>(module.getModuleObjects());
    }

    Object newModuleObject = createNewModuleObject(actionHandler, context);
    projectedCollection.add(newModuleObject);
    module.setModuleObjects(projectedCollection);

    setSelectedModels(Collections.singleton(newModuleObject), context);
    return super.execute(actionHandler, context);
  }

  /**
   * Creates a new entity to add to the projected object collection.
   *
   * @param actionHandler
   *          the action handler (generally the controller).
   * @param context
   *          the action context.
   * @return the created entity.
   */
  @SuppressWarnings({"unchecked", "UnusedParameters"})
  protected Object createNewModuleObject(IActionHandler actionHandler,
      Map<String, Object> context) {
    IComponentDescriptor<? extends IEntity> projectedDesc = ((ICollectionDescriptorProvider<IEntity>) getModelDescriptor(context))
        .getCollectionDescriptor().getElementDescriptor();

    Class<? extends IEntity> componentContract = projectedDesc.getComponentContract();
    return getEntityFactory(context).createEntityInstance(
        componentContract);
  }
}
