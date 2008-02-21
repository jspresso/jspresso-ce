/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.wizard;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.ulc.AbstractUlcAction;
import com.d2s.framework.application.frontend.action.wizard.IWizardStepDescriptor;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.model.IModelConnectorFactory;
import com.d2s.framework.gui.ulc.components.server.ULCExtendedButton;
import com.d2s.framework.util.collection.ObjectEqualityMap;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.util.ulc.UlcUtil;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;
import com.ulcjava.base.application.ULCBorderLayoutPane;
import com.ulcjava.base.application.ULCBoxLayoutPane;
import com.ulcjava.base.application.ULCCardPane;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDialog;
import com.ulcjava.base.application.ULCFiller;
import com.ulcjava.base.application.ULCSeparator;
import com.ulcjava.base.application.ULCWindow;
import com.ulcjava.base.application.border.ULCEmptyBorder;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;
import com.ulcjava.base.application.util.Insets;
import com.ulcjava.base.shared.IWindowConstants;

/**
 * A Wizard swing action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class WizardAction extends AbstractUlcAction {

  private IDisplayableAction     finishAction;
  private IWizardStepDescriptor  firstWizardStep;

  private IModelConnectorFactory modelConnectorFactory;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(final IActionHandler actionHandler,
      final Map<String, Object> context) {
    final ULCDialog dialog;
    final Set<String> alreadyDisplayedSteps = new HashSet<String>();
    final IValueConnector modelConnector = modelConnectorFactory
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

    ULCWindow window = UlcUtil.getVisibleWindow(getSourceComponent(context));
    dialog = new ULCDialog(window, getI18nName(getTranslationProvider(context),
        getLocale(context)), true);

    ULCBorderLayoutPane contentPane = new ULCBorderLayoutPane();
    contentPane.setBorder(new ULCEmptyBorder(new Insets(5, 10, 5, 10)));
    dialog.getContentPane().add(contentPane);
    dialog.setDefaultCloseOperation(IWindowConstants.DO_NOTHING_ON_CLOSE);

    ULCSeparator separator = new ULCSeparator();

    final ULCCardPane cardPanel = new ULCCardPane();

    final ULCExtendedButton backButton = new ULCExtendedButton(getIconFactory(
        context).getBackwardIcon(IIconFactory.SMALL_ICON_SIZE));
    final ULCExtendedButton nextButton = new ULCExtendedButton(getIconFactory(
        context).getForwardIcon(IIconFactory.SMALL_ICON_SIZE));
    final ULCExtendedButton finishButton = new ULCExtendedButton(
        getIconFactory(context).getIcon(finishAction.getIconImageURL(),
            IIconFactory.SMALL_ICON_SIZE));
    finishButton.setText(finishAction.getI18nName(
        getTranslationProvider(context), getLocale(context)));
    ULCExtendedButton cancelButton = new ULCExtendedButton(getIconFactory(
        context).getCancelIcon(IIconFactory.SMALL_ICON_SIZE));
    cancelButton.setText(getTranslationProvider(context).getTranslation(
        "cancel", getLocale(context)));

    backButton.addActionListener(new IActionListener() {

      private static final long serialVersionUID = 1694147075934780963L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        IWizardStepDescriptor currentWizardStep = getCurrentWizardStep(context);
        IWizardStepDescriptor backWizardStep = currentWizardStep
            .getPreviousStepDescriptor(context);
        show(dialog, cardPanel, alreadyDisplayedSteps, backWizardStep,
            backButton, nextButton, finishButton, modelConnector,
            actionHandler, context);
      }
    });

    nextButton.addActionListener(new IActionListener() {

      private static final long serialVersionUID = -3606110326421944011L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        IWizardStepDescriptor currentWizardStep = getCurrentWizardStep(context);
        if (currentWizardStep.getOnLeaveAction() == null
            || actionHandler.execute(currentWizardStep.getOnLeaveAction(),
                context)) {
          IWizardStepDescriptor nextWizardStep = currentWizardStep
              .getNextStepDescriptor(context);
          show(dialog, cardPanel, alreadyDisplayedSteps, nextWizardStep,
              backButton, nextButton, finishButton, modelConnector,
              actionHandler, context);
          if (nextWizardStep.getOnEnterAction() != null) {
            actionHandler.execute(nextWizardStep.getOnEnterAction(), context);
          }
        }
      }
    });

    finishButton.addActionListener(new IActionListener() {

      private static final long serialVersionUID = -8118428872923819202L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        IWizardStepDescriptor currentWizardStep = getCurrentWizardStep(context);
        if (currentWizardStep.getOnLeaveAction() == null
            || actionHandler.execute(currentWizardStep.getOnLeaveAction(),
                context)) {
          dialog.setVisible(false);
          actionHandler.execute(finishAction, context);
        }
      }
    });

    cancelButton.addActionListener(new IActionListener() {

      private static final long serialVersionUID = -203799299156645547L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        dialog.setVisible(false);
      }
    });

    ULCBorderLayoutPane buttonPanel = new ULCBorderLayoutPane();
    ULCBoxLayoutPane buttonBox = new ULCBoxLayoutPane(
        ULCBoxLayoutPane.LINE_AXIS);

    buttonPanel.add(separator, ULCBorderLayoutPane.NORTH);

    buttonBox.setBorder(new ULCEmptyBorder(new Insets(5, 10, 5, 10)));
    buttonBox.add(backButton);
    buttonBox.add(ULCFiller.createHorizontalStrut(10));
    buttonBox.add(nextButton);
    buttonBox.add(ULCFiller.createHorizontalStrut(10));
    buttonBox.add(finishButton);
    buttonBox.add(ULCFiller.createHorizontalStrut(30));
    buttonBox.add(cancelButton);

    buttonPanel.add(buttonBox, ULCBorderLayoutPane.EAST);

    contentPane.add(buttonPanel, ULCBorderLayoutPane.SOUTH);
    contentPane.add(cardPanel, ULCBorderLayoutPane.CENTER);

    dialog.getRootPane().setDefaultButton(nextButton);

    show(dialog, cardPanel, alreadyDisplayedSteps, firstWizardStep, backButton,
        nextButton, finishButton, modelConnector, actionHandler, context);

    dialog.pack();

    dialog.setVisible(true);
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the finishAction.
   * 
   * @param finishAction
   *            the finishAction to set.
   */
  public void setFinishAction(IDisplayableAction finishAction) {
    this.finishAction = finishAction;
  }

  /**
   * Sets the firstWizardStep.
   * 
   * @param firstWizardStep
   *            the firstWizardStep to set.
   */
  public void setFirstWizardStep(IWizardStepDescriptor firstWizardStep) {
    this.firstWizardStep = firstWizardStep;
  }

  /**
   * Sets the modelConnectorFactory.
   * 
   * @param modelConnectorFactory
   *            the modelConnectorFactory to set.
   */
  public void setModelConnectorFactory(
      IModelConnectorFactory modelConnectorFactory) {
    this.modelConnectorFactory = modelConnectorFactory;
  }

  /**
   * Creates (and initializes) the wizard model.
   * 
   * @param initialWizardModel
   *            the initial wizard model.
   * @param context
   *            the action context.
   */
  protected void completeInitialWizardModel(@SuppressWarnings("unused")
  Map<String, Object> initialWizardModel, @SuppressWarnings("unused")
  Map<String, Object> context) {
    // No-op by default.
  }

  private IWizardStepDescriptor getCurrentWizardStep(Map<String, Object> context) {
    IWizardStepDescriptor currentWizardStep = (IWizardStepDescriptor) context
        .get(ActionContextConstants.DIALOG_VIEW);
    return currentWizardStep;
  }

  private void setCurrentWizardStep(IWizardStepDescriptor currentWizardStep,
      Map<String, Object> context) {
    context.put(ActionContextConstants.DIALOG_VIEW, currentWizardStep);
  }

  private void show(ULCDialog dialog, ULCCardPane cardPanel,
      Set<String> alreadyDisplayedSteps, IWizardStepDescriptor wizardStep,
      ULCExtendedButton backButton, ULCExtendedButton nextButton,
      ULCExtendedButton finishButton, IValueConnector modelConnector,
      IActionHandler actionHandler, Map<String, Object> context) {
    String cardName = wizardStep.getName();
    if (!alreadyDisplayedSteps.contains(cardName)) {
      alreadyDisplayedSteps.add(cardName);
      IView<ULCComponent> view = getViewFactory(context).createView(
          wizardStep.getViewDescriptor(), actionHandler, getLocale(context));
      cardPanel.addCard(cardName, view.getPeer());
      getMvcBinder(context).bind(view.getConnector(), modelConnector);
    }
    cardPanel.setSelectedName(cardName);

    ITranslationProvider translationProvider = getTranslationProvider(context);
    Locale locale = getLocale(context);

    if (wizardStep.getPreviousStepDescriptor(context) != null) {
      backButton.setEnabled(true);
    } else {
      backButton.setEnabled(false);
    }
    if (wizardStep.getPreviousLabelKey() != null) {
      backButton.setText(translationProvider.getTranslation(wizardStep
          .getPreviousLabelKey(), locale));
    } else {
      backButton.setText(translationProvider.getTranslation(
          IWizardStepDescriptor.DEFAULT_PREVIOUS_KEY, locale));
    }

    if (wizardStep.getNextStepDescriptor(context) != null) {
      nextButton.setEnabled(true);
    } else {
      nextButton.setEnabled(false);
    }
    if (wizardStep.getNextLabelKey() != null) {
      nextButton.setText(translationProvider.getTranslation(wizardStep
          .getNextLabelKey(), locale));
    } else {
      nextButton.setText(translationProvider.getTranslation(
          IWizardStepDescriptor.DEFAULT_NEXT_KEY, locale));
    }

    if (wizardStep.canFinish(context)) {
      finishButton.setEnabled(true);
    } else {
      finishButton.setEnabled(false);
    }
    dialog.setTitle(getI18nName(translationProvider, locale) + " - "
        + wizardStep.getI18nName(translationProvider, locale));
    setCurrentWizardStep(wizardStep, context);
  }
}
