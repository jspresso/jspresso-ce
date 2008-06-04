/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.ulc.components.client;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import org.jspresso.framework.util.swing.SwingUtil;

import com.ulcjava.base.client.UITextField;
import com.ulcjava.base.shared.internal.Anything;

/**
 * The client halph object of ULCOnFocusSelectTextField.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
public class UIOnFocusSelectTextField extends UITextField {

  /**
   * {@inheritDoc}
   */
  @Override
  public TableCellEditor getTableCellEditor() {
    TableCellEditor cellEditor = super.getTableCellEditor();
    return new TableCellEditorWrapper(cellEditor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object createBasicObject(Anything args) {
    JTextField textField = (JTextField) super.createBasicObject(args);
    SwingUtil.enableSelectionOnFocusGained(textField);
    return textField;
  }

  private class TableCellEditorWrapper implements TableCellEditor {

    private TableCellEditor delegate;

    /**
     * Constructs a new <code>TableCellEditorWrapper</code> instance.
     * 
     * @param delegate
     *            the editor delegate.
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
