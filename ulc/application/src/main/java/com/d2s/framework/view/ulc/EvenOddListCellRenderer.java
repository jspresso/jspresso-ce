/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.ulc;

import com.d2s.framework.util.ulc.UlcUtil;
import com.ulcjava.base.application.DefaultListCellRenderer;
import com.ulcjava.base.application.IRendererComponent;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCList;

/**
 * A default list cell renderer rendering even and odd rows background slightly
 * differently.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EvenOddListCellRenderer extends DefaultListCellRenderer {

  private static final long serialVersionUID = 3450896483325205907L;

  /**
   * {@inheritDoc}
   */
  @Override
  public IRendererComponent getListCellRendererComponent(ULCList list,
      Object value, boolean isSelected, boolean hasFocus, int row) {
    IRendererComponent renderer = super.getListCellRendererComponent(list,
        value, isSelected, hasFocus, row);
    UlcUtil.alternateEvenOddBackground((ULCComponent) renderer, list,
        isSelected, row);
    return renderer;
  }
}
