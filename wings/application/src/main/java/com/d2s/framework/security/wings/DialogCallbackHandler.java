package com.d2s.framework.security.wings;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.SwingConstants;

import org.wings.SButton;
import org.wings.SComponent;
import org.wings.SDialog;
import org.wings.SFrame;
import org.wings.SGridBagLayout;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SPasswordField;
import org.wings.STextField;

import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.IIconFactory;

/**
 * <p>
 * Uses a Wings dialog to query the user for answers to authentication
 * questions. This can be used by a JAAS application to instantiate a
 * CallbackHandler
 *
 * @see javax.security.auth.callback
 */
public class DialogCallbackHandler implements CallbackHandler {

  private SComponent            parentComponent;
  private IIconFactory<SIcon>   iconFactory;
  private static final Insets  DEFAULT_INSETS       = new Insets(5, 5, 5, 5);

  private Locale               locale;
  private ITranslationProvider translationProvider;

  private SIcon getIcon(TextOutputCallback callback)
      throws UnsupportedCallbackException {
    switch (callback.getMessageType()) {
      case TextOutputCallback.INFORMATION:
        return iconFactory.getInfoIcon(IIconFactory.SMALL_ICON_SIZE);
      case TextOutputCallback.WARNING:
        return iconFactory.getWarningIcon(IIconFactory.SMALL_ICON_SIZE);
      case TextOutputCallback.ERROR:
        return iconFactory.getErrorIcon(IIconFactory.SMALL_ICON_SIZE);
      default:
        throw new UnsupportedCallbackException(callback,
            "Unrecognized message type");
    }
  }

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

    boolean tocFound = false;
    boolean pcFound = false;
    for (Callback callback : callbacks) {
      if (callback instanceof TextOutputCallback) {
        tocFound = true;
      } else if (callback instanceof PasswordCallback) {
        pcFound = true;
      }
    }

    if (pcFound && !tocFound) {
      TextOutputCallback defaultToc = new TextOutputCallback(
          TextOutputCallback.INFORMATION, translationProvider.getTranslation(
              "credentialMessage", locale));
      List<Callback> completedCallBacks = new ArrayList<Callback>(Arrays
          .asList(callbacks));
      completedCallBacks.add(defaultToc);
      callbacks = completedCallBacks
          .toArray(new Callback[callbacks.length + 1]);
    }

    String dialogTitle = null;

    final SDialog callbackDialog;
    if (parentComponent != null) {
      callbackDialog = new SDialog(parentComponent.getParentFrame());
    } else {
      callbackDialog = new SDialog((SFrame) null);
    }

    List<ActionListener> proceedActions = new ArrayList<ActionListener>(2);

    SPanel messagePanel = null;
    SPanel inputPanel = null;
    SPanel optionPanel = null;

    for (Callback callback : callbacks) {
      if (callback instanceof TextOutputCallback) {
        if (messagePanel == null) {
          messagePanel = new SPanel();
          messagePanel.setLayout(new SGridBagLayout());
        }
        processTextOutputCallback(messagePanel, (TextOutputCallback) callback);
        if (dialogTitle == null) {
          dialogTitle = ((TextOutputCallback) callback).getMessage();
        }
      } else if (callback instanceof NameCallback) {
        if (inputPanel == null) {
          inputPanel = new SPanel();
          inputPanel.setLayout(new SGridBagLayout());
        }
        processNameCallback(proceedActions, inputPanel, (NameCallback) callback);

      } else if (callback instanceof PasswordCallback) {
        if (inputPanel == null) {
          inputPanel = new SPanel();
          inputPanel.setLayout(new SGridBagLayout());
        }
        processPasswordCallback(proceedActions, inputPanel,
            (PasswordCallback) callback);

      } else if (callback instanceof ConfirmationCallback) {
        if (optionPanel == null) {
          optionPanel = new SPanel();
          optionPanel.setLayout(new SGridBagLayout());
        }
        processConfirmationCallback(callbackDialog, proceedActions,
            optionPanel, (ConfirmationCallback) callback, inputPanel != null);

      } else {
        throw new UnsupportedCallbackException(callback,
            "Unrecognized Callback");
      }
    }

    SPanel dialogPanel = new SPanel();
    dialogPanel.setLayout(new SGridBagLayout());

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = DEFAULT_INSETS;
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = GridBagConstraints.RELATIVE;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    constraints.weightx = 1.0d;
    constraints.weighty = 0.0d;
    constraints.fill = GridBagConstraints.BOTH;

    if (messagePanel != null) {
      dialogPanel.add(messagePanel, constraints);
    }
    if (inputPanel != null) {
      dialogPanel.add(inputPanel, constraints);
    }
    if (optionPanel == null) {
      optionPanel = new SPanel();
      optionPanel.setLayout(new SGridBagLayout());
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
    callbackDialog.add(dialogPanel);
    callbackDialog.setVisible(true);
  }

  private void processConfirmationCallback(final SDialog callbackDialog,
      List<ActionListener> proceedActions, SPanel optionPanel,
      final ConfirmationCallback cc, boolean hasInput)
      throws UnsupportedCallbackException {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = DEFAULT_INSETS;
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = GridBagConstraints.RELATIVE;
    constraints.gridwidth = 1;
    constraints.weightx = 0.0d;
    constraints.fill = GridBagConstraints.NONE;

    int confirmationOptionType = cc.getOptionType();
    if (confirmationOptionType == ConfirmationCallback.UNSPECIFIED_OPTION) {
      for (int i = 0; i < cc.getOptions().length; i++) {
        final int optionIndex = i;

        SButton optionButton = new SButton(cc.getOptions()[i]);
        optionButton.addActionListener(new ActionListener() {

          public void actionPerformed(@SuppressWarnings("unused")
          ActionEvent e) {
            cc.setSelectedIndex(optionIndex);
            callbackDialog.dispose();
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

  private void processPasswordCallback(
      final List<ActionListener> proceedActions, SPanel inputPanel,
      final PasswordCallback pc) {
    // SLabel promptLabel = new SLabel(pc.getPrompt());
    SLabel promptLabel = new SLabel(translationProvider.getTranslation(
        "password", locale)
        + " :");

    final SPasswordField passwordField = new SPasswordField();

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = DEFAULT_INSETS;
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = GridBagConstraints.RELATIVE;
    constraints.gridwidth = 1;
    inputPanel.add(promptLabel, constraints);

    constraints.weightx = 1.0d;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    inputPanel.add(passwordField, constraints);

    proceedActions.add(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        pc.setPassword(passwordField.getText().toCharArray());
      }
    });
  }

  private void processNameCallback(final List<ActionListener> proceedActions,
      SPanel inputPanel, final NameCallback nc) {
    SLabel promptLabel = new SLabel(translationProvider.getTranslation("user",
        locale)
        + " :");
    final STextField nameTextField = new STextField();

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = DEFAULT_INSETS;
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = GridBagConstraints.RELATIVE;
    constraints.gridwidth = 1;
    inputPanel.add(promptLabel, constraints);

    constraints.weightx = 1.0d;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    inputPanel.add(nameTextField, constraints);

    proceedActions.add(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        nc.setName(nameTextField.getText());
      }
    });
  }

  private void processTextOutputCallback(SPanel messagePanel,
      TextOutputCallback toc) throws UnsupportedCallbackException {
    SLabel messageLabel = new SLabel(toc.getMessage(), getIcon(toc),
        SwingConstants.LEADING);
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = DEFAULT_INSETS;
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = GridBagConstraints.RELATIVE;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    constraints.weightx = 1.0d;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    messagePanel.add(messageLabel, constraints);
  }

  private SButton createOptionButton(final SDialog callbackDialog,
      final ConfirmationCallback cc, final int option, String text,
      final List<ActionListener> proceedActions) {
    SButton optionButton = new SButton(text);
    if (option == ConfirmationCallback.YES || option == ConfirmationCallback.OK) {
      optionButton.setIcon(iconFactory
          .getOkYesIcon(IIconFactory.SMALL_ICON_SIZE));
      optionButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          for (ActionListener proceedAction : proceedActions) {
            proceedAction.actionPerformed(e);
          }
          cc.setSelectedIndex(option);
          callbackDialog.dispose();
        }
      });
    } else {
      if (option == ConfirmationCallback.NO) {
        optionButton.setIcon(iconFactory
            .getNoIcon(IIconFactory.SMALL_ICON_SIZE));
      } else if (option == ConfirmationCallback.CANCEL) {
        optionButton.setIcon(iconFactory
            .getCancelIcon(IIconFactory.SMALL_ICON_SIZE));
      }
      optionButton.addActionListener(new ActionListener() {

        public void actionPerformed(@SuppressWarnings("unused")
        ActionEvent e) {
          cc.setSelectedIndex(option);
          callbackDialog.dispose();
        }
      });
    }
    if (cc.getDefaultOption() == option) {
      callbackDialog.setDefaultButton(optionButton);
    }
    return optionButton;
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
  public void setParentComponent(SComponent parentComponent) {
    this.parentComponent = parentComponent;
  }

  /**
   * Sets the iconFactory.
   *
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<SIcon> iconFactory) {
    this.iconFactory = iconFactory;
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
}
