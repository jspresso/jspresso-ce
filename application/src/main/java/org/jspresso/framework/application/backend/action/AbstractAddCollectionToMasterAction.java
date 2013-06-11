/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.model.ModelPropertyConnector;
import org.jspresso.framework.model.descriptor.IModelDescriptorAware;
import org.jspresso.framework.util.accessor.ICollectionAccessor;
import org.jspresso.framework.util.accessor.IListAccessor;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;

/**
 * An action used in master/detail views to create and add a new detail to a
 * master domain object. The only method to be implemented by concrete subclasses
 * to retrieve the instances to be added to the master is :
 * <p>
 * 
 * <pre>
 * protected abstract List&lt;?&gt;
 *           getAddedComponents(Map&lt;String, Object&gt; context)
 * </pre>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractAddCollectionToMasterAction extends
    AbstractCollectionAction {

  /**
   * Constructs a new {@code AbstractAddCollectionToMasterAction} instance.
   */
  public AbstractAddCollectionToMasterAction() {
    // Disable bad frontend access checks.
    setBadFrontendAccessChecked(false);
  }

  /**
   * Retrieves the master and its managed collection from the model connector
   * then creates a new detail and adds it to the managed collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (collectionConnector == null) {
      return false;
    }

    List<?> newComponents = getAddedComponents(context);
    if (newComponents != null && newComponents.size() > 0) {
      Class<?> newComponentContract = getModelDescriptor(context)
          .getCollectionDescriptor().getElementDescriptor()
          .getComponentContract();
      Object master = collectionConnector.getParentConnector()
          .getConnectorValue();
      ICollectionAccessor collectionAccessor = getAccessorFactory(context)
          .createCollectionPropertyAccessor(
              collectionConnector.getId(),
              collectionConnector.getModelProvider().getModelDescriptor()
                  .getComponentDescriptor().getComponentContract(),
              newComponentContract);
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
        Collection<?> existingCollection = collectionAccessor.getValue(master);
        for (int i = 0; i < newComponents.size(); i++) {
          Object addedComponent = newComponents.get(i);
          if (existingCollection == null
              || !existingCollection.contains(addedComponent)) {
            if (index >= 0 && collectionAccessor instanceof IListAccessor) {
              ((IListAccessor) collectionAccessor).addToValue(master, index + 1
                  + i, addedComponent);
            } else {
              collectionAccessor.addToValue(master, addedComponent);
            }
          }
        }
        if (!(master instanceof IPropertyChangeCapable)
            && collectionConnector instanceof ModelPropertyConnector) {
          ((ModelPropertyConnector) collectionConnector).propertyChange(null);
        }
      } catch (IllegalAccessException ex) {
        throw new ActionException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ActionException(ex.getCause());
      } catch (NoSuchMethodException ex) {
        throw new ActionException(ex);
      }
      setActionParameter(newComponents, context);
      setSelectedModels(newComponents, context);
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the new entity to add. It is created using the information contained
   * in the context.
   * 
   * @param context
   *          the action context.
   * @return the entity to add to the collection.
   */
  protected abstract List<?> getAddedComponents(Map<String, Object> context);
}
