/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.d2s.framework.model.datatransfer.ComponentTransferStructure;
import com.d2s.framework.model.datatransfer.TransferMode;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityCloneFactory;

/**
 * An action used in master/detail views to paste previously copy or cut detail
 * to a master domain object.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class PasteCollectionToMasterAction extends AbstractAddCollectionToMasterAction {

  private IEntityCloneFactory entityCloneFactory;

  /**
   * Gets the buffered entities from the backend controller.
   *
   * @param context
   *          the action context.
   * @return the entities to add to the collection.
   */
  @Override
  protected List<?> getAddedComponents(Map<String, Object> context) {
    ComponentTransferStructure transferStructure = getController(context)
        .retrieveComponents();
    if (transferStructure.getContent() != null) {
      List<Object> componentsToTransfer;
      if (getModelDescriptor(context)
          .getCollectionDescriptor()
          .getElementDescriptor()
          .getComponentContract()
          .isAssignableFrom(
              transferStructure.getComponentDescriptor().getComponentContract())) {
        if (transferStructure.getContent() instanceof Collection<?>) {
          componentsToTransfer = new ArrayList<Object>(
              (Collection<?>) transferStructure.getContent());
        } else {
          componentsToTransfer = Collections.singletonList(transferStructure
              .getContent());
        }
        if (transferStructure.getTransferMode() == TransferMode.COPY) {
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
