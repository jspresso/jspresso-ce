/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import com.d2s.framework.view.descriptor.ICompositeViewDescriptor;

/**
 * Default implementation of a composite view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicCompositeViewDescriptor extends BasicViewDescriptor
    implements ICompositeViewDescriptor {

  private boolean masterDetail;

  /**
   * {@inheritDoc}
   */
  public boolean isMasterDetail() {
    return masterDetail;
  }

  /**
   * Sets the masterDetail.
   * 
   * @param masterDetail
   *            true if this descriptor is a master / detail one.
   */
  public void setMasterDetail(boolean masterDetail) {
    this.masterDetail = masterDetail;
  }
}
