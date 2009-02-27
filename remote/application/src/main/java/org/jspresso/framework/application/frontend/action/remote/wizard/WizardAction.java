/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.remote.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.AbstractFrontendAction;
import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.application.frontend.action.wizard.IWizardStepDescriptor;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.util.collection.ObjectEqualityMap;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.IView;
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

  private IDisplayableAction     finishAction;
  private IDisplayableAction     cancelAction;
  private IWizardStepDescriptor  firstWizardStep;
  private IModelConnectorFactory modelConnectorFactory;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    IValueConnector modelConnector = modelConnectorFactory
        .createModelConnector(ACTION_MODEL_NAME, firstWizardStep
            .getViewDescriptor().getModelDescriptor());
    Map<String, Object> wizardModelInit = (Map<String, Object>) context
        .get(ActionContextConstants.ACTION_PARAM);
    Map<String, Object> wizardModel = new ObjectEqualityMap<String, Object>();
    if (wizardModelInit != null) {
      wizardModel.putAll(wizardModelInit);
    }
    completeInitialWizardModel(wizardModel, context);
    modelConnector.setConnectorValue(wizardModel);
    context.put(ActionContextConstants.ACTION_PARAM, wizardModel);
    displayWizardStep(firstWizardStep, modelConnector, actionHandler, context);
    return super.execute(actionHandler, context);
  }

  private void displayWizardStep(IWizardStepDescriptor wizardStep,
      IValueConnector modelConnector, IActionHandler actionHandler,
      Map<String, Object> context) {

    ITranslationProvider translationProvider = getTranslationProvider(context);
    Locale locale = getLocale(context);
    IView<RComponent> view = getViewFactory(context).createView(
        wizardStep.getViewDescriptor(), actionHandler, getLocale(context));
    getMvcBinder(context).bind(view.getConnector(), modelConnector);

    String title = getI18nName(translationProvider, locale) + " - "
        + wizardStep.getI18nName(translationProvider, locale);
    getController(context).displayModalDialog(
        view.getPeer(),
        createWizardStepActions(wizardStep, view, actionHandler,
            translationProvider, locale, modelConnector, context), title,
        getSourceComponent(context), context, true);
  }

  private List<RAction> createWizardStepActions(
      IWizardStepDescriptor wizardStep, IView<RComponent> view,
      IActionHandler actionHandler, ITranslationProvider translationProvider,
      Locale locale, IValueConnector modelConnector, Map<String, Object> context) {

    List<RAction> wizardStepActions = new ArrayList<RAction>();

    RAction previousRAction = createPreviousAction(wizardStep, actionHandler,
        view, translationProvider, locale, modelConnector, context);
    RAction nextRAction = createNextAction(wizardStep, actionHandler, view,
        translationProvider, locale, modelConnector, context);
    RAction cancelRAction = createCancelAction(wizardStep, actionHandler, view,
        locale, context);
    RAction finishRAction = createFinishAction(wizardStep, actionHandler, view,
        locale, context);

    wizardStepActions.add(previousRAction);
    wizardStepActions.add(nextRAction);
    wizardStepActions.add(finishRAction);
    wizardStepActions.add(cancelRAction);

    return wizardStepActions;
  }

  private RAction createFinishAction(IWizardStepDescriptor wizardStep,
      IActionHandler actionHandler, IView<RComponent> view, Locale locale,
      Map<String, Object> context) {
    IDisplayableAction finishActionAdapter = new FinishAction(wizardStep,
        finishAction);
    RAction finishRAction = getActionFactory(context).createAction(
        finishActionAdapter, actionHandler, view, locale);
    if (wizardStep.canFinish(context)) {
      finishRAction.setEnabled(true);
    } else {
      finishRAction.setEnabled(false);
    }
    return finishRAction;
  }

  private RAction createCancelAction(IWizardStepDescriptor wizardStep,
      IActionHandler actionHandler, IView<RComponent> view, Locale locale,
      Map<String, Object> context) {
    IDisplayableAction cancelActionAdapter = new CancelAction(wizardStep,
        cancelAction);
    RAction cancelRAction = getActionFactory(context).createAction(
        cancelActionAdapter, actionHandler, view, locale);
    return cancelRAction;
  }

  private RAction createNextAction(IWizardStepDescriptor wizardStep,
      IActionHandler actionHandler, IView<RComponent> view,
      ITranslationProvider translationProvider, Locale locale,
      IValueConnector modelConnector, Map<String, Object> context) {
    IDisplayableAction nextAction = new NextAction(wizardStep, modelConnector);
    RAction nextRAction = getActionFactory(context).createAction(nextAction,
        actionHandler, view, locale);
    if (wizardStep.getNextStepDescriptor(context) != null) {
      nextRAction.setEnabled(true);
    } else {
      nextRAction.setEnabled(false);
    }
    if (wizardStep.getNextLabelKey() != null) {
      nextRAction.setName(translationProvider.getTranslation(wizardStep
          .getNextLabelKey(), locale));
    } else {
      nextRAction.setName(translationProvider.getTranslation(
          IWizardStepDescriptor.DEFAULT_NEXT_KEY, locale));
    }
    return nextRAction;
  }

  private RAction createPreviousAction(IWizardStepDescriptor wizardStep,
      IActionHandler actionHandler, IView<RComponent> view,
      ITranslationProvider translationProvider, Locale locale,
      IValueConnector modelConnector, Map<String, Object> context) {
    IDisplayableAction previousAction = new PreviousAction(wizardStep,
        modelConnector);
    RAction previousRAction = getActionFactory(context).createAction(
        previousAction, actionHandler, view, locale);
    if (wizardStep.getPreviousStepDescriptor(context) != null) {
      previousRAction.setEnabled(true);
    } else {
      previousRAction.setEnabled(false);
    }
    if (wizardStep.getPreviousLabelKey() != null) {
      previousRAction.setName(translationProvider.getTranslation(wizardStep
          .getPreviousLabelKey(), locale));
    } else {
      previousRAction.setName(translationProvider.getTranslation(
          IWizardStepDescriptor.DEFAULT_PREVIOUS_KEY, locale));
    }
    return previousRAction;
  }

  /**
   * Creates (and initializes) the wizard model.
   * 
   * @param initialWizardModel
   *          the initial wizard model.
   * @param context
   *          the action context.
   */
  protected void completeInitialWizardModel(
      Map<String, Object> initialWizardModel, Map<String, Object> context) {
    // No-op by default.
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

  /**
   * Sets the cancelAction.
   * 
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(IDisplayableAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  private class PreviousAction extends
      AbstractFrontendAction<RComponent, RIcon, RAction> {

    private IWizardStepDescriptor wizardStep;
    private IValueConnector       modelConnector;

    public PreviousAction(IWizardStepDescriptor wizardStep,
        IValueConnector modelConnector) {
      this.wizardStep = wizardStep;
      this.modelConnector = modelConnector;
    }

    @Override
    public boolean execute(IActionHandler actionHandler,
        Map<String, Object> context) {
      if (wizardStep.getOnLeaveAction() == null
          || actionHandler.execute(wizardStep.getOnLeaveAction(), context)) {
        IWizardStepDescriptor nextWizardStep = wizardStep
            .getNextStepDescriptor(context);
        displayWizardStep(nextWizardStep, modelConnector, actionHandler,
            context);
        if (nextWizardStep.getOnEnterAction() != null) {
          actionHandler.execute(nextWizardStep.getOnEnterAction(), context);
        }
      }
      return true;
    }
  }

  private class NextAction extends
      AbstractFrontendAction<RComponent, RIcon, RAction> {

    private IWizardStepDescriptor wizardStep;
    private IValueConnector       modelConnector;

    public NextAction(IWizardStepDescriptor wizardStep,
        IValueConnector modelConnector) {
      this.wizardStep = wizardStep;
      this.modelConnector = modelConnector;
    }

    @Override
    public boolean execute(IActionHandler actionHandler,
        Map<String, Object> context) {
      IWizardStepDescriptor previousWizardStep = wizardStep
          .getPreviousStepDescriptor(context);
      displayWizardStep(previousWizardStep, modelConnector, actionHandler,
          context);
      return true;
    }
  }

  private class FinishAction extends
      AbstractFrontendAction<RComponent, RIcon, RAction> {

    private IWizardStepDescriptor wizardStep;
    private IDisplayableAction    wrappedFinishAction;

    public FinishAction(IWizardStepDescriptor wizardStep,
        IDisplayableAction wrappedFinishAction) {
      this.wizardStep = wizardStep;
      this.wrappedFinishAction = wrappedFinishAction;
    }

    @Override
    public boolean execute(IActionHandler actionHandler,
        Map<String, Object> context) {
      if (wizardStep.getOnLeaveAction() == null
          || actionHandler.execute(wizardStep.getOnLeaveAction(), context)) {
        getController(context).disposeModalDialog(getSourceComponent(context),
            context);
        actionHandler.execute(wrappedFinishAction, context);
      }
      return true;
    }
  }

  private class CancelAction extends
      AbstractFrontendAction<RComponent, RIcon, RAction> {

    @SuppressWarnings("unused")
    private IWizardStepDescriptor wizardStep;
    private IDisplayableAction    wrappedCancelAction;

    public CancelAction(IWizardStepDescriptor wizardStep,
        IDisplayableAction wrappedCancelAction) {
      this.wizardStep = wizardStep;
      this.wrappedCancelAction = wrappedCancelAction;
    }

    @Override
    public boolean execute(IActionHandler actionHandler,
        Map<String, Object> context) {
      actionHandler.execute(wrappedCancelAction, context);
      return true;
    }
  }
}
