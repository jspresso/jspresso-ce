/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.gui.ulc.components.shared.ColorPickerConstants;
import com.d2s.framework.util.gui.ColorHelper;
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
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCColorPicker extends ULCComponent implements IEditorComponent {

  private static final long serialVersionUID = 5230716348564257623L;
  private Color             value;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void saveState(Anything a) {
    super.saveState(a);
    valueToAnything(a);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIColorPicker";
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
   * Sets the color picker value.
   *
   * @param value
   *          the color picker value.
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
    String hexColor = "";
    if (value != null) {
      hexColor = ColorHelper.toHexString(value.getRed(), value.getGreen(),
          value.getBlue(), value.getAlpha());
    }
    args.put(ColorPickerConstants.VALUE_KEY, hexColor);
  }

  /**
   * {@inheritDoc}
   */
  public void copyAttributes(@SuppressWarnings("unused")
  ICellComponent source) {
    // no specific copy to achieve.
    // All attributes except value are identical.
  }

  /**
   * {@inheritDoc}
   */
  public boolean areAttributesEqual(ICellComponent component) {
    if (!(component instanceof ULCColorPicker)) {
      return false;
    }
    // no specific copy to achieve.
    // All attributes except value are identical.
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public int attributesHashCode() {
    return new HashCodeBuilder(13, 37).toHashCode();
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

  private void handleSetValue(Anything args) {
    String hexColor = args.get(ColorPickerConstants.VALUE_KEY, "");
    Color newValue = null;
    if (hexColor.length() > 0) {
      int[] rgba = ColorHelper.fromHexString(hexColor);
      newValue = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
    }
    this.value = newValue;
  }
}
