package com.d2s.framework.security.swing;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.ConfirmationCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.view.IIconFactory;

/**
 * <p>
 * Uses a Swing dialog to query the user for answers to authentication
 * questions. This can be used by a JAAS application to instantiate a
 * CallbackHandler
 * 
 * @see javax.security.auth.callback
 */
public class DialogCallbackHandler implements CallbackHandler {

  private Component            parentComponent;
  private IIconFactory<Icon>   iconFactory;
  private static final int     DEFAULT_FIELD_LENGTH = 32;
  private static final Insets  DEFAULT_INSETS       = new Insets(5, 5, 5, 5);

  private Locale               locale;
  private ITranslationProvider translationProvider;

  private Icon getIcon(TextOutputCallback callback)
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

    String dialogTitle = null;

    final JDialog callbackDialog;
    if (parentComponent != null) {
      Window parentWindow;
      if (parentComponent instanceof Window) {
        parentWindow = (Window) parentComponent;
      } else {
        parentWindow = SwingUtilities.windowForComponent(parentComponent);
      }
      if (parentWindow instanceof Dialog) {
        callbackDialog = new JDialog((Dialog) parentWindow, true);
      } else {
        callbackDialog = new JDialog((Frame) parentWindow, true);
      }
    } else {
      callbackDialog = new JDialog((Frame) null, true);
    }
    callbackDialog
        .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    List<ActionListener> proceedActions = new ArrayList<ActionListener>(2);

    JPanel messagePanel = null;
    JPanel inputPanel = null;
    JPanel optionPanel = null;

    for (Callback callback : callbacks) {
      if (callback instanceof TextOutputCallback) {
        if (messagePanel == null) {
          messagePanel = new JPanel();
          messagePanel.setLayout(new GridBagLayout());
        }
        processTextOutputCallback(messagePanel, (TextOutputCallback) callback);
        if (dialogTitle == null) {
          dialogTitle = ((TextOutputCallback) callback).getMessage();
        }
      } else if (callback instanceof NameCallback) {
        if (inputPanel == null) {
          inputPanel = new JPanel();
          inputPanel.setLayout(new GridBagLayout());
        }
        processNameCallback(proceedActions, inputPanel, (NameCallback) callback);

      } else if (callback instanceof PasswordCallback) {
        if (inputPanel == null) {
          inputPanel = new JPanel();
          inputPanel.setLayout(new GridBagLayout());
        }
        processPasswordCallback(proceedActions, inputPanel,
            (PasswordCallback) callback);

      } else if (callback instanceof ConfirmationCallback) {
        if (optionPanel == null) {
          optionPanel = new JPanel();
          optionPanel.setLayout(new GridBagLayout());
        }
        processConfirmationCallback(callbackDialog, proceedActions,
            optionPanel, (ConfirmationCallback) callback, inputPanel != null);

      } else {
        throw new UnsupportedCallbackException(callback,
            "Unrecognized Callback");
      }
    }

    JPanel dialogPanel = new JPanel();
    dialogPanel.setLayout(new GridBagLayout());

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
      optionPanel = new JPanel();
      optionPanel.setLayout(new GridBagLayout());
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
    callbackDialog.pack();
    SwingUtil.centerOnScreen(callbackDialog);
    callbackDialog.setVisible(true);
  }

  private void processConfirmationCallback(final JDialog callbackDialog,
      List<ActionListener> proceedActions, JPanel optionPanel,
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

        JButton optionButton = new JButton(cc.getOptions()[i]);
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
              ConfirmationCallback.YES, translationProvider.getTranslation("YES",
                  locale), proceedActions), constraints);
          optionPanel.add(createOptionButton(callbackDialog, cc,
              ConfirmationCallback.NO, translationProvider.getTranslation("NO",
                  locale), proceedActions), constraints);
          break;
        case ConfirmationCallback.YES_NO_CANCEL_OPTION:
          optionPanel.add(createOptionButton(callbackDialog, cc,
              ConfirmationCallback.YES, translationProvider.getTranslation("YES",
                  locale), proceedActions), constraints);
          optionPanel.add(createOptionButton(callbackDialog, cc,
              ConfirmationCallback.NO, translationProvider.getTranslation("NO",
                  locale), proceedActions), constraints);
          optionPanel.add(createOptionButton(callbackDialog, cc,
              ConfirmationCallback.CANCEL, translationProvider.getTranslation(
                  "CANCEL", locale), proceedActions), constraints);
          break;
        case ConfirmationCallback.OK_CANCEL_OPTION:
          optionPanel.add(createOptionButton(callbackDialog, cc,
              ConfirmationCallback.OK, translationProvider.getTranslation("OK",
                  locale), proceedActions), constraints);
          if (hasInput) {
            optionPanel.add(createOptionButton(callbackDialog, cc,
                ConfirmationCallback.CANCEL, translationProvider.getTranslation(
                    "CANCEL", locale), proceedActions), constraints);
          }
          break;
        default:
          throw new UnsupportedCallbackException(cc,
              "Unrecognized option type: " + confirmationOptionType);
      }
    }
  }

  private void processPasswordCallback(
      final List<ActionListener> proceedActions, JPanel inputPanel,
      final PasswordCallback pc) {
    JLabel promptLabel = new JLabel(pc.getPrompt());

    final JPasswordField passwordField = new JPasswordField(
        DEFAULT_FIELD_LENGTH);
    if (!pc.isEchoOn()) {
      passwordField.setEchoChar('*');
    }

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = DEFAULT_INSETS;
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = GridBagConstraints.RELATIVE;
    constraints.gridwidth = 1;
    constraints.weightx = 1.0d;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    inputPanel.add(promptLabel, constraints);

    constraints.gridwidth = GridBagConstraints.REMAINDER;
    inputPanel.add(passwordField, constraints);

    proceedActions.add(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        pc.setPassword(passwordField.getPassword());
      }
    });
  }

  private void processNameCallback(final List<ActionListener> proceedActions,
      JPanel inputPanel, final NameCallback nc) {
    JLabel promptLabel = new JLabel(nc.getPrompt());
    final JTextField nameTextField = new JTextField(DEFAULT_FIELD_LENGTH);

    String defaultName = nc.getDefaultName();
    if (defaultName != null) {
      nameTextField.setText(defaultName);
    }

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = DEFAULT_INSETS;
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = GridBagConstraints.RELATIVE;
    constraints.gridwidth = 1;
    constraints.weightx = 1.0d;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    inputPanel.add(promptLabel, constraints);

    constraints.gridwidth = GridBagConstraints.REMAINDER;
    inputPanel.add(nameTextField, constraints);

    proceedActions.add(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        nc.setName(nameTextField.getText());
      }
    });
  }

  private void processTextOutputCallback(JPanel messagePanel,
      TextOutputCallback toc) throws UnsupportedCallbackException {
    JLabel messageLabel = new JLabel(toc.getMessage(), getIcon(toc),
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

  private JButton createOptionButton(final JDialog callbackDialog,
      final ConfirmationCallback cc, final int option, String text,
      final List<ActionListener> proceedActions) {
    JButton optionButton = new JButton(text);
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
      callbackDialog.getRootPane().setDefaultButton(optionButton);
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
  public void setParentComponent(Component parentComponent) {
    this.parentComponent = parentComponent;
  }

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<Icon> iconFactory) {
    this.iconFactory = iconFactory;
  }

  
  /**
   * Sets the translationProvider.
   * 
   * @param translationProvider the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }
}
