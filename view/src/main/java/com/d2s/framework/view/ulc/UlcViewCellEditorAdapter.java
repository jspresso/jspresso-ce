/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.ulc;

import com.d2s.framework.binding.ConnectorValueChangeEvent;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.binding.ui.ulc.ULCActionFieldConnector;
import com.d2s.framework.gui.ulc.components.server.ULCActionField;
import com.d2s.framework.view.IView;
import com.ulcjava.base.application.DefaultCellEditor;
import com.ulcjava.base.application.IEditorComponent;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCTable;

/**
 * An adapter around an ULC view to implement an ULC cell editor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcViewCellEditorAdapter extends DefaultCellEditor {

  private static final long             serialVersionUID = -1439202520153834858L;

  private IView<ULCComponent>           editorView;
  private IConnectorValueChangeListener editorViewConnectorListener;

  /**
   * Constructs a new <code>UlcViewCellEditorAdapter</code> instance.
   * 
   * @param editorView
   *          the ulc view responsible for editing the cell.
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
      editorView.getConnector().addConnectorValueChangeListener(
          getEditorViewConnectorListener(table, (ULCActionField) editorView
              .getPeer()));
      ((ULCActionField) editorView.getPeer()).setParent(table.getParent());
    }
    return editorComponent;
  }

  private IConnectorValueChangeListener getEditorViewConnectorListener(
      ULCTable table, ULCActionField editorPeer) {
    if (editorViewConnectorListener == null) {
      editorViewConnectorListener = new ActionFieldConnectorListener(table,
          editorPeer);
    }
    return editorViewConnectorListener;
  }

  private class ActionFieldConnectorListener implements
      IConnectorValueChangeListener {

    private ULCTable       table;
    private ULCActionField editorPeer;

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
    public void connectorValueChange(ConnectorValueChangeEvent evt) {
      table.setValueAt(evt.getNewValue(), editorPeer.getEditingRow(),
          editorPeer.getEditingColumn());
    }
  }
}
