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
package org.jspresso.framework.application.backend.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.ILifecycleCapable;
import org.jspresso.framework.model.datatransfer.ComponentTransferStructure;
import org.jspresso.framework.model.datatransfer.ETransferMode;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityCloneFactory;

/**
 * An action used in master/detail views to paste previously copied or cut
 * detail(s) to a master domain object. The application clipboard is used to
 * retrieve the entities (or components) to paste. Whenever the components have
 * been previously <i>copied</i> to the clipboard, the paste action will clone
 * them when executed using the configured entity clone factory. Whenever the
 * components have been previously <i>cut</i>, the paste action will simply use
 * the exact same instances as the one placed on the clipboard.
 *
 * @author Vincent Vandenschrick
 */
public class PasteCollectionToMasterAction extends
    AbstractAddCollectionToMasterAction {

  private IEntityCloneFactory entityCloneFactory;

  /**
   * Configures the entity clone factory to use when the paste action is
   * triggered after a copy.
   *
   * @param entityCloneFactory
   *          the entityCloneFactory to set.
   */
  public void setEntityCloneFactory(IEntityCloneFactory entityCloneFactory) {
    this.entityCloneFactory = entityCloneFactory;
  }

  /**
   * Gets the buffered entities from the backend controller.
   *
   * @param context
   *          the action context.
   * @return the entities to add to the collection.
   */
  @Override
  protected List<?> getAddedComponents(Map<String, Object> context) {
    ComponentTransferStructure<? extends IComponent> transferStructure = getController(
        context).retrieveComponents();
    if (transferStructure != null && transferStructure.getContent() != null) {
      List<Object> componentsToTransfer = new ArrayList<>();
      Class<?> allowedContract = getModelDescriptor(context)
          .getCollectionDescriptor().getElementDescriptor()
          .getComponentContract();
      for (Object comp : transferStructure.getContent()) {
        if (allowedContract.isAssignableFrom(comp.getClass())) {
          componentsToTransfer.add(comp);
        }
      }
      if (transferStructure.getTransferMode() == ETransferMode.COPY) {
        for (int i = 0; i < componentsToTransfer.size(); i++) {
          Object component = componentsToTransfer.get(i);
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
            componentsToTransfer.set(i, clone);
          }
        }
      }
      return componentsToTransfer;
    }
    return null;
  }
}
