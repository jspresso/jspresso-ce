/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.datatransfer;

import com.d2s.framework.model.descriptor.IComponentDescriptor;

/**
 * A simple container for transfering component(s). Its usage is targetting at
 * copy/cut/paste operations.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ComponentTransferStructure {

  private IComponentDescriptor componentDescriptor;
  private Object               content;
  private TransferMode         transferMode;

  /**
   * Constructs a new <code>ComponentTransferStructure</code> instance.
   * 
   * @param componentDescriptor
   *          the descriptor of the components contained in the structure.
   * @param content
   *          the content of the structure.
   * @param transferMode
   *          the transfer mode (copy or move).
   */
  public ComponentTransferStructure(IComponentDescriptor componentDescriptor,
      Object content, TransferMode transferMode) {
    this.componentDescriptor = componentDescriptor;
    this.content = content;
    this.transferMode = transferMode;
  }

  /**
   * Gets the componentDescriptor.
   * 
   * @return the componentDescriptor.
   */
  public IComponentDescriptor getComponentDescriptor() {
    return componentDescriptor;
  }

  /**
   * Gets the content.
   * 
   * @return the content.
   */
  public Object getContent() {
    return content;
  }

  /**
   * Gets the transferMode.
   * 
   * @return the transferMode.
   */
  public TransferMode getTransferMode() {
    return transferMode;
  }

}
