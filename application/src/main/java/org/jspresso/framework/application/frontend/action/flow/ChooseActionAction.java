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
package org.jspresso.framework.application.frontend.action.flow;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.model.descriptor.basic.BasicDescriptorDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicListDescriptor;
import org.jspresso.framework.util.descriptor.IDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * This action displays a list of frontend actions so that the user can choose
 * and launch one of them. This action is meant to be chained with the generic
 * {@code ChooseComponentAction} so that the action list is actually
 * displayed.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ChooseActionAction<E, F, G> extends FrontendAction<E, F, G> {

  private List<IDisplayableAction> actions;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    BasicListDescriptor<IDescriptor> modelDescriptor = new BasicListDescriptor<>();
    modelDescriptor.setElementDescriptor(BasicDescriptorDescriptor.INSTANCE);
    modelDescriptor.setName(ACTION_MODEL_NAME);
    // IValueConnector actionsConnector = modelConnectorFactory
    // .createModelConnector(ACTION_MODEL_NAME, modelDescriptor, actionHandler
    // .getSubject());
    IValueConnector actionsConnector = getBackendController(context)
        .createModelConnector(ACTION_MODEL_NAME, modelDescriptor);
    actionsConnector.setConnectorValue(createActionProxies(
        getTranslationProvider(context), getLocale(context)));
    setActionParameter(actionsConnector, context);
    return super.execute(actionHandler, context);
  }

  /**
   * Configures the list of actions to choose from.
   *
   * @param actions
   *          the actions to set.
   */
  public void setActions(List<IDisplayableAction> actions) {
    this.actions = actions;
  }

  /**
   * Sets the modelConnectorFactory.
   *
   * @param modelConnectorFactory
   *          the beanConnectorFactory to set.
   * @deprecated model connector is now created by the backend controller.
   */
  @SuppressWarnings({"EmptyMethod", "UnusedParameters"})
  @Deprecated
  public void setModelConnectorFactory(
      IModelConnectorFactory modelConnectorFactory) {
    // this.modelConnectorFactory = modelConnectorFactory;
  }

  private List<IDisplayableAction> createActionProxies(
      ITranslationProvider translationProvider, Locale locale) {
    List<IDisplayableAction> actionProxies = new ArrayList<>(
        actions.size());
    for (IDisplayableAction action : actions) {
      actionProxies.add(createActionProxy(action, translationProvider, locale));
    }
    return actionProxies;
  }

  private IDisplayableAction createActionProxy(IDisplayableAction delegate,
      ITranslationProvider translationProvider, Locale locale) {
    return (IDisplayableAction) Proxy.newProxyInstance(delegate.getClass()
        .getClassLoader(), new Class<?>[] {
      IDisplayableAction.class
    }, new I18nActionInvocationHandler(delegate, translationProvider, locale));
  }

  private static final class I18nActionInvocationHandler implements
      InvocationHandler {

    private final IDisplayableAction   delegate;
    private final Locale               locale;
    private final ITranslationProvider translationProvider;

    private I18nActionInvocationHandler(IDisplayableAction delegate,
        ITranslationProvider translationProvider, Locale locale) {
      this.delegate = delegate;
      this.translationProvider = translationProvider;
      this.locale = locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {
      if (method.getName().equals("getName")) {
        return delegate.getI18nName(translationProvider, locale);
      }
      if (method.getName().equals("getDescription")) {
        return delegate.getI18nDescription(translationProvider, locale);
      }
      return method.invoke(delegate, args);
    }
  }
}
