/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 * Default implementation of a tree level descriptor.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTreeLevelDescriptor implements ITreeLevelDescriptor {

  private IListViewDescriptor nodeGroupDescriptor;
  private Collection<String>  grantedRoles;

  /**
   * {@inheritDoc}
   */
  public IListViewDescriptor getNodeGroupDescriptor() {
    return nodeGroupDescriptor;
  }

  /**
   * Sets the nodeGroupDescriptor.
   * 
   * @param nodeGroupDescriptor
   *          the nodeGroupDescriptor to set.
   */
  public void setNodeGroupDescriptor(IListViewDescriptor nodeGroupDescriptor) {
    this.nodeGroupDescriptor = nodeGroupDescriptor;
  }

  /**
   * Sets the grantedRoles.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = StringUtils.ensureSpaceFree(grantedRoles);
  }

  /**
   * Gets the grantedRoles.
   * 
   * @return the grantedRoles.
   */
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
}
