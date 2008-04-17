/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ConnectorHelper;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.model.IModelValueConnector;
import org.jspresso.framework.binding.model.ModelPropertyConnector;
import org.jspresso.framework.model.descriptor.IModelDescriptorAware;
import org.jspresso.framework.util.accessor.ICollectionAccessor;
import org.jspresso.framework.util.accessor.IListAccessor;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;


/**
 * An action used in master/detail views to create and add a new detail to a
 * master domain object.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractAddCollectionToMasterAction extends
    AbstractCollectionAction {

  /**
   * Retrieves the master and its managed collection from the model connector
   * then creates a new detail and adds it to the managed collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (collectionConnector == null) {
      return false;
    }

    List<?> newComponents = getAddedComponents(context);
    if (newComponents != null && newComponents.size() > 0) {
      Class newComponentContract = getModelDescriptor(context)
          .getCollectionDescriptor().getElementDescriptor()
          .getComponentContract();
      Object master = collectionConnector.getParentConnector()
          .getConnectorValue();
      ICollectionAccessor collectionAccessor = getAccessorFactory(context)
          .createCollectionPropertyAccessor(
              collectionConnector.getId(),
              ((IModelValueConnector) collectionConnector).getModelProvider()
                  .getModelDescriptor().getComponentDescriptor()
                  .getComponentContract(), newComponentContract);
      if (collectionAccessor instanceof IModelDescriptorAware) {
        ((IModelDescriptorAware) collectionAccessor)
            .setModelDescriptor(getModelDescriptor(context));
      }
      try {
        int index = -1;
        if (collectionAccessor instanceof IListAccessor) {
          if (getSelectedIndices(context) != null
              && getSelectedIndices(context).length > 0) {
            index = getSelectedIndices(context)[getSelectedIndices(context).length - 1];
          }
        }
        for (int i = 0; i < newComponents.size(); i++) {
          if (index >= 0) {
            ((IListAccessor) collectionAccessor).addToValue(master, index + 1
                + i, newComponents.get(i));
          } else {
            collectionAccessor.addToValue(master, newComponents.get(i));
          }
        }
        if (!(master instanceof IPropertyChangeCapable)
            && collectionConnector instanceof ModelPropertyConnector) {
          ((ModelPropertyConnector) collectionConnector).propertyChange(null);
        }
      } catch (IllegalAccessException ex) {
        throw new ActionException(ex);
      } catch (InvocationTargetException ex) {
        throw new ActionException(ex);
      } catch (NoSuchMethodException ex) {
        throw new ActionException(ex);
      }
      context.put(ActionContextConstants.ACTION_PARAM, newComponents);
      context.put(ActionContextConstants.SELECTED_INDICES, ConnectorHelper
          .getIndicesOf(collectionConnector, newComponents));
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the new entity to add. It is created using the informations contained
   * in the context.
   * 
   * @param context
   *            the action context.
   * @return the entity to add to the collection.
   */
  @SuppressWarnings("unchecked")
  protected abstract List<?> getAddedComponents(Map<String, Object> context);
}
