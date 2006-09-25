/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.gui.ulc.components.shared.JEditTextAreaConstants;
import com.ulcjava.base.application.IEditorComponent;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.event.ValueChangedEvent;
import com.ulcjava.base.application.event.serializable.IValueChangedListener;
import com.ulcjava.base.server.ICellComponent;
import com.ulcjava.base.shared.IUlcEventConstants;
import com.ulcjava.base.shared.internal.Anything;

/**
 * ULC server half object for JEditTextArea.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCJEditTextArea extends ULCComponent implements IEditorComponent {

  private static final long serialVersionUID = 2839452029572051785L;

  private String            text;
  private String            language;
  private boolean           editable;

  /**
   * Constructs a new <code>ULCJEditTextArea</code> instance.
   */
  public ULCJEditTextArea() {
    this("Java");
  }

  /**
   * Constructs a new <code>ULCJEditTextArea</code> instance.
   * 
   * @param language
   *          the date format pattern to be used in the date field.
   */
  public ULCJEditTextArea(String language) {
    this.language = language;
    editable = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void saveState(Anything a) {
    super.saveState(a);
    textToAnything(a);
    a.put(JEditTextAreaConstants.LANGUAGE_KEY, language);
    editableToAnything(a);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIJEditTextArea";
  }

  /**
   * Gets the date field text.
   * 
   * @return the date field text.
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the date field text.
   * 
   * @param text
   *          the date field text.
   */
  public void setText(String text) {
    if (!ObjectUtils.equals(this.text, text)) {
      this.text = text;
      Anything valueAnything = new Anything();
      textToAnything(valueAnything);
      sendUI(JEditTextAreaConstants.SET_TEXT_REQUEST, valueAnything);
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
    return "TextArea";
  }

  private void textToAnything(Anything args) {
    args.put(JEditTextAreaConstants.TEXT_KEY, text);
  }

  private void editableToAnything(Anything args) {
    args.put(JEditTextAreaConstants.EDITABLE_KEY, editable);
  }

  /**
   * {@inheritDoc}
   */
  public void copyAttributes(ICellComponent source) {
    ULCJEditTextArea sourceDateField = (ULCJEditTextArea) source;
    language = sourceDateField.language;
  }

  /**
   * {@inheritDoc}
   */
  public boolean areAttributesEqual(ICellComponent component) {
    if (!(component instanceof ULCJEditTextArea)) {
      return false;
    }
    if (this == component) {
      return true;
    }
    ULCJEditTextArea sourceDateField = (ULCJEditTextArea) component;
    return new EqualsBuilder().append(language, sourceDateField.language)
        .isEquals();
  }

  /**
   * {@inheritDoc}
   */
  public int attributesHashCode() {
    return new HashCodeBuilder(13, 57).append(language).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRequest(String request, Anything args) {
    if (request.equals(JEditTextAreaConstants.SET_TEXT_REQUEST)) {
      handleSetValue(args);
    } else {
      super.handleRequest(request, args);
    }
  }

  private void handleSetValue(Anything args) {
    this.text = args.get(JEditTextAreaConstants.TEXT_KEY, "");
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
      sendUI(JEditTextAreaConstants.SET_EDITABLE_REQUEST, editableAnything);
    }
  }
}
