/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action;

import com.d2s.framework.action.AbstractAction;
import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IDisplayableAction;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IMvcBinder;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.util.descriptor.DefaultIconDescriptor;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IViewFactory;

/**
 * Base class for frontend actions.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractFrontendAction extends AbstractAction implements
    IDisplayableAction {

  private String                mnemonicAsString;
  private String                acceleratorAsString;
  private DefaultIconDescriptor actionDescriptor;
  private IMvcBinder            mvcBinder;
  private IViewFactory          viewFactory;
  private IIconFactory          iconFactory;

  /**
   * Constructs a new <code>AbstractFrontendAction</code> instance.
   */
  public AbstractFrontendAction() {
    actionDescriptor = new DefaultIconDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  public String getMnemonicAsString() {
    return mnemonicAsString;
  }

  /**
   * Sets the mnemonic of the action.
   * 
   * @param mnemonicStringRep
   *          the mnemonic to set represented as a string as KeyStroke factory
   *          would parse it.
   */
  public void setMnemonicAsString(String mnemonicStringRep) {
    this.mnemonicAsString = mnemonicStringRep;
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
  public String getIconImageURL() {
    return actionDescriptor.getIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return actionDescriptor.getName();
  }

  /**
   * Sets the description.
   * 
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    actionDescriptor.setDescription(description);
  }

  /**
   * Sets the iconImageURL.
   * 
   * @param iconImageURL
   *          the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    actionDescriptor.setIconImageURL(iconImageURL);
  }

  /**
   * Sets the name.
   * 
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    actionDescriptor.setName(name);
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
   * @return the value connector this model action was triggered on.
   */
  public IValueConnector getViewConnector() {
    return (IValueConnector) getContext().get(
        ActionContextConstants.VIEW_CONNECTOR);
  }

  /**
   * This is a utility method which is able to retrieve the projection view
   * connector this action has been executed on from its context. It uses
   * well-known context keys of the action context which are:
   * <ul>
   * <li> <code>ActionContextConstants.PROJECTION_VIEW_CONNECTOR</code> to get
   * the the projection view connector the action executes on.
   * </ul>
   * <p>
   * The returned connector mainly serves for acting on the view component the
   * action has to be triggered on.
   * 
   * @return the value connector this model action was triggered on.
   */
  public ICompositeValueConnector getProjectionConnector() {
    return (ICompositeValueConnector) getContext().get(
        ActionContextConstants.PROJECTION_VIEW_CONNECTOR);
  }

  /**
   * This is a utility method which is able to retrieve the parent projection
   * view connector this action has been executed on from its context. It uses
   * well-known context keys of the action context which are:
   * <ul>
   * <li> <code>ActionContextConstants.PARENT_PROJECTION_VIEW_CONNECTOR</code>
   * to get the the parent projection view connector the action executes on.
   * </ul>
   * <p>
   * The returned connector mainly serves for acting on the view component the
   * action has to be triggered on.
   * 
   * @return the parent value connector this model action was triggered on.
   */
  public ICompositeValueConnector getParentProjectionConnector() {
    return (ICompositeValueConnector) getContext().get(
        ActionContextConstants.PARENT_PROJECTION_VIEW_CONNECTOR);
  }

  /**
   * {@inheritDoc}
   */
  public String getAcceleratorAsString() {
    return acceleratorAsString;
  }

  /**
   * Sets the acceleratorAsString.
   * 
   * @param acceleratorAsString
   *          the acceleratorAsString to set.
   */
  public void setAcceleratorAsString(String acceleratorAsString) {
    this.acceleratorAsString = acceleratorAsString;
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
   * Gets the mvcBinder.
   * 
   * @return the mvcBinder.
   */
  protected IMvcBinder getMvcBinder() {
    return mvcBinder;
  }

  /**
   * Sets the mvcBinder.
   * 
   * @param mvcBinder
   *          the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
  }

  /**
   * Gets the viewFactory.
   * 
   * @return the viewFactory.
   */
  protected IViewFactory getViewFactory() {
    return viewFactory;
  }

  /**
   * Sets the viewFactory.
   * 
   * @param viewFactory
   *          the viewFactory to set.
   */
  public void setViewFactory(IViewFactory viewFactory) {
    this.viewFactory = viewFactory;
  }

  /**
   * Gets the iconFactory.
   * 
   * @return the iconFactory.
   */
  protected IIconFactory getIconFactory() {
    return iconFactory;
  }

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory iconFactory) {
    this.iconFactory = iconFactory;
  }
}
