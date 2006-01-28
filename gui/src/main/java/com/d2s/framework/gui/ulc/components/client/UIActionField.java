/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.ComboBoxEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

import com.d2s.framework.gui.swing.components.JActionField;
import com.d2s.framework.gui.ulc.components.shared.ActionFieldConstants;
import com.d2s.framework.gui.ulc.components.shared.DateFieldConstants;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.client.IEditorComponent;
import com.ulcjava.base.client.UIComponent;
import com.ulcjava.base.client.UIIcon;
import com.ulcjava.base.client.tabletree.TableTreeCellEditor;
import com.ulcjava.base.shared.internal.Anything;

/**
 * ULC UI class responsible for handling action field client half object.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UIActionField extends UIComponent implements IEditorComponent {

  private TableCellEditor tableCellEditor;
  private UIIcon          actionIcon;
  private FocusListener   actionFocusListener;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object createBasicObject(@SuppressWarnings("unused")
  Anything args) {
    JActionField actionField = new JActionField() {

      private static final long serialVersionUID = 7747321535435615536L;

      /**
       * Prevent ULC framework for installing bogus focus listeners on the
       * component.
       * <p>
       * {@inheritDoc}
       */
      @Override
      public synchronized void addFocusListener(FocusListener l) {
        if (l.getClass().getName()
            .startsWith("com.ulcjava.base.client.UITable")) {
          return;
        }
        super.addFocusListener(l);
      }
    };
    actionFocusListener = new FocusAdapter() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void focusLost(@SuppressWarnings("unused")
      FocusEvent e) {
        if (!getBasicObject().isSynchronized()) {
          sendActionText();
        }
      }

    };
    actionField.addTextFieldFocusListener(actionFocusListener);
    return actionField;
  }

  private void sendActionText() {
    Anything actionTextAnything = new Anything();
    actionTextAnything.put(ActionFieldConstants.ACTION_TEXT_KEY,
        getBasicObject().getActionText());
    sendULC(ActionFieldConstants.SET_ACTION_TEXT_REQUEST, actionTextAnything);
  }

  private void syncState() {
    Anything actionTextAnything = new Anything();
    actionTextAnything.put(ActionFieldConstants.ACTION_TEXT_KEY,
        getBasicObject().getActionText());
    sendULC(ActionFieldConstants.SYNC_STATE_REQUEST, actionTextAnything);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void restoreState(Anything args) {
    super.restoreState(args);
    handleSetActionText(args);
    actionIcon = (UIIcon) getSession().getManaged(UIIcon.class,
        args.get(ActionFieldConstants.ICON_KEY));
    handleSetAction(args.get(ActionFieldConstants.ACTION_KEY));
    getBasicObject().setEditable(
        args.get(DateFieldConstants.EDITABLE_KEY, true));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JActionField getBasicObject() {
    return (JActionField) super.getBasicObject();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRequest(String request, Anything args) {
    if (request.equals(ActionFieldConstants.SET_ACTION_TEXT_REQUEST)) {
      handleSetActionText(args);
    } else if (request.equals(ActionFieldConstants.SET_ACTION_REQUEST)) {
      handleSetAction(args);
    } else if (request.equals(ActionFieldConstants.SET_EDITABLE_REQUEST)) {
      handleSetEditable(args);
    } else {
      super.handleRequest(request, args);
    }
  }

  private void handleSetActionText(Anything args) {
    getBasicObject().setActionText(
        args.get(ActionFieldConstants.ACTION_TEXT_KEY, ""));
    getBasicObject().setValue(getBasicObject().getActionText());
  }

  private void handleSetEditable(Anything args) {
    getBasicObject().setEditable(
        args.get(DateFieldConstants.EDITABLE_KEY, true));
  }

  private void handleSetAction(Anything args) {
    getBasicObject().setAction(anythingToAction(args));
  }

  private Action anythingToAction(Anything actionAnything) {
    if (actionAnything == null) {
      return null;
    }
    Action action = new AbstractAction() {

      private static final long serialVersionUID = -1208228660452838215L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent evt) {
        if (evt.getSource() instanceof JButton) {
          getBasicObject().setActionText(
              ((JButton) evt.getSource()).getActionCommand());
        }
        sendActionText();
      }
    };
    if (actionAnything.get(IAction.ACCELERATOR_KEY, -1) != -1) {
      action.putValue(Action.ACCELERATOR_KEY, new Integer(actionAnything.get(
          IAction.ACCELERATOR_KEY, -1)));
    }
    action.putValue(Action.ACTION_COMMAND_KEY, actionAnything.get(
        IAction.ACTION_COMMAND_KEY, null));
    action.putValue(Action.LONG_DESCRIPTION, actionAnything.get(
        IAction.LONG_DESCRIPTION, null));
    if (actionAnything.get(IAction.MNEMONIC_KEY, -1) != -1) {
      action.putValue(Action.MNEMONIC_KEY, new Integer(actionAnything.get(
          IAction.MNEMONIC_KEY, -1)));
    }
    action.putValue(Action.NAME, actionAnything.get(IAction.NAME, null));
    action.putValue(Action.SHORT_DESCRIPTION, actionAnything.get(
        IAction.SHORT_DESCRIPTION, null));
    if (actionIcon != null) {
      action.putValue(Action.SMALL_ICON, new ImageIcon(actionIcon.getImage()));
    }
    return action;
  }

  /**
   * {@inheritDoc}
   */
  public TableCellEditor getTableCellEditor() {
    if (tableCellEditor == null) {
      tableCellEditor = new ActionFieldTableCellEditor();
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
  public ComboBoxEditor getComboBoxEditor() {
    throw new UnsupportedOperationException();
  }

  private final class ActionFieldTableCellEditor extends AbstractCellEditor
      implements TableCellEditor {

    private static final long  serialVersionUID = 7703653671044392483L;

    private TableModelListener stopEditingListener;

    /**
     * Constructs a new <code>DateFieldTableCellEditor</code> instance.
     */
    public ActionFieldTableCellEditor() {
      getBasicObject().removeTextFieldFocusListener(actionFocusListener);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unused")
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int col) {
      getBasicObject().setValue(value);
      getBasicObject().selectAll();
      Anything cellEditingAnything = new Anything();
      cellEditingAnything.put(ActionFieldConstants.EDITING_ROW_KEY, row);
      cellEditingAnything.put(ActionFieldConstants.EDITING_COLUMN_KEY, col);
      sendULC(ActionFieldConstants.SET_EDITING_CELL_REQUEST,
          cellEditingAnything);
      syncState();
      table.getModel().removeTableModelListener(getStopEditingListener(table));
      table.getModel().addTableModelListener(getStopEditingListener(table));
      return getBasicObject();
    }

    private TableModelListener getStopEditingListener(final JTable table) {
      if (stopEditingListener == null) {
        stopEditingListener = new TableModelListener() {

          public void tableChanged(TableModelEvent e) {
            if (table.isEditing() && e.getColumn() > 0) {
              table.editingCanceled(new ChangeEvent(getBasicObject()));
            }
          }
        };
      }
      return stopEditingListener;
    }

    /**
     * {@inheritDoc}
     */
    public Object getCellEditorValue() {
      // Don't return anything since the cell editor value is not used.
      // The model is directly updated by the server half object.
      return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(EventObject evt) {
      if (evt instanceof MouseEvent) {
        MouseEvent me = (MouseEvent) evt;
        return (me.getClickCount() > 1);
      }
      return super.isCellEditable(evt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopCellEditing() {
      if (getBasicObject().isSynchronized()) {
        fireEditingCanceled();
        return true;
      }
      sendActionText();
      return false;
    }
  }
}
