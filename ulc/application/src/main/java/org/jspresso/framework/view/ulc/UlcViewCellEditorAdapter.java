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
package org.jspresso.framework.view.ulc;

import org.jspresso.framework.binding.ulc.ULCActionFieldConnector;
import org.jspresso.framework.gui.ulc.components.server.ULCActionField;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.view.IView;

import com.ulcjava.base.application.DefaultCellEditor;
import com.ulcjava.base.application.IEditorComponent;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCTable;

/**
 * An adapter around an ULC view to implement an ULC cell editor.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcViewCellEditorAdapter extends DefaultCellEditor {

  private static final long             serialVersionUID = -1439202520153834858L;

  private IView<ULCComponent>           editorView;
  private IValueChangeListener editorViewConnectorListener;

  /**
   * Constructs a new <code>UlcViewCellEditorAdapter</code> instance.
   * 
   * @param editorView
   *            the ulc view responsible for editing the cell.
   */
  public UlcViewCellEditorAdapter(IView<ULCComponent> editorView) {
    super((IEditorComponent) editorView.getPeer());
    this.editorView = editorView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IEditorComponent getTableCellEditorComponent(ULCTable table,
      @SuppressWarnings("unused")
      Object value, @SuppressWarnings("unused")
      int row) {
    IEditorComponent editorComponent = (IEditorComponent) editorView.getPeer();
    if (editorView.getConnector() instanceof ULCActionFieldConnector) {
      editorView.getConnector().addValueChangeListener(
          getEditorViewConnectorListener(table, (ULCActionField) editorView
              .getPeer()));
      ((ULCActionField) editorView.getPeer()).setParent(table.getParent());
    }
    return editorComponent;
  }

  private IValueChangeListener getEditorViewConnectorListener(
      ULCTable table, ULCActionField editorPeer) {
    if (editorViewConnectorListener == null) {
      editorViewConnectorListener = new ActionFieldConnectorListener(table,
          editorPeer);
    }
    return editorViewConnectorListener;
  }

  private class ActionFieldConnectorListener implements
      IValueChangeListener {

    private ULCActionField editorPeer;
    private ULCTable       table;

    /**
     * Constructs a new <code>ActionFieldConnectorListener</code> instance.
     * 
     * @param table
     * @param editorPeer
     */
    public ActionFieldConnectorListener(ULCTable table,
        ULCActionField editorPeer) {
      this.table = table;
      this.editorPeer = editorPeer;
    }

    /**
     * {@inheritDoc}
     */
    public void valueChange(ValueChangeEvent evt) {
      table.setValueAt(evt.getNewValue(), editorPeer.getEditingRow(),
          editorPeer.getEditingColumn());
    }
  }
}
