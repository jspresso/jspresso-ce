/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.state.remote;

import java.util.List;

/**
 * The state of a composite remote value.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteCompositeValueState extends RemoteValueState {

  private List<RemoteValueState> children;
  private String description;
  private String iconImageUrl;

  /**
   * Constructs a new <code>RemoteCompositeValueState</code> instance.
   * 
   * @param guid
   *          the state guid.
   */
  public RemoteCompositeValueState(String guid) {
    super(guid);
  }

  
  /**
   * Gets the children.
   * 
   * @return the children.
   */
  public List<RemoteValueState> getChildren() {
    return children;
  }

  
  /**
   * Sets the children.
   * 
   * @param children the children to set.
   */
  public void setChildren(List<RemoteValueState> children) {
    this.children = children;
  }


  /**
   * Gets the description.
   * 
   * @return the description.
   */
  public String getDescription() {
    return description;
  }


  /**
   * Sets the description.
   * 
   * @param description the description to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }


  /**
   * Gets the iconImageUrl.
   * 
   * @return the iconImageUrl.
   */
  public String getIconImageUrl() {
    return iconImageUrl;
  }


  /**
   * Sets the iconImageUrl.
   * 
   * @param iconImageUrl the iconImageUrl to set.
   */
  public void setIconImageUrl(String iconImageUrl) {
    this.iconImageUrl = iconImageUrl;
  }
}
