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
package org.jspresso.framework.view.wings;

import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.basic.BasicValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.gui.wings.components.SActionField;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.view.IView;
import org.wings.SAbstractButton;
import org.wings.SCheckBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.STable;
import org.wings.table.STableCellEditor;

/**
 * This class is an adapter around a WingsView to be able to use it as a cell
 * editor.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class WingsViewCellEditorAdapter implements STableCellEditor,
    IValueChangeListener {

  private static final Map<String, Object> NULLMAP          = Collections
                                                                .unmodifiableMap(new HashMap<String, Object>());
  private static final long                serialVersionUID = 8182961519931949735L;
  private ChangeEvent                      changeEvent      = null;
  private IView<SComponent>                editorView;
  private EventListenerList                listenerList;

  private IValueConnector                  modelConnector;

  /**
   * Constructs a new <code>WingsViewCellEditorAdapter</code> instance.
   * 
   * @param editorView
   *          the swing view used as editor.
   * @param modelConnectorFactory
   *          the model connector factory.
   * @param mvcBinder
   *          the mvc binder.
   * @param subject
   *          the JAAS subject.
   */
  public WingsViewCellEditorAdapter(IView<SComponent> editorView,
      IModelConnectorFactory modelConnectorFactory, IMvcBinder mvcBinder,
      Subject subject) {
    this.listenerList = new EventListenerList();
    this.editorView = editorView;
    if (editorView.getPeer() instanceof SAbstractButton) {
      ((SAbstractButton) editorView.getPeer())
          .setHorizontalAlignment(SConstants.CENTER);
    }

    // To prevent the editor from being read-only.
    if (editorView.getDescriptor().getModelDescriptor() instanceof IComponentDescriptorProvider<?>) {
      modelConnector = modelConnectorFactory.createModelConnector(editorView
          .getConnector().getId(),
          ((IComponentDescriptorProvider<?>) editorView.getDescriptor()
              .getModelDescriptor()).getComponentDescriptor(), subject);

    } else {
      modelConnector = new BasicValueConnector(editorView.getConnector()
          .getId());
    }
    mvcBinder.bind(editorView.getConnector(), modelConnector);
  }

  /**
   * {@inheritDoc}
   */
  public void addCellEditorListener(CellEditorListener l) {
    listenerList.add(CellEditorListener.class, l);
  }

  /**
   * Calls <code>fireEditingCanceled</code>.
   */
  public void cancelCellEditing() {
    fireEditingCanceled();
  }

  /**
   * Returns the value of the swing view's connector.
   * <p>
   * {@inheritDoc}
   */
  public Object getCellEditorValue() {
    Object cellEditorValue = editorView.getConnector().getConnectorValue();
    if (cellEditorValue == NULLMAP) {
      return null;
    }
    return cellEditorValue;
  }

  /**
   * Returns the SComponent peer of the wings view.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public SComponent getTableCellEditorComponent(STable table, Object value,
      boolean isSelected, int row, int column) {
    modelConnector.removeValueChangeListener(this);
    Object connectorValue;
    if (value instanceof IValueConnector) {
      connectorValue = ((IValueConnector) value).getConnectorValue();
    } else {
      connectorValue = value;
    }
    if (connectorValue == null
        && modelConnector.getModelDescriptor() instanceof IComponentDescriptorProvider<?>) {
      // To prevent the editor to be read-only.
      connectorValue = NULLMAP;
    }
    modelConnector.setConnectorValue(connectorValue);
    modelConnector.addValueChangeListener(this);
    if (editorView.getPeer() instanceof SCheckBox) {
      ((SCheckBox) editorView.getPeer()).setSelected(!((SCheckBox) editorView
          .getPeer()).isSelected());
    }
    if (!isSelected) {
      table.setSelectedRow(row);
    }
    return editorView.getPeer();
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
   * {@inheritDoc}
   */
  public void removeCellEditorListener(CellEditorListener l) {
    listenerList.remove(CellEditorListener.class, l);
  }

  /**
   * Returns true.
   * 
   * @param anEvent
   *          an event object
   * @return true
   */
  public boolean shouldSelectCell(EventObject anEvent) {
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
   * {@inheritDoc}
   */
  public void valueChange(@SuppressWarnings("unused") ValueChangeEvent evt) {
    stopCellEditing();
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
   * Gets the editorView.
   * 
   * @return the editorView.
   */
  protected IView<SComponent> getEditorView() {
    return editorView;
  }
}
