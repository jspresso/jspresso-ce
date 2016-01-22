/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
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
