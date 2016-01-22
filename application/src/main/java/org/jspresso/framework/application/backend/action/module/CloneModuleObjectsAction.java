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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractCollectionAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.ILifecycleCapable;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityCloneFactory;

/**
 * This action clones the selected objects in the projected collection.
 *
 * @author Vincent Vandenschrick
 * @deprecated use CloneComponentCollectionAction
 */
@Deprecated
public class CloneModuleObjectsAction extends AbstractCollectionAction {

  private IEntityCloneFactory entityCloneFactory;

  /**
   * Clones the selected objects in the projected collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);

    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return false;
    }

    BeanCollectionModule module = (BeanCollectionModule) getModule(context);

    List<Object> projectedCollection;
    if (module.getModuleObjects() == null) {
      projectedCollection = new ArrayList<>();
    } else {
      projectedCollection = new ArrayList<>(module.getModuleObjects());
    }
    Collection<IComponent> entityClones = new ArrayList<>();
    for (int selectedIndice : selectedIndices) {
      Object component = collectionConnector.getChildConnector(
          selectedIndice).getConnectorValue();
      if (component instanceof IComponent) {
        IComponent clone;
        if (component instanceof IEntity) {
          clone = entityCloneFactory.cloneEntity((IEntity) component,
              getEntityFactory(context));
        } else {
          clone = entityCloneFactory.cloneComponent((IComponent) component,
              getEntityFactory(context));
        }
        if (clone instanceof ILifecycleCapable) {
          ((ILifecycleCapable) clone).onClone((IComponent) component);
        }
        entityClones.add(clone);
      }
    }
    projectedCollection.addAll(entityClones);
    module.setModuleObjects(projectedCollection);

    setSelectedModels(entityClones, context);

    return super.execute(actionHandler, context);
  }

  /**
   * Sets the entityCloneFactory.
   *
   * @param entityCloneFactory
   *          the entityCloneFactory to set.
   */
  public void setEntityCloneFactory(IEntityCloneFactory entityCloneFactory) {
    this.entityCloneFactory = entityCloneFactory;
  }
}
