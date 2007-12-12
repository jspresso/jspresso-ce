/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.ComboBoxEditor;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

import com.d2s.framework.gui.swing.components.JColorPicker;
import com.d2s.framework.gui.ulc.components.shared.ColorPickerConstants;
import com.d2s.framework.util.gui.ColorHelper;
import com.ulcjava.base.client.IEditorComponent;
import com.ulcjava.base.client.UIComponent;
import com.ulcjava.base.client.tabletree.TableTreeCellEditor;
import com.ulcjava.base.shared.IUlcEventConstants;
import com.ulcjava.base.shared.internal.Anything;

/**
 * ULC UI class responsible for handling color picker client half object.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UIColorPicker extends UIComponent implements IEditorComponent {

  private TableCellEditor tableCellEditor;

  /**
   * {@inheritDoc}
   */
  @Override
  public JColorPicker getBasicObject() {
    return (JColorPicker) super.getBasicObject();
  }

  /**
   * {@inheritDoc}
   */
  public ComboBoxEditor getComboBoxEditor() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public TableCellEditor getTableCellEditor() {
    if (tableCellEditor == null) {
      tableCellEditor = new ColorPickerTableCellEditor();
    }
    return tableCellEditor;
  }

  /**
   * {@inheritDoc}
   */
  public TableTreeCellEditor getTableTreeCellEditor() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public TreeCellEditor getTreeCellEditor() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRequest(String request, Anything args) {
    if (request.equals(ColorPickerConstants.SET_VALUE_REQUEST)) {
      handleSetValue(args);
    } else if (request.equals(ColorPickerConstants.SET_RESETVALUE_REQUEST)) {
      handleSetResetValue(args);
    } else {
      super.handleRequest(request, args);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void restoreState(Anything args) {
    super.restoreState(args);
    handleSetValue(args);
    handleResetValue(args);
    getBasicObject().addChangeListener(new ChangeListener() {

      public void stateChanged(@SuppressWarnings("unused")
      ChangeEvent e) {
        notifyULCValueChange(getBasicObject().getValue());
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object createBasicObject(@SuppressWarnings("unused")
  Anything args) {
    JColorPicker colorPicker = new JColorPicker();
    return colorPicker;
  }

  private void handleResetValue(Anything args) {
    String hexColor = args.get(ColorPickerConstants.RESETVALUE_KEY, "");
    Color resetValue = null;
    if (hexColor.length() > 0) {
      int[] rgba = ColorHelper.fromHexString(hexColor);
      resetValue = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
    }
    getBasicObject().setResetValue(resetValue);
  }

  private void handleSetResetValue(Anything args) {
    String hexColor = args.get(ColorPickerConstants.RESETVALUE_KEY, "");
    Color resetValue = null;
    if (hexColor.length() > 0) {
      int[] rgba = ColorHelper.fromHexString(hexColor);
      resetValue = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
    }
    getBasicObject().setResetValue(resetValue);
  }

  private void handleSetValue(Anything args) {
    String hexColor = args.get(ColorPickerConstants.VALUE_KEY, "");
    Color value = null;
    if (hexColor.length() > 0) {
      int[] rgba = ColorHelper.fromHexString(hexColor);
      value = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
    }
    getBasicObject().setValue(value);
  }

  private void notifyULCValueChange(Object newValue) {
    Anything args = new Anything();
    valueToAnything((Color) newValue, args);
    sendULC(ColorPickerConstants.SET_VALUE_REQUEST, args);
    sendOptionalEventULC(IUlcEventConstants.VALUE_CHANGED_EVENT,
        IUlcEventConstants.VALUE_CHANGED);
  }

  private void valueToAnything(Color value, Anything args) {
    String hexColor = "";
    if (value != null) {
      hexColor = ColorHelper.toHexString(value.getRed(), value.getGreen(),
          value.getBlue(), value.getAlpha());
    }
    args.put(ColorPickerConstants.VALUE_KEY, hexColor);
  }

  private final class ColorPickerTableCellEditor extends AbstractCellEditor
      implements TableCellEditor {

    private static final long serialVersionUID = 1775568960846323577L;

    private ChangeListener    editingStopChangeListener;

    /**
     * Constructs a new <code>ColorPickerTableCellEditor</code> instance.
     */
    public ColorPickerTableCellEditor() {
      editingStopChangeListener = new ChangeListener() {

        public void stateChanged(@SuppressWarnings("unused")
        ChangeEvent e) {
          stopCellEditing();
        }
      };
    }

    /**
     * {@inheritDoc}
     */
    public Object getCellEditorValue() {
      return getBasicObject().getValue();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unused")
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int col) {
      getBasicObject().removeChangeListener(editingStopChangeListener);
      int[] rgba = ColorHelper.fromHexString((String) value);
      getBasicObject().setValue(new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
      getBasicObject().addChangeListener(editingStopChangeListener);
      return getBasicObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(EventObject evt) {
      if (evt instanceof MouseEvent) {
        MouseEvent me = (MouseEvent) evt;
        return (me.getClickCount() >= 1);
      }
      return super.isCellEditable(evt);
    }
  }
}
