/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.basic.BasicValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.gui.swing.components.JActionField;
import org.jspresso.framework.gui.swing.components.JDateField;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.security.ISecurityHandler;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.view.IView;

/**
 * This class is an adapter around a SwingView to be able to use it as a cell
 * editor.
 *
 * @author Vincent Vandenschrick
 */
public class SwingViewCellEditorAdapter extends AbstractCellEditor implements TableCellEditor, TreeCellEditor {

  private static final Map<String, Object> NULLMAP          = Collections.unmodifiableMap(
      new HashMap<String, Object>());
  private static final long                serialVersionUID = 8182961519931949735L;
  private final IView<JComponent> editorView;

  /**
   * Constructs a new {@code SwingViewCellEditorAdapter} instance.
   *
   * @param editorView
   *     the swing view used as editor.
   * @param modelConnectorFactory
   *     the model connector factory.
   * @param mvcBinder
   *     the mvc binder.
   * @param securityHandler
   *     the security handler.
   */
  public SwingViewCellEditorAdapter(IView<JComponent> editorView, IModelConnectorFactory modelConnectorFactory,
                                    IMvcBinder mvcBinder, ISecurityHandler securityHandler) {
    this.editorView = editorView;
    if (editorView.getPeer() instanceof AbstractButton) {
      ((AbstractButton) editorView.getPeer()).setHorizontalAlignment(SwingConstants.CENTER);
    }

    if ((editorView.getPeer() instanceof AbstractButton)) {
      editorView.getConnector().addValueChangeListener(new IValueChangeListener() {

        @Override
        public void valueChange(ValueChangeEvent evt) {
          stopCellEditing();
        }
      });
    }

    // To prevent the editor from being read-only.
    IValueConnector modelConnector;
    String modelConnectorId = editorView.getConnector()
        // To prevent the model connector to believe it's on a nested property
        .getId().replace(IAccessor.NESTED_DELIM, '_');
    if (editorView.getDescriptor().getModelDescriptor() instanceof IComponentDescriptorProvider<?>) {
      modelConnector = modelConnectorFactory.createModelConnector(modelConnectorId,
          ((IComponentDescriptorProvider<?>) editorView.getDescriptor().getModelDescriptor()).getComponentDescriptor(),
          securityHandler);
    } else {
      modelConnector = new BasicValueConnector(modelConnectorId);
    }
    mvcBinder.bind(editorView.getConnector(), modelConnector);
  }

  /**
   * Returns the value of the swing view's connector.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public Object getCellEditorValue() {
    Object cellEditorValue = editorView.getConnector().getConnectorValue();
    if (cellEditorValue == NULLMAP) {
      return null;
    }
    return cellEditorValue;
  }

  /**
   * Returns the JComponent peer of the swing view.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    IValueConnector editorConnector = editorView.getConnector();
    Object connectorValue;
    if (value instanceof IValueConnector) {
      connectorValue = ((IValueConnector) value).getConnectorValue();
    } else {
      connectorValue = value;
    }
    if (connectorValue == null && editorConnector.getModelDescriptor() instanceof IComponentDescriptorProvider<?>) {
      // To prevent the editor to be read-only.
      connectorValue = NULLMAP;
    }
    editorConnector.setConnectorValue(connectorValue);
    Component editorComponent = editorView.getPeer();
    if (editorComponent instanceof JTextField) {
      ((JTextField) editorComponent).selectAll();
    } else if (editorComponent instanceof JActionField) {
      ((JActionField) editorComponent).selectAll();
    } else if (editorComponent instanceof JDateField) {
      ((JDateField) editorComponent).getFormattedTextField().selectAll();
    }
    if (!(editorComponent instanceof JTextArea || editorComponent instanceof JScrollPane)) {
      JPanel wrapperPanel = new JPanel();
      wrapperPanel.setLayout(new GridBagLayout());
      wrapperPanel.add(editorComponent, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
          GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
      editorComponent = wrapperPanel;
    }
    return editorComponent;
  }

  /**
   * Gets the component peer of the editor view.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
                                              boolean leaf, int row) {
    return editorView.getPeer();
  }

  /**
   * Returns false if the event object is a single mouse click.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public boolean isCellEditable(EventObject anEvent) {
    if (anEvent instanceof MouseEvent) {
      if (editorView.getPeer() instanceof AbstractButton) {
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
