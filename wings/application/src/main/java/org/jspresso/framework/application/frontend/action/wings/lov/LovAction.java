/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.wings.lov;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.CreateQueryComponentAction;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.frontend.action.wings.std.ModalDialogAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ILovViewDescriptorFactory;
import org.wings.SComponent;


/**
 * A standard List of value action for reference property views. This action
 * should be used in view factories.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class LovAction extends ModalDialogAction {

  private boolean                               autoquery;
  private IDisplayableAction                    cancelAction;
  private CreateQueryComponentAction            createQueryComponentAction;
  private IReferencePropertyDescriptor<IEntity> entityRefQueryDescriptor;
  private IComponentDescriptor<IEntity>         entityDescriptor;
  private Map<String, String>                   initializationMapping;
  private IDisplayableAction                    findAction;
  private ILovViewDescriptorFactory             lovViewDescriptorFactory;
  private IDisplayableAction                    okAction;

  /**
   * Constructs a new <code>LovAction</code> instance.
   */
  public LovAction() {
    setAutoquery(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    if (getName() == null) {
      if (entityDescriptor != null) {
        return translationProvider.getTranslation("lov.element.name",
            new Object[] {entityDescriptor.getI18nName(translationProvider,
                locale)}, locale);
      }
      return translationProvider.getTranslation("lov.name", locale);
    }
    return super.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    if (getDescription() == null) {
      if (entityDescriptor != null) {
        return translationProvider.getTranslation("lov.element.description",
            new Object[] {entityDescriptor.getI18nName(translationProvider,
                locale)}, locale);
      }
      return translationProvider.getTranslation("lov.description", locale);
    }
    return super.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL() {
    String iconImageURL = super.getIconImageURL();
    if (iconImageURL == null) {
      if (entityDescriptor != null) {
        iconImageURL = entityDescriptor.getIconImageURL();
      }
      if (iconImageURL == null) {
        iconImageURL = "classpath:org/jspresso/framework/application/images/find-48x48.png";
      }
    }
    return iconImageURL;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<IDisplayableAction> actions = new ArrayList<IDisplayableAction>();
    getViewConnector(context).setConnectorValue(
        getViewConnector(context).getConnectorValue());
    okAction.putInitialContext(ActionContextConstants.SOURCE_VIEW_CONNECTOR,
        getViewConnector(context));
    actions.add(findAction);
    actions.add(okAction);
    actions.add(cancelAction);
    context.put(ActionContextConstants.DIALOG_ACTIONS, actions);
    IReferencePropertyDescriptor<IEntity> erqDescriptor = getEntityRefQueryDescriptor(context);
    IView<SComponent> lovView = getViewFactory(context).createView(
        lovViewDescriptorFactory.createLovViewDescriptor(erqDescriptor),
        actionHandler, getLocale(context));
    context.put(ActionContextConstants.DIALOG_VIEW, lovView);
    context.put(ActionContextConstants.COMPONENT_REF_DESCRIPTOR, erqDescriptor);
    actionHandler.execute(createQueryComponentAction, context);
    IValueConnector queryEntityConnector = (IValueConnector) context
        .get(ActionContextConstants.QUERY_MODEL_CONNECTOR);
    getMvcBinder(context).bind(lovView.getConnector(), queryEntityConnector);
    findAction.putInitialContext(ActionContextConstants.QUERY_MODEL_CONNECTOR,
        queryEntityConnector);
    Object queryPropertyValue = context
        .get(ActionContextConstants.ACTION_COMMAND);
    if (autoquery && queryPropertyValue != null
        && !queryPropertyValue.equals("*")) {
      actionHandler.execute(findAction, context);
      IQueryComponent queryComponent = (IQueryComponent) queryEntityConnector
          .getConnectorValue();
      if (queryComponent.getQueriedComponents() != null
          && queryComponent.getQueriedComponents().size() == 1) {
        IEntity selectedEntity = getController(context).getApplicationSession()
            .merge((IEntity) queryComponent.getQueriedComponents().get(0),
                EMergeMode.MERGE_KEEP);
        getViewConnector(context).setConnectorValue(selectedEntity);
        return true;
      }
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the autoquery.
   * 
   * @param autoquery
   *            the autoquery to set.
   */
  public void setAutoquery(boolean autoquery) {
    this.autoquery = autoquery;
  }

  /**
   * Sets the cancelAction.
   * 
   * @param cancelAction
   *            the cancelAction to set.
   */
  public void setCancelAction(IDisplayableAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Sets the createQueryComponentAction.
   * 
   * @param createQueryComponentAction
   *            the createQueryComponentAction to set.
   */
  public void setCreateQueryComponentAction(
      CreateQueryComponentAction createQueryComponentAction) {
    this.createQueryComponentAction = createQueryComponentAction;
  }

  /**
   * Sets the findAction.
   * 
   * @param findAction
   *            the findAction to set.
   */
  public void setFindAction(IDisplayableAction findAction) {
    this.findAction = findAction;
  }

  /**
   * Sets the lovViewDescriptorFactory.
   * 
   * @param lovViewDescriptorFactory
   *            the lovViewDescriptorFactory to set.
   */
  public void setLovViewDescriptorFactory(
      ILovViewDescriptorFactory lovViewDescriptorFactory) {
    this.lovViewDescriptorFactory = lovViewDescriptorFactory;
  }

  /**
   * Sets the okAction.
   * 
   * @param okAction
   *            the okAction to set.
   */
  public void setOkAction(IDisplayableAction okAction) {
    this.okAction = okAction;
  }

  /**
   * Gets the entityRefQueryDescriptor.
   * 
   * @param context
   *            the action context.
   * @return the entityRefQueryDescriptor.
   */
  @SuppressWarnings("unchecked")
  protected IReferencePropertyDescriptor<IEntity> getEntityRefQueryDescriptor(
      Map<String, Object> context) {
    if (entityDescriptor != null) {
      if (entityRefQueryDescriptor == null) {
        entityRefQueryDescriptor = new BasicReferencePropertyDescriptor<IEntity>();
        ((BasicReferencePropertyDescriptor<IEntity>) entityRefQueryDescriptor)
            .setReferencedDescriptor(entityDescriptor);
        ((BasicReferencePropertyDescriptor<IEntity>) entityRefQueryDescriptor)
            .setInitializationMapping(initializationMapping);
      }
      return entityRefQueryDescriptor;
    }
    IModelDescriptor modelDescriptor = (IModelDescriptor) context
        .get(ActionContextConstants.MODEL_DESCRIPTOR);
    if (modelDescriptor instanceof IReferencePropertyDescriptor) {
      return (IReferencePropertyDescriptor<IEntity>) modelDescriptor;
    }
    return null;
  }

  /**
   * Sets the entityDescriptor.
   * 
   * @param entityDescriptor the entityDescriptor to set.
   */
  public void setEntityDescriptor(IComponentDescriptor<IEntity> entityDescriptor) {
    this.entityDescriptor = entityDescriptor;
  }

  
  /**
   * Sets the initializationMapping.
   * 
   * @param initializationMapping the initializationMapping to set.
   */
  public void setInitializationMapping(Map<String, String> initializationMapping) {
    this.initializationMapping = initializationMapping;
  }
}
