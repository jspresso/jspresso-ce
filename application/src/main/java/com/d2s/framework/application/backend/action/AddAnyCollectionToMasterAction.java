/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.model.ModelCollectionPropertyConnector;
import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;

/**
 * An action used in master/detail views to add new detail(s) to a master domain
 * object. The details to add are taken from the action context through the
 * ACTION_PARAM constant.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AddAnyCollectionToMasterAction extends
    AbstractAddCollectionToMasterAction {

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelCollectionPropertyConnector getModelConnector(
      Map<String, Object> context) {
    return (ModelCollectionPropertyConnector) ((ICollectionConnector) context
        .get(ActionContextConstants.SOURCE_VIEW_CONNECTOR)).getModelConnector();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ICollectionDescriptorProvider<?> getModelDescriptor(
      Map<String, Object> context) {
    return (ICollectionDescriptorProvider<?>) getModelConnector(context)
        .getModelDescriptor();
  }

  /**
   * Gets the new detail to or collection of details to add from the context.
   * The ACTION_PARAM variable is used.
   * 
   * @param context
   *            the action context.
   * @return the collection of details to add to the collection.
   */
  @Override
  @SuppressWarnings("unchecked")
  protected List<?> getAddedComponents(Map<String, Object> context) {
    Object detailOrList = context.get(ActionContextConstants.ACTION_PARAM);
    if (detailOrList instanceof List) {
      return (List<?>) detailOrList;
    }
    return Collections.singletonList(detailOrList);
  }
}
