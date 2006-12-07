/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wizard;

import java.util.Locale;
import java.util.Map;

import com.d2s.framework.action.IAction;
import com.d2s.framework.util.descriptor.DefaultDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * A static wizard step.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class StaticWizardStepDescriptor implements IWizardStepDescriptor {

  private IWizardStepDescriptor nextStepDescriptor;
  private IWizardStepDescriptor previousStepDescriptor;
  private IViewDescriptor       viewDescriptor;

  private IAction               onEnterAction;
  private IAction               onLeaveAction;

  private String                nextLabelKey;
  private String                previousLabelKey;

  private DefaultDescriptor     descriptor;

  /**
   * Constructs a new <code>StaticWizardStepDescriptor</code> instance.
   */
  public StaticWizardStepDescriptor() {
    descriptor = new DefaultDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  public IWizardStepDescriptor getNextStepDescriptor(
      @SuppressWarnings("unused")
      Map<String, Object> context) {
    return nextStepDescriptor;
  }

  /**
   * Sets the nextStepDescriptor.
   * 
   * @param nextStepDescriptor
   *          the nextStepDescriptor to set.
   */
  public void setNextStepDescriptor(IWizardStepDescriptor nextStepDescriptor) {
    this.nextStepDescriptor = nextStepDescriptor;
    if (nextStepDescriptor instanceof StaticWizardStepDescriptor) {
      ((StaticWizardStepDescriptor) nextStepDescriptor).previousStepDescriptor = this;
    }
  }

  /**
   * {@inheritDoc}
   */
  public IWizardStepDescriptor getPreviousStepDescriptor(
      @SuppressWarnings("unused")
      Map<String, Object> context) {
    return previousStepDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getViewDescriptor() {
    return viewDescriptor;
  }

  /**
   * Sets the viewDescriptor.
   * 
   * @param viewDescriptor
   *          the viewDescriptor to set.
   */
  public void setViewDescriptor(IViewDescriptor viewDescriptor) {
    this.viewDescriptor = viewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public boolean canFinish(Map<String, Object> context) {
    return getNextStepDescriptor(context) == null;
  }

  /**
   * Gets the onEnterAction.
   * 
   * @return the onEnterAction.
   */
  public IAction getOnEnterAction() {
    return onEnterAction;
  }

  /**
   * Sets the onEnterAction.
   * 
   * @param onEnterAction
   *          the onEnterAction to set.
   */
  public void setOnEnterAction(IAction onEnterAction) {
    this.onEnterAction = onEnterAction;
  }

  /**
   * Gets the onLeaveAction.
   * 
   * @return the onLeaveAction.
   */
  public IAction getOnLeaveAction() {
    return onLeaveAction;
  }

  /**
   * Sets the onLeaveAction.
   * 
   * @param onLeaveAction
   *          the onLeaveAction to set.
   */
  public void setOnLeaveAction(IAction onLeaveAction) {
    this.onLeaveAction = onLeaveAction;
  }

  /**
   * Gets the nextLabelKey.
   * 
   * @return the nextLabelKey.
   */
  public String getNextLabelKey() {
    return nextLabelKey;
  }

  /**
   * Sets the nextLabelKey.
   * 
   * @param nextLabelKey
   *          the nextLabelKey to set.
   */
  public void setNextLabelKey(String nextLabelKey) {
    this.nextLabelKey = nextLabelKey;
  }

  /**
   * Gets the previousLabelKey.
   * 
   * @return the previousLabelKey.
   */
  public String getPreviousLabelKey() {
    return previousLabelKey;
  }

  /**
   * Sets the previousLabelKey.
   * 
   * @param previousLabelKey
   *          the previousLabelKey to set.
   */
  public void setPreviousLabelKey(String previousLabelKey) {
    this.previousLabelKey = previousLabelKey;
  }

  /**
   * {@inheritDoc}
   */
  public String getDescription() {
    return descriptor.getDescription();
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    return descriptor.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    return descriptor.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return descriptor.getName();
  }

  /**
   * @param description
   *          the description to set.
   * @see com.d2s.framework.util.descriptor.DefaultDescriptor#setDescription(java.lang.String)
   */
  public void setDescription(String description) {
    descriptor.setDescription(description);
  }

  /**
   * @param name
   *          the name to set.
   * @see com.d2s.framework.util.descriptor.DefaultDescriptor#setName(java.lang.String)
   */
  public void setName(String name) {
    descriptor.setName(name);
  }

}
