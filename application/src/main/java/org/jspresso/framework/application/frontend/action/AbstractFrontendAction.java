/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.application.action.AbstractAction;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.action.IDisplayableAction;

import com.d2s.framework.action.ActionContextConstants;

/**
 * Base class for frontend actions.
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
public abstract class AbstractFrontendAction<E, F, G> extends AbstractAction
    implements IDisplayableAction {

  private String                acceleratorAsString;
  private Collection<IGate>     actionabilityGates;
  private DefaultIconDescriptor actionDescriptor;
  private String                mnemonicAsString;

  /**
   * Constructs a new <code>AbstractFrontendAction</code> instance.
   */
  public AbstractFrontendAction() {
    actionDescriptor = new DefaultIconDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final IDisplayableAction other = (IDisplayableAction) obj;
    if (getName() == null) {
      return false;
    } else if (!getName().equals(other.getName())) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public String getAcceleratorAsString() {
    return acceleratorAsString;
  }

  /**
   * Gets the actionabilityGates.
   * 
   * @return the actionabilityGates.
   */
  public Collection<IGate> getActionabilityGates() {
    return actionabilityGates;
  }

  /**
   * {@inheritDoc}
   */
  public String getDescription() {
    return actionDescriptor.getDescription();
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    if (getDescription() != null) {
      return translationProvider.getTranslation(getDescription(), locale);
    }
    return getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    return translationProvider.getTranslation(getName(), locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getIconImageURL() {
    return actionDescriptor.getIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Locale getLocale(Map<String, Object> context) {
    return getController(context).getLocale();
  }

  /**
   * {@inheritDoc}
   */
  public String getMnemonicAsString() {
    return mnemonicAsString;
  }

  /**
   * This is a utility method which is able to retrieve the module view
   * connector this action has been executed on from its context. It uses
   * well-known context keys of the action context which are:
   * <ul>
   * <li> <code>ActionContextConstants.MODULE_VIEW_CONNECTOR</code> to get the
   * the module view connector the action executes on.
   * </ul>
   * <p>
   * The returned connector mainly serves for acting on the view component the
   * action has to be triggered on.
   * 
   * @param context
   *            the action context.
   * @return the value connector this model action was triggered on.
   */
  public ICompositeValueConnector getModuleConnector(Map<String, Object> context) {
    return (ICompositeValueConnector) context
        .get(ActionContextConstants.MODULE_VIEW_CONNECTOR);
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return actionDescriptor.getName();
  }

  /**
   * This is a utility method which is able to retrieve the view connector this
   * action has been executed on from its context. It uses well-known context
   * keys of the action context which are:
   * <ul>
   * <li> <code>ActionContextConstants.SOURCE_VIEW_CONNECTOR</code> to get the
   * the view value connector the action executes on.
   * </ul>
   * <p>
   * The returned connector mainly serves for acting on the view component the
   * action has to be triggered on.
   * 
   * @param context
   *            the action context.
   * @return the value connector this model action was triggered on.
   */
  public IValueConnector getSourceViewConnector(Map<String, Object> context) {
    return (IValueConnector) context
        .get(ActionContextConstants.SOURCE_VIEW_CONNECTOR);
  }

  /**
   * This is a utility method which is able to retrieve the view connector this
   * action has been executed on from its context. It uses well-known context
   * keys of the action context which are:
   * <ul>
   * <li> <code>ActionContextConstants.VIEW_CONNECTOR</code> to get the the
   * view value connector the action executes on.
   * </ul>
   * <p>
   * The returned connector mainly serves for acting on the view component the
   * action has to be triggered on.
   * 
   * @param context
   *            the action context.
   * @return the value connector this model action was triggered on.
   */
  public IValueConnector getViewConnector(Map<String, Object> context) {
    return (IValueConnector) context.get(ActionContextConstants.VIEW_CONNECTOR);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result;
    if (getName() != null) {
      result += getName().hashCode();
    }
    return result;
  }

  /**
   * Returns false.
   * <p>
   * {@inheritDoc}
   */
  public boolean isBackend() {
    return false;
  }

  /**
   * Sets the acceleratorAsString.
   * 
   * @param acceleratorAsString
   *            the acceleratorAsString to set.
   */
  public void setAcceleratorAsString(String acceleratorAsString) {
    this.acceleratorAsString = acceleratorAsString;
  }

  /**
   * Sets the actionabilityGates.
   * 
   * @param actionabilityGates
   *            the actionabilityGates to set.
   */
  public void setActionabilityGates(Collection<IGate> actionabilityGates) {
    this.actionabilityGates = actionabilityGates;
  }

  /**
   * Sets the description.
   * 
   * @param description
   *            the description to set.
   */
  public void setDescription(String description) {
    actionDescriptor.setDescription(description);
  }

  /**
   * Sets the iconImageURL.
   * 
   * @param iconImageURL
   *            the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    actionDescriptor.setIconImageURL(iconImageURL);
  }

  /**
   * Sets the mnemonic of the action.
   * 
   * @param mnemonicStringRep
   *            the mnemonic to set represented as a string as KeyStroke factory
   *            would parse it.
   */
  public void setMnemonicAsString(String mnemonicStringRep) {
    this.mnemonicAsString = mnemonicStringRep;
  }

  /**
   * Sets the name.
   * 
   * @param name
   *            the name to set.
   */
  public void setName(String name) {
    actionDescriptor.setName(name);
  }

  /**
   * Gets the actionFactory.
   * 
   * @param context
   *            the action context.
   * @return the actionFactory.
   */
  protected IActionFactory<G, E> getActionFactory(Map<String, Object> context) {
    return getViewFactory(context).getActionFactory();
  }

  /**
   * Gets the frontend controller out of the action context.
   * 
   * @param context
   *            the action context.
   * @return the frontend controller.
   */
  @Override
  @SuppressWarnings("unchecked")
  protected IFrontendController<E, F, G> getController(
      Map<String, Object> context) {
    return (IFrontendController<E, F, G>) context
        .get(ActionContextConstants.FRONT_CONTROLLER);
  }

  /**
   * Gets the iconFactory.
   * 
   * @param context
   *            the action context.
   * @return the iconFactory.
   */
  protected IIconFactory<F> getIconFactory(Map<String, Object> context) {
    return getViewFactory(context).getIconFactory();
  }

  /**
   * Gets the mvcBinder.
   * 
   * @param context
   *            the action context.
   * @return the mvcBinder.
   */
  protected IMvcBinder getMvcBinder(Map<String, Object> context) {
    return getController(context).getMvcBinder();
  }

  /**
   * Gets the viewFactory.
   * 
   * @param context
   *            the action context.
   * @return the viewFactory.
   */
  protected IViewFactory<E, F, G> getViewFactory(Map<String, Object> context) {
    return getController(context).getViewFactory();
  }
}
