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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.ModalDialogAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IScalarPropertyDescriptor;
import org.jspresso.framework.util.collection.ObjectEqualityMap;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * This action implements a &quot;wizard&quot;. It can be configured from the
 * simplest use-case (as for a value entering) to the most complex, multi-step
 * wizard. Here are a usage directions :
 * <ol>
 * <li>The generic wizard front-end action is registered in the Spring context
 * under the name wizardAction. So you will typically inherit the bean
 * definition using the parent="wizardAction" when declaring yours.</li>
 * <li>The goal of the wizard action is to work on a map - the wizard model -
 * (potentially containing other maps) that represents a hierarchical data
 * structure that can be used seamlessly as model for any Jspresso view. In your
 * case, the map will only contain 1 key-value pair (the property you want your
 * user to enter). When finishing the wizard, the action context will contain
 * the map with all the key-value pairs the user has created/modified through
 * the wizard steps. It will be accessible in the action context under the
 * ActionContextConstants.ACTION_PARAM key and will typically serve as input for
 * the finish chained action.</li>
 * <li>The wizard action is configured using chained
 * org.jspresso.framework.application
 * .frontend.action.wizard.IWizardStepDescriptor. A concrete, directly usable,
 * implementation of this interface is
 * org.jspresso.framework.application.frontend
 * .action.wizard.StaticWizardStepDescriptor. Each wizard step is highly
 * configurable (name, description, icon, ...) but its most important properties
 * are :
 * <ol>
 * <li>{@code viewDescriptor} : the Jspresso view descriptor to be shown in
 * the wizard GUI when the user enters this step. It can be arbitrarily complex
 * (even with master-detail like views, inner actions, constraints, security
 * enforcements, ...). Of course, the view descriptor needs a model descriptor.
 * So you must describe the wizard model as you would do for persistent entities
 * or components (so that step views can configure themselves). You will
 * typically use a BasicComponentDescriptor without name so that it is
 * automatically excluded from code generation. Note that your actual model
 * object will be a map (and not Jspresso generated java bean) but Jspresso
 * connectors are "smart" enough to detect the situation and work with the
 * hierarchy of maps as if it was a hierarchy of java beans.</li>
 * <li>optional {@code onEnterAction} and {@code onLeaveAction} :
 * actions that will respectively be executed when entering and when exiting the
 * wizard step.</li>
 * <li>optional {@code nextLabelKey} and {@code previousLabelKey} :
 * i18n keys for next and previous buttons if you want to change the default
 * ones.</li>
 * <li>optional {@code nextStepDescriptor} : the next wizard step. If null,
 * the wizard GUI will enable the finish action.</li>
 * </ol>
 * </li>
 * <li>The first wizard step is registered on the wizard action using the
 * {@code firstWizardStep} property.</li>
 * <li>When the user leaves the last wizard step (clicking the finish action
 * button), the finish action is triggered. The finish action can be registered
 * on the wizard action using the {@code finishAction} property. This is
 * typically the place where you explore the wizard map model -
 * {@code ACTION_PARAM} - to get back all the data the user has worked on.
 * Note that the finish button is entirely configured from the finish action
 * (label and icon).</li>
 * </ol>
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class WizardAction<E, F, G> extends FrontendAction<E, F, G> {

  private IDisplayableAction    cancelAction;
  private IDisplayableAction    finishAction;
  private IWizardStepDescriptor firstWizardStep;
  private Integer               height;
  private Integer               width;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    IValueConnector modelConnector = getBackendController(context)
        .createModelConnector(ACTION_MODEL_NAME,
            firstWizardStep.getViewDescriptor().getModelDescriptor());
    Map<String, Object> wizardModelInit = (Map<String, Object>) context
        .get(IWizardStepDescriptor.INITIAL_WIZARD_MODEL);
    Map<String, Object> wizardModel = new ObjectEqualityMap<>();
    if (wizardModelInit != null) {
      wizardModel.putAll(wizardModelInit);
    }
    completeInitialWizardModel(wizardModel, context);
    modelConnector.setConnectorValue(wizardModel);
    displayWizardStep(firstWizardStep, modelConnector, actionHandler, context,
        false);
    return super.execute(actionHandler, context);
  }

  /**
   * Configures the action that will be executed whenever the user cancels the
   * wizard.
   *
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(IDisplayableAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Configures the action that will be executed whenever the user validates the
   * wizard.
   *
   * @param finishAction
   *          the finishAction to set.
   */
  public void setFinishAction(IDisplayableAction finishAction) {
    this.finishAction = finishAction;
  }

  /**
   * Configures the first wizard step to display.
   *
   * @param firstWizardStep
   *          the firstWizardStep to set.
   */
  public void setFirstWizardStep(IWizardStepDescriptor firstWizardStep) {
    this.firstWizardStep = firstWizardStep;
  }

  /**
   * Configures explicitly the height of the wizard dialog. It prevents the
   * dialog from resizing dynamically depending on the displayed wizard step.
   *
   * @param height
   *          the height to set.
   */
  public void setHeight(Integer height) {
    this.height = height;
  }

  /**
   * Sets the modelConnectorFactory.
   *
   * @param modelConnectorFactory
   *          the modelConnectorFactory to set.
   * @deprecated model connector is now created by the backend controller.
   */
  @SuppressWarnings({"EmptyMethod", "UnusedParameters"})
  @Deprecated
  public void setModelConnectorFactory(
      IModelConnectorFactory modelConnectorFactory) {
    // this.modelConnectorFactory = modelConnectorFactory;
  }

  /**
   * Configures explicitly the width of the wizard dialog. It prevents the
   * dialog from resizing dynamically depending on the displayed wizard step.
   *
   * @param width
   *          the width to set.
   */
  public void setWidth(Integer width) {
    this.width = width;
  }

  /**
   * Creates (and initializes) the wizard model.
   *
   * @param initialWizardModel
   *          the initial wizard model.
   * @param context
   *          the action context.
   */
  @SuppressWarnings({"EmptyMethod", "UnusedParameters"})
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

    List<G> wizardStepActions = new ArrayList<>();

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
    IModelDescriptor modelDescriptor = wizardStep.getViewDescriptor()
        .getModelDescriptor();
    if (modelDescriptor instanceof IComponentDescriptor<?>) {
      for (IPropertyDescriptor propertyDescriptor : ((IComponentDescriptor<?>) modelDescriptor)
          .getPropertyDescriptors()) {
        if (propertyDescriptor instanceof IScalarPropertyDescriptor
            && ((IScalarPropertyDescriptor) propertyDescriptor)
                .getDefaultValue() != null) {
          Map<String, Object> wizardModel = modelConnector
              .getConnectorValue();
          if (!wizardModel.containsKey(propertyDescriptor.getName())) {
            wizardModel.put(propertyDescriptor.getName(),
                ((IScalarPropertyDescriptor) propertyDescriptor)
                    .getDefaultValue());
          }
        }
      }
    }
    getMvcBinder(context).bind(view.getConnector(), modelConnector);

    String title = getI18nName(translationProvider, locale) + " - "
        + wizardStep.getI18nName(translationProvider, locale);
    Dimension dialogSize = getDialogSize(context);
    getController(context).displayModalDialog(
        view.getPeer(),
        createWizardStepActions(wizardStep, view, actionHandler,
            translationProvider, locale, modelConnector, context), title,
        getSourceComponent(context), context, dialogSize, reuseCurrent);
    // We must update the context
    context.putAll(getActionFactory(context).createActionContext(actionHandler,
        view, view.getConnector(), getActionCommand(context),
        getActionWidget(context)));
  }

  private Dimension getDialogSize(Map<String, Object> context) {
    Dimension dialogSize = (Dimension) context
        .get(ModalDialogAction.DIALOG_SIZE);
    if (width != null && height != null) {
      dialogSize = new Dimension(width, height);
    }
    return dialogSize;
  }

  private class CancelAction extends FrontendAction<E, F, G> {

    @SuppressWarnings("unused")
    private final IWizardStepDescriptor wizardStep;
    private final IDisplayableAction    wrappedCancelAction;

    public CancelAction(IWizardStepDescriptor wizardStep,
        IDisplayableAction wrappedCancelAction) {
      this.wizardStep = wizardStep;
      this.wrappedCancelAction = wrappedCancelAction;
    }

    @Override
    public boolean execute(IActionHandler actionHandler,
        Map<String, Object> context) {
      actionHandler.execute(wrappedCancelAction, context);
      return super.execute(actionHandler, context);
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
    public Icon getIcon() {
      return wrappedCancelAction.getIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
      return wrappedCancelAction.getName();
    }
  }

  private class FinishAction extends FrontendAction<E, F, G> {

    private final IWizardStepDescriptor wizardStep;
    private final IDisplayableAction    wrappedFinishAction;

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
        setActionParameter(getViewConnector(context).getConnectorValue(),
            context);
        actionHandler.execute(wrappedFinishAction, context);
      }
      return super.execute(actionHandler, context);
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
    public Icon getIcon() {
      return wrappedFinishAction.getIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
      return wrappedFinishAction.getName();
    }
  }

  private class NextAction extends FrontendAction<E, F, G> {

    private final IValueConnector       modelConnector;
    private final IWizardStepDescriptor wizardStep;

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
      return super.execute(actionHandler, context);
    }
  }

  private class PreviousAction extends FrontendAction<E, F, G> {

    private final IValueConnector       modelConnector;
    private final IWizardStepDescriptor wizardStep;

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
      return super.execute(actionHandler, context);
    }
  }
}
