/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.wings;

import java.awt.event.MouseEvent;
import java.util.EventObject;

import org.wings.SAbstractButton;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDefaultCellEditor;
import org.wings.STable;
import org.wings.STextComponent;
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
public class WingsViewCellEditorAdapter extends SDefaultCellEditor implements
    STableCellEditor {

  private static final long serialVersionUID = 8182961519931949735L;
  private IView<SComponent> editorView;

  /**
   * Constructs a new <code>WingsViewCellEditorAdapter</code> instance.
   *
   * @param editorView
   *          the swing view used as editor.
   */
  public WingsViewCellEditorAdapter(IView<SComponent> editorView) {
    super(editorView.getPeer(), true);
    this.editorView = editorView;
    if (editorView.getPeer() instanceof SAbstractButton) {
      ((SAbstractButton) editorView.getPeer())
          .setHorizontalAlignment(SConstants.CENTER);
    }

    if (!(editorView.getPeer() instanceof STextComponent)) {
      editorView.getConnector().addConnectorValueChangeListener(
          new IConnectorValueChangeListener() {

            public void connectorValueChange(@SuppressWarnings("unused")
            ConnectorValueChangeEvent evt) {
              stopCellEditing();
            }
          });
    }

    // To prevent the editor from being read-only.
    editorView.getConnector().setModelConnector(
        new BasicValueConnector(editorView.getConnector().getId()));
  }

  /**
   * Returns the SComponent peer of the wings view.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unused")
  public SComponent getTableCellEditorComponent(STable table, Object value,
      boolean isSelected, int row, int column) {
    IValueConnector editorConnector = editorView.getConnector();
    if (value instanceof IValueConnector) {
      editorConnector.setConnectorValue(((IValueConnector) value)
          .getConnectorValue());
    } else {
      editorConnector.setConnectorValue(value);
    }
    return super.getTableCellEditorComponent(table, value, isSelected, row,
        column);
  }

  /**
   * Returns the value of the swing view's connector.
   * <p>
   * {@inheritDoc}
   */
  @Override
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
  @Override
  public boolean shouldSelectCell(@SuppressWarnings("unused")
  EventObject anEvent) {
    return true;
  }

  /**
   * Calls <code>fireEditingStopped</code> and returns true.
   *
   * @return true
   */
  @Override
  public boolean stopCellEditing() {
    fireEditingStopped();
    return true;
  }

  /**
   * Calls <code>fireEditingCanceled</code>.
   */
  @Override
  public void cancelCellEditing() {
    fireEditingCanceled();
  }

  /**
   * Returns false if the event object is a single mouse click.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isCellEditable(EventObject anEvent) {
    if (anEvent instanceof MouseEvent) {
      if (editorView.getPeer() instanceof SAbstractButton
          || (editorView.getPeer() instanceof SActionField && !((SActionField) editorView
              .getPeer()).isShowingTextField())) {
        return ((MouseEvent) anEvent).getClickCount() >= 1;
      }
      return ((MouseEvent) anEvent).getClickCount() >= 2;
    }
    return super.isCellEditable(anEvent);
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
