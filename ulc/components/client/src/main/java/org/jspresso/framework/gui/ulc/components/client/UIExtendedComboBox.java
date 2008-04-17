/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.gui.ulc.components.client;

import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellEditor;

/**
 * This subclass implements some default behaviors which are not yet
 * configurable using ULC.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UIExtendedComboBox extends com.ulcjava.base.client.UIComboBox {

  /**
   * {@inheritDoc}
   */
  @Override
  public TableCellEditor getTableCellEditor() {
    TableCellEditor defaultCellEditor = super.getTableCellEditor();
    if (defaultCellEditor instanceof DefaultCellEditor) {
      ((DefaultCellEditor) defaultCellEditor).setClickCountToStart(2);
    }
    return defaultCellEditor;
  }

}
