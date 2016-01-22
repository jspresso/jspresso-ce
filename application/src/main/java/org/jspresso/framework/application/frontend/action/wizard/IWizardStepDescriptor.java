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
