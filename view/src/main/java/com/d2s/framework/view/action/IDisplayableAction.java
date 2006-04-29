/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.action;

import com.d2s.framework.action.IAction;
import com.d2s.framework.util.descriptor.IIconDescriptor;

/**
 * This interface must be implemented by any displayable action of the
 * application.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IDisplayableAction extends IAction, IIconDescriptor {

  /**
   * Provides the action mnemonic to the runtime framework using its string
   * representation.
   * 
   * @return the mnemonic string representation.
   */
  String getMnemonicAsString();

  /**
   * Provides the action accelerator to the runtime framework using its string
   * representation.
   * 
   * @return the accelerator string representation.
   */
  String getAcceleratorAsString();
}
