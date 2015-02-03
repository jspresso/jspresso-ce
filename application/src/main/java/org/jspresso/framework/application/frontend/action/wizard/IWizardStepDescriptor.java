/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.wizard;

import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.util.descriptor.IDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * The descriptor contract for a wizard view step.
 * 
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("UnusedParameters")
public interface IWizardStepDescriptor extends IDescriptor {

  /**
   * {@code DEFAULT_NEXT_KEY}.
   */
  String DEFAULT_NEXT_KEY     = "next";
  /**
   * {@code DEFAULT_PREVIOUS_KEY}.
   */
  String DEFAULT_PREVIOUS_KEY = "previous";

  /**
   * {@code INITIAL_WIZARD_MODEL} is a context entry used to store the
   * initial wizard model.
   */
  String INITIAL_WIZARD_MODEL = "INITIAL_WIZARD_MODEL";

  /**
   * Gets whether this wizard step descriptor can finish the wizard.
   * 
   * @param context
   *          the current value of the context.
   * @return whether this wizard step descriptor can finish the wizard.
   */
  boolean canFinish(Map<String, Object> context);

  /**
   * Gets the label key to use for the next action. Defaults to "next" if null.
   * 
   * @return the label key to use for the next action.
   */
  String getNextLabelKey();

  /**
   * Gets the next wizard step descriptor if any.
   * 
   * @param context
   *          the current value of the context.
   * @return the next wizard step descriptor if any.
   */
  IWizardStepDescriptor getNextStepDescriptor(Map<String, Object> context);

  /**
   * Gets the action, if any, triggered by entering this step.
   * 
   * @return the action, if any, triggered by entering this step.
   */
  IAction getOnEnterAction();

  /**
   * Gets the action, if any, triggered by leaving this step.
   * 
   * @return the action, if any, triggered by leaving this step.
   */
  IAction getOnLeaveAction();

  /**
   * Gets the label key to use for the previous action. Defaults to "back" if
   * null.
   * 
   * @return the label key to use for the previous action.
   */
  String getPreviousLabelKey();

  /**
   * Gets the previous wizard step descriptor if any.
   * 
   * @param context
   *          the current value of the context.
   * @return the previous wizard step descriptor if any.
   */
  IWizardStepDescriptor getPreviousStepDescriptor(Map<String, Object> context);

  /**
   * Gets the view to display in the wizard view.
   * 
   * @return the view to display in the wizard view.
   */
  IViewDescriptor getViewDescriptor();

}
