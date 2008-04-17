/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;


/**
 * Base class for backend actions acting on collections.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractCollectionAction extends AbstractBackendAction {

  /**
   * refined to return a collection connector.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ICollectionConnector getModelConnector(Map<String, Object> context) {
    return (ICollectionConnector) super.getModelConnector(context);
  }

  /**
   * Refined to return a collection descriptor.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ICollectionDescriptorProvider<?> getModelDescriptor(
      Map<String, Object> context) {
    return (ICollectionDescriptorProvider<?>) super.getModelDescriptor(context);
  }

  /**
   * Gets the selected indices from the context. it uses the
   * <code>ActionContextConstants.SELECTED_INDICES</code> key.
   * 
   * @param context
   *            the action context.
   * @return the selected indices if any.
   */
  public int[] getSelectedIndices(Map<String, Object> context) {
    return (int[]) context.get(ActionContextConstants.SELECTED_INDICES);
  }

  /**
   * refined to return a collection connector.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ICollectionConnector getSourceModelConnector(
      Map<String, Object> context) {
    return (ICollectionConnector) super.getSourceModelConnector(context);
  }
}
