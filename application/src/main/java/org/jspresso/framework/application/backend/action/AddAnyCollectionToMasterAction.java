/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.action.ActionBusinessException;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.binding.model.ModelCollectionPropertyConnector;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;

/**
 * An action used in master/detail views to add new detail(s) to a master domain
 * object. The details to add are taken from the action context through the
 * {@code ActionParameter} context value.
 *
 * @author Vincent Vandenschrick
 */
public class AddAnyCollectionToMasterAction extends
    AbstractAddCollectionToMasterAction {

  private static final Logger LOG = LoggerFactory.getLogger(AddAnyCollectionToMasterAction.class);

  /**
   * Gets the new detail to or collection of details to add from the context.
   * The ACTION_PARAM variable is used.
   *
   * @param context
   *          the action context.
   * @return the collection of details to add to the collection.
   */
  @Override
  protected List<?> getAddedComponents(Map<String, Object> context) {
    if (!context.containsKey(ActionContextConstants.ACTION_PARAM)) {
      LOG.error(getClass().getName() + " action ('addAnyToMasterFrontAction') takes the added element "
          + "from the ACTION_PARAM context value. It seems that you've not added any. "
          + "Use " + AddComponentCollectionToMasterAction.class.getName() + " instead. "
          + "A default instance is registered under 'addToMasterFrontAction'");
    }
    Object detailOrList = getActionParameter(context);
    if (detailOrList instanceof List<?>) {
      return (List<?>) detailOrList;
    }
    return Collections.singletonList(detailOrList);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ModelCollectionPropertyConnector getModelConnector(
      Map<String, Object> context) {
    return (ModelCollectionPropertyConnector) super.getModelConnector(context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICollectionDescriptorProvider<?> getModelDescriptor(
      Map<String, Object> context) {
    return (ICollectionDescriptorProvider<?>) getModelConnector(context)
        .getModelDescriptor();
  }
}
