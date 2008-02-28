/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.flow;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.model.IModelConnectorFactory;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicDescriptorDescriptor;
import com.d2s.framework.util.descriptor.IDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.action.IDisplayableAction;

/**
 * Frontend action to select an action and launch it.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the actual gui component type used.
 * @param <F>
 *            the actual icon type used.
 * @param <G>
 *            the actual action type used.
 */
public class ChooseActionAction<E, F, G> extends AbstractChainedAction<E, F, G> {

  private List<IDisplayableAction> actions;

  private IModelConnectorFactory   modelConnectorFactory;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    BasicCollectionDescriptor<IDescriptor> modelDescriptor = new BasicCollectionDescriptor<IDescriptor>();
    modelDescriptor.setCollectionInterface(List.class);
    modelDescriptor.setElementDescriptor(BasicDescriptorDescriptor.INSTANCE);
    modelDescriptor.setName(ACTION_MODEL_NAME);
    IValueConnector actionsConnector = modelConnectorFactory
        .createModelConnector(ACTION_MODEL_NAME, modelDescriptor);
    actionsConnector.setConnectorValue(createActionProxies(
        getTranslationProvider(context), getLocale(context)));
    context.put(ActionContextConstants.ACTION_PARAM, actionsConnector);
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the actions.
   * 
   * @param actions
   *            the actions to set.
   */
  public void setActions(List<IDisplayableAction> actions) {
    this.actions = actions;
  }

  /**
   * Sets the modelConnectorFactory.
   * 
   * @param modelConnectorFactory
   *            the beanConnectorFactory to set.
   */
  public void setModelConnectorFactory(
      IModelConnectorFactory modelConnectorFactory) {
    this.modelConnectorFactory = modelConnectorFactory;
  }

  private List<IDisplayableAction> createActionProxies(
      ITranslationProvider translationProvider, Locale locale) {
    List<IDisplayableAction> actionProxies = new ArrayList<IDisplayableAction>(
        actions.size());
    for (IDisplayableAction action : actions) {
      actionProxies.add(createActionProxy(action, translationProvider, locale));
    }
    return actionProxies;
  }

  private IDisplayableAction createActionProxy(IDisplayableAction delegate,
      ITranslationProvider translationProvider, Locale locale) {
    return (IDisplayableAction) Proxy.newProxyInstance(delegate.getClass()
        .getClassLoader(), new Class[] {IDisplayableAction.class},
        new I18nActionInvocationHandler(delegate, translationProvider, locale));
  }

  private static final class I18nActionInvocationHandler implements
      InvocationHandler {

    private IDisplayableAction   delegate;
    private Locale               locale;
    private ITranslationProvider translationProvider;

    private I18nActionInvocationHandler(IDisplayableAction delegate,
        ITranslationProvider translationProvider, Locale locale) {
      this.delegate = delegate;
      this.translationProvider = translationProvider;
      this.locale = locale;
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(@SuppressWarnings("unused")
    Object proxy, Method method, Object[] args) throws Throwable {
      if (method.getName().equals("getName")) {
        return delegate.getI18nName(translationProvider, locale);
      } else if (method.getName().equals("getDescription")) {
        return delegate.getI18nDescription(translationProvider, locale);
      }
      return method.invoke(delegate, args);
    }
  }
}
