/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.wizard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.jspresso.framework.application.frontend.action.wizard.IWizardStepDescriptor;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.util.collection.ObjectEqualityMap;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.swing.AbstractSwingAction;
import com.d2s.framework.util.swing.SwingUtil;

/**
 * A Wizard swing action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class WizardAction extends AbstractSwingAction {

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
    final JDialog dialog;
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

    Window window = SwingUtil.getVisibleWindow(getSourceComponent(context));
    if (window instanceof Dialog) {
      dialog = new JDialog((Dialog) window, getI18nName(
          getTranslationProvider(context), getLocale(context)), true);
    } else {
      dialog = new JDialog((Frame) window, getI18nName(
          getTranslationProvider(context), getLocale(context)), true);
    }

    dialog.getContentPane().setLayout(new BorderLayout());
    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    JSeparator separator = new JSeparator();

    final JPanel cardPanel = new JPanel();
    cardPanel.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));

    final CardLayout cardLayout = new CardLayout();
    cardPanel.setLayout(cardLayout);

    final JButton backButton = new JButton(getIconFactory(context)
        .getBackwardIcon(IIconFactory.SMALL_ICON_SIZE));
    final JButton nextButton = new JButton(getIconFactory(context)
        .getForwardIcon(IIconFactory.SMALL_ICON_SIZE));
    final JButton finishButton = new JButton(getIconFactory(context).getIcon(
        finishAction.getIconImageURL(), IIconFactory.SMALL_ICON_SIZE));
    finishButton.setText(finishAction.getI18nName(
        getTranslationProvider(context), getLocale(context)));
    JButton cancelButton = new JButton(getIconFactory(context).getCancelIcon(
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

    JPanel buttonPanel = new JPanel();
    Box buttonBox = new Box(BoxLayout.LINE_AXIS);

    buttonPanel.setLayout(new BorderLayout());
    buttonPanel.add(separator, BorderLayout.NORTH);

    buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
    buttonBox.add(backButton);
    buttonBox.add(Box.createHorizontalStrut(10));
    buttonBox.add(nextButton);
    buttonBox.add(Box.createHorizontalStrut(10));
    buttonBox.add(finishButton);
    buttonBox.add(Box.createHorizontalStrut(30));
    buttonBox.add(cancelButton);

    buttonPanel.add(buttonBox, BorderLayout.EAST);

    dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    dialog.getContentPane().add(cardPanel, BorderLayout.CENTER);

    dialog.getRootPane().setDefaultButton(nextButton);

    show(dialog, cardPanel, alreadyDisplayedSteps, firstWizardStep, backButton,
        nextButton, finishButton, modelConnector, actionHandler, context);

    dialog.pack();

    SwingUtil.centerInParent(dialog);
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

  private void show(JDialog dialog, JPanel cardPanel,
      Set<String> alreadyDisplayedSteps, IWizardStepDescriptor wizardStep,
      JButton backButton, JButton nextButton, JButton finishButton,
      IValueConnector modelConnector, IActionHandler actionHandler,
      Map<String, Object> context) {
    String cardName = wizardStep.getName();
    if (!alreadyDisplayedSteps.contains(cardName)) {
      alreadyDisplayedSteps.add(cardName);
      IView<JComponent> view = getViewFactory(context).createView(
          wizardStep.getViewDescriptor(), actionHandler, getLocale(context));
      cardPanel.add(view.getPeer(), cardName);
      getMvcBinder(context).bind(view.getConnector(), modelConnector);
    }
    ((CardLayout) (cardPanel.getLayout())).show(cardPanel, cardName);

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
