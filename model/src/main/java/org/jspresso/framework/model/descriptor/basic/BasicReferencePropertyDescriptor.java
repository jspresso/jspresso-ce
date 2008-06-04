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
package org.jspresso.framework.model.descriptor.basic;

import java.util.Map;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;


/**
 * Default implementation of a reference descriptor.
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
 * @param <E>
 *            the concrete component type.
 */
public class BasicReferencePropertyDescriptor<E> extends
    BasicRelationshipEndPropertyDescriptor implements
    IReferencePropertyDescriptor<E> {

  private Map<String, String>     initializationMapping;
  private IComponentDescriptor<E> referencedDescriptor;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public BasicReferencePropertyDescriptor<E> clone() {
    BasicReferencePropertyDescriptor<E> clonedDescriptor = (BasicReferencePropertyDescriptor<E>) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<E> getComponentDescriptor() {
    return getReferencedDescriptor();
  }

  /**
   * Gets the initializationMapping.
   * 
   * @return the initializationMapping.
   */
  public Map<String, String> getInitializationMapping() {
    if (initializationMapping != null) {
      return initializationMapping;
    }
    if (getParentDescriptor() != null) {
      return ((IReferencePropertyDescriptor<?>) getParentDescriptor())
          .getInitializationMapping();
    }
    return initializationMapping;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return getReferencedDescriptor().getComponentContract();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public IComponentDescriptor<E> getReferencedDescriptor() {
    if (referencedDescriptor != null) {
      return referencedDescriptor;
    }
    if (getParentDescriptor() != null) {
      return ((IReferencePropertyDescriptor<E>) getParentDescriptor())
          .getReferencedDescriptor();
    }
    return referencedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isQueryable() {
    return getDelegateClassName() == null;
  }

  /**
   * Sets the initializationMapping.
   * 
   * @param initializationMapping
   *            the initializationMapping to set.
   */
  public void setInitializationMapping(Map<String, String> initializationMapping) {
    this.initializationMapping = initializationMapping;
  }

  /**
   * Sets the referencedDescriptor property.
   * 
   * @param referencedDescriptor
   *            the referencedDescriptor to set.
   */
  public void setReferencedDescriptor(
      IComponentDescriptor<E> referencedDescriptor) {
    this.referencedDescriptor = referencedDescriptor;
  }

  /**
   * return true for a 1-1 relationship and false for a 1-N relationship.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected boolean getDefaultComposition() {
    if (getReverseRelationEnd() == null
        || getReverseRelationEnd() instanceof IReferencePropertyDescriptor<?>) {
      return true;
    }
    return false;
  }
}
