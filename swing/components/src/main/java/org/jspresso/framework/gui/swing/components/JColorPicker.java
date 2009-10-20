/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * A swing component used to choose or reset to null a color.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
public class JColorPicker extends JPanel {

  private static final long     serialVersionUID = -1151212557773199513L;

  private transient ChangeEvent changeEvent      = null;
  private JButton               chooseButton;
  private EventListenerList     listenerList     = new EventListenerList();
  private JButton               resetButton;

  private Color                 resetValue;
  private Color                 value;

  /**
   * Constructs a new <code>JColorPicker</code> instance.
   */
  public JColorPicker() {
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    chooseButton = new JButton(" ") {

      private static final long serialVersionUID = 3207916802450634387L;

      @Override
      public void paint(Graphics g) {
        super.paint(g);
        if (value != null) {
          g.setColor(value);
          Rectangle r = getBounds();
          r.grow(-5, -5);
          g.fill3DRect(r.x, r.y, r.width, r.height, true);
        }
      }
    };
    chooseButton.addActionListener(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        Color chosenColor = JColorChooser.showDialog(chooseButton, "",
            getValue());
        if (chosenColor != null) {
          setValue(chosenColor);
        }
      }
    });
    chooseButton.setPreferredSize(new Dimension(16, 16));

    resetButton = new JButton("X");
    resetButton.addActionListener(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        setValue(resetValue);
      }
    });
    resetButton.setPreferredSize(new Dimension(16, 16));

    addPropertyChangeListener("enabled", new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {
        chooseButton.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
        resetButton.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
      }
    });

    add(chooseButton);
    add(resetButton);
  }

  /**
   * Adds a <code>ChangeListener</code> to the JColorPicker.
   * 
   * @param l
   *            the listener to add
   */
  public void addChangeListener(ChangeListener l) {
    listenerList.add(ChangeListener.class, l);
  }

  /**
   * Returns an array of all the change listeners registered on this
   * <code>JColorPicker</code>.
   * 
   * @return all of this model's <code>ChangeListener</code>s or an empty
   *         array if no change listeners are currently registered
   * @see #addChangeListener
   * @see #removeChangeListener
   */
  public ChangeListener[] getChangeListeners() {
    return listenerList.getListeners(ChangeListener.class);
  }

  /**
   * Gets the resetValue.
   * 
   * @return the resetValue.
   */
  public Color getResetValue() {
    return resetValue;
  }

  /**
   * Gets the value.
   * 
   * @return the value.
   */
  public Color getValue() {
    return value;
  }

  /**
   * Removes a <code>ChangeListener</code> from the JColorPicker.
   * 
   * @param l
   *            the listener to remove
   */
  public void removeChangeListener(ChangeListener l) {
    listenerList.remove(ChangeListener.class, l);
  }

  /**
   * Sets the resetValue.
   * 
   * @param resetValue
   *            the resetValue to set.
   */
  public void setResetValue(Color resetValue) {
    this.resetValue = resetValue;
  }

  /**
   * Sets the value.
   * 
   * @param value
   *            the value to set.
   */
  public void setValue(Color value) {
    this.value = value;
    chooseButton.repaint();
    fireStateChanged();
  }

  /**
   * Notifies all listeners that have registered interest for notification on
   * this event type. The event instance is created lazily.
   * 
   * @see EventListenerList
   */
  protected void fireStateChanged() {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ChangeListener.class) {
        // Lazily create the event:
        if (changeEvent == null) {
          changeEvent = new ChangeEvent(this);
        }
        ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
      }
    }
  }
}
