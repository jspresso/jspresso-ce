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
package org.jspresso.framework.security.swing;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
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

import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.view.IIconFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Uses a Swing dialog to query the user for answers to authentication
 * questions. This can be used by a JAAS application to instantiate a
 * CallbackHandler
 */
public class DialogCallbackHandler implements CallbackHandler {

  private static final Logger  LOG                  = LoggerFactory
                                                        .getLogger(DialogCallbackHandler.class);

  private static final int     DEFAULT_FIELD_LENGTH = 32;
  private static final Insets  DEFAULT_INSETS       = new Insets(5, 5, 5, 5);
  private IIconFactory<Icon>   iconFactory;
  private Locale               locale;

  private Component            parentComponent;
  private ITranslationProvider translationProvider;

  /**
   * Handles the specified set of callbacks.
   *
   * @param callbacks
   *          the callbacks to handle
   * @throws UnsupportedCallbackException
   *           if the callback is not an instance of NameCallback or
   *           PasswordCallback
   */

  @Override
  public void handle(final Callback[] callbacks)
      throws UnsupportedCallbackException {
    try {
      SwingUtilities.invokeAndWait(new Runnable() {

        @SuppressWarnings("ConstantConditions")
        @Override
        public void run() {
          try {
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
              List<Callback> completedCallBacks = new ArrayList<>(
                  Arrays.asList(varCallbacks));
              completedCallBacks.add(defaultToc);
              varCallbacks = completedCallBacks
                  .toArray(new Callback[varCallbacks.length + 1]);
            }

            String dialogTitle = null;

            final JDialog callbackDialog;
            if (parentComponent != null) {
              Window parentWindow = SwingUtil.getVisibleWindow(parentComponent);
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

            List<ActionListener> proceedActions = new ArrayList<>(
                2);

            JPanel messagePanel = null;
            JPanel inputPanel = null;
            JPanel optionPanel = null;

            for (Callback callback : varCallbacks) {
              if (callback instanceof TextOutputCallback) {
                if (messagePanel == null) {
                  messagePanel = new JPanel();
                  messagePanel.setLayout(new GridBagLayout());
                }
                processTextOutputCallback(messagePanel,
                    (TextOutputCallback) callback);
                if (dialogTitle == null) {
                  dialogTitle = translationProvider.getTranslation(
                      ((TextOutputCallback) callback).getMessage(), locale);
                }
              } else if (callback instanceof NameCallback) {
                if (inputPanel == null) {
                  inputPanel = new JPanel();
                  inputPanel.setLayout(new GridBagLayout());
                }
                processNameCallback(proceedActions, inputPanel,
                    (NameCallback) callback);

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
                    optionPanel, (ConfirmationCallback) callback,
                    inputPanel != null);

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
                  ConfirmationCallback.OK_CANCEL_OPTION,
                  ConfirmationCallback.OK);
              processConfirmationCallback(callbackDialog, proceedActions,
                  optionPanel, cc, inputPanel != null);
            }
            dialogPanel.add(optionPanel, constraints);

            if (dialogTitle != null) {
              callbackDialog.setTitle(dialogTitle);
            }
            callbackDialog.getContentPane().add(dialogPanel);
            int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
            callbackDialog.setSize(new Dimension(4 * screenRes, screenRes
                * (varCallbacks.length + 1) / 2));
            callbackDialog.pack();
            SwingUtil.centerOnScreen(callbackDialog);
            callbackDialog.setVisible(true);
          } catch (UnsupportedCallbackException ex) {
            throw new RuntimeException(ex);
          }
        }
      });
    } catch (InterruptedException ex) {
      LOG.error("An unexpected error occurred when handing callbacks.", ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException
          && ex.getCause().getCause() instanceof UnsupportedCallbackException) {
        throw (UnsupportedCallbackException) ex.getCause().getCause();
      }
      LOG.error("An unexpected error occurred when handing callbacks.", ex);
    }
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
   * Sets the translationProvider.
   *
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }

  private JButton createOptionButton(final JDialog callbackDialog,
      final ConfirmationCallback cc, final int option, String text,
      final List<ActionListener> proceedActions) {
    JButton optionButton = new JButton(text);
    if (option == ConfirmationCallback.YES || option == ConfirmationCallback.OK) {
      optionButton.setIcon(iconFactory.getOkYesIcon(iconFactory
          .getSmallIconSize()));
      optionButton.addActionListener(new ActionListener() {

        @Override
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
        optionButton.setIcon(iconFactory.getNoIcon(iconFactory
            .getSmallIconSize()));
      } else if (option == ConfirmationCallback.CANCEL) {
        optionButton.setIcon(iconFactory.getCancelIcon(iconFactory
            .getSmallIconSize()));
      }
      optionButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
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

  private Icon getIcon(TextOutputCallback callback)
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

          @Override
          public void actionPerformed(ActionEvent e) {
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
          optionPanel.add(
              createOptionButton(callbackDialog, cc, ConfirmationCallback.YES,
                  translationProvider.getTranslation("yes", locale),
                  proceedActions), constraints);
          optionPanel.add(
              createOptionButton(callbackDialog, cc, ConfirmationCallback.NO,
                  translationProvider.getTranslation("no", locale),
                  proceedActions), constraints);
          break;
        case ConfirmationCallback.YES_NO_CANCEL_OPTION:
          optionPanel.add(
              createOptionButton(callbackDialog, cc, ConfirmationCallback.YES,
                  translationProvider.getTranslation("yes", locale),
                  proceedActions), constraints);
          optionPanel.add(
              createOptionButton(callbackDialog, cc, ConfirmationCallback.NO,
                  translationProvider.getTranslation("no", locale),
                  proceedActions), constraints);
          optionPanel.add(
              createOptionButton(callbackDialog, cc,
                  ConfirmationCallback.CANCEL,
                  translationProvider.getTranslation("cancel", locale),
                  proceedActions), constraints);
          break;
        case ConfirmationCallback.OK_CANCEL_OPTION:
          optionPanel.add(
              createOptionButton(callbackDialog, cc, ConfirmationCallback.OK,
                  translationProvider.getTranslation("ok", locale),
                  proceedActions), constraints);
          if (hasInput) {
            optionPanel.add(
                createOptionButton(callbackDialog, cc,
                    ConfirmationCallback.CANCEL,
                    translationProvider.getTranslation("cancel", locale),
                    proceedActions), constraints);
          }
          break;
        default:
          throw new UnsupportedCallbackException(cc,
              "Unrecognized option type: " + confirmationOptionType);
      }
    }
  }

  private void processNameCallback(final List<ActionListener> proceedActions,
      JPanel inputPanel, final NameCallback nc) {
    // JLabel promptLabel = new JLabel(nc.getPrompt());
    JLabel promptLabel = new JLabel(translationProvider.getTranslation("user",
        locale) + " :");
    final JTextField nameTextField = new JTextField(DEFAULT_FIELD_LENGTH);

    // String defaultName = nc.getDefaultName();
    // if (defaultName != null) {
    // nameTextField.setText(defaultName);
    // }

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

      @Override
      public void actionPerformed(ActionEvent e) {
        nc.setName(nameTextField.getText());
      }
    });
  }

  private void processPasswordCallback(
      final List<ActionListener> proceedActions, JPanel inputPanel,
      final PasswordCallback pc) {
    // JLabel promptLabel = new JLabel(pc.getPrompt());
    JLabel promptLabel = new JLabel(translationProvider.getTranslation(
        "password", locale) + " :");

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
    inputPanel.add(promptLabel, constraints);

    constraints.weightx = 1.0d;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    inputPanel.add(passwordField, constraints);

    proceedActions.add(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        pc.setPassword(passwordField.getPassword());
      }
    });
  }

  private void processTextOutputCallback(JPanel messagePanel,
      TextOutputCallback toc) throws UnsupportedCallbackException {
    JLabel messageLabel = new JLabel(translationProvider.getTranslation(
        toc.getMessage(), locale), getIcon(toc), SwingConstants.LEADING);
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = DEFAULT_INSETS;
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = GridBagConstraints.RELATIVE;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    constraints.weightx = 1.0d;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    messagePanel.add(messageLabel, constraints);
  }
}
