/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.datatransfer.ComponentTransferStructure;
import org.jspresso.framework.model.datatransfer.ETransferMode;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityCloneFactory;


/**
 * An action used in master/detail views to paste previously copy or cut detail
 * to a master domain object.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class PasteCollectionToMasterAction extends
    AbstractAddCollectionToMasterAction {

  private IEntityCloneFactory entityCloneFactory;

  /**
   * Sets the entityCloneFactory.
   * 
   * @param entityCloneFactory
   *            the entityCloneFactory to set.
   */
  public void setEntityCloneFactory(IEntityCloneFactory entityCloneFactory) {
    this.entityCloneFactory = entityCloneFactory;
  }

  /**
   * Gets the buffered entities from the backend controller.
   * 
   * @param context
   *            the action context.
   * @return the entities to add to the collection.
   */
  @Override
  protected List<?> getAddedComponents(Map<String, Object> context) {
    ComponentTransferStructure<? extends IComponent> transferStructure = getController(
        context).retrieveComponents();
    if (transferStructure != null && transferStructure.getContent() != null) {
      List<Object> componentsToTransfer;
      if (((Class<?>) getModelDescriptor(context).getCollectionDescriptor()
          .getElementDescriptor().getComponentContract())
          .isAssignableFrom(transferStructure.getComponentDescriptor()
              .getComponentContract())) {
        if (transferStructure.getContent() instanceof Collection<?>) {
          componentsToTransfer = new ArrayList<Object>(
              (Collection<?>) transferStructure.getContent());
        } else {
          componentsToTransfer = Collections.singletonList(transferStructure
              .getContent());
        }
        if (transferStructure.getTransferMode() == ETransferMode.COPY) {
          for (int i = 0; i < componentsToTransfer.size(); i++) {
            Object component = componentsToTransfer.get(i);
            if (component instanceof IEntity) {
              componentsToTransfer.set(i, entityCloneFactory.cloneEntity(
                  (IEntity) component, getEntityFactory(context)));
            }
          }
        }
        return componentsToTransfer;
      }
    }
    return null;
  }
}
