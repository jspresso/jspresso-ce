/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.datatransfer;

import java.util.List;

/**
 * A simple container for transferring component(s). Its usage is targeting at
 * copy/cut/paste operations.
 * 
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete component type.
 */
public class ComponentTransferStructure<E> {

  private final List<E> content;
  private final ETransferMode     transferMode;

  /**
   * Constructs a new {@code ComponentTransferStructure} instance.
   *
   * @param content
   *          the content of the structure.
   * @param transferMode
   *          the transfer mode (copy or move).
   */
  public ComponentTransferStructure(List<E> content, ETransferMode transferMode) {
    this.content = content;
    this.transferMode = transferMode;
  }

  /**
   * Gets the content.
   * 
   * @return the content.
   */
  public List<E> getContent() {
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
