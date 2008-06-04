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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jspresso.framework.gui.ulc.components.shared.ColorPickerConstants;
import org.jspresso.framework.util.gui.ColorHelper;
import org.jspresso.framework.util.lang.ObjectUtils;

import com.ulcjava.base.application.IEditorComponent;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.event.ValueChangedEvent;
import com.ulcjava.base.application.event.serializable.IValueChangedListener;
import com.ulcjava.base.application.util.Color;
import com.ulcjava.base.server.ICellComponent;
import com.ulcjava.base.shared.IUlcEventConstants;
import com.ulcjava.base.shared.internal.Anything;

/**
 * ULC server half object for color picker. The client-side half object is a
 * button having its background set to the currently selected color or the
 * default one if the underlying value is null.
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
public class ULCColorPicker extends ULCComponent implements IEditorComponent {

  private static final long serialVersionUID = 5230716348564257623L;
  private Color             resetValue;
  private Color             value;

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
    if (!(component instanceof ULCColorPicker)) {
      return false;
    }
    if (this == component) {
      return true;
    }
    ULCColorPicker sourceColorPicker = (ULCColorPicker) component;
    return new EqualsBuilder().append(resetValue, sourceColorPicker.resetValue)
        .isEquals();
  }

  /**
   * {@inheritDoc}
   */
  public int attributesHashCode() {
    return new HashCodeBuilder(13, 37).append(resetValue).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  public void copyAttributes(@SuppressWarnings("unused")
  ICellComponent source) {
    ULCColorPicker sourceColorPicker = (ULCColorPicker) source;
    resetValue = sourceColorPicker.resetValue;
  }

  /**
   * Gets the color picker resetValue.
   * 
   * @return the color picker value.
   */
  public Color getResetValue() {
    return resetValue;
  }

  /**
   * Gets the color picker value.
   * 
   * @return the color picker value.
   */
  public Color getValue() {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRequest(String request, Anything args) {
    if (request.equals(ColorPickerConstants.SET_VALUE_REQUEST)) {
      handleSetValue(args);
    } else {
      super.handleRequest(request, args);
    }
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
   * Sets the color picker resetValue.
   * 
   * @param resetValue
   *            the color picker resetValue.
   */
  public void setResetValue(Color resetValue) {
    if (!ObjectUtils.equals(this.resetValue, resetValue)) {
      this.resetValue = resetValue;
      Anything resetValueAnything = new Anything();
      resetValueToAnything(resetValueAnything);
      sendUI(ColorPickerConstants.SET_RESETVALUE_REQUEST, resetValueAnything);
    }
  }

  /**
   * Sets the color picker value.
   * 
   * @param value
   *            the color picker value.
   */
  public void setValue(Color value) {
    if (!ObjectUtils.equals(this.value, value)) {
      this.value = value;
      Anything valueAnything = new Anything();
      valueToAnything(valueAnything);
      sendUI(ColorPickerConstants.SET_VALUE_REQUEST, valueAnything);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String typeString() {
    return "org.jspresso.framework.gui.ulc.components.client.UIColorPicker";
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
    resetValueToAnything(a);
  }

  private void handleSetValue(Anything args) {
    String hexColor = args.get(ColorPickerConstants.VALUE_KEY, "");
    Color newValue = null;
    if (hexColor.length() > 0) {
      int[] rgba = ColorHelper.fromHexString(hexColor);
      newValue = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
    }
    this.value = newValue;
  }

  private void resetValueToAnything(Anything args) {
    String hexColor = "";
    if (resetValue != null) {
      hexColor = ColorHelper.toHexString(resetValue.getRed(), resetValue
          .getGreen(), resetValue.getBlue(), resetValue.getAlpha());
    }
    args.put(ColorPickerConstants.RESETVALUE_KEY, hexColor);
  }

  private void valueToAnything(Anything args) {
    String hexColor = "";
    if (value != null) {
      hexColor = ColorHelper.toHexString(value.getRed(), value.getGreen(),
          value.getBlue(), value.getAlpha());
    }
    args.put(ColorPickerConstants.VALUE_KEY, hexColor);
  }
}
