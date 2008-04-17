/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import com.ulcjava.base.application.ULCTable;

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
public class ULCExtendedTable extends ULCTable {

  private static final long serialVersionUID = 6066465803426487503L;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "org.jspresso.framework.gui.ulc.components.client.UIExtendedTable";
  }

}
