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
package org.jspresso.framework.view.descriptor.basic;

import java.util.Collection;

import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.lang.StringUtils;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;

/**
 * This is the base descriptor for all tree levels. A tree level is a collection
 * of sibling nodes that belong to the same formal collection (usually a
 * component collection property). This base descriptor does not accept nested
 * subtrees so it is only used to describe a collection of leaf nodes. If you
 * need to describe intermediary tree levels, yous will use one of the 2
 * subtypes :
 * <ul>
 * <li><i>BasicSimpleTreeLevelDescriptor</i> to define a collection of tree
 * nodes that accept a single subtree</li>
 * <li><i>BasicCompositeTreeLevelDescriptor</i> to define a collection of tree
 * nodes that accept a list of subtrees</li>
 * </ul>
 * Defining a tree level is mainly a matter of defining its representation as an
 * individual list of components (i.e. the {@code nodeGroupDescriptor}
 * property).
 *
 * @author Vincent Vandenschrick
 */
public class BasicTreeLevelDescriptor implements ITreeLevelDescriptor {

  private Collection<String>  grantedRoles;
  private IListViewDescriptor nodeGroupDescriptor;
  private String              permId;

  /**
   * Gets the grantedRoles.
   *
   * @return the grantedRoles.
   */
  @Override
  public Collection<String> getGrantedRoles() {
    Collection<String> gr = grantedRoles;
    if (gr == null && getNodeGroupDescriptor() != null) {
      gr = getNodeGroupDescriptor().getGrantedRoles();
      if (gr == null) {
        IModelDescriptor modelDescriptor = getNodeGroupDescriptor()
            .getModelDescriptor();
        if (modelDescriptor != null) {
          if (modelDescriptor instanceof ISecurable) {
            gr = ((ISecurable) modelDescriptor).getGrantedRoles();
          }
          if (gr == null
              && modelDescriptor instanceof ICollectionDescriptorProvider<?>
              && ((ICollectionDescriptorProvider<?>) modelDescriptor)
                  .getCollectionDescriptor() != null
              && ((ICollectionDescriptorProvider<?>) modelDescriptor)
                  .getCollectionDescriptor().getElementDescriptor() != null) {
            gr = ((ICollectionDescriptorProvider<?>) modelDescriptor)
                .getCollectionDescriptor().getElementDescriptor()
                .getGrantedRoles();
          }
        }
      }
    }
    return gr;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IListViewDescriptor getNodeGroupDescriptor() {
    return nodeGroupDescriptor;
  }

  /**
   * Assigns the roles that are authorized to use this subtree. It supports
   * &quot;<b>!</b>&quot; prefix to negate the role(s). Whenever the user is not
   * granted sufficient privileges, the subtree is simply hidden. Setting the
   * collection of granted roles to {@code null} (default value) disables
   * role based authorization on the node group level. The framework then checks
   * for the model roles authorizations and will apply the same restrictions. If
   * both view and model granted roles collections are {@code null}, then
   * access is granted to anyone.
   *
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = StringUtils.ensureSpaceFree(grantedRoles);
  }

  /**
   * Describes the collection of sibling nodes (node group) as a if it were a
   * list view. This is how you instruct Jspresso the type of the components
   * that are used as model behind the tree nodes and which model property is
   * used to compute the node labels. Most of the other properties defined on
   * the node group descriptor itself are ignored (font, color, selection
   * action, ...) since a tree group is not a "real" view but just a mean of
   * defining a subtree. All these properties that are ignored on the tree group
   * can be defined on the tree view itself.
   *
   * @param nodeGroupDescriptor
   *          the nodeGroupDescriptor to set.
   */
  public void setNodeGroupDescriptor(IListViewDescriptor nodeGroupDescriptor) {
    this.nodeGroupDescriptor = nodeGroupDescriptor;
  }

  /**
   * Gets the permId.
   *
   * @return the permId.
   */
  @Override
  public String getPermId() {
    return permId;
  }

  /**
   * Sets the permanent identifier to this application element. Permanent
   * identifiers are used by different framework parts, like dynamic security or
   * record/replay controllers to uniquely identify an application element.
   * Permanent identifiers are generated by the SJS build based on the element
   * id but must be explicitly set if Spring XML is used.
   *
   * @param permId
   *          the permId to set.
   */
  @Override
  public void setPermId(String permId) {
    this.permId = permId;
  }
}
