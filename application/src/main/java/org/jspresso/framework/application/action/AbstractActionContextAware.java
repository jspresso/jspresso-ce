/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.binding.ConnectorHelper;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * Abstract class for all objects that need to manipulate an action context. It
 * contains helper methods that takes the developer away from the standard
 * context internal knowledge. Action developers can (should) use these helper
 * methods (for reference manual readers, give an eye to the linked javadoc).
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractActionContextAware {

  /**
   * Retrieves the model descriptor from the context using the
   * <code>ActionContextConstants.MODEL_DESCRIPTOR</code> standard key. The
   * model descriptor is registered in the action context based on the model of
   * the view to which the action is attached.
   * 
   * @param context
   *          the action context.
   * @return the model descriptor this model action was triggered on.
   */
  protected IModelDescriptor getModelDescriptor(Map<String, Object> context) {
    return (IModelDescriptor) context
        .get(ActionContextConstants.MODEL_DESCRIPTOR);
  }

  /**
   * Gets the backend controller out of the action context using either :
   * <ul>
   * <li>the context frontend controller if it exists</li>
   * <li>the <code>ActionContextConstants.BACK_CONTROLLER</code> standard key if
   * the action was triggered without going through a frontend controller (a
   * batch action for instance).</li>
   * </ul>
   * 
   * @param context
   *          the action context.
   * @return the backend controller.
   */
  protected IBackendController getBackendController(Map<String, Object> context) {
    IFrontendController<?, ?, ?> frontController = getFrontendController(context);
    if (frontController != null) {
      return frontController.getBackendController();
    }
    return (IBackendController) context
        .get(ActionContextConstants.BACK_CONTROLLER);
  }

  /**
   * Gets the frontend controller out of the action context using the
   * <code>ActionContextConstants.FRONT_CONTROLLER</code> standard key. If the
   * action was triggered without going through a frontend controller (a batch
   * action for instance), this method might return null.
   * 
   * @param context
   *          the action context.
   * @return the frontend controller.
   */
  protected IFrontendController<?, ?, ?> getFrontendController(
      Map<String, Object> context) {
    return (IFrontendController<?, ?, ?>) context
        .get(ActionContextConstants.FRONT_CONTROLLER);
  }

  /**
   * Gets he application translation provider out of the action context. This
   * method simply delegates to the context backend controller.
   * 
   * @param context
   *          the action context.
   * @return the translation provider.
   */
  protected ITranslationProvider getTranslationProvider(
      Map<String, Object> context) {
    return getBackendController(context).getTranslationProvider();
  }

  /**
   * Retrieves the locale the action has to use to execute. This method
   * delegates to the context backend controller that in turn will delegate to
   * the session.
   * 
   * @param context
   *          the action context.
   * @return the locale the action executes in.
   */
  protected Locale getLocale(Map<String, Object> context) {
    return getBackendController(context).getLocale();
  }

  /**
   * Gets the action parameter out of the context. The action parameter is a
   * general purpose context entry that can be used to pass an arbitrary
   * parameter along the action chain. It is stored using the
   * <code>ActionContextConstants.ACTION_PARAM</code> standard key.
   * 
   * @param context
   *          the action context.
   * @return the action parameter if it exists in the action context or null.
   */
  protected Object getActionParameter(Map<String, Object> context) {
    return context.get(ActionContextConstants.ACTION_PARAM);
  }

  /**
   * Sets the action parameter out of the context. The action parameter is a
   * general purpose context entry that can be used to pass an arbitrary
   * parameter along the action chain. It is stored using the
   * <code>ActionContextConstants.ACTION_PARAM</code> standard key.
   * 
   * @param actionParam
   *          the action parameter to set to the context.
   * @param context
   *          the action context.
   */
  protected void setActionParameter(Object actionParam,
      Map<String, Object> context) {
    context.put(ActionContextConstants.ACTION_PARAM, actionParam);
  }

  /**
   * Gets the (string) action command out of the context. The action command is
   * an arbitrary character string that can be set by the UI component
   * triggering the action. It is stored using the
   * <code>ActionContextConstants.ACTION_COMMAND</code> standard key.
   * 
   * @param context
   *          the action context.
   * @return the (string) action command if it exists in the action context or
   *         null.
   */
  protected String getActionCommand(Map<String, Object> context) {
    return (String) context.get(ActionContextConstants.ACTION_COMMAND);
  }

  /**
   * Gets the model connector this action was triggered on. The model connector
   * is the versatile binding structure that adapts the actual model to the
   * Jspresso binding architecture. The actual model is stored in the model
   * connector value. Unless developing very generic actions, this method will
   * rarely be used in favor of the more concrete <code>getXXXModel</code>
   * context accessors.
   * 
   * @param context
   *          the action context.
   * @return the model connector this action was triggered on.
   */
  protected IValueConnector getModelConnector(Map<String, Object> context) {
    return ((IValueConnector) context
        .get(ActionContextConstants.VIEW_CONNECTOR)).getModelConnector();
  }

  /**
   * Gets the selected indices out of the action context. The value is stored
   * with the key <code>ActionContextConstants.SELECTED_INDICES</code>. The
   * context is initialized with the array of selected indices of the UI
   * component if it is a collection component (table, list, ...). More
   * acurately, the selected indices are taken from the view connector that
   * adapts the UI component to the Jspresso binding architecture.
   * 
   * @param context
   *          the action context.
   * @return the selected indices stored in the action context.
   */
  protected int[] getSelectedIndices(Map<String, Object> context) {
    return (int[]) context.get(ActionContextConstants.SELECTED_INDICES);
  }

  /**
   * Sets the selected indices to the action context. The value is stored using
   * the <code>ActionContextConstants.SELECTED_INDICES</code> key.
   * 
   * @param selectedIndices
   *          the selected indices to store in the action context.
   * @param context
   *          the action context.
   */
  protected void setSelectedIndices(int[] selectedIndices,
      Map<String, Object> context) {
    context.put(ActionContextConstants.SELECTED_INDICES, selectedIndices);
  }

  /**
   * Gets the module this action has been executed on. The value is stored using
   * the <code>ActionContextConstants.MODULE</code> key.
   * 
   * @param context
   *          the action context.
   * @return the module this action executes on.
   */
  protected Module getModule(Map<String, Object> context) {
    return (Module) context.get(ActionContextConstants.MODULE);
  }

  /**
   * Gets the model this action was triggered on.
   * 
   * @param context
   *          the action context.
   * @return the model.
   */
  protected Object getModel(Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(context);
    if (modelConnector != null) {
      return modelConnector.getConnectorValue();
    }
    return null;
  }

  /**
   * Gets the parent model this action was triggered on.
   * 
   * @param context
   *          the action context.
   * @return the parent model.
   */
  protected Object getParentModel(Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(context);
    if (modelConnector != null) {
      if (modelConnector.getParentConnector() != null) {
        return modelConnector.getParentConnector().getConnectorValue();
      }
    }
    return null;
  }

  /**
   * This is a versatile helper method that retrieves the selected model either
   * from the 1st selected child connector if the action was trigerred on a
   * collection connector or from the connector itself.
   * 
   * @param context
   *          the action context.
   * @return the selected model.
   */
  protected Object getSelectedModel(Map<String, Object> context) {
    Object model = null;
    if (context.containsKey(ActionContextConstants.SELECTED_MODEL)) {
      // we are on a IItemSelectable.
      model = context.get(ActionContextConstants.SELECTED_MODEL);
    } else {
      model = getModel(context);
    }
    return model;
  }

  /**
   * This is a versatile helper method that retrieves the selected models model
   * either from the selected child connectors if the action was trigerred on a
   * collection connector or from the connector itself.
   * 
   * @param context
   *          the action context.
   * @return the list of selected models.
   */
  protected List<?> getSelectedModels(Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(context);
    if (modelConnector == null) {
      return null;
    }
    List<Object> models;
    if (modelConnector instanceof ICollectionConnector) {
      models = new ArrayList<Object>();
      int[] selectedIndices = getSelectedIndices(context);
      if (selectedIndices != null && selectedIndices.length > 0) {
        for (int i = 0; i < selectedIndices.length; i++) {
          models.add(((ICollectionConnector) modelConnector).getChildConnector(
              selectedIndices[i]).getConnectorValue());
        }
      }
    } else {
      models = Collections.singletonList(getSelectedModel(context));
    }
    return models;
  }

  /**
   * Retrieves the selected models indices out of the model connector if it's a
   * collection connector and set them as selected indices in the action
   * context.
   * 
   * @param selectedModels
   *          the list of models to select in the view connector.
   * @param context
   *          the action context.
   */
  protected void setSelectedModels(Collection<?> selectedModels,
      Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(context);
    if (modelConnector instanceof ICollectionConnector) {
      setSelectedIndices(ConnectorHelper.getIndicesOf(
          (ICollectionConnector) modelConnector, selectedModels), context);
    }
  }

}
