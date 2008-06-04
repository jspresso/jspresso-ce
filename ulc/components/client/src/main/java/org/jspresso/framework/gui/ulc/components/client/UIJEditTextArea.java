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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.ComboBoxEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

import org.jspresso.framework.gui.ulc.components.shared.ActionFieldConstants;
import org.jspresso.framework.gui.ulc.components.shared.JEditTextAreaConstants;
import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.tokenmarker.TokenMarker;

import com.ulcjava.base.client.IEditorComponent;
import com.ulcjava.base.client.UIComponent;
import com.ulcjava.base.client.tabletree.TableTreeCellEditor;
import com.ulcjava.base.shared.IUlcEventConstants;
import com.ulcjava.base.shared.internal.Anything;

/**
 * ULC UI class responsible for handling JEdit text area client half object.
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
public class UIJEditTextArea extends UIComponent implements IEditorComponent {

  private TableCellEditor tableCellEditor;

  /**
   * {@inheritDoc}
   */
  @Override
  public JEditTextArea getBasicObject() {
    return (JEditTextArea) super.getBasicObject();
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
      tableCellEditor = new JEditTextAreaTableCellEditor();
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
    if (request.equals(JEditTextAreaConstants.SET_TEXT_REQUEST)) {
      handleSetText(args);
    } else if (request.equals(ActionFieldConstants.SET_EDITABLE_REQUEST)) {
      handleSetEditable(args);
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
    handleSetText(args);
    getBasicObject().addFocusListener(new FocusAdapter() {

      @Override
      public void focusLost(@SuppressWarnings("unused")
      FocusEvent e) {
        notifyULCValueChange(getBasicObject().getText());
      }
    });
    getBasicObject().setEditable(
        args.get(JEditTextAreaConstants.EDITABLE_KEY, true));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object createBasicObject(Anything args) {
    JEditTextArea textArea = new JEditTextArea();
    String language = args.get(JEditTextAreaConstants.LANGUAGE_KEY, "");
    if (language != null && language.length() > 0) {
      try {
        textArea.setTokenMarker((TokenMarker) Class.forName(
            "org.syntax.jedit.tokenmarker." + language + "TokenMarker")
            .newInstance());
      } catch (InstantiationException ex) {
        // Nothing to do. just don't colorize.
      } catch (IllegalAccessException ex) {
        // Nothing to do. just don't colorize.
      } catch (ClassNotFoundException ex) {
        // Nothing to do. just don't colorize.
      }
    }
    return textArea;
  }

  private void handleSetEditable(Anything args) {
    getBasicObject().setEditable(
        args.get(JEditTextAreaConstants.EDITABLE_KEY, true));
  }

  private void handleSetText(Anything args) {
    getBasicObject().setText(args.get(JEditTextAreaConstants.TEXT_KEY, ""));
  }

  private void notifyULCValueChange(Object newValue) {
    Anything args = new Anything();
    textToAnything((String) newValue, args);
    sendULC(JEditTextAreaConstants.SET_TEXT_REQUEST, args);
    sendOptionalEventULC(IUlcEventConstants.VALUE_CHANGED_EVENT,
        IUlcEventConstants.VALUE_CHANGED);
  }

  private void textToAnything(String text, Anything args) {
    args.put(JEditTextAreaConstants.TEXT_KEY, text);
  }

  private final class JEditTextAreaTableCellEditor extends AbstractCellEditor
      implements TableCellEditor {

    private static final long serialVersionUID = 225151112200061365L;

    /**
     * {@inheritDoc}
     */
    public Object getCellEditorValue() {
      return getBasicObject().getText();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unused")
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int col) {
      getBasicObject().setText((String) value);
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
