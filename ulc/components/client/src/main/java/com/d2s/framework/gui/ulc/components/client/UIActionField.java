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
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

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

  private FocusListener   actionFocusListener;
  private TableCellEditor tableCellEditor;

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
  public ComboBoxEditor getComboBoxEditor() {
    throw new UnsupportedOperationException();
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
  @Override
  public void handleRequest(String request, Anything args) {
    if (request.equals(ActionFieldConstants.SET_ACTION_TEXT_REQUEST)) {
      handleSetActionText(args);
    } else if (request.equals(ActionFieldConstants.SET_ACTIONS_REQUEST)) {
      handleSetActions(args);
    } else if (request.equals(ActionFieldConstants.SET_EDITABLE_REQUEST)) {
      handleSetEditable(args);
    } else if (request.equals(ActionFieldConstants.SET_DECORATED_REQUEST)) {
      handleSetDecorated(args);
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
    handleSetActionText(args);
    handleSetActions(args);
    getBasicObject().setEditable(
        args.get(DateFieldConstants.EDITABLE_KEY, true));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object createBasicObject(Anything args) {
    boolean showTextField = args.get(ActionFieldConstants.SHOW_TEXTFIELD_KEY,
        true);

    JActionField actionField = new JActionField(showTextField) {

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

  private Action anythingToAction(final int index, Anything actionAnything,
      UIIcon actionIcon) {
    if (actionAnything == null) {
      return null;
    }
    Action action = new AbstractAction() {

      private static final long serialVersionUID = -1208228660452838215L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent evt) {
        if (evt.getSource() instanceof JButton) {
          triggerAction(index, ((JButton) evt.getSource()).getActionCommand());
          // sendActionText(((JButton) evt.getSource()).getActionCommand());
        } else {
          sendActionText();
        }
      }
    };
    if (actionAnything.get(ActionFieldConstants.ACCELERATOR_KEY, -1) != -1) {
      action.putValue(Action.ACCELERATOR_KEY, new Integer(actionAnything.get(
          ActionFieldConstants.ACCELERATOR_KEY, -1)));
    }
    action.putValue(Action.ACTION_COMMAND_KEY, actionAnything.get(
        ActionFieldConstants.ACTION_COMMAND_KEY, null));
    action.putValue(Action.LONG_DESCRIPTION, actionAnything.get(
        ActionFieldConstants.LONG_DESCRIPTION, null));
    if (actionAnything.get(ActionFieldConstants.MNEMONIC_KEY, -1) != -1) {
      action.putValue(Action.MNEMONIC_KEY, new Integer(actionAnything.get(
          ActionFieldConstants.MNEMONIC_KEY, -1)));
    }
    action.putValue(Action.NAME, actionAnything.get(ActionFieldConstants.NAME,
        null));
    action.putValue(Action.SHORT_DESCRIPTION, actionAnything.get(
        ActionFieldConstants.SHORT_DESCRIPTION, null));
    if (actionIcon != null) {
      action.putValue(Action.SMALL_ICON, new ImageIcon(actionIcon.getImage()));
    }
    return action;
  }

  private void handleSetActions(Anything args) {
    List<UIIcon> icons = new ArrayList<UIIcon>();
    List<Action> actions = new ArrayList<Action>();

    Anything iconsAnything = args.get(ActionFieldConstants.ICONS_KEY);
    for (int index = 0; index < iconsAnything.size(); index++) {
      icons.add((UIIcon) getSession().getManaged(UIIcon.class,
          iconsAnything.get(index)));
    }

    Anything actionsAnything = args.get(ActionFieldConstants.ACTIONS_KEY);
    for (int index = 0; index < actionsAnything.size(); index++) {
      actions.add(anythingToAction(index, actionsAnything.get(index), icons
          .get(index)));
    }
    getBasicObject().setActions(actions);
  }

  private void handleSetActionText(Anything args) {
    getBasicObject().setActionText(
        args.get(ActionFieldConstants.ACTION_TEXT_KEY, ""));
    getBasicObject().setValue(getBasicObject().getActionText());
  }

  private void handleSetDecorated(Anything args) {
    getBasicObject().setDecorated(
        args.get(ActionFieldConstants.DECORATED_KEY, true));
  }

  private void handleSetEditable(Anything args) {
    getBasicObject().setEditable(
        args.get(ActionFieldConstants.EDITABLE_KEY, true));
  }

  private void sendActionText() {
    sendActionText(getBasicObject().getActionText());
  }

  private void sendActionText(String actionText) {
    Anything actionTextAnything = new Anything();
    actionTextAnything.put(ActionFieldConstants.ACTION_TEXT_KEY, actionText);
    sendULC(ActionFieldConstants.SET_ACTION_TEXT_REQUEST, actionTextAnything);
  }

  private void syncState() {
    Anything actionTextAnything = new Anything();
    actionTextAnything.put(ActionFieldConstants.ACTION_TEXT_KEY,
        getBasicObject().getActionText());
    sendULC(ActionFieldConstants.SYNC_STATE_REQUEST, actionTextAnything);
  }

  private void triggerAction(int index, String actionText) {
    Anything actionTextAnything = new Anything();
    actionTextAnything.put(ActionFieldConstants.ACTION_INDEX_KEY, index);
    actionTextAnything.put(ActionFieldConstants.ACTION_TEXT_KEY, actionText);
    sendULC(ActionFieldConstants.TRIGGER_ACTION_REQUEST, actionTextAnything);
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
    public Object getCellEditorValue() {
      // Don't return anything since the cell editor value is not used.
      // The model is directly updated by the server half object.
      return null;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(EventObject evt) {
      if (evt instanceof MouseEvent) {
        MouseEvent me = (MouseEvent) evt;
        if (getBasicObject().isShowingTextField()) {
          return (me.getClickCount() >= 2);
        }
        return (me.getClickCount() >= 1);
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
      if (getBasicObject().getActionText().length() == 0) {
        return true;
      }
      return false;
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
  }
}
