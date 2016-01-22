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
package org.jspresso.framework.gui.swing.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;

import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.swing.SwingUtil;

/**
 * A swing component to represent an actionable field. It should behave like a
 * button except that the action is parametrized by the field text value. This
 * component is represented by a text field and a button. Actioning the button
 * or the text field triggers the action.
 *
 * @author Vincent Vandenschrick
 */
public class JActionField extends JPanel {

  private static final long serialVersionUID = 5741890319182521808L;

  private final JPanel       buttonPanel;
  private final boolean      showTextField;
  private final JTextField   textField;
  private       List<Action> actions;
  private       String       value;
  private       boolean      fieldEditable;

  /**
   * Constructs a new {@code JActionField} instance.
   *
   * @param showTextField
   *          is the text field visible to the user.
   */
  public JActionField(boolean showTextField) {
    textField = new JTextField();
    textField.getDocument().putProperty("filterNewlines", Boolean.FALSE);
    setLayout(new GridBagLayout());
    if (showTextField) {
      add(textField, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
          new Insets(0, 0, 0, 0), 0, 0));
      super.addFocusListener(new FocusAdapter() {

        @Override
        public void focusGained(FocusEvent e) {
          textField.requestFocusInWindow();
        }
      });
      SwingUtil.enableSelectionOnFocusGained(textField);
    }
    this.showTextField = showTextField;
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

    int buttonPosition;
    if (showTextField) {
      buttonPosition = 1;
    } else {
      buttonPosition = 0;
    }
    add(buttonPanel,
        new GridBagConstraints(buttonPosition, 0, 1, 1, 0, 1, GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
            new Insets(0, 0, 0, 0), 0, 0));
    fieldEditable = true;
  }

  /**
   * Adds a focus listener to the text field.
   *
   * @param l
   *          the listener to add.
   */
  public void addTextFieldFocusListener(FocusListener l) {
    textField.addFocusListener(l);
  }

  /**
   * Gets the actions.
   *
   * @return the actions.
   */
  public List<Action> getActions() {
    return actions;
  }

  /**
   * Gets the action field text.
   *
   * @return the action field text.
   */
  public String getActionText() {
    return textField.getText();
  }

  /**
   * Gets the value.
   *
   * @return the value.
   */
  public Object getValue() {
    return value;
  }

  /**
   * Gets the showTextField.
   *
   * @return the showTextField.
   */
  public boolean isShowingTextField() {
    return showTextField;
  }

  /**
   * Gets whether this action field text is synchronized with its underlying
   * value.
   *
   * @return true if this action field text is synchronized with its underlying
   *         value.
   */
  public boolean isSynchronized() {
    return ObjectUtils.equals(value, textField.getText());
  }

  /**
   * performs the registered action programmatically.
   */
  public void performAction() {
    textField.getAction().actionPerformed(
        new ActionEvent(this, 0, getActionText()));
  }

  /**
   * Removes a focus listener from the text field.
   *
   * @param l
   *          the listener to remove.
   */
  public void removeTextFieldFocusListener(FocusListener l) {
    textField.removeFocusListener(l);
  }

  /**
   * Delegates to textField.
   */
  public void selectAll() {
    textField.selectAll();
  }

  /**
   * Sets the action field action.
   *
   * @param actions
   *          the action field actions.
   */
  public void setActions(List<Action> actions) {
    if (!ObjectUtils.equals(this.actions, actions)) {
      buttonPanel.removeAll();
      this.actions = actions;
      for (Action action : actions) {
        JButton actionButton = new JButton();
        SwingUtil.configureButton(actionButton);
        actionButton.setBackground(textField.getBackground());
        actionButton.setAction(action);
        actionButton.setActionCommand("*");
        actionButton.setText("");
        int buttonSquareSize;
        if (showTextField) {
          buttonSquareSize = textField.getPreferredSize().height;
          if (textField.getAction() == null) {
            textField.setAction(action);
          }
        } else {
          buttonSquareSize = actionButton.getPreferredSize().height;
        }
        actionButton.setPreferredSize(new Dimension(buttonSquareSize,
            buttonSquareSize));
        actionButton.setMinimumSize(actionButton.getPreferredSize());
        actionButton.setMaximumSize(actionButton.getPreferredSize());
        buttonPanel.add(actionButton);
      }
    }
  }

  /**
   * Gets the action field text.
   *
   * @param actionText
   *          the action field text.
   */
  public void setActionText(String actionText) {
    textField.setText(actionText);
  }

  /**
   * Decorates the component with a marker.
   *
   * @param decorated
   *          if the component should be decorated.
   */
  public void setDecorated(boolean decorated) {
    if (decorated) {
      buttonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
          Color.RED.brighter(), Color.RED.darker()));
    } else {
      buttonPanel.setBorder(null);
    }
  }

  /**
   * Turns the action field to be editable or not.
   *
   * @param enabled
   *          true if enabled.
   */
  @Override
  public void setEnabled(boolean enabled) {
    if (textField.getAction() != null) {
      textField.getAction().setEnabled(enabled);
    }
    textField.setEditable(enabled && fieldEditable);
  }

  /**
   * Sets the value.
   *
   * @param value
   *          the value to set.
   */
  public void setValue(String value) {
    this.value = value;
    textField.setText(value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition,
      boolean pressed) {
    if (super.processKeyBinding(ks, e, condition, pressed)) {
      return true;
    }
    Object binding = textField.getInputMap(condition).get(ks);
    if (binding != null && textField.getActionMap().get(binding) != null) {
      textField.dispatchEvent(e);
      return true;
    }
    return false;
  }

  /**
   * Sets field editable.
   *
   * @param fieldEditable the field editable
   */
  public void setFieldEditable(boolean fieldEditable) {
    this.fieldEditable = fieldEditable;
    textField.setEditable(isEnabled() && fieldEditable);
  }

  /**
   * Gets the text field in this action field.
   *
   * @return the text field
   */
  public JTextField getTextField() {
    return textField;
  }
}
