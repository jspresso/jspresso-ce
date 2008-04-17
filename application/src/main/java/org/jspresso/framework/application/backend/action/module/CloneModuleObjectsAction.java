/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractCollectionAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.binding.ConnectorHelper;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityCloneFactory;


/**
 * This action clones the selected objects in the projected collection.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CloneModuleObjectsAction extends AbstractCollectionAction {

  private IEntityCloneFactory entityCloneFactory;

  /**
   * Clones the selected objects in the projected collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);

    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return false;
    }

    ICompositeValueConnector moduleConnector = getModuleConnector(context);
    BeanCollectionModule module = (BeanCollectionModule) moduleConnector
        .getConnectorValue();

    Collection<Object> projectedCollection;
    if (module.getModuleObjects() == null) {
      projectedCollection = new ArrayList<Object>();
    } else {
      projectedCollection = new ArrayList<Object>(module.getModuleObjects());
    }
    Collection<IEntity> entityClones = new ArrayList<IEntity>();
    for (int i = 0; i < selectedIndices.length; i++) {
      entityClones.add(entityCloneFactory.cloneEntity(
          ((IEntity) collectionConnector.getChildConnector(selectedIndices[i])
              .getConnectorValue()), getEntityFactory(context)));
    }
    projectedCollection.addAll(entityClones);
    module.setModuleObjects(projectedCollection);

    getModelConnector(context).setConnectorValue(projectedCollection);

    context.put(ActionContextConstants.SELECTED_INDICES, ConnectorHelper
        .getIndicesOf(collectionConnector, entityClones));
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the entityCloneFactory.
   * 
   * @param entityCloneFactory
   *            the entityCloneFactory to set.
   */
  public void setEntityCloneFactory(IEntityCloneFactory entityCloneFactory) {
    this.entityCloneFactory = entityCloneFactory;
  }
}
