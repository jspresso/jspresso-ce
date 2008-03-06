/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import com.d2s.framework.util.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.gui.ulc.components.shared.ActionFieldConstants;
import com.d2s.framework.util.bean.SinglePropertyChangeSupport;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.IEditorComponent;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCContainer;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.IFocusListener;
import com.ulcjava.base.application.util.ULCIcon;
import com.ulcjava.base.server.ICellComponent;
import com.ulcjava.base.shared.internal.Anything;

/**
 * ULC server half object for action fields.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCActionField extends ULCComponent implements IEditorComponent {

  private static final long     serialVersionUID = 8107147713298807811L;

  private List<IAction>         actions;

  private String                actionText;
  private boolean               decorated;
  private boolean               editable;
  private int                   editingColumn;
  private int                   editingRow;
  private PropertyChangeSupport propertyChangeSupport;
  private boolean               showTextField;
  private ULCActionField        sourceActionField;

  private Object                value;

  /**
   * Constructs a new <code>ULCActionField</code> instance.
   */
  public ULCActionField() {
    this(true);
  }

  /**
   * Constructs a new <code>ULCActionField</code> instance.
   * 
   * @param showTextField
   *            is the text field visible to the user.
   */
  public ULCActionField(boolean showTextField) {
    propertyChangeSupport = new SinglePropertyChangeSupport(this);
    editable = true;
    this.showTextField = showTextField;
  }

  /**
   * Not supported.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void addFocusListener(@SuppressWarnings("unused")
  IFocusListener l) {
    throw new UnsupportedOperationException();
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param listener
   *            the listener to add.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param propertyName
   *            the name of the property.
   * @param listener
   *            the listener to add.
   */
  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * {@inheritDoc}
   */
  public boolean areAttributesEqual(ICellComponent component) {
    if (!(component instanceof ULCActionField)) {
      return false;
    }
    if (this == component) {
      return true;
    }
    ULCActionField otherActionField = (ULCActionField) component;
    return new EqualsBuilder().append(actions, otherActionField.actions)
        .isEquals();
  }

  /**
   * {@inheritDoc}
   */
  public int attributesHashCode() {
    return new HashCodeBuilder(17, 53).append(actions).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  public void copyAttributes(ICellComponent source) {
    sourceActionField = (ULCActionField) source;
    setActions(sourceActionField.actions);
    showTextField = sourceActionField.showTextField;
  }

  /**
   * Gets the actions.
   * 
   * @return the actions.
   */
  public List<IAction> getActions() {
    return actions;
  }

  /**
   * Gets the action field text.
   * 
   * @return the action field text.
   */
  public String getActionText() {
    return actionText;
  }

  /**
   * Gets the editingColumn.
   * 
   * @return the editingColumn.
   */
  public int getEditingColumn() {
    return editingColumn;
  }

  /**
   * Gets the editingRow.
   * 
   * @return the editingRow.
   */
  public int getEditingRow() {
    return editingRow;
  }

  /**
   * Gets the action field value.
   * 
   * @return the action field value.
   */
  public Object getValue() {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRequest(String request, Anything args) {
    if (request.equals(ActionFieldConstants.SET_ACTION_TEXT_REQUEST)) {
      handleSetActionText(args);
    } else if (request.equals(ActionFieldConstants.TRIGGER_ACTION_REQUEST)) {
      handleTriggerAction(args);
    } else if (request.equals(ActionFieldConstants.SET_EDITING_CELL_REQUEST)) {
      handleSetEditingCell(args);
    } else if (request.equals(ActionFieldConstants.SYNC_STATE_REQUEST)) {
      handleSyncState(args);
    } else {
      super.handleRequest(request, args);
    }
  }

  /**
   * Gets the editable.
   * 
   * @return the editable.
   */
  public boolean isEditable() {
    return editable;
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
   * Gets wether this action text is synchronized with the value.
   * 
   * @return true if this action field text is synchronized with its underlying
   *         value.
   */
  public boolean isSynchronized() {
    return ObjectUtils.equals(valueToString(), actionText);
  }

  /**
   * performs the registered action programatically.
   * 
   * @param index
   *            the index of the action to be triggerred.
   */
  public void performAction(int index) {
    performAction(index, actionText);
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param listener
   *            the listener to remove.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param propertyName
   *            the name of the property.
   * @param listener
   *            the listener to remove.
   */
  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
  }

  /**
   * Sets the action.
   * 
   * @param actions
   *            the actions to set.
   */
  public void setActions(List<IAction> actions) {
    if (!ObjectUtils.equals(this.actions, actions)) {
      this.actions = actions;

      Anything actionsAnything = new Anything();
      Anything actionIconsAnything = new Anything();
      fillActionData(actionsAnything, actionIconsAnything);

      Anything args = new Anything();
      args.put(ActionFieldConstants.ACTIONS_KEY, actionsAnything);
      args.put(ActionFieldConstants.ICONS_KEY, actionIconsAnything);

      sendUI(ActionFieldConstants.SET_ACTIONS_REQUEST, args);
    }
  }

  /**
   * Sets the action field text.
   * 
   * @param actionText
   *            the action field text.
   */
  public void setActionText(String actionText) {
    updateActionText(actionText, true);
  }

  /**
   * Decorates the component with a marker.
   * 
   * @param decorated
   *            if the component should be decorated.
   */
  public void setDecorated(boolean decorated) {
    if (this.decorated != decorated) {
      this.decorated = decorated;
      Anything decoratedAnything = new Anything();
      decoratedToAnything(decoratedAnything);
      sendUI(ActionFieldConstants.SET_DECORATED_REQUEST, decoratedAnything);
    }
  }

  /**
   * Sets the editable.
   * 
   * @param editable
   *            the editable to set.
   */
  public void setEditable(boolean editable) {
    if (this.editable != editable) {
      this.editable = editable;
      Anything editableAnything = new Anything();
      editableToAnything(editableAnything);
      sendUI(ActionFieldConstants.SET_EDITABLE_REQUEST, editableAnything);
    }
  }

  /**
   * Only to be used when ULCAction field is used as an editor inside a ULC
   * table.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setParent(ULCContainer parent) {
    super.setParent(parent);
  }

  /**
   * Sets the action field value.
   * 
   * @param value
   *            the action field value.
   */
  public void setValue(Object value) {
    updateValue(value, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIActionField";
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param propertyName
   *            the name of the property.
   * @param oldValue
   *            the old property value.
   * @param newValue
   *            the new property value.
   */
  protected void firePropertyChange(String propertyName, boolean oldValue,
      boolean newValue) {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param propertyName
   *            the name of the property.
   * @param oldValue
   *            the old property value.
   * @param newValue
   *            the new property value.
   */
  protected void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getPropertyPrefix() {
    return "Panel";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void saveState(Anything a) {
    super.saveState(a);

    a.put(ActionFieldConstants.SHOW_TEXTFIELD_KEY, showTextField);
    a.put(ActionFieldConstants.ACTION_TEXT_KEY, actionText);

    Anything actionIconsAnything = new Anything();
    Anything actionsAnything = new Anything();

    fillActionData(actionsAnything, actionIconsAnything);

    a.put(ActionFieldConstants.ICONS_KEY, actionIconsAnything);
    a.put(ActionFieldConstants.ACTIONS_KEY, actionsAnything);

    editableToAnything(a);
  }

  private Anything actionToAnything(IAction action) {
    if (action == null) {
      return null;
    }
    Anything actionAnything = new Anything();
    if (action.getValue(IAction.ACCELERATOR_KEY) != null) {
      actionAnything.put(ActionFieldConstants.ACCELERATOR_KEY,
          ((Integer) action.getValue(IAction.ACCELERATOR_KEY)).intValue());
    } else {
      actionAnything.put(ActionFieldConstants.ACCELERATOR_KEY, -1);
    }
    actionAnything.put(ActionFieldConstants.ACTION_COMMAND_KEY, (String) action
        .getValue(IAction.ACTION_COMMAND_KEY));
    actionAnything.put(ActionFieldConstants.LONG_DESCRIPTION, (String) action
        .getValue(IAction.LONG_DESCRIPTION));
    if (action.getValue(IAction.MNEMONIC_KEY) != null) {
      actionAnything.put(ActionFieldConstants.MNEMONIC_KEY, ((Integer) action
          .getValue(IAction.MNEMONIC_KEY)).intValue());
    } else {
      actionAnything.put(ActionFieldConstants.ACCELERATOR_KEY, -1);
    }
    actionAnything.put(ActionFieldConstants.NAME, (String) action
        .getValue(IAction.NAME));
    actionAnything.put(ActionFieldConstants.SHORT_DESCRIPTION, (String) action
        .getValue(IAction.SHORT_DESCRIPTION));
    return actionAnything;
  }

  private void decoratedToAnything(Anything args) {
    args.put(ActionFieldConstants.DECORATED_KEY, decorated);
  }

  private void editableToAnything(Anything args) {
    args.put(ActionFieldConstants.EDITABLE_KEY, editable);
  }

  private void fillActionData(Anything actionsAnything, Anything iconsAnything) {
    if (actions != null) {
      for (IAction action : actions) {
        actionsAnything.append(actionToAnything(action));
        ULCIcon icon = (ULCIcon) action.getValue(IAction.SMALL_ICON);
        icon.upload();
        iconsAnything.append(icon.getRef());
      }
    }
  }

  private void handleSetActionText(Anything args) {
    updateActionText(args.get(ActionFieldConstants.ACTION_TEXT_KEY, ""), false);
  }

  private void handleSetEditingCell(Anything args) {
    setEditingRow(args.get(ActionFieldConstants.EDITING_ROW_KEY, -1));
    setEditingColumn(args.get(ActionFieldConstants.EDITING_COLUMN_KEY, -1));
  }

  private void handleSyncState(Anything args) {
    updateValue(args.get(ActionFieldConstants.ACTION_TEXT_KEY, ""), false);
  }

  private void handleTriggerAction(Anything args) {
    performAction(args.get(ActionFieldConstants.ACTION_INDEX_KEY, 0), args.get(
        ActionFieldConstants.ACTION_TEXT_KEY, ""));
  }

  private void performAction(int index, String command) {
    actions.get(index).actionPerformed(new ActionEvent(this, command));
  }

  private void setEditingColumn(int editingColumn) {
    this.editingColumn = editingColumn;
    if (sourceActionField != null) {
      sourceActionField.setEditingColumn(editingColumn);
    }
  }

  private void setEditingRow(int editingRow) {
    this.editingRow = editingRow;
    if (sourceActionField != null) {
      sourceActionField.setEditingRow(editingRow);
    }
  }

  private void updateActionText(String text, boolean notifyClient) {
    if (!ObjectUtils.equals(this.actionText, text)) {
      String oldActionText = this.actionText;
      this.actionText = text;
      if (notifyClient) {
        Anything actionTextAnything = new Anything();
        actionTextAnything.put(ActionFieldConstants.ACTION_TEXT_KEY, text);
        sendUI(ActionFieldConstants.SET_ACTION_TEXT_REQUEST, actionTextAnything);
      }
      firePropertyChange("actionText", oldActionText, text);
    }
    if (sourceActionField != null) {
      sourceActionField.updateActionText(text, notifyClient);
    }
  }

  private void updateValue(Object aValue, boolean notifyClient) {
    if (!ObjectUtils.equals(this.value, aValue)) {
      Object oldValue = this.value;
      this.value = aValue;
      firePropertyChange("value", oldValue, aValue);
    }
    if (sourceActionField != null) {
      sourceActionField.updateValue(aValue, notifyClient);
    }
    updateActionText(valueToString(), notifyClient);
  }

  private String valueToString() {
    if (value == null) {
      return "";
    }
    return value.toString();
  }
}
