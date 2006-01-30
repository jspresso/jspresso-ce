/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.gui.ulc.components.shared.DateFieldConstants;
import com.ulcjava.base.application.IEditorComponent;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.event.ValueChangedEvent;
import com.ulcjava.base.application.event.serializable.IValueChangedListener;
import com.ulcjava.base.server.ICellComponent;
import com.ulcjava.base.shared.IUlcEventConstants;
import com.ulcjava.base.shared.internal.Anything;

/**
 * ULC server half object for date fields.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCDateField extends ULCComponent implements IEditorComponent {

  private static final long serialVersionUID = -2279684685951950878L;

  private Date              value;
  private String            formatPattern;
  private boolean           editable;

  /**
   * Constructs a new <code>ULCDateField</code> instance.
   */
  public ULCDateField() {
    this(((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT))
        .toPattern());
  }

  /**
   * Constructs a new <code>ULCDateField</code> instance.
   * 
   * @param formatPattern
   *          the date format pattern to be used in the date field.
   */
  public ULCDateField(String formatPattern) {
    this.formatPattern = formatPattern;
    editable = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void saveState(Anything a) {
    super.saveState(a);
    valueToAnything(a);
    a.put(DateFieldConstants.FORMAT_PATTERN_KEY, formatPattern);
    editableToAnything(a);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIDateField";
  }

  /**
   * Gets the date field value.
   * 
   * @return the date field value.
   */
  public Date getValue() {
    return value;
  }

  /**
   * Sets the date field value.
   * 
   * @param value
   *          the date field value.
   */
  public void setValue(Date value) {
    if (!ObjectUtils.equals(this.value, value)) {
      this.value = value;
      Anything valueAnything = new Anything();
      valueToAnything(valueAnything);
      sendUI(DateFieldConstants.SET_VALUE_REQUEST, valueAnything);
    }
  }

  /**
   * Adds a value change listener.
   * 
   * @param listener
   *          the listener to add.
   */
  public void addValueChangedListener(IValueChangedListener listener) {
    internalAddListener(IUlcEventConstants.VALUE_CHANGED_EVENT, listener);
  }

  /**
   * Removes a value change listener.
   * 
   * @param listener
   *          the listener to remove.
   */
  public void removeValueChangedListener(IValueChangedListener listener) {
    internalRemoveListener(IUlcEventConstants.VALUE_CHANGED_EVENT, listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void handleEvent(int listenerType, int eventId, Anything args) {
    if (listenerType == IUlcEventConstants.VALUE_CHANGED_EVENT) {
      distributeToListeners(new ValueChangedEvent(this));
    } else {
      super.handleEvent(listenerType, eventId, args);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getPropertyPrefix() {
    return "Panel";
  }

  private void valueToAnything(Anything args) {
    long timeMillis = -0L;
    if (value != null) {
      timeMillis = value.getTime();
    }
    args.put(DateFieldConstants.VALUE_KEY, timeMillis);
  }

  private void editableToAnything(Anything args) {
    args.put(DateFieldConstants.EDITABLE_KEY, editable);
  }

  /**
   * {@inheritDoc}
   */
  public void copyAttributes(ICellComponent source) {
    ULCDateField sourceDateField = (ULCDateField) source;
    formatPattern = sourceDateField.formatPattern;
  }

  /**
   * {@inheritDoc}
   */
  public boolean areAttributesEqual(ICellComponent component) {
    if (!(component instanceof ULCDateField)) {
      return false;
    }
    if (this == component) {
      return true;
    }
    ULCDateField sourceDateField = (ULCDateField) component;
    return new EqualsBuilder().append(formatPattern,
        sourceDateField.formatPattern).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  public int attributesHashCode() {
    return new HashCodeBuilder(13, 57).append(formatPattern).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRequest(String request, Anything args) {
    if (request.equals(DateFieldConstants.SET_VALUE_REQUEST)) {
      handleSetValue(args);
    } else {
      super.handleRequest(request, args);
    }
  }

  private void handleSetValue(Anything args) {
    long timeMillis = args.get(DateFieldConstants.VALUE_KEY, 0L);
    Date newValue = null;
    if (timeMillis != 0L) {
      newValue = new Date(timeMillis);
    }
    this.value = newValue;
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
