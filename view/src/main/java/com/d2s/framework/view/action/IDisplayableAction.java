/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.action;

import java.util.Collection;

import com.d2s.framework.action.IAction;
import com.d2s.framework.util.descriptor.IIconDescriptor;
import com.d2s.framework.util.gate.IGate;

/**
 * This interface must be implemented by any displayable action of the
 * application.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IDisplayableAction extends IAction, IIconDescriptor {

  /**
   * Provides the action accelerator to the runtime framework using its string
   * representation.
   * 
   * @return the accelerator string representation.
   */
  String getAcceleratorAsString();

  /**
   * Gets the collection of gates determining the actionability state of this
   * action.
   * 
   * @return the collection of gates determining the actionability state of this
   *         action.
   */
  Collection<IGate> getActionabilityGates();

  /**
   * Provides the action mnemonic to the runtime framework using its string
   * representation.
   * 
   * @return the mnemonic string representation.
   */
  String getMnemonicAsString();
}
