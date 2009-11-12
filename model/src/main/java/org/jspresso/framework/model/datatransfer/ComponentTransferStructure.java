/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.datatransfer;

/**
 * A simple container for transfering component(s). Its usage is targetting at
 * copy/cut/paste operations.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete component type.
 */
public class ComponentTransferStructure<E> {

  private Object        content;
  private ETransferMode transferMode;

  /**
   * Constructs a new <code>ComponentTransferStructure</code> instance.
   * 
   * @param content
   *          the content of the structure.
   * @param transferMode
   *          the transfer mode (copy or move).
   */
  public ComponentTransferStructure(Object content, ETransferMode transferMode) {
    this.content = content;
    this.transferMode = transferMode;
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
  public ETransferMode getTransferMode() {
    return transferMode;
  }

}
