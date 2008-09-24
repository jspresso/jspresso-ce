/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.ulc.components.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jspresso.framework.gui.ulc.components.shared.DateFieldConstants;
import org.jspresso.framework.util.lang.ObjectUtils;

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
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCDateField extends ULCComponent implements IEditorComponent {

  private static final long serialVersionUID = -2279684685951950878L;

  private boolean           editable;
  private String            formatPattern;
  private Locale            locale;
  private Date              value;

  /**
   * Constructs a new <code>ULCDateField</code> instance.
   */
  public ULCDateField() {
    this(((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT))
        .toPattern(), Locale.getDefault());
  }

  /**
   * Constructs a new <code>ULCDateField</code> instance.
   * 
   * @param locale
   *            the user locale.
   */
  public ULCDateField(Locale locale) {
    this(((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT))
        .toPattern(), locale);
  }

  /**
   * Constructs a new <code>ULCDateField</code> instance.
   * 
   * @param formatPattern
   *            the date format pattern to be used in the date field.
   * @param locale
   *            the user locale.
   */
  public ULCDateField(String formatPattern, Locale locale) {
    this.formatPattern = formatPattern;
    this.locale = locale;
    editable = true;
  }

  /**
   * Adds a value change listener.
   * 
   * @param listener
   *            the listener to add.
   */
  public void addValueChangedListener(IValueChangedListener listener) {
    internalAddListener(IUlcEventConstants.VALUE_CHANGED_EVENT, listener);
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
  public void copyAttributes(ICellComponent source) {
    ULCDateField sourceDateField = (ULCDateField) source;
    formatPattern = sourceDateField.formatPattern;
    locale = sourceDateField.locale;
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

  /**
   * Gets the editable.
   * 
   * @return the editable.
   */
  public boolean isEditable() {
    return editable;
  }

  /**
   * Removes a value change listener.
   * 
   * @param listener
   *            the listener to remove.
   */
  public void removeValueChangedListener(IValueChangedListener listener) {
    internalRemoveListener(IUlcEventConstants.VALUE_CHANGED_EVENT, listener);
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
      sendUI(DateFieldConstants.SET_EDITABLE_REQUEST, editableAnything);
    }
  }

  /**
   * Sets the date field value.
   * 
   * @param value
   *            the date field value.
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
   * {@inheritDoc}
   */
  @Override
  public String typeString() {
    return "org.jspresso.framework.gui.ulc.components.client.UIDateField";
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
  protected void saveState(Anything a) {
    super.saveState(a);
    valueToAnything(a);
    a.put(DateFieldConstants.FORMAT_PATTERN_KEY, formatPattern);
    a.put(DateFieldConstants.LANGUAGE_KEY, locale.getLanguage());
    editableToAnything(a);
  }

  private void editableToAnything(Anything args) {
    args.put(DateFieldConstants.EDITABLE_KEY, editable);
  }

  private void handleSetValue(Anything args) {
    long timeMillis = args.get(DateFieldConstants.VALUE_KEY, 0L);
    Date newValue = null;
    if (timeMillis != 0L) {
      newValue = new Date(timeMillis);
    }
    this.value = newValue;
  }

  private void valueToAnything(Anything args) {
    long timeMillis = -0L;
    if (value != null) {
      timeMillis = value.getTime();
    }
    args.put(DateFieldConstants.VALUE_KEY, timeMillis);
  }
}
