/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.gui.ulc.components.shared.ActionFieldConstants;
import com.d2s.framework.gui.ulc.components.shared.DateFieldConstants;
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
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCActionField extends ULCComponent implements IEditorComponent {

  private static final long     serialVersionUID = 8107147713298807811L;

  private PropertyChangeSupport propertyChangeSupport;

  private Object                value;
  private String                actionText;
  private boolean               editable;
  private IAction               action;
  private ULCIcon               actionIcon;
  private int                   editingRow;
  private int                   editingColumn;

  private ULCActionField        sourceActionField;

  /**
   * Constructs a new <code>ULCActionField</code> instance.
   */
  public ULCActionField() {
    propertyChangeSupport = new PropertyChangeSupport(this);
    editable = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void saveState(Anything a) {
    super.saveState(a);
    a.put(ActionFieldConstants.ACTION_TEXT_KEY, actionText);
    saveState(a, ActionFieldConstants.ICON_KEY, actionIcon, null);
    a.put(ActionFieldConstants.ACTION_KEY, actionToAnything());
    editableToAnything(a);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIActionField";
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
   * Sets the action field value.
   * 
   * @param value
   *          the action field value.
   */
  public void setValue(Object value) {
    updateValue(value, true);
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

  /**
   * Gets the action field text.
   * 
   * @return the action field text.
   */
  public String getActionText() {
    return actionText;
  }

  /**
   * Sets the action field text.
   * 
   * @param actionText
   *          the action field text.
   */
  public void setActionText(String actionText) {
    updateActionText(actionText, true);
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

  /**
   * Gets the action.
   * 
   * @return the action.
   */
  public IAction getAction() {
    return action;
  }

  /**
   * Sets the action.
   * 
   * @param action
   *          the action to set.
   */
  public void setAction(IAction action) {
    if (!ObjectUtils.equals(this.action, action)) {
      this.action = action;
      if (action != null) {
        setActionIcon((ULCIcon) action.getValue(IAction.SMALL_ICON));
      }
      Anything args = new Anything();
      args.append(actionToAnything());
      sendUI(ActionFieldConstants.SET_ACTION_REQUEST, args);
    }
  }

  private void setActionIcon(ULCIcon actionIcon) {
    if (!ObjectUtils.equals(this.actionIcon, actionIcon)) {
      ULCIcon oldActionIcon = this.actionIcon;
      this.actionIcon = actionIcon;
      update(ActionFieldConstants.ICON_KEY, oldActionIcon, this.actionIcon);
    }
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

  private Anything actionToAnything() {
    if (action == null) {
      return null;
    }
    Anything actionAnything = new Anything();
    if (action.getValue(ActionFieldConstants.ACCELERATOR_KEY) != null) {
      actionAnything.put(ActionFieldConstants.ACCELERATOR_KEY,
          ((Integer) action.getValue(ActionFieldConstants.ACCELERATOR_KEY))
              .intValue());
    } else {
      actionAnything.put(ActionFieldConstants.ACCELERATOR_KEY, -1);
    }
    actionAnything.put(ActionFieldConstants.ACTION_COMMAND_KEY, (String) action
        .getValue(ActionFieldConstants.ACTION_COMMAND_KEY));
    actionAnything.put(ActionFieldConstants.LONG_DESCRIPTION, (String) action
        .getValue(ActionFieldConstants.LONG_DESCRIPTION));
    if (action.getValue(ActionFieldConstants.MNEMONIC_KEY) != null) {
      actionAnything.put(ActionFieldConstants.MNEMONIC_KEY, ((Integer) action
          .getValue(ActionFieldConstants.MNEMONIC_KEY)).intValue());
    } else {
      actionAnything.put(ActionFieldConstants.ACCELERATOR_KEY, -1);
    }
    actionAnything.put(ActionFieldConstants.NAME, (String) action
        .getValue(ActionFieldConstants.NAME));
    actionAnything.put(ActionFieldConstants.SHORT_DESCRIPTION, (String) action
        .getValue(ActionFieldConstants.SHORT_DESCRIPTION));
    return actionAnything;
  }

  private void editableToAnything(Anything args) {
    args.put(DateFieldConstants.EDITABLE_KEY, editable);
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
  public void copyAttributes(ICellComponent source) {
    sourceActionField = (ULCActionField) source;
    setAction(sourceActionField.action);
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
    return new EqualsBuilder().append(action, otherActionField.action)
        .isEquals();
  }

  /**
   * {@inheritDoc}
   */
  public int attributesHashCode() {
    return new HashCodeBuilder(17, 53).append(action).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRequest(String request, Anything args) {
    if (request.equals(ActionFieldConstants.SET_ACTION_TEXT_REQUEST)) {
      handleSetActionText(args);
    } else if (request.equals(ActionFieldConstants.SET_EDITING_CELL_REQUEST)) {
      handleSetEditingCell(args);
    } else if (request.equals(ActionFieldConstants.SYNC_STATE_REQUEST)) {
      handleSyncState(args);
    } else {
      super.handleRequest(request, args);
    }
  }

  private void handleSetActionText(Anything args) {
    updateActionText(args.get(ActionFieldConstants.ACTION_TEXT_KEY, ""), false);
  }

  private void handleSyncState(Anything args) {
    updateValue(args.get(ActionFieldConstants.ACTION_TEXT_KEY, ""), false);
  }

  private void handleSetEditingCell(Anything args) {
    setEditingRow(args.get(ActionFieldConstants.EDITING_ROW_KEY, -1));
    setEditingColumn(args.get(ActionFieldConstants.EDITING_COLUMN_KEY, -1));
  }

  /**
   * performs the registered action programatically.
   */
  public void performAction() {
    action.actionPerformed(new ActionEvent(this, actionText));
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
   *          the listener to add.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param propertyName
   *          the name of the property.
   * @param listener
   *          the listener to add.
   */
  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param listener
   *          the listener to remove.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param propertyName
   *          the name of the property.
   * @param listener
   *          the listener to remove.
   */
  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param propertyName
   *          the name of the property.
   * @param oldValue
   *          the old property value.
   * @param newValue
   *          the new property value.
   */
  protected void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param propertyName
   *          the name of the property.
   * @param oldValue
   *          the old property value.
   * @param newValue
   *          the new property value.
   */
  protected void firePropertyChange(String propertyName, boolean oldValue,
      boolean newValue) {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
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
   * Gets the editable.
   * 
   * @return the editable.
   */
  public boolean isEditable() {
    return editable;
  }

  /**
   * Sets the editable.
   * 
   * @param editable
   *          the editable to set.
   */
  public void setEditable(boolean editable) {
    if (this.editable != editable) {
      this.editable = editable;
      Anything editableAnything = new Anything();
      editableToAnything(editableAnything);
      sendUI(DateFieldConstants.SET_EDITABLE_REQUEST, editableAnything);
    }
  }
}
