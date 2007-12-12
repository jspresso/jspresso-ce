/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.swing;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractButton;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreeCellEditor;

import com.d2s.framework.binding.ConnectorValueChangeEvent;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.basic.BasicValueConnector;
import com.d2s.framework.gui.swing.components.JActionField;
import com.d2s.framework.gui.swing.components.JDateField;
import com.d2s.framework.view.IView;

/**
 * This class is an adapter around a SwingView to be able to use it as a cell
 * editor.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SwingViewCellEditorAdapter extends AbstractCellEditor implements
    TableCellEditor, TreeCellEditor {

  private static final long serialVersionUID = 8182961519931949735L;
  private IView<JComponent> editorView;

  /**
   * Constructs a new <code>SwingViewCellEditorAdapter</code> instance.
   * 
   * @param editorView
   *            the swing view used as editor.
   */
  public SwingViewCellEditorAdapter(IView<JComponent> editorView) {
    this.editorView = editorView;
    if (editorView.getPeer() instanceof AbstractButton) {
      ((AbstractButton) editorView.getPeer())
          .setHorizontalAlignment(SwingConstants.CENTER);
    }

    if (!(editorView.getPeer() instanceof JTextComponent)) {
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
   * Returns the value of the swing view's connector.
   * <p>
   * {@inheritDoc}
   */
  public Object getCellEditorValue() {
    return editorView.getConnector().getConnectorValue();
  }

  /**
   * Returns the JComponent peer of the swing view.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {
    IValueConnector editorConnector = editorView.getConnector();
    if (value instanceof IValueConnector) {
      editorConnector.setConnectorValue(((IValueConnector) value)
          .getConnectorValue());
    } else {
      editorConnector.setConnectorValue(value);
    }
    Component editorComponent = editorView.getPeer();
    if (editorComponent instanceof JTextField) {
      ((JTextField) editorComponent).selectAll();
    } else if (editorComponent instanceof JActionField) {
      ((JActionField) editorComponent).selectAll();
    } else if (editorComponent instanceof JDateField) {
      ((JDateField) editorComponent).getFormattedTextField().selectAll();
    }
    return editorComponent;
  }

  /**
   * Gets the component peer of the editor view.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public Component getTreeCellEditorComponent(JTree tree, Object value,
      boolean isSelected, boolean expanded, boolean leaf, int row) {
    return editorView.getPeer();
  }

  /**
   * Returns false if the event object is a single mouse click.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isCellEditable(EventObject anEvent) {
    if (anEvent instanceof MouseEvent) {
      if (editorView.getPeer() instanceof AbstractButton
          || (editorView.getPeer() instanceof JActionField && !((JActionField) editorView
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
  protected IView<JComponent> getEditorView() {
    return editorView;
  }

}
