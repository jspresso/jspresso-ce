/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.wings.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.wings.SBorderFactory;
import org.wings.SBorderLayout;
import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SDialog;
import org.wings.SDimension;
import org.wings.SPanel;
import org.wingx.XColorPicker;

/**
 * A wings component used to choose or reset to null a color.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SColorPicker extends SPanel {

  private static final SDimension BUTTON_DIM       = new SDimension(24, 24);

  private static final long       serialVersionUID = -9175630518246041139L;
  private transient ChangeEvent   changeEvent      = null;
  private SButton                 chooseButton;
  private EventListenerList       listenerList     = new EventListenerList();

  private SButton                 resetButton;
  private Color                   resetValue;

  private Color                   value;

  /**
   * Constructs a new <code>SColorPicker</code> instance.
   */
  public SColorPicker() {
    setLayout(new SBoxLayout(this, SBoxLayout.X_AXIS));
    chooseButton = new SButton();
    chooseButton.addActionListener(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        showColorPickerDialog();
      }
    });
    chooseButton.setShowAsFormComponent(false);
    chooseButton.setPreferredSize(BUTTON_DIM);
    chooseButton.setBorder(SBorderFactory.createSLineBorder(Color.GRAY));

    resetButton = new SButton();
    resetButton.addActionListener(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        setValue(resetValue);
      }
    });
    resetButton.setShowAsFormComponent(false);
    resetButton.setPreferredSize(BUTTON_DIM);
    resetButton.setBorder(SBorderFactory.createSLineBorder(Color.GRAY));

    add(chooseButton);
    add(resetButton);
  }

  /**
   * Adds a <code>ChangeListener</code> to the SColorPicker.
   * 
   * @param l
   *            the listener to add
   */
  public void addChangeListener(ChangeListener l) {
    listenerList.add(ChangeListener.class, l);
  }

  /**
   * Returns an array of all the change listeners registered on this
   * <code>SColorPicker</code>.
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
   * Removes a <code>ChangeListener</code> from the SColorPicker.
   * 
   * @param l
   *            the listener to remove
   */
  public void removeChangeListener(ChangeListener l) {
    listenerList.remove(ChangeListener.class, l);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    chooseButton.setEnabled(isEnabled());
    resetButton.setEnabled(isEnabled());
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
    chooseButton.setBackground(value);
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

  private void showColorPickerDialog() {
    final XColorPicker colorPicker = new XColorPicker();
    final SDialog colorPickerDialog = new SDialog(getParentFrame(), true);
    colorPickerDialog.setDraggable(true);
    SButton okButton = new SButton("OK");
    SButton cancelButton = new SButton("Cancel");
    SButton restoreButton = new SButton("Restore");

    if (getValue() != null) {
      colorPicker.setSelectedColor(getValue().getRed(), getValue().getGreen(),
          getValue().getBlue());
    }

    okButton.addActionListener(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        setValue(colorPicker.getSelectedColor());
        colorPickerDialog.dispose();
      }
    });

    cancelButton.addActionListener(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        colorPickerDialog.dispose();
      }
    });

    restoreButton.addActionListener(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        if (getValue() != null) {
          colorPicker.setSelectedColor(getValue().getRed(), getValue()
              .getGreen(), getValue().getBlue());
        }
      }
    });

    SPanel actionPanel = new SPanel();
    actionPanel.setLayout(new SBoxLayout(actionPanel, SBoxLayout.X_AXIS));
    actionPanel.add(okButton);
    actionPanel.add(cancelButton);
    actionPanel.add(restoreButton);

    SPanel mainPanel = new SPanel(new SBorderLayout());
    mainPanel.add(colorPicker, SBorderLayout.CENTER);
    mainPanel.add(actionPanel, SBorderLayout.SOUTH);

    // colorPickerDialog.setClosable(false);
    colorPickerDialog.setDefaultButton(okButton);
    colorPickerDialog.add(mainPanel);

    colorPickerDialog.setVisible(true);
  }
}
