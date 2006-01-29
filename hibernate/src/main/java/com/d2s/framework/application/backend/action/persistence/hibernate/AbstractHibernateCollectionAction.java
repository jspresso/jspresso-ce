/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;
import com.d2s.framework.view.action.ActionContextConstants;

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
  public ICollectionConnector getModelConnector() {
    return (ICollectionConnector) super.getModelConnector();
  }

  /**
   * Gets the selected indices from the context. it uses the
   * <code>ActionContextConstants.SELECTED_INDICES</code> key.
   * 
   * @return the selected indices if any.
   */
  public int[] getSelectedIndices() {
    return (int[]) getContext().get(ActionContextConstants.SELECTED_INDICES);
  }

  /**
   * Refined to return a collection descriptor.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ICollectionDescriptorProvider getModelDescriptor() {
    return (ICollectionDescriptorProvider) super.getModelDescriptor();
  }
}
