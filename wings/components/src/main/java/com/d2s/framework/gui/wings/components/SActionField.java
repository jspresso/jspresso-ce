/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.wings.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.Action;

import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SDimension;
import org.wings.SGridBagLayout;
import org.wings.SPanel;
import org.wings.STextField;
import org.wings.border.SBevelBorder;
import org.wings.event.SDocumentListener;

import com.d2s.framework.util.lang.ObjectUtils;

/**
 * A wings component to represent an actionable field. It should behave like a
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
public class SActionField extends SPanel  {

  private static final long serialVersionUID = 5741890319182521808L;

  private STextField        textField;
  private Object            value;
  private boolean           showTextField;
  private List<Action>      actions;
  private SPanel            buttonPanel;
  private Action            defaultAction;

  /**
   * Constructs a new <code>SActionField</code> instance.
   *
   * @param showTextField
   *          is the text field visible to the user.
   */
  public SActionField(boolean showTextField) {
    textField = new STextField();
    setLayout(new SGridBagLayout());
    if (showTextField) {
      add(textField, new GridBagConstraints(0, 0, 1, 1, 1, 0,
          GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
              0, 0, 0, 0), 0, 0));
      textField.setPreferredSize(SDimension.FULLWIDTH);
    }
    this.showTextField = showTextField;
    buttonPanel = new SPanel();
    buttonPanel.setLayout(new SBoxLayout(SBoxLayout.X_AXIS));

    int buttonPosition;
    if (showTextField) {
      buttonPosition = 1;
    } else {
      buttonPosition = 0;
    }
    add(buttonPanel, new GridBagConstraints(buttonPosition, 0, 1, 1, 0, 1,
        GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0,
            0, 0), 0, 0));
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
        SButton actionButton = new SButton();
        actionButton.setAction(action);
        actionButton.setActionCommand("%");
        actionButton.setText(null);
        if (showTextField) {
          if (defaultAction == null) {
            defaultAction = action;
            textField.addActionListener(action);
          }
        }
        buttonPanel.add(actionButton);
      }
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
   *          the listener to add.
   */
  public void addTextFieldDocumentListener(SDocumentListener l) {
    textField.addDocumentListener(l);
  }

  /**
   * Removes a focus listener from the text field.
   *
   * @param l
   *          the listener to remove.
   */
  public void removeTextFieldFocusListener(SDocumentListener l) {
    textField.removeDocumentListener(l);
  }

  /**
   * performs the registered action programatically.
   */
  public void performAction() {
    defaultAction.actionPerformed(new ActionEvent(this, 0, getActionText()));
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
   * Turns the date field to be editable or not.
   *
   * @param editable
   *          true if editable.
   */
  public void setEditable(boolean editable) {
    if (defaultAction != null) {
      defaultAction.setEnabled(editable);
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
    if (defaultAction != null) {
      defaultAction.setEnabled(enabled);
    }
    textField.setEnabled(enabled);
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
   * Decorates the component with a marker.
   *
   * @param decorated
   *          if the component should be decorated.
   */
  public void setDecorated(boolean decorated) {
    if (decorated) {
      SBevelBorder border = new SBevelBorder(SBevelBorder.RAISED);
      border.setColor(Color.red);
      buttonPanel.setBorder(border);
    } else {
      buttonPanel.setBorder(null);
    }
  }
}
