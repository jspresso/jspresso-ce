/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.swing;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.util.collection.ESort;

/**
 * A table sorter that triggers an action whenever the sorting state changes.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ActionTableSorter extends AbstractTableSorter {

  private static final long serialVersionUID = -7470159235923510369L;
  private IActionHandler    actionHandler;
  private IAction           sortingAction;

  /**
   * Constructs a new <code>ActionTableSorter</code> instance.
   * 
   * @param tableModel
   *          tableModel.
   * @param tableHeader
   *          tableHeader.
   * @param actionHandler
   *          the action handler.
   * @param sortingAction
   *          the action triggered when sorting.
   */
  public ActionTableSorter(TableModel tableModel, JTableHeader tableHeader,
      IActionHandler actionHandler, IAction sortingAction) {
    super(tableModel, tableHeader);
    this.actionHandler = actionHandler;
    this.sortingAction = sortingAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected TableModelListener createTableModelHandler() {
    return new TableModelHandler();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void sortingStatusChanged() {
    Map<String, Object> actionContext = new HashMap<String, Object>();
    Map<String, ESort> orderingProperties = new LinkedHashMap<String, ESort>();
    for (Directive directive : getSortingColumns()) {
      TableColumnModel columnModel = getTableHeader().getColumnModel();
      String property = String.valueOf(columnModel.getColumn(
          convertColumnIndexToView(directive.getColumn())).getIdentifier());
      ESort direction;
      if (directive.getDirection() == ASCENDING) {
        direction = ESort.ASCENDING;
      } else if (directive.getDirection() == DESCENDING) {
        direction = ESort.DESCENDING;
      } else {
        direction = null;
      }
      if (direction != null) {
        orderingProperties.put(property, direction);
      }
    }
    actionContext.put(ActionContextConstants.ORDERING_PROPERTIES,
        orderingProperties);
    actionHandler.execute(sortingAction, actionContext);
  }

  /**
   * {@inheritDoc}
   */
  public int modelIndex(int viewIndex) {
    // there is no mapping.
    return viewIndex;
  }

  /**
   * {@inheritDoc}
   */
  public int viewIndex(int modelIndex) {
    // there is no mapping.
    return modelIndex;
  }

  private class TableModelHandler implements TableModelListener {

    /**
     * {@inheritDoc}
     */
    public void tableChanged(TableModelEvent e) {
      // just forward the event
      fireTableChanged(e);
    }
  }
}
