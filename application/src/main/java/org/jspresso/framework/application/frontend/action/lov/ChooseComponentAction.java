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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.ModalDialogAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicListDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicSetDescriptor;
import org.jspresso.framework.util.descriptor.DefaultDescriptor;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicCollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;

/**
 * This action takes an arbitrary model collection connector from the action
 * context parameter and binds it to a newly created table view. This action is
 * meant to be chained to the generic {@code ModalDialogAction} so that the
 * table is actually popped-up in a dialog. Two actions ({@code okAction}
 * and {@code cancelAction}) can be configured to react to the user
 * decision.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ChooseComponentAction<E, F, G> extends FrontendAction<E, F, G> {

  private IDisplayableAction        cancelAction;
  private IDisplayableAction        okAction;

  private ICollectionViewDescriptor collectionViewDescriptor;
  private IComponentDescriptor<?>   componentDescriptor;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<IDisplayableAction> actions = new ArrayList<>();

    if (okAction != null) {
      actions.add(okAction);
    }
    if (cancelAction != null) {
      actions.add(cancelAction);
    }
    context.put(ModalDialogAction.DIALOG_ACTIONS, actions);

    Object actionParam = getActionParameter(context);
    ICollectionViewDescriptor viewDescriptor = getCollectionViewDescriptor(context);
    IValueConnector componentsModelConnector;
    if (actionParam instanceof IValueConnector) {
      componentsModelConnector = (IValueConnector) actionParam;
      if (viewDescriptor == null) {
        viewDescriptor = new BasicTableViewDescriptor();
        ((BasicTableViewDescriptor) viewDescriptor).setReadOnly(true);
        ((BasicTableViewDescriptor) viewDescriptor)
            .setSelectionMode(ESelectionMode.SINGLE_SELECTION);
        ((BasicTableViewDescriptor) viewDescriptor)
            .setModelDescriptor(componentsModelConnector.getModelDescriptor());
        viewDescriptor.setPermId("Choose."
            + componentsModelConnector.getModelDescriptor().getName());
      }
    } else if (actionParam instanceof Collection<?>) {
      ICollectionDescriptorProvider<?> collectionDescriptorProvider;
      IComponentDescriptor<?> elementDescriptor = getComponentDescriptor(context);
      if (viewDescriptor != null
          && viewDescriptor.getModelDescriptor() instanceof ICollectionDescriptorProvider<?>) {
        collectionDescriptorProvider = (ICollectionDescriptorProvider<?>) viewDescriptor
            .getModelDescriptor();
      } else {
        if (viewDescriptor == null) {
          viewDescriptor = new BasicTableViewDescriptor();
          ((BasicTableViewDescriptor) viewDescriptor).setReadOnly(true);
          ((BasicTableViewDescriptor) viewDescriptor)
              .setSelectionMode(ESelectionMode.SINGLE_SELECTION);
        }
        if (elementDescriptor != null) {
          if (actionParam instanceof List<?>) {
            collectionDescriptorProvider = new BasicListDescriptor<Object>();
          } else if (actionParam instanceof Set<?>) {
            collectionDescriptorProvider = new BasicSetDescriptor<Object>();
          } else {
            throw new ActionException("Unsupported collection type : "
                + actionParam.getClass().getName());
          }
          ((BasicCollectionDescriptor<Object>) collectionDescriptorProvider)
              .setElementDescriptor(elementDescriptor);
          ((BasicCollectionViewDescriptor) viewDescriptor)
              .setModelDescriptor(collectionDescriptorProvider
                  .getCollectionDescriptor());
        } else {
          throw new ActionException(
              "Could not determine component descriptor of the collection element.");
        }
      }
      componentsModelConnector = getBackendController(context)
          .createModelConnector(ACTION_MODEL_NAME,
              collectionDescriptorProvider.getCollectionDescriptor());
      componentsModelConnector.setConnectorValue(actionParam);
    } else {
      throw new ActionException(
          "Could not determine component collection to use for choosing among.");
    }

    ((DefaultDescriptor) viewDescriptor.getModelDescriptor())
        .setName(ACTION_MODEL_NAME);
    IView<E> collectionView = getViewFactory(context).createView(
        viewDescriptor, actionHandler, getLocale(context));
    String dialogTitle = getI18nName(getTranslationProvider(context),
        getLocale(context));
    if (dialogTitle != null && dialogTitle.length() > 0) {
      context.put(ModalDialogAction.DIALOG_TITLE, dialogTitle);
    }
    context.put(ModalDialogAction.DIALOG_VIEW, collectionView);

    getMvcBinder(context).bind(collectionView.getConnector(),
        componentsModelConnector);

    return super.execute(actionHandler, context);
  }

  /**
   * Configures the action that will be triggered when the user cancels the
   * component choice.
   *
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(IDisplayableAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Configures the action that will be triggered when the user confirms the
   * component choice. the chosen component will then be retrieved from the
   * selected view item.
   *
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(IDisplayableAction okAction) {
    this.okAction = okAction;
  }

  /**
   * Gets the collectionViewDescriptor.
   *
   * @param context
   *          the action context.
   * @return the collectionViewDescriptor.
   */
  @SuppressWarnings("UnusedParameters")
  protected ICollectionViewDescriptor getCollectionViewDescriptor(
      Map<String, Object> context) {
    return collectionViewDescriptor;
  }

  /**
   * Sets the collectionViewDescriptor.
   *
   * @param collectionViewDescriptor
   *          the collectionViewDescriptor to set.
   */
  public void setCollectionViewDescriptor(
      ICollectionViewDescriptor collectionViewDescriptor) {
    this.collectionViewDescriptor = collectionViewDescriptor;
  }

  /**
   * Gets the componentDescriptor.
   *
   * @param context
   *          the action context.
   * @return the componentDescriptor.
   */
  @SuppressWarnings("UnusedParameters")
  protected IComponentDescriptor<?> getComponentDescriptor(
      Map<String, Object> context) {
    return componentDescriptor;
  }

  /**
   * Sets the componentDescriptor.
   *
   * @param componentDescriptor
   *          the componentDescriptor to set.
   */
  public void setComponentDescriptor(IComponentDescriptor<?> componentDescriptor) {
    this.componentDescriptor = componentDescriptor;
  }
}
