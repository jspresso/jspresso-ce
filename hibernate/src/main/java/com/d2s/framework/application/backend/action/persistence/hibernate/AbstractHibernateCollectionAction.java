/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;

/**
 * Base class for backend actions acting on collections.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractHibernateCollectionAction extends
    AbstractHibernateAction {

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
}
