/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.model.entity.IEntity;


/**
 * An action used in master/detail views to remove selected details from a
 * master domain object.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoveCollectionFromMasterAction extends
    AbstractHibernateCollectionAction {

  /**
   * Retrieves the master and its managed collection from the model connector
   * then removes selected details from the managed collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (collectionConnector == null) {
      return false;
    }
    int[] selectedIndices = getSelectedIndices(context);
    if (selectedIndices != null) {
      // Traverse the collection reversly for performance reasons.
      for (int i = selectedIndices.length - 1; i >= 0; i--) {
        int selectedIndex = selectedIndices[i];
        IEntity nextDetailToRemove = (IEntity) collectionConnector
            .getChildConnector(selectedIndex).getConnectorValue();
        try {
          cleanRelationshipsOnDeletion(nextDetailToRemove, context);
        } catch (IllegalAccessException ex) {
          throw new ActionException(ex);
        } catch (InvocationTargetException ex) {
          throw new ActionException(ex);
        } catch (NoSuchMethodException ex) {
          throw new ActionException(ex);
        }
      }
    }
    return super.execute(actionHandler, context);
  }
}
