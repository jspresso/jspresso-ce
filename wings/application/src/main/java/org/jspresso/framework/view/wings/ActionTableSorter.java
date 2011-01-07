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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.gui.wings.components.ClickableHeaderSTable;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.util.collection.ESort;
import org.wings.table.STableColumnModel;

/**
 * A table sorter that triggers an action whenever the sorting state changes.
 * 
 * @version $LastChangedRevision: 1863 $
 * @author Vincent Vandenschrick
 */
public class ActionTableSorter extends AbstractTableSorter {

  private static final long serialVersionUID = 1669749134625090409L;

  private IActionHandler    actionHandler;
  private IAction           sortingAction;

  /**
   * Constructs a new <code>ActionTableSorter</code> instance.
   * 
   * @param tableModel
   *          tableModel.
   * @param table
   *          table.
   * @param actionHandler
   *          the action handler.
   * @param sortingAction
   *          the action triggered when sorting.
   */
  public ActionTableSorter(TableModel tableModel, ClickableHeaderSTable table,
      IActionHandler actionHandler, IAction sortingAction) {
    super(tableModel, table);
    this.actionHandler = actionHandler;
    this.sortingAction = sortingAction;
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
    if (getTableModel().getRowCount() > 1) {
      Map<String, Object> actionContext = new HashMap<String, Object>();
      Map<String, ESort> orderingProperties = new LinkedHashMap<String, ESort>();
      for (Directive directive : getSortingColumns()) {
        STableColumnModel columnModel = getTableColumnModel();
        String property = String.valueOf(columnModel.getColumn(
            directive.getColumn()).getIdentifier());
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
      actionContext
          .put(IQueryComponent.ORDERING_PROPERTIES, orderingProperties);
      actionHandler.execute(sortingAction, actionContext);
    }
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
