/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.wings.wizard;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.wings.AbstractWingsAction;
import org.jspresso.framework.application.frontend.action.wizard.IWizardStepDescriptor;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.util.collection.ObjectEqualityMap;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.wings.SBorderLayout;
import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SCardLayout;
import org.wings.SComponent;
import org.wings.SDialog;
import org.wings.SDimension;
import org.wings.SFrame;
import org.wings.SPanel;
import org.wings.SSeparator;
import org.wings.SSpacer;
import org.wings.border.SEmptyBorder;


/**
 * A Wizard wings action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class WizardAction extends AbstractWingsAction {

  private static final SDimension WIZARD_DIMENSION = new SDimension("500px",
                                                       "250px");
  private IDisplayableAction      finishAction;
  private IWizardStepDescriptor   firstWizardStep;

  private IModelConnectorFactory  modelConnectorFactory;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(final IActionHandler actionHandler,
      final Map<String, Object> context) {
    final SDialog dialog;
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

    SFrame window = getSourceComponent(context).getParentFrame();
    dialog = new SDialog(window, getI18nName(getTranslationProvider(context),
        getLocale(context)), true);
    dialog.setDraggable(true);
    dialog.setLayout(new SBorderLayout());

    final SPanel cardPanel = new SPanel();
    cardPanel.setBorder(new SEmptyBorder(new Insets(5, 10, 5, 10)));

    final SCardLayout cardLayout = new SCardLayout();
    cardPanel.setLayout(cardLayout);

    final SButton backButton = new SButton(getIconFactory(context)
        .getBackwardIcon(IIconFactory.SMALL_ICON_SIZE));
    final SButton nextButton = new SButton(getIconFactory(context)
        .getForwardIcon(IIconFactory.SMALL_ICON_SIZE));
    final SButton finishButton = new SButton(getIconFactory(context).getIcon(
        finishAction.getIconImageURL(), IIconFactory.SMALL_ICON_SIZE));
    finishButton.setText(finishAction.getI18nName(
        getTranslationProvider(context), getLocale(context)));
    SButton cancelButton = new SButton(getIconFactory(context).getCancelIcon(
        IIconFactory.SMALL_ICON_SIZE));
    cancelButton.setText(getTranslationProvider(context).getTranslation(
        "cancel", getLocale(context)));

    backButton.addActionListener(new ActionListener() {

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

    nextButton.addActionListener(new ActionListener() {

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

    finishButton.addActionListener(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        IWizardStepDescriptor currentWizardStep = getCurrentWizardStep(context);
        if (currentWizardStep.getOnLeaveAction() == null
            || actionHandler.execute(currentWizardStep.getOnLeaveAction(),
                context)) {
          dialog.dispose();
          actionHandler.execute(finishAction, context);
        }
      }
    });

    cancelButton.addActionListener(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        dialog.dispose();
      }
    });

    SPanel buttonPanel = new SPanel();
    SPanel buttonBox = new SPanel(
        new SBoxLayout(buttonPanel, SBoxLayout.X_AXIS));

    buttonPanel.setLayout(new SBorderLayout());
    buttonPanel.add(new SSeparator(), SBorderLayout.NORTH);

    buttonBox.setBorder(new SEmptyBorder(new Insets(5, 10, 5, 10)));
    buttonBox.add(backButton);
    buttonBox.add(new SSpacer(10, 10));
    buttonBox.add(nextButton);
    buttonBox.add(new SSpacer(10, 10));
    buttonBox.add(finishButton);
    buttonBox.add(new SSpacer(10, 10));
    buttonBox.add(cancelButton);

    buttonPanel.add(buttonBox, SBorderLayout.EAST);

    dialog.add(buttonPanel, SBorderLayout.SOUTH);
    dialog.add(cardPanel, SBorderLayout.CENTER);

    dialog.setDefaultButton(nextButton);
    dialog.setPreferredSize(new SDimension(WIZARD_DIMENSION.getWidth(),
        SDimension.AUTO));

    show(dialog, cardPanel, alreadyDisplayedSteps, firstWizardStep, backButton,
        nextButton, finishButton, modelConnector, actionHandler, context);

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
  protected void completeInitialWizardModel(Map<String, Object> initialWizardModel, Map<String, Object> context) {
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

  private void show(SDialog dialog, SPanel cardPanel,
      Set<String> alreadyDisplayedSteps, IWizardStepDescriptor wizardStep,
      SButton backButton, SButton nextButton, SButton finishButton,
      IValueConnector modelConnector, IActionHandler actionHandler,
      Map<String, Object> context) {
    String cardName = wizardStep.getName();
    if (!alreadyDisplayedSteps.contains(cardName)) {
      alreadyDisplayedSteps.add(cardName);
      IView<SComponent> view = getViewFactory(context).createView(
          wizardStep.getViewDescriptor(), actionHandler, getLocale(context));
      view.getPeer().setPreferredSize(WIZARD_DIMENSION);
      cardPanel.add(view.getPeer(), cardName);
      getMvcBinder(context).bind(view.getConnector(), modelConnector);
    }
    ((SCardLayout) (cardPanel.getLayout())).show(cardPanel, cardName);

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
