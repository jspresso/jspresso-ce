/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.swing.components;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.DateFormatter;

import net.sf.nachocalendar.components.DateField;

import com.d2s.framework.util.swing.SwingUtil;

/**
 * A subclass of Nacho calendar datefield with some default values and
 * behaviour.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JDateField extends DateField {

  private static final long serialVersionUID = -4535367240988468690L;

  /**
   * Constructs a new <code>JDateField</code> instance.
   * 
   * @param showWeekNumbers
   *          true if the week numbers must be shown.
   */
  public JDateField(boolean showWeekNumbers) {
    super(showWeekNumbers);
    initDefaultBehaviour();
  }

  /**
   * Constructs a new <code>JDateField</code> instance.
   */
  public JDateField() {
    super();
    initDefaultBehaviour();
  }

  /**
   * Constructs a new <code>JDateField</code> instance.
   * 
   * @param formatter
   *          formatter used for the textfield.
   */
  public JDateField(DateFormatter formatter) {
    super(formatter);
    initDefaultBehaviour();
  }

  private void initDefaultBehaviour() {
    getFormattedTextField().setBorder(
        BorderFactory.createEmptyBorder(1, 5, 1, 5));
    SwingUtil.enableSelectionOnFocusGained(getFormattedTextField());
    addFocusListener(new FocusAdapter() {

      @Override
      public void focusGained(FocusEvent e) {
        if (!e.isTemporary()) {
          JTextField tf = ((DateField) e.getSource()).getFormattedTextField();
          tf.requestFocus();
        }
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition,
      boolean pressed) {
    if (super.processKeyBinding(ks, e, condition, pressed)) {
      return true;
    }
    Object binding = getFormattedTextField().getInputMap(condition).get(ks);
    if (binding != null
        && getFormattedTextField().getActionMap().get(binding) != null) {
      getFormattedTextField().dispatchEvent(e);
      return true;
    }
    return false;
  }

  /**
   * Turns the date field to be editable or not.
   * 
   * @param editable
   *          true if editable.
   */
  public void setEditable(boolean editable) {
    super.setEnabled(editable);
    getFormattedTextField().setEnabled(true);
    getFormattedTextField().setEditable(editable);
  }
}
