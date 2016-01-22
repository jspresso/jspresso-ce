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
package org.jspresso.framework.application.frontend.action.lov;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.action.CreateQueryComponentAction;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.binding.CollectionConnectorHelper;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.QueryComponent;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This action augments the context by setting the action parameter to the
 * selected entity of the LOV result list (or null if none is selected).
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 * @author Vincent Vandenschrick
 */
public class OkLovAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    Object masterComponent = context.get(CreateQueryComponentAction.MASTER_COMPONENT);
    IViewDescriptor viewDescriptor = (IViewDescriptor) context.get(LovAction.REF_VIEW_DESCRIPTOR);
    String refProperty = null;
    String tsProperty = null;
    List<String> childPropertiesToExtract = null;
    if (masterComponent instanceof QueryComponent && viewDescriptor instanceof IPropertyViewDescriptor) {
      IModelDescriptor modelDescriptor = viewDescriptor.getModelDescriptor();
      refProperty = modelDescriptor.getName();
      if (modelDescriptor instanceof IReferencePropertyDescriptor<?>) {
        tsProperty = ((IReferencePropertyDescriptor) modelDescriptor).getReferencedDescriptor().getToStringProperty();
      }
      if (((IPropertyViewDescriptor) viewDescriptor).getRenderedChildProperties() != null
          && ((IPropertyViewDescriptor) viewDescriptor).getRenderedChildProperties().size() > 0) {
        childPropertiesToExtract = ((IPropertyViewDescriptor) viewDescriptor).getRenderedChildProperties();
      }
    }
    if (!context.containsKey(LovAction.LOV_SELECTED_ITEM)) {
      ICollectionConnector resultConnector = null;
      // Do not use getViewConnector(context) since, on the table, it will return the cell connector
      IValueConnector viewConnector = getView(context).getConnector();
      // to support double click on the table
      if (viewConnector instanceof ICollectionConnector) {
        // this is from the table itself
        resultConnector = (ICollectionConnector) viewConnector;
      } else if (viewConnector instanceof ICompositeValueConnector) {
        // this is from the dialog.
        resultConnector = (ICollectionConnector) ((ICompositeValueConnector) viewConnector).getChildConnector(
            IQueryComponent.QUERIED_COMPONENTS);
        if (resultConnector == null) {
          resultConnector = CollectionConnectorHelper.extractMainCollectionConnector(viewConnector);
        }
      }
      if (resultConnector != null) {
        int[] resultSelectedIndices = resultConnector.getSelectedIndices();
        IBackendController bc = getController(context).getBackendController();
        if (resultSelectedIndices != null && resultSelectedIndices.length > 0) {
          List<Object> selectedElements = new ArrayList<>();
          for (int resultSelectedIndex : resultSelectedIndices) {
            Object selectedElement = resultConnector.getChildConnector(resultSelectedIndex).getConnectorValue();
            if (selectedElement instanceof IEntity) {
              if (!bc.isUnitOfWorkActive()) {
                selectedElement = bc.merge((IEntity) selectedElement, EMergeMode.MERGE_LAZY);
              }
            }
            selectedElements.add(selectedElement);
          }
          if (childPropertiesToExtract != null) {
            Object nextActionParam = ((QueryComponent) masterComponent).buildNestedQueryComponent(refProperty, selectedElements,
                tsProperty, childPropertiesToExtract);
            setActionParameter(nextActionParam, context);
          } else {
            if (selectedElements.size() == 1) {
              setActionParameter(selectedElements.get(0), context);
            } else {
              setActionParameter(selectedElements, context);
            }
          }
        } else {
          setActionParameter(null, context);
        }
      }
      // Only in that case a dialog has been opened
      if (!getController(context).disposeModalDialog(getActionWidget(context), context)) {
        return false;
      }
    } else {
      Object selectedElement = context.get(LovAction.LOV_SELECTED_ITEM);
      if (childPropertiesToExtract != null) {
        selectedElement = ((QueryComponent) masterComponent).buildNestedQueryComponent(refProperty, selectedElement, tsProperty,
            childPropertiesToExtract);
      }
      setActionParameter(selectedElement, context);
    }
    return super.execute(actionHandler, context);
  }
}
