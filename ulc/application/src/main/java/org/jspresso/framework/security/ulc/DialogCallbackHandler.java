package org.jspresso.framework.security.ulc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.ConfirmationCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.jspresso.framework.gui.ulc.components.server.ULCExtendedButton;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.ulc.UlcUtil;
import org.jspresso.framework.view.IIconFactory;

import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.GridBagConstraints;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDialog;
import com.ulcjava.base.application.ULCGridBagLayoutPane;
import com.ulcjava.base.application.ULCLabel;
import com.ulcjava.base.application.ULCPasswordField;
import com.ulcjava.base.application.ULCTextField;
import com.ulcjava.base.application.ULCWindow;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;
import com.ulcjava.base.application.util.Dimension;
import com.ulcjava.base.application.util.Insets;
import com.ulcjava.base.application.util.ULCIcon;
import com.ulcjava.base.shared.IDefaults;
import com.ulcjava.base.shared.IWindowConstants;

/**
 * <p>
 * Uses a ULC dialog to query the user for answers to authentication questions.
 * This can be used by a JAAS application to instantiate a CallbackHandler
 * 
 * @see javax.security.auth.callback
 */
public class DialogCallbackHandler implements CallbackHandler {

  private static final int         DEFAULT_FIELD_LENGTH = 32;
  private static final Insets      DEFAULT_INSETS       = new Insets(5, 5, 5, 5);

  private ICallbackHandlerListener callbackHandlerListener;
  private IIconFactory<ULCIcon>    iconFactory;
  private Locale                   locale;

  private ULCComponent             parentComponent;
  private ITranslationProvider     translationProvider;

  /**
   * Handles the specified set of callbacks.
   * 
   * @param callbacks
   *          the callbacks to handle
   * @throws UnsupportedCallbackException
   *           if the callback is not an instance of NameCallback or
   *           PasswordCallback
   */

  public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
    Callback[] varCallbacks = callbacks;
    boolean tocFound = false;
    boolean pcFound = false;
    for (Callback callback : varCallbacks) {
      if (callback instanceof TextOutputCallback) {
        tocFound = true;
      } else if (callback instanceof PasswordCallback) {
        pcFound = true;
      }
    }

    if (pcFound && !tocFound) {
      TextOutputCallback defaultToc = new TextOutputCallback(
          TextOutputCallback.INFORMATION, "credentialMessage");
      List<Callback> completedCallBacks = new ArrayList<Callback>(Arrays
          .asList(varCallbacks));
      completedCallBacks.add(defaultToc);
      varCallbacks = completedCallBacks
          .toArray(new Callback[varCallbacks.length + 1]);
    }

    String dialogTitle = null;

    final ULCDialog callbackDialog;
    if (parentComponent != null) {
      ULCWindow parentWindow;
      if (parentComponent instanceof ULCWindow) {
        parentWindow = (ULCWindow) parentComponent;
      } else {
        parentWindow = UlcUtil.getVisibleWindow(parentComponent);
      }
      callbackDialog = new ULCDialog(parentWindow, true);
    } else {
      callbackDialog = new ULCDialog((ULCWindow) null, true);
    }
    callbackDialog
        .setDefaultCloseOperation(IWindowConstants.DO_NOTHING_ON_CLOSE);

    List<IActionListener> proceedActions = new ArrayList<IActionListener>(2);

    ULCGridBagLayoutPane messagePanel = null;
    ULCGridBagLayoutPane inputPanel = null;
    ULCGridBagLayoutPane optionPanel = null;

    for (Callback callback : varCallbacks) {
      if (callback instanceof TextOutputCallback) {
        if (messagePanel == null) {
          messagePanel = new ULCGridBagLayoutPane();
        }
        processTextOutputCallback(messagePanel, (TextOutputCallback) callback);
        if (dialogTitle == null) {
          dialogTitle = translationProvider.getTranslation(
              ((TextOutputCallback) callback).getMessage(), locale);
        }
      } else if (callback instanceof NameCallback) {
        if (inputPanel == null) {
          inputPanel = new ULCGridBagLayoutPane();
        }
        processNameCallback(proceedActions, inputPanel, (NameCallback) callback);

      } else if (callback instanceof PasswordCallback) {
        if (inputPanel == null) {
          inputPanel = new ULCGridBagLayoutPane();
        }
        processPasswordCallback(proceedActions, inputPanel,
            (PasswordCallback) callback);

      } else if (callback instanceof ConfirmationCallback) {
        if (optionPanel == null) {
          optionPanel = new ULCGridBagLayoutPane();
        }
        processConfirmationCallback(callbackDialog, proceedActions,
            optionPanel, (ConfirmationCallback) callback, inputPanel != null);

      } else {
        throw new UnsupportedCallbackException(callback,
            "Unrecognized Callback");
      }
    }

    ULCGridBagLayoutPane dialogPanel = new ULCGridBagLayoutPane();

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.setInsets(DEFAULT_INSETS);
    constraints.setGridX(GridBagConstraints.RELATIVE);
    constraints.setGridY(GridBagConstraints.RELATIVE);
    constraints.setGridWidth(GridBagConstraints.REMAINDER);
    constraints.setWeightX(1.0d);
    constraints.setWeightY(0.0d);
    constraints.setFill(GridBagConstraints.BOTH);

    if (messagePanel != null) {
      dialogPanel.add(messagePanel, constraints);
    }
    if (inputPanel != null) {
      dialogPanel.add(inputPanel, constraints);
    }
    if (optionPanel == null) {
      optionPanel = new ULCGridBagLayoutPane();
      ConfirmationCallback cc = new ConfirmationCallback(
          ConfirmationCallback.INFORMATION,
          ConfirmationCallback.OK_CANCEL_OPTION, ConfirmationCallback.OK);
      processConfirmationCallback(callbackDialog, proceedActions, optionPanel,
          cc, inputPanel != null);
    }
    dialogPanel.add(optionPanel, constraints);

    if (dialogTitle != null) {
      callbackDialog.setTitle(dialogTitle);
    }
    callbackDialog.getContentPane().add(dialogPanel);
    int screenRes = ClientContext.getScreenResolution();
    callbackDialog.setSize(new Dimension(4 * screenRes, screenRes
        * (varCallbacks.length + 1) / 2));
    callbackDialog.pack();
    UlcUtil.centerOnScreen(callbackDialog);
    callbackDialog.setVisible(true);
  }

  /**
   * Sets the callbackHandlerListener.
   * 
   * @param callbackHandlerListener
   *          the callbackHandlerListener to set.
   */
  public void setCallbackHandlerListener(
      ICallbackHandlerListener callbackHandlerListener) {
    this.callbackHandlerListener = callbackHandlerListener;
  }

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<ULCIcon> iconFactory) {
    this.iconFactory = iconFactory;
  }

  /**
   * Sets the locale.
   * 
   * @param locale
   *          the locale to set.
   */
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  /**
   * Sets the parentComponent.
   * 
   * @param parentComponent
   *          the parentComponent to set.
   */
  public void setParentComponent(ULCComponent parentComponent) {
    this.parentComponent = parentComponent;
  }

  /**
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }

  private ULCExtendedButton createOptionButton(final ULCDialog callbackDialog,
      final ConfirmationCallback cc, final int option, String text,
      final List<IActionListener> proceedActions) {
    ULCExtendedButton optionButton = new ULCExtendedButton();
    optionButton.setText(text);
    if (option == ConfirmationCallback.YES || option == ConfirmationCallback.OK) {
      optionButton.setIcon(iconFactory.getOkYesIcon(iconFactory
          .getSmallIconSize()));
      optionButton.addActionListener(new IActionListener() {

        private static final long serialVersionUID = -1794878333128512291L;

        public void actionPerformed(ActionEvent e) {
          for (IActionListener proceedAction : proceedActions) {
            proceedAction.actionPerformed(e);
          }
          cc.setSelectedIndex(option);
          endClientSideLoginProcess(callbackDialog);
        }
      });
    } else {
      if (option == ConfirmationCallback.NO) {
        optionButton.setIcon(iconFactory.getNoIcon(iconFactory
            .getSmallIconSize()));
      } else if (option == ConfirmationCallback.CANCEL) {
        optionButton.setIcon(iconFactory.getCancelIcon(iconFactory
            .getSmallIconSize()));
      }
      optionButton.addActionListener(new IActionListener() {

        private static final long serialVersionUID = -1787817960559101628L;

        public void actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
          cc.setSelectedIndex(option);
          endClientSideLoginProcess(callbackDialog);
        }
      });
    }
    if (cc.getDefaultOption() == option) {
      callbackDialog.getRootPane().setDefaultButton(optionButton);
    }
    return optionButton;
  }

  private void endClientSideLoginProcess(ULCDialog callbackDialog) {
    if (callbackHandlerListener != null) {
      callbackHandlerListener.callbackHandlingComplete();
    }
    callbackDialog.setVisible(false);
  }

  private ULCIcon getIcon(TextOutputCallback callback)
      throws UnsupportedCallbackException {
    switch (callback.getMessageType()) {
      case TextOutputCallback.INFORMATION:
        return iconFactory.getInfoIcon(iconFactory.getSmallIconSize());
      case TextOutputCallback.WARNING:
        return iconFactory.getWarningIcon(iconFactory.getSmallIconSize());
      case TextOutputCallback.ERROR:
        return iconFactory.getErrorIcon(iconFactory.getSmallIconSize());
      default:
        throw new UnsupportedCallbackException(callback,
            "Unrecognized message type");
    }
  }

  private void processConfirmationCallback(final ULCDialog callbackDialog,
      List<IActionListener> proceedActions, ULCGridBagLayoutPane optionPanel,
      final ConfirmationCallback cc, boolean hasInput)
      throws UnsupportedCallbackException {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.setInsets(DEFAULT_INSETS);
    constraints.setGridX(GridBagConstraints.RELATIVE);
    constraints.setGridY(GridBagConstraints.RELATIVE);
    constraints.setGridWidth(1);
    constraints.setWeightX(0.0d);
    constraints.setFill(GridBagConstraints.NONE);

    int confirmationOptionType = cc.getOptionType();
    if (confirmationOptionType == ConfirmationCallback.UNSPECIFIED_OPTION) {
      for (int i = 0; i < cc.getOptions().length; i++) {
        final int optionIndex = i;

        ULCExtendedButton optionButton = new ULCExtendedButton();
        optionButton.setText(cc.getOptions()[i]);
        optionButton.addActionListener(new IActionListener() {

          private static final long serialVersionUID = -5727287836828197725L;

          public void actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
            cc.setSelectedIndex(optionIndex);
            endClientSideLoginProcess(callbackDialog);
          }
        });
        optionPanel.add(optionButton, constraints);
      }
    } else {
      if (locale == null) {
        locale = Locale.getDefault();
      }
      switch (confirmationOptionType) {
        case ConfirmationCallback.YES_NO_OPTION:
          optionPanel.add(createOptionButton(callbackDialog, cc,
              ConfirmationCallback.YES, translationProvider.getTranslation(
                  "yes", locale), proceedActions), constraints);
          optionPanel.add(createOptionButton(callbackDialog, cc,
              ConfirmationCallback.NO, translationProvider.getTranslation("no",
                  locale), proceedActions), constraints);
          break;
        case ConfirmationCallback.YES_NO_CANCEL_OPTION:
          optionPanel.add(createOptionButton(callbackDialog, cc,
              ConfirmationCallback.YES, translationProvider.getTranslation(
                  "yes", locale), proceedActions), constraints);
          optionPanel.add(createOptionButton(callbackDialog, cc,
              ConfirmationCallback.NO, translationProvider.getTranslation("no",
                  locale), proceedActions), constraints);
          optionPanel.add(createOptionButton(callbackDialog, cc,
              ConfirmationCallback.CANCEL, translationProvider.getTranslation(
                  "cancel", locale), proceedActions), constraints);
          break;
        case ConfirmationCallback.OK_CANCEL_OPTION:
          optionPanel.add(createOptionButton(callbackDialog, cc,
              ConfirmationCallback.OK, translationProvider.getTranslation("ok",
                  locale), proceedActions), constraints);
          if (hasInput) {
            optionPanel.add(createOptionButton(callbackDialog, cc,
                ConfirmationCallback.CANCEL, translationProvider
                    .getTranslation("cancel", locale), proceedActions),
                constraints);
          }
          break;
        default:
          throw new UnsupportedCallbackException(cc,
              "Unrecognized option type: " + confirmationOptionType);
      }
    }
  }

  private void processNameCallback(final List<IActionListener> proceedActions,
      ULCGridBagLayoutPane inputPanel, final NameCallback nc) {
    // ULCLabel promptLabel = new ULCLabel(nc.getPrompt());
    ULCLabel promptLabel = new ULCLabel(translationProvider.getTranslation(
        "user", locale)
        + " :");
    final ULCTextField nameTextField = new ULCTextField(DEFAULT_FIELD_LENGTH);

    // String defaultName = nc.getDefaultName();
    // if (defaultName != null) {
    // nameTextField.setText(defaultName);
    // }

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.setInsets(DEFAULT_INSETS);
    constraints.setGridX(GridBagConstraints.RELATIVE);
    constraints.setGridY(GridBagConstraints.RELATIVE);
    constraints.setGridWidth(1);
    inputPanel.add(promptLabel, constraints);

    constraints.setWeightX(1.0d);
    constraints.setFill(GridBagConstraints.HORIZONTAL);
    constraints.setGridWidth(GridBagConstraints.REMAINDER);
    inputPanel.add(nameTextField, constraints);

    proceedActions.add(new IActionListener() {

      private static final long serialVersionUID = 974089545700172602L;

      public void actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
        nc.setName(nameTextField.getText());
      }
    });
  }

  private void processPasswordCallback(
      final List<IActionListener> proceedActions,
      ULCGridBagLayoutPane inputPanel, final PasswordCallback pc) {
    // ULCLabel promptLabel = new ULCLabel(pc.getPrompt());
    ULCLabel promptLabel = new ULCLabel(translationProvider.getTranslation(
        "password", locale)
        + " :");

    final ULCPasswordField passwordField = new ULCPasswordField(
        DEFAULT_FIELD_LENGTH);
    if (!pc.isEchoOn()) {
      passwordField.setEchoChar('*');
    }

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.setInsets(DEFAULT_INSETS);
    constraints.setGridX(GridBagConstraints.RELATIVE);
    constraints.setGridY(GridBagConstraints.RELATIVE);
    constraints.setGridWidth(1);
    inputPanel.add(promptLabel, constraints);

    constraints.setWeightX(1.0d);
    constraints.setFill(GridBagConstraints.HORIZONTAL);
    constraints.setGridWidth(GridBagConstraints.REMAINDER);
    inputPanel.add(passwordField, constraints);

    proceedActions.add(new IActionListener() {

      private static final long serialVersionUID = -8212061905248284632L;

      public void actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
        if (passwordField.getText() != null) {
          pc.setPassword(passwordField.getText().toCharArray());
        }
      }
    });
  }

  private void processTextOutputCallback(ULCGridBagLayoutPane messagePanel,
      TextOutputCallback toc) throws UnsupportedCallbackException {
    ULCLabel messageLabel = new ULCLabel(translationProvider.getTranslation(toc
        .getMessage(), locale), getIcon(toc), IDefaults.LEADING);
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.setInsets(DEFAULT_INSETS);
    constraints.setGridX(GridBagConstraints.RELATIVE);
    constraints.setGridY(GridBagConstraints.RELATIVE);
    constraints.setGridWidth(GridBagConstraints.REMAINDER);
    constraints.setWeightX(1.0d);
    constraints.setFill(GridBagConstraints.HORIZONTAL);
    messagePanel.add(messageLabel, constraints);
  }
}
