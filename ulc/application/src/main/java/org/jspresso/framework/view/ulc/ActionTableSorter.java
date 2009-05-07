/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.ulc;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.util.collection.ESort;

import com.ulcjava.base.application.event.TableModelEvent;
import com.ulcjava.base.application.event.serializable.ITableModelListener;
import com.ulcjava.base.application.table.ITableModel;
import com.ulcjava.base.application.table.ULCTableHeader;

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
  public ActionTableSorter(ITableModel tableModel, ULCTableHeader tableHeader,
      IActionHandler actionHandler, IAction sortingAction) {
    super(tableModel, tableHeader);
    this.actionHandler = actionHandler;
    this.sortingAction = sortingAction;
  }

  /**
   * Constructs a new <code>ActionTableSorter</code> instance.
   * 
   * @param tableModel
   *          tableModel.
   * @param actionHandler
   *          the action handler.
   * @param sortingAction
   *          the action triggered when sorting.
   */
  public ActionTableSorter(ITableModel tableModel,
      IActionHandler actionHandler, IAction sortingAction) {
    super(tableModel);
    this.actionHandler = actionHandler;
    this.sortingAction = sortingAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ITableModelListener createTableModelHandler() {
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
      String property = String.valueOf(getSortedColumn(directive.getColumn())
          .getIdentifier());
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
  @Override
  public int modelIndex(int viewIndex) {
    // there is no mapping.
    return viewIndex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int viewIndex(int modelIndex) {
    // there is no mapping.
    return modelIndex;
  }

  private class TableModelHandler implements ITableModelListener {

    private static final long serialVersionUID = 2605202157129318262L;

    /**
     * {@inheritDoc}
     */
    @Override
    public void tableChanged(TableModelEvent e) {
      // just forward the event
      fireTableChanged(e);
    }

  }
}
