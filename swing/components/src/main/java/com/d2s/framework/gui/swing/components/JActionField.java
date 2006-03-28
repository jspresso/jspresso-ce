/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.swing.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import org.apache.commons.lang.ObjectUtils;

import com.d2s.framework.util.swing.SwingUtil;

/**
 * A swing component to represent an actionable field. It should behave like a
 * button except that the action is parametrized by the field text value. This
 * component is represented by a text field and a button. Actionning the button
 * or the text field triggers the action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JActionField extends JPanel {

  private static final long serialVersionUID = 5741890319182521808L;

  private JTextField        textField;
  private Object            value;
  private boolean           showTextField;

  /**
   * Constructs a new <code>JActionField</code> instance.
   * 
   * @param showTextField
   *          is the text field visible to the user.
   */
  public JActionField(boolean showTextField) {
    textField = new JTextField();
    setLayout(new BorderLayout());
    if (showTextField) {
      add(textField, BorderLayout.CENTER);
      Border border = textField.getBorder();
      textField.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
      setBorder(border);
      super.addFocusListener(new FocusAdapter() {

        @Override
        public void focusGained(@SuppressWarnings("unused")
        FocusEvent e) {
          textField.requestFocus();
        }
      });
      SwingUtil.enableSelectionOnFocusGained(textField);
    }
    this.showTextField = showTextField;
  }

  /**
   * Sets the action field action.
   * 
   * @param action
   *          the action field action.
   */
  public void setAction(Action action) {
    textField.setAction(action);
    JButton actionButton = new JButton();
    actionButton.setBackground(textField.getBackground());
    actionButton.setAction(action);
    actionButton.setActionCommand("%");
    actionButton.setText("");
    if (showTextField) {
      actionButton.setPreferredSize(new Dimension(
          textField.getPreferredSize().height,
          textField.getPreferredSize().height));
      add(actionButton, BorderLayout.EAST);
    } else {
      add(actionButton, BorderLayout.CENTER);
    }
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
   * Sets the value.
   * 
   * @param value
   *          the value to set.
   */
  public void setValue(Object value) {
    this.value = value;
    textField.setText(valueToString());
  }

  /**
   * Adds a focus listener to the text field.
   * 
   * @param l
   *          the listener to remove.
   */
  public void addTextFieldFocusListener(FocusListener l) {
    textField.addFocusListener(l);
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
   * performs the registered action programatically.
   */
  public void performAction() {
    textField.getAction().actionPerformed(
        new ActionEvent(this, 0, getActionText()));
  }

  /**
   * Gets wether this action field text is synchronized with its underlying
   * value.
   * 
   * @return true if this action field text is synchronized with its underlying
   *         value.
   */
  public boolean isSynchronized() {
    return ObjectUtils.equals(valueToString(), textField.getText());
  }

  private String valueToString() {
    if (value == null) {
      return "";
    }
    return value.toString();
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
   * Gets the action field text.
   * 
   * @param actionText
   *          the action field text.
   */
  public void setActionText(String actionText) {
    textField.setText(actionText);
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
   * Turns the date field to be editable or not.
   * 
   * @param editable
   *          true if editable.
   */
  public void setEditable(boolean editable) {
    if (textField.getAction() != null) {
      textField.getAction().setEnabled(editable);
    }
    textField.setEditable(editable);
  }

  /**
   * Turns the date field to be enabled or not.
   * 
   * @param enabled
   *          true if enabled.
   */
  @Override
  public void setEnabled(boolean enabled) {
    if (textField.getAction() != null) {
      textField.getAction().setEnabled(enabled);
    }
    textField.setEnabled(enabled);
  }

  /**
   * Delegates to textField.
   */
  public void selectAll() {
    textField.selectAll();
  }
}
