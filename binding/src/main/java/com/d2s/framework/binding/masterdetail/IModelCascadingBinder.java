/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding.masterdetail;

import com.d2s.framework.binding.IValueConnector;

/**
 * This is the interface which has to be implemented by classes which bind two
 * connectors in a master / detail relationship where the master connector is
 * determined by the selection in a <code>ISelectableConnector</code>.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelCascadingBinder {

  /**
   * Binds two connectors altogether.
   * 
   * @param masterConnector
   *            The master collection connector.
   * @param detailConnector
   *            The detail connector tracking the master selectable selection.
   */
  void bind(IValueConnector masterConnector, IValueConnector detailConnector);
}
