/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.ulc.components.client;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import com.d2s.framework.util.swing.SwingUtil;
import com.ulcjava.base.client.UITextField;
import com.ulcjava.base.shared.internal.Anything;

/**
 * The client halph object of ULCOnFocusSelectTextField.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UIOnFocusSelectTextField extends UITextField {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object createBasicObject(Anything args) {
    JTextField textField = (JTextField) super.createBasicObject(args);
    SwingUtil.enableSelectionOnFocusGained(textField);
    return textField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TableCellEditor getTableCellEditor() {
    TableCellEditor cellEditor = super.getTableCellEditor();
    return new TableCellEditorWrapper(cellEditor);
  }

  private class TableCellEditorWrapper implements TableCellEditor {

    private TableCellEditor delegate;

    /**
     * Constructs a new <code>TableCellEditorWrapper</code> instance.
     * 
     * @param delegate
     *          the editor delegate.
     */
    public TableCellEditorWrapper(TableCellEditor delegate) {
      this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    public void addCellEditorListener(CellEditorListener l) {
      delegate.addCellEditorListener(l);
    }

    /**
     * {@inheritDoc}
     */
    public void cancelCellEditing() {
      delegate.cancelCellEditing();
    }

    /**
     * {@inheritDoc}
     */
    public Object getCellEditorValue() {
      return delegate.getCellEditorValue();
    }

    /**
     * {@inheritDoc}
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
      Component editorComp = delegate.getTableCellEditorComponent(table, value,
          isSelected, row, column);
      if (editorComp instanceof JTextField) {
        ((JTextField) editorComp).selectAll();
      }
      return editorComp;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCellEditable(EventObject anEvent) {
      return delegate.isCellEditable(anEvent);
    }

    /**
     * {@inheritDoc}
     */
    public void removeCellEditorListener(CellEditorListener l) {
      delegate.removeCellEditorListener(l);
    }

    /**
     * {@inheritDoc}
     */
    public boolean shouldSelectCell(EventObject anEvent) {
      return delegate.shouldSelectCell(anEvent);
    }

    /**
     * {@inheritDoc}
     */
    public boolean stopCellEditing() {
      return delegate.stopCellEditing();
    }

  }
}
