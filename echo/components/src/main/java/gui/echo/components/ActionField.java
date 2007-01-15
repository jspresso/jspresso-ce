/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package gui.echo.components;

import java.util.List;

import nextapp.echo2.app.Border;
import nextapp.echo2.app.Button;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

import com.d2s.framework.util.lang.ObjectUtils;

import echopointng.TextFieldEx;

/**
 * An echo component to represent an actionable field. It should behave like a
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
public class ActionField extends Row {

  private static final long serialVersionUID = -7222022464238651710L;

  private TextFieldEx       textField;
  private Object            value;
  private boolean           showTextField;
  private List<Button>      actions;
  private Row               buttonRow;
  private Button            defaultAction;

  /**
   * Constructs a new <code>JActionField</code> instance.
   *
   * @param showTextField
   *          is the text field visible to the user.
   */
  public ActionField(boolean showTextField) {
    textField = new TextFieldEx();
    if (showTextField) {
      add(textField);
    }
    this.showTextField = showTextField;
    buttonRow = new Row();
    add(buttonRow);
  }

  /**
   * Gets the actions.
   *
   * @return the actions.
   */
  public List<Button> getActions() {
    return actions;
  }

  /**
   * Sets the action field action.
   *
   * @param actions
   *          the action field actions.
   */
  public void setActions(List<Button> actions) {
    if (!ObjectUtils.equals(this.actions, actions)) {
      buttonRow.removeAll();
      this.actions = actions;
      for (Button action : actions) {
        action.setBackground(textField.getBackground());
        action.setActionCommand(null);
        textField.setActionCommand("%");
        action.setText("");
        Extent buttonSquareSize;
        if (showTextField) {
          buttonSquareSize = textField.getHeight();
          if (defaultAction == null) {
            defaultAction = action;
            textField.addActionListener(new ActionListener() {

              private static final long serialVersionUID = -884454749290831238L;

              public void actionPerformed(@SuppressWarnings("unused")
              ActionEvent e) {
                defaultAction.doAction();
              }
            });
          }
        } else {
          textField.setActionCommand("%");
          buttonSquareSize = action.getHeight();
        }
        action.setWidth(buttonSquareSize);
        action.setHeight(buttonSquareSize);
        buttonRow.add(action);
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
   * Adds an action listener to the text field.
   *
   * @param l
   *          the listener to add.
   */
  public void addTextFieldActionListener(ActionListener l) {
    textField.addActionListener(l);
  }

  /**
   * Adds an action listener to the text field.
   *
   * @param l
   *          the listener to remove.
   */
  public void removeTextFieldActionListener(ActionListener l) {
    textField.removeActionListener(l);
  }

  /**
   * performs the registered action programatically.
   */
  public void performAction() {
    defaultAction.doAction();
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
    setEnabled(editable);
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
      buttonRow.setBorder(new Border(3, new Color(254, 0, 0),
          Border.STYLE_OUTSET));
    } else {
      buttonRow.setBorder(null);
    }
  }
}
