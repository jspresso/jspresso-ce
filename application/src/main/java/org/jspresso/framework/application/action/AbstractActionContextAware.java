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
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.ISelectable;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;

/**
 * Abstract class for all objects that need to manipulate an action context. It
 * contains helper methods that takes the developer away from the standard
 * context internal knowledge. Action developers can (should) use these helper
 * methods (for reference manual readers, give an eye to the linked javadoc).
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractActionContextAware {

  /**
   * Gets the (string) action command out of the context. The action command is
   * an arbitrary character string that can be set by the UI component
   * triggering the action. It is stored using the
   * {@code ActionContextConstants.ACTION_COMMAND} standard key.
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
   * Gets the action parameter out of the context. The action parameter is a
   * general purpose context entry that can be used to pass an arbitrary
   * parameter along the action chain. It is stored using the
   * {@code ActionContextConstants.ACTION_PARAM} standard key.
   *
   * @param <T>
   *     type inference return.
   * @param context
   *          the action context.
   * @return the action parameter if it exists in the action context or null.
   */
  @SuppressWarnings("unchecked")
  protected <T> T getActionParameter(Map<String, Object> context) {
    return (T) context.get(ActionContextConstants.ACTION_PARAM);
  }

  /**
   * Gets the backend controller out of the action context using either :
   * <ul>
   * <li>the context frontend controller if it exists</li>
   * <li>the {@code ActionContextConstants.BACK_CONTROLLER} standard key if
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
   * {@code ActionContextConstants.FRONT_CONTROLLER} standard key. If the
   * action was triggered without going through a frontend controller (a batch
   * action for instance), this method might return null.
   *
   * @param <E>
   *     the actual gui component type used.
   * @param <F>
   *     the actual icon type used.
   * @param <G>
   *     the actual action type used.
   * @param context
   *          the action context.
   * @return the frontend controller.
   */
  @SuppressWarnings("unchecked")
  protected <E, F, G> IFrontendController<E, F, G> getFrontendController(
      Map<String, Object> context) {
    return (IFrontendController<E, F, G>) context
        .get(ActionContextConstants.FRONT_CONTROLLER);
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
    IFrontendController<?, ?, ?> frontController = getFrontendController(context);
    if (frontController != null) {
      return frontController.getLocale();
    }
    return getBackendController(context).getLocale();
  }

  /**
   * Gets the model this action was triggered on.
   *
   * @param <T>
   *     type inference return.
   * @param context
   *          the action context.
   * @return the model.
   */
  protected <T> T getModel(Map<String, Object> context) {
    return getModel(null, context);
  }

  /**
   * Gets the model this action was triggered on.
   *
   * @param <T>
   *     type inference return.
   * @param viewPath
   *          the view index path to follow.
   *          <ul>
   *          <li>A positive integer n means the nth child.</li>
   *          <li>A negative integer -n means the nth parent.</li>
   *          </ul>
   * @param context
   *          the action context.
   * @return the model.
   */
  protected <T> T getModel(int[] viewPath, Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(viewPath, context);
    if (modelConnector != null) {
      if (modelConnector instanceof ICompositeValueConnector) {
        return modelConnector.getConnectorValue();
      } else if (modelConnector.getModelProvider() != null) {
        return modelConnector.getModelProvider().getModel();
      }
    }
    return null;
  }

  /**
   * Gets the model connector this action was triggered on. The model connector
   * is the versatile binding structure that adapts the actual model to the
   * Jspresso binding architecture. The actual model is stored in the model
   * connector value. Unless developing very generic actions, this method will
   * rarely be used in favor of the more concrete {@code getXXXModel}
   * context accessors.
   *
   * @param context
   *          the action context.
   * @return the model connector this action was triggered on.
   */
  protected IValueConnector getModelConnector(Map<String, Object> context) {
    return getModelConnector(null, context);
  }

  /**
   * Gets the model connector this action was triggered on. The model connector
   * is the versatile binding structure that adapts the actual model to the
   * Jspresso binding architecture. The actual model is stored in the model
   * connector value. Unless developing very generic actions, this method will
   * rarely be used in favor of the more concrete {@code getXXXModel}
   * context accessors.
   *
   * @param viewPath
   *          the view index path to follow.
   *          <ul>
   *          <li>A positive integer n means the nth child.</li>
   *          <li>A negative integer -n means the nth parent.</li>
   *          </ul>
   * @param context
   *          the action context.
   * @return the model connector this action was triggered on.
   */
  protected IValueConnector getModelConnector(int[] viewPath,
      Map<String, Object> context) {
    IValueConnector viewConnector = getViewConnector(viewPath, context);
    if (viewConnector != null) {
      return viewConnector.getModelConnector();
    }
    return null;
  }

  /**
   * Retrieves the model descriptor from the context using the
   * {@code ActionContextConstants.MODEL_DESCRIPTOR} standard key. The
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
   * Gets the module this action has been executed on. The value is immutable
   * during the action chain and is stored using the
   * {@code ActionContextConstants.MODULE} key.
   *
   * @param context
   *          the action context.
   * @return the module this action executes on.
   */
  protected Module getModule(Map<String, Object> context) {
    return (Module) context.get(ActionContextConstants.MODULE);
  }

  /**
   * Gets the current module from the context. The value might change during the
   * action chain if one of the action navigates the workspace/module. It is
   * stored using the {@code ActionContextConstants.CURRENT_MODULE} key.
   *
   * @param context
   *          the action context.
   * @return the module this action executes on.
   */
  protected Module getCurrentModule(Map<String, Object> context) {
    return (Module) context.get(ActionContextConstants.CURRENT_MODULE);
  }

  /**
   * Gets the parent model this action was triggered on.
   *
   * @param <T>
   *     type inference return.
   * @param context
   *          the action context.
   * @return the parent model.
   */
  protected <T> T getParentModel(Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(context);
    if (modelConnector != null) {
      if (modelConnector.getParentConnector() != null) {
        return modelConnector.getParentConnector().getConnectorValue();
      }
    }
    return null;
  }

  /**
   * Gets the selected indices out of the UI component if it is a collection
   * component (table, list, ...). More accurately, the selected indices are
   * taken from the view connector that adapts the UI component to the Jspresso
   * binding architecture.
   *
   * @param context
   *          the action context.
   * @return the selected indices stored in the action context.
   */
  protected int[] getSelectedIndices(Map<String, Object> context) {
    return getSelectedIndices(null, context);
  }

  /**
   * Gets the selected indices out of the UI component if it is a collection
   * component (table, list, ...). More accurately, the selected indices are
   * taken from the view connector that adapts the UI component to the Jspresso
   * binding architecture.
   *
   * @param viewPath
   *          the view index path to follow.
   *          <ul>
   *          <li>A positive integer n means the nth child.</li>
   *          <li>A negative integer -n means the nth parent.</li>
   *          </ul>
   * @param context
   *          the action context.
   * @return the selected indices stored in the action context.
   */
  protected int[] getSelectedIndices(int[] viewPath, Map<String, Object> context) {
    int[] selectedIndices = null;
    IValueConnector selectableConnector = getViewConnector(viewPath, context);
    while (selectableConnector != null
        && !(selectableConnector instanceof ISelectable)) {
      selectableConnector = selectableConnector.getParentConnector();
    }
    if (selectableConnector != null) {
      selectedIndices = ((ISelectable) selectableConnector)
          .getSelectedIndices();
    }
    return selectedIndices;
  }

  /**
   * This is a versatile helper method that retrieves the selected model either
   * from the 1st selected child connector if the action was triggered on a
   * collection connector or from the connector itself.
   *
   * @param <T>
   *     type inference return.
   * @param context
   *          the action context.
   * @return the selected model.
   */
  protected <T> T getSelectedModel(Map<String, Object> context) {
    return getSelectedModel(null, context);
  }

  /**
   * This is a versatile helper method that retrieves the selected model either
   * from the 1st selected child connector if the action was triggered on a
   * collection connector or from the connector itself.
   *
   * @param <T>
   *     type inference return.
   * @param viewPath
   *          the view index path to follow.
   *          <ul>
   *          <li>A positive integer n means the nth child.</li>
   *          <li>A negative integer -n means the nth parent.</li>
   *          </ul>
   * @param context
   *          the action context.
   * @return the selected model.
   */
  protected <T> T getSelectedModel(int[] viewPath, Map<String, Object> context) {
    IValueConnector viewConnector = getViewConnector(viewPath, context);
    T selectedModel;
    if (viewConnector instanceof IItemSelectable) {
      selectedModel = ((IItemSelectable) viewConnector).getSelectedItem();
      if (selectedModel instanceof IValueConnector) {
        selectedModel = ((IValueConnector) selectedModel).getConnectorValue();
      }
    } else {
      selectedModel = getModel(viewPath, context);
    }
    return selectedModel;
  }

  /**
   * This is a versatile helper method that retrieves the selected models model
   * either from the selected child connectors if the action was triggered on a
   * collection connector or from the connector itself.
   *
   * @param <T>
   *     type inference return.
   * @param context
   *          the action context.
   * @return the list of selected models.
   */
  protected <T> List<T> getSelectedModels(Map<String, Object> context) {
    return getSelectedModels(null, context);
  }

  /**
   * This is a versatile helper method that retrieves the selected models model
   * either from the selected child connectors if the action was triggered on a
   * collection connector or from the connector itself.
   *
   * @param <T>
   *     type inference return.
   * @param viewPath
   *          the view index path to follow.
   *          <ul>
   *          <li>A positive integer n means the nth child.</li>
   *          <li>A negative integer -n means the nth parent.</li>
   *          </ul>
   * @param context
   *          the action context.
   * @return the list of selected models.
   */
  @SuppressWarnings("unchecked")
  protected <T> List<T> getSelectedModels(int[] viewPath,
                                          Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(viewPath, context);
    if (modelConnector == null) {
      return null;
    }
    List<T> models;
    if (modelConnector instanceof ICollectionConnector) {
      models = new ArrayList<>();
      int[] selectedIndices = getSelectedIndices(viewPath, context);
      if (selectedIndices != null && selectedIndices.length > 0) {
        for (int selectedIndice : selectedIndices) {
          IValueConnector childConnector = ((ICollectionConnector) modelConnector)
              .getChildConnector(selectedIndice);
          if (childConnector != null) {
            models.add((T) childConnector.getConnectorValue());
          }
        }
      }
    } else {
      T model = getSelectedModel(viewPath, context);
      models = Collections.singletonList(model);
    }
    return models;
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
    return getBackendController(context);
  }

  /**
   * Sets the action parameter out of the context. The action parameter is a
   * general purpose context entry that can be used to pass an arbitrary
   * parameter along the action chain. It is stored using the
   * {@code ActionContextConstants.ACTION_PARAM} standard key.
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
   * Sets the selected indices of the UI component if it is a collection
   * component (table, list, ...). More accurately, the selected indices are set
   * to the view connector that adapts the UI component to the Jspresso binding
   * architecture.
   *
   * @param selectedIndices
   *          the selected indices to store in the action context.
   * @param context
   *          the action context.
   */
  protected void setSelectedIndices(int[] selectedIndices,
      Map<String, Object> context) {
    setSelectedIndices(null, selectedIndices, context);
  }

  /**
   * Sets the selected indices of the UI component if it is a collection
   * component (table, list, ...). More accurately, the selected indices are set
   * to the view connector that adapts the UI component to the Jspresso binding
   * architecture.
   *
   * @param viewPath
   *          the view index path to follow.
   *          <ul>
   *          <li>A positive integer n means the nth child.</li>
   *          <li>A negative integer -n means the nth parent.</li>
   *          </ul>
   * @param selectedIndices
   *          the selected indices to store in the action context.
   * @param context
   *          the action context.
   */
  protected void setSelectedIndices(int[] viewPath, int[] selectedIndices,
      Map<String, Object> context) {
    IValueConnector selectableConnector = getViewConnector(viewPath, context);
    while (selectableConnector != null
        && !(selectableConnector instanceof ISelectable)) {
      selectableConnector = selectableConnector.getParentConnector();
    }
    if (selectableConnector != null) {
      ((ISelectable) selectableConnector).setSelectedIndices(selectedIndices);
    }
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
    setSelectedModels(null, selectedModels, context);
  }

  /**
   * Retrieves the selected models indices out of the model connector if it's a
   * collection connector and set them as selected indices in the action
   * context.
   *
   * @param viewPath
   *          the view index path to follow.
   *          <ul>
   *          <li>A positive integer n means the nth child.</li>
   *          <li>A negative integer -n means the nth parent.</li>
   *          </ul>
   * @param selectedModels
   *          the list of models to select in the view connector.
   * @param context
   *          the action context.
   */
  protected void setSelectedModels(int[] viewPath,
      Collection<?> selectedModels, Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(viewPath, context);
    if (modelConnector instanceof ICollectionConnector) {
      setSelectedIndices(viewPath, ConnectorHelper.getIndicesOf(
          (ICollectionConnector) modelConnector, selectedModels), context);
    }
  }

  /**
   * Gets the property view descriptor out of the context. The property view
   * descriptor is only filled when an action is triggered from a table column
   * or a form field. It contains a reference to the column/field descriptor the
   * action was triggered from.
   *
   * @param context
   *          the action context.
   * @return the property view descriptor.
   */
  protected IPropertyViewDescriptor getPropertyViewDescriptor(
      Map<String, Object> context) {
    return (IPropertyViewDescriptor) context
        .get(ActionContextConstants.PROPERTY_VIEW_DESCRIPTOR);
  }

  /**
   * This is a utility method which is able to retrieve the view this action has
   * been executed on from its context. It uses well-known context keys of the
   * action context which are:
   * <ul>
   * <li> {@code ActionContextConstants.VIEW} to get the the view the action
   * executes on.
   * </ul>
   * <p>
   * The returned view mainly serves for acting on the view component the action
   * has to be triggered on.
   *
   * @param <T>
   *     type inference return.
   * @param context
   *          the action context.
   * @return the view this action was triggered on.
   */
  protected <T> IView<T> getView(Map<String, Object> context) {
    return getView(null, context);
  }

  /**
   * This is a utility method which is able to retrieve the view this action has
   * been executed on from its context. It uses well-known context keys of the
   * action context which are:
   * <ul>
   * <li> {@code ActionContextConstants.VIEW} to get the the view the action
   * executes on.
   * </ul>
   * <p>
   * The returned view mainly serves for acting on the view component the action
   * has to be triggered on.
   *
   * @param <T>
   *     type inference return.
   * @param viewPath
   *          the view index path to follow.
   *          <ul>
   *          <li>A positive integer n means the nth child.</li> <li>A negative
   *          integer -n means the nth parent.</li>
   *          </ul>
   * @param context
   *          the action context.
   * @return the view this action was triggered on.
   */
  @SuppressWarnings("unchecked")
  protected <T> IView<T> getView(int[] viewPath, Map<String, Object> context) {
    return navigate((IView<T>) context.get(ActionContextConstants.VIEW),
        viewPath);
  }

  /**
   * This is a utility method which is able to retrieve the view connector this
   * action has been executed on from its context. It uses well-known context
   * keys of the action context which are:
   * <ul>
   * <li> {@code ActionContextConstants.VIEW_CONNECTOR} to get the the view
   * value connector the action executes on.
   * </ul>
   * <p>
   * The returned connector mainly serves for acting on the view component the
   * action has to be triggered on.
   *
   * @param context
   *          the action context.
   * @return the value connector this action was triggered on.
   */
  protected IValueConnector getViewConnector(Map<String, Object> context) {
    return getViewConnector(null, context);
  }

  /**
   * This is a utility method which is able to retrieve the view connector this
   * action has been executed on from its context. It uses well-known context
   * keys of the action context which are:
   * <ul>
   * <li> {@code ActionContextConstants.VIEW_CONNECTOR} to get the the view
   * value connector the action executes on.
   * </ul>
   * <p>
   * The returned connector mainly serves for acting on the view component the
   * action has to be triggered on.
   *
   * @param viewPath
   *          the view index path to follow.
   *          <ul>
   *          <li>A positive integer n means the nth child.</li> <li>A negative
   *          integer -n means the nth parent.</li>
   *          </ul>
   * @param context
   *          the action context.
   * @return the value connector this action was triggered on.
   */
  protected IValueConnector getViewConnector(int[] viewPath,
      Map<String, Object> context) {
    if (viewPath == null || viewPath.length == 0) {
      return (IValueConnector) context
          .get(ActionContextConstants.VIEW_CONNECTOR);
    }
    IView<?> view = getView(viewPath, context);
    if (view != null) {
      return view.getConnector();
    }
    return null;
  }

  /**
   * Starts from a view and navigates the view hierarchy following an index
   * navigation path.
   *
   * @param <T>
   *     The root class of the view peers.
   * @param fromView
   *          the view to start from.
   * @param viewPath
   *          the view index path to follow.
   *          <ul>
   *          <li>A positive integer n means the nth child.</li>
   *          <li>A negative integer -n means the nth parent.</li>
   *          </ul>
   * @return the view navigated to.
   */
  protected <T> IView<T> navigate(IView<T> fromView, int... viewPath) {
    IView<T> target = fromView;
    if (viewPath != null) {
      for (int nextIndex : viewPath) {
        if (target != null) {
          if (nextIndex < 0) {
            for (int j = 0; j > nextIndex; j--) {
              if (target != null) {
                target = target.getParent();
              }
            }
          } else {
            if (target instanceof ICompositeView<?>
                && ((ICompositeView<?>) target).getChildren() != null
                && nextIndex < ((ICompositeView<?>) target).getChildren()
                    .size()) {
              target = ((ICompositeView<T>) target).getChildren()
                  .get(nextIndex);
            } else {
              target = null;
            }
          }
        }
      }
    }
    return target;
  }
}
