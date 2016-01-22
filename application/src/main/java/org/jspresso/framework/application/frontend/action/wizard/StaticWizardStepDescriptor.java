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
package org.jspresso.framework.application.frontend.action.wizard;

import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.util.descriptor.DefaultDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A static wizard step.
 *
 * @author Vincent Vandenschrick
 */
public class StaticWizardStepDescriptor implements IWizardStepDescriptor {

  private final DefaultDescriptor     descriptor;
  private String                nextLabelKey;
  private IWizardStepDescriptor nextStepDescriptor;

  private IAction               onEnterAction;
  private IAction               onLeaveAction;

  private String                previousLabelKey;
  private IWizardStepDescriptor previousStepDescriptor;

  private IViewDescriptor       viewDescriptor;

  /**
   * Constructs a new {@code StaticWizardStepDescriptor} instance.
   */
  public StaticWizardStepDescriptor() {
    descriptor = new DefaultDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canFinish(Map<String, Object> context) {
    return getNextStepDescriptor(context) == null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    return descriptor.getDescription();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    if (getDescription() != null) {
      return translationProvider.getTranslation(getDescription(), "", locale);
    }
    return getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    return translationProvider.getTranslation(getName(), locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return descriptor.getName();
  }

  /**
   * Gets the nextLabelKey.
   *
   * @return the nextLabelKey.
   */
  @Override
  public String getNextLabelKey() {
    return nextLabelKey;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IWizardStepDescriptor getNextStepDescriptor(Map<String, Object> context) {
    return nextStepDescriptor;
  }

  /**
   * Gets the onEnterAction.
   *
   * @return the onEnterAction.
   */
  @Override
  public IAction getOnEnterAction() {
    return onEnterAction;
  }

  /**
   * Gets the onLeaveAction.
   *
   * @return the onLeaveAction.
   */
  @Override
  public IAction getOnLeaveAction() {
    return onLeaveAction;
  }

  /**
   * Gets the previousLabelKey.
   *
   * @return the previousLabelKey.
   */
  @Override
  public String getPreviousLabelKey() {
    return previousLabelKey;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IWizardStepDescriptor getPreviousStepDescriptor(

  Map<String, Object> context) {
    return previousStepDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getViewDescriptor() {
    return viewDescriptor;
  }

  /**
   * Sets the description.
   * @param description
   *          the description to set.
   * @see org.jspresso.framework.util.descriptor.DefaultDescriptor#setDescription(java.lang.String)
   */
  public void setDescription(String description) {
    descriptor.setDescription(description);
  }

  /**
   * Sets the name.
   * @param name
   *          the name to set.
   * @see org.jspresso.framework.util.descriptor.DefaultDescriptor#setName(java.lang.String)
   */
  public void setName(String name) {
    descriptor.setName(name);
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
   * Sets the onEnterAction.
   *
   * @param onEnterAction
   *          the onEnterAction to set.
   */
  public void setOnEnterAction(IAction onEnterAction) {
    this.onEnterAction = onEnterAction;
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
   * Sets the previousLabelKey.
   *
   * @param previousLabelKey
   *          the previousLabelKey to set.
   */
  public void setPreviousLabelKey(String previousLabelKey) {
    this.previousLabelKey = previousLabelKey;
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
   * Gets the lastUpdated.
   *
   * @return the lastUpdated.
   */
  @Override
  public long getLastUpdated() {
    return descriptor.getLastUpdated();
  }

  /**
   * Sets the lastUpdated.
   *
   * @param lastUpdated
   *          the lastUpdated to set.
   * @internal
   */
  public void setLastUpdated(long lastUpdated) {
    descriptor.setLastUpdated(lastUpdated);
  }
}
