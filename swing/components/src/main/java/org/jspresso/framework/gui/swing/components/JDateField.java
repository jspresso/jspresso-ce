/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.swing.components;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.DateFormatter;

import net.sf.nachocalendar.components.DateField;
import net.sf.nachocalendar.components.DayPanel;
import net.sf.nachocalendar.components.DefaultDayRenderer;
import net.sf.nachocalendar.components.FormatSymbols;

import org.jspresso.framework.util.swing.SwingUtil;

/**
 * A subclass of Nacho calendar datefield with some default values and
 * behaviour.
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
   * @param locale
   *          the user locale.
   */
  public JDateField(boolean showWeekNumbers, Locale locale) {
    super(showWeekNumbers);
    initDefaultBehaviour(locale);
  }

  /**
   * Constructs a new <code>JDateField</code> instance.
   * 
   * @param formatter
   *          formatter used for the textfield.
   * @param locale
   *          the user locale.
   */
  public JDateField(DateFormatter formatter, Locale locale) {
    super(formatter);
    initDefaultBehaviour(locale);
  }

  /**
   * Constructs a new <code>JDateField</code> instance.
   * 
   * @param locale
   *          the user locale.
   */
  public JDateField(Locale locale) {
    super();
    initDefaultBehaviour(locale);
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
    setRenderer(new FixedDayRenderer());
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

  private static class FixedDayRenderer extends DefaultDayRenderer {

    private static final long serialVersionUID = 2527435032589911078L;

    /**
     * Constructs a new <code>FixedDayRenderer</code> instance.
     * 
     */
    public FixedDayRenderer() {
      //putClientProperty(SubstanceLookAndFeel.WATERMARK_PROPERTY, Boolean.FALSE);
    }

    @Override
    public Component getDayRenderer(DayPanel daypanel, Date day, Object data,
        boolean selected, boolean working, boolean enabled) {
      JLabel renderer = (JLabel) super.getDayRenderer(daypanel, day, data,
          selected, working, enabled);
      if (selected) {
        renderer.setBorder(BorderFactory.createLineBorder(renderer
            .getBackground()));
      } else {
        renderer.setBorder(null);
      }
      return renderer;
    }
  }
}
