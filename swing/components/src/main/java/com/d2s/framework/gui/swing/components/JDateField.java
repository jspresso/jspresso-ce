/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.swing.components;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.DateFormatter;

import net.sf.nachocalendar.components.DateField;
import net.sf.nachocalendar.components.FormatSymbols;

import com.d2s.framework.util.swing.SwingUtil;

/**
 * A subclass of Nacho calendar datefield with some default values and
 * behaviour.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
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
   *            true if the week numbers must be shown.
   * @param locale
   *            the user locale.
   */
  public JDateField(boolean showWeekNumbers, Locale locale) {
    super(showWeekNumbers);
    initDefaultBehaviour(locale);
  }

  /**
   * Constructs a new <code>JDateField</code> instance.
   * 
   * @param formatter
   *            formatter used for the textfield.
   * @param locale
   *            the user locale.
   */
  public JDateField(DateFormatter formatter, Locale locale) {
    super(formatter);
    initDefaultBehaviour(locale);
  }

  /**
   * Constructs a new <code>JDateField</code> instance.
   * 
   * @param locale
   *            the user locale.
   */
  public JDateField(Locale locale) {
    super();
    initDefaultBehaviour(locale);
  }

  /**
   * Turns the date field to be editable or not.
   * 
   * @param editable
   *            true if editable.
   */
  public void setEditable(boolean editable) {
    super.setEnabled(editable);
    getFormattedTextField().setEnabled(true);
    getFormattedTextField().setEditable(editable);
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

  private void initDefaultBehaviour(Locale locale) {
    new FormatSymbols((DateFormatter) getFormattedTextField().getFormatter(),
        locale);
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
    setValue(null);
  }
}
