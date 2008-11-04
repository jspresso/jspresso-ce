/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.remote.wizard;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.application.frontend.action.wizard.IWizardStepDescriptor;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * A Wizard remote action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class WizardAction extends AbstractRemoteAction {

  @SuppressWarnings("unused")
  private IDisplayableAction     finishAction;
  @SuppressWarnings("unused")
  private IWizardStepDescriptor  firstWizardStep;
  @SuppressWarnings("unused")
  private IModelConnectorFactory modelConnectorFactory;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the finishAction.
   * 
   * @param finishAction
   *          the finishAction to set.
   */
  public void setFinishAction(IDisplayableAction finishAction) {
    this.finishAction = finishAction;
  }

  /**
   * Sets the firstWizardStep.
   * 
   * @param firstWizardStep
   *          the firstWizardStep to set.
   */
  public void setFirstWizardStep(IWizardStepDescriptor firstWizardStep) {
    this.firstWizardStep = firstWizardStep;
  }

  /**
   * Sets the modelConnectorFactory.
   * 
   * @param modelConnectorFactory
   *          the modelConnectorFactory to set.
   */
  public void setModelConnectorFactory(
      IModelConnectorFactory modelConnectorFactory) {
    this.modelConnectorFactory = modelConnectorFactory;
  }
}
