/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.wings;

import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

import org.wings.SAbstractButton;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.STable;
import org.wings.table.STableCellEditor;

import com.d2s.framework.binding.ConnectorValueChangeEvent;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.basic.BasicValueConnector;
import com.d2s.framework.gui.wings.components.SActionField;
import com.d2s.framework.view.IView;

/**
 * This class is an adapter around a WingsView to be able to use it as a cell
 * editor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class WingsViewCellEditorAdapter implements STableCellEditor,
    IConnectorValueChangeListener {

  private static final long serialVersionUID = 8182961519931949735L;
  private IView<SComponent> editorView;
  private EventListenerList listenerList;
  private IValueConnector   modelConnector;

  /**
   * Constructs a new <code>WingsViewCellEditorAdapter</code> instance.
   * 
   * @param editorView
   *          the swing view used as editor.
   */
  public WingsViewCellEditorAdapter(IView<SComponent> editorView) {
    this.listenerList = new EventListenerList();
    this.editorView = editorView;
    if (editorView.getPeer() instanceof SAbstractButton) {
      ((SAbstractButton) editorView.getPeer())
          .setHorizontalAlignment(SConstants.CENTER);
    }

    modelConnector = new BasicValueConnector(editorView.getConnector().getId());
    // To prevent the editor from being read-only.
    editorView.getConnector().setModelConnector(modelConnector);
  }

  /**
   * Returns the SComponent peer of the wings view.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public SComponent getTableCellEditorComponent(STable table, Object value,
      boolean isSelected, int row, int column) {
    modelConnector.removeConnectorValueChangeListener(this);
    if (value instanceof IValueConnector) {
      modelConnector.setConnectorValue(((IValueConnector) value)
          .getConnectorValue());
    } else {
      modelConnector.setConnectorValue(value);
    }
    modelConnector.addConnectorValueChangeListener(this);
    return editorView.getPeer();
  }

  /**
   * Returns the value of the swing view's connector.
   * <p>
   * {@inheritDoc}
   */
  public Object getCellEditorValue() {
    return editorView.getConnector().getConnectorValue();
  }

  /**
   * Returns true.
   * 
   * @param anEvent
   *          an event object
   * @return true
   */
  public boolean shouldSelectCell(@SuppressWarnings("unused")
  EventObject anEvent) {
    return true;
  }

  /**
   * Calls <code>fireEditingStopped</code> and returns true.
   * 
   * @return true
   */
  public boolean stopCellEditing() {
    fireEditingStopped();
    return true;
  }

  /**
   * Calls <code>fireEditingCanceled</code>.
   */
  public void cancelCellEditing() {
    fireEditingCanceled();
  }

  /**
   * Returns false if the event object is a single mouse click.
   * <p>
   * {@inheritDoc}
   */
  public boolean isCellEditable(EventObject anEvent) {
    if (anEvent instanceof MouseEvent) {
      if (editorView.getPeer() instanceof SAbstractButton
          || (editorView.getPeer() instanceof SActionField && !((SActionField) editorView
              .getPeer()).isShowingTextField())) {
        return ((MouseEvent) anEvent).getClickCount() >= 1;
      }
      return ((MouseEvent) anEvent).getClickCount() >= 2;
    }
    return true;
  }

  /**
   * Gets the editorView.
   * 
   * @return the editorView.
   */
  protected IView<SComponent> getEditorView() {
    return editorView;
  }

  /**
   * {@inheritDoc}
   */
  public void addCellEditorListener(CellEditorListener l) {
    listenerList.add(CellEditorListener.class, l);
  }

  /**
   * {@inheritDoc}
   */
  public void removeCellEditorListener(CellEditorListener l) {
    listenerList.remove(CellEditorListener.class, l);
  }

  private ChangeEvent changeEvent = null;

  /**
   * Notify all listeners that have registered interest for notification on this
   * event type. The event instance is lazily created using the parameters
   * passed into the fire method.
   * 
   * @see EventListenerList
   */
  protected void fireEditingStopped() {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == CellEditorListener.class) {
        if (changeEvent == null) {
          changeEvent = new ChangeEvent(this);
        }
        ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
      }
    }
  }

  /**
   * Notify all listeners that have registered interest for notification on this
   * event type. The event instance is lazily created using the parameters
   * passed into the fire method.
   * 
   * @see EventListenerList
   */
  protected void fireEditingCanceled() {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == CellEditorListener.class) {
        if (changeEvent == null) {
          changeEvent = new ChangeEvent(this);
        }
        ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void connectorValueChange(@SuppressWarnings("unused")
  ConnectorValueChangeEvent evt) {
    stopCellEditing();
  }
}
