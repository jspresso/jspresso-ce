/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.wings;

import org.wings.SComponent;
import org.wings.SDefaultListCellRenderer;

import com.d2s.framework.util.wings.WingsUtil;

/**
 * A default list cell renderer rendering even and odd rows background slightly
 * differently.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EvenOddListCellRenderer extends SDefaultListCellRenderer {

  private static final long serialVersionUID = 2051850807889065438L;

  /**
   * {@inheritDoc}
   */
  @Override
  public SComponent getListCellRendererComponent(SComponent list, Object value,
       boolean isSelected, int index) {
    SComponent renderer = super.getListCellRendererComponent(list, value, isSelected, index
        );
    WingsUtil.alternateEvenOddBackground(renderer, list, isSelected, index);
    return renderer;
  }
}
