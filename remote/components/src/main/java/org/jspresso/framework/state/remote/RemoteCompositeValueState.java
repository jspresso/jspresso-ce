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
package org.jspresso.framework.state.remote;

import java.util.List;

/**
 * The state of a composite remote value.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteCompositeValueState extends RemoteValueState {

  private static final long      serialVersionUID = -50313269125938620L;

  private List<RemoteValueState> children;
  private String                 description;
  private String                 iconImageUrl;
  private int                    leadingIndex;
  private int[]                  selectedIndices;

  /**
   * Constructs a new {@code RemoteCompositeValueState} instance.
   *
   * @param guid
   *          the state guid.
   */
  public RemoteCompositeValueState(String guid) {
    super(guid);
    leadingIndex = -1;
  }

  /**
   * Constructs a new {@code RemoteCompositeValueState} instance. Only used
   * for GWT serialization support.
   */
  public RemoteCompositeValueState() {
    // For serialization support
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
   * Gets the description.
   *
   * @return the description.
   */
  public String getDescription() {
    return description;
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
   * Gets the leadingIndex.
   *
   * @return the leadingIndex.
   */
  public int getLeadingIndex() {
    return leadingIndex;
  }

  /**
   * Gets the selectedIndices.
   *
   * @return the selectedIndices.
   */
  public int[] getSelectedIndices() {
    return selectedIndices;
  }

  /**
   * Sets the children.
   *
   * @param children
   *          the children to set.
   */
  public void setChildren(List<RemoteValueState> children) {
    this.children = children;
    if (children != null) {
      for (RemoteValueState child : children) {
        child.setParent(this);
      }
    }
  }

  /**
   * Sets the description.
   *
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the iconImageUrl.
   *
   * @param iconImageUrl
   *          the iconImageUrl to set.
   */
  public void setIconImageUrl(String iconImageUrl) {
    this.iconImageUrl = iconImageUrl;
  }

  /**
   * Sets the leadingIndex.
   *
   * @param leadingIndex
   *          the leadingIndex to set.
   */
  public void setLeadingIndex(int leadingIndex) {
    this.leadingIndex = leadingIndex;
  }

  /**
   * Sets the selectedIndices.
   *
   * @param selectedIndices
   *          the selectedIndices to set.
   */
  public void setSelectedIndices(int... selectedIndices) {
    this.selectedIndices = selectedIndices;
  }
}
