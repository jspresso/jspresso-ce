/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import com.ulcjava.base.application.ULCTextField;

/**
 * A subclass of ULCTextField which selects its content on focus gained..
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCOnFocusSelectTextField extends ULCTextField {

  private static final long serialVersionUID = 5891586122459055284L;

  /**
   * Constructs a new <code>ULCOnFocusSelectTextField</code> instance.
   */
  public ULCOnFocusSelectTextField() {
    super();
  }

  /**
   * Constructs a new <code>ULCOnFocusSelectTextField</code> instance.
   * 
   * @param columns
   *            field column.
   */
  public ULCOnFocusSelectTextField(int columns) {
    super(columns);
  }

  /**
   * Constructs a new <code>ULCOnFocusSelectTextField</code> instance.
   * 
   * @param text
   *            field text.
   */
  public ULCOnFocusSelectTextField(String text) {
    super(text);
  }

  /**
   * Constructs a new <code>ULCOnFocusSelectTextField</code> instance.
   * 
   * @param text
   *            field text.
   * @param columns
   *            field columns.
   */
  public ULCOnFocusSelectTextField(String text, int columns) {
    super(text, columns);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIOnFocusSelectTextField";
  }
}
