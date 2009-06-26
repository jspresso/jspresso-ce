/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.AbstractFrontendAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.util.collection.ObjectEqualityMap;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * A Wizard action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision: 1302 $
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class WizardAction<E, F, G> extends AbstractFrontendAction<E, F, G> {

  private IDisplayableAction     cancelAction;
  private IDisplayableAction     finishAction;
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
        .get(IWizardStepDescriptor.INITIAL_WIZARD_MODEL);
    Map<String, Object> wizardModel = new ObjectEqualityMap<String, Object>();
    if (wizardModelInit != null) {
      wizardModel.putAll(wizardModelInit);
    }
    completeInitialWizardModel(wizardModel, context);
    modelConnector.setConnectorValue(wizardModel);
    displayWizardStep(firstWizardStep, modelConnector, actionHandler, context,
        false);
    return true;
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

  private G createCancelAction(IWizardStepDescriptor wizardStep,
      IActionHandler actionHandler, IView<E> view, Locale locale,
      Map<String, Object> context) {
    IDisplayableAction cancelActionAdapter = new CancelAction(wizardStep,
        cancelAction);
    G cancelGAction = getActionFactory(context).createAction(
        cancelActionAdapter, actionHandler, view, locale);
    return cancelGAction;
  }

  private G createFinishAction(IWizardStepDescriptor wizardStep,
      IActionHandler actionHandler, IView<E> view, Locale locale,
      Map<String, Object> context) {
    IDisplayableAction finishActionAdapter = new FinishAction(wizardStep,
        finishAction);
    G finishGAction = getActionFactory(context).createAction(
        finishActionAdapter, actionHandler, view, locale);
    if (wizardStep.canFinish(context)) {
      getActionFactory(context).setActionEnabled(finishGAction, true);
    } else {
      getActionFactory(context).setActionEnabled(finishGAction, false);
    }
    return finishGAction;
  }

  private G createNextAction(IWizardStepDescriptor wizardStep,
      IActionHandler actionHandler, IView<E> view,
      ITranslationProvider translationProvider, Locale locale,
      IValueConnector modelConnector, Map<String, Object> context) {
    NextAction nextAction = new NextAction(wizardStep, modelConnector);
    nextAction
        .setIconImageURL(getIconFactory(context).getForwardIconImageURL());
    G nextGAction = getActionFactory(context).createAction(nextAction,
        actionHandler, view, locale);
    if (wizardStep.getNextStepDescriptor(context) != null) {
      getActionFactory(context).setActionEnabled(nextGAction, true);
    } else {
      getActionFactory(context).setActionEnabled(nextGAction, false);
    }
    if (wizardStep.getNextLabelKey() != null) {
      getActionFactory(context).setActionName(
          nextGAction,
          translationProvider.getTranslation(wizardStep.getNextLabelKey(),
              locale));
    } else {
      getActionFactory(context).setActionName(
          nextGAction,
          translationProvider.getTranslation(
              IWizardStepDescriptor.DEFAULT_NEXT_KEY, locale));
    }
    return nextGAction;
  }

  private G createPreviousAction(IWizardStepDescriptor wizardStep,
      IActionHandler actionHandler, IView<E> view,
      ITranslationProvider translationProvider, Locale locale,
      IValueConnector modelConnector, Map<String, Object> context) {
    PreviousAction previousAction = new PreviousAction(wizardStep,
        modelConnector);
    previousAction.setIconImageURL(getIconFactory(context)
        .getBackwardIconImageURL());
    G previousGAction = getActionFactory(context).createAction(previousAction,
        actionHandler, view, locale);
    if (wizardStep.getPreviousStepDescriptor(context) != null) {
      getActionFactory(context).setActionEnabled(previousGAction, true);
    } else {
      getActionFactory(context).setActionEnabled(previousGAction, false);
    }
    if (wizardStep.getPreviousLabelKey() != null) {
      getActionFactory(context).setActionName(
          previousGAction,
          translationProvider.getTranslation(wizardStep.getPreviousLabelKey(),
              locale));
    } else {
      getActionFactory(context).setActionName(
          previousGAction,
          translationProvider.getTranslation(
              IWizardStepDescriptor.DEFAULT_PREVIOUS_KEY, locale));
    }
    return previousGAction;
  }

  private List<G> createWizardStepActions(IWizardStepDescriptor wizardStep,
      IView<E> view, IActionHandler actionHandler,
      ITranslationProvider translationProvider, Locale locale,
      IValueConnector modelConnector, Map<String, Object> context) {

    List<G> wizardStepActions = new ArrayList<G>();

    G previousGAction = createPreviousAction(wizardStep, actionHandler, view,
        translationProvider, locale, modelConnector, context);
    G nextGAction = createNextAction(wizardStep, actionHandler, view,
        translationProvider, locale, modelConnector, context);
    G cancelGAction = createCancelAction(wizardStep, actionHandler, view,
        locale, context);
    G finishGAction = createFinishAction(wizardStep, actionHandler, view,
        locale, context);

    wizardStepActions.add(previousGAction);
    wizardStepActions.add(nextGAction);
    wizardStepActions.add(finishGAction);
    wizardStepActions.add(cancelGAction);

    return wizardStepActions;
  }

  private void displayWizardStep(IWizardStepDescriptor wizardStep,
      IValueConnector modelConnector, IActionHandler actionHandler,
      Map<String, Object> context, boolean reuseCurrent) {

    ITranslationProvider translationProvider = getTranslationProvider(context);
    Locale locale = getLocale(context);
    IView<E> view = getViewFactory(context).createView(
        wizardStep.getViewDescriptor(), actionHandler, getLocale(context));
    getMvcBinder(context).bind(view.getConnector(), modelConnector);

    String title = getI18nName(translationProvider, locale) + " - "
        + wizardStep.getI18nName(translationProvider, locale);
    Dimension dialogSize = (Dimension) context
        .get(ActionContextConstants.DIALOG_SIZE);
    getController(context).displayModalDialog(
        view.getPeer(),
        createWizardStepActions(wizardStep, view, actionHandler,
            translationProvider, locale, modelConnector, context), title,
        getSourceComponent(context), context, dialogSize, reuseCurrent);
  }

  private class CancelAction extends AbstractFrontendAction<E, F, G> {

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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
      return wrappedCancelAction.getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIconImageURL() {
      return wrappedCancelAction.getIconImageURL();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
      return wrappedCancelAction.getName();
    }
  }

  private class FinishAction extends AbstractFrontendAction<E, F, G> {

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
        context.put(ActionContextConstants.ACTION_PARAM, getViewConnector(
            context).getConnectorValue());
        actionHandler.execute(wrappedFinishAction, context);
      }
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
      return wrappedFinishAction.getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIconImageURL() {
      return wrappedFinishAction.getIconImageURL();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
      return wrappedFinishAction.getName();
    }
  }

  private class NextAction extends AbstractFrontendAction<E, F, G> {

    private IValueConnector       modelConnector;
    private IWizardStepDescriptor wizardStep;

    public NextAction(IWizardStepDescriptor wizardStep,
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
            context, true);
        if (nextWizardStep.getOnEnterAction() != null) {
          actionHandler.execute(nextWizardStep.getOnEnterAction(), context);
        }
      }
      return true;
    }
  }

  private class PreviousAction extends AbstractFrontendAction<E, F, G> {

    private IValueConnector       modelConnector;
    private IWizardStepDescriptor wizardStep;

    public PreviousAction(IWizardStepDescriptor wizardStep,
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
          context, true);
      return true;
    }
  }
}
