/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.projection;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * A projection is a central element in the application architecture. It serves
 * as an entry point on the domain model. Projections are organized as a tree
 * structure since they can (optionally) provide children projections and a
 * parent projection. A projection can be seen as a window on the business
 * grouping processes forming a business activity (like master data management,
 * customer contract handling, ...). Each projection can (optionally) provide a
 * projected object serving as model root for trigerring grouped processes.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class Projection implements IPropertyChangeCapable {

  private PropertyChangeSupport propertyChangeSupport;
  private String                name;
  private String                description;
  private List<ChildProjection> children;

  /**
   * Constructs a new <code>Projection</code> instance.
   */
  public Projection() {
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  /**
   * {@inheritDoc}
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * {@inheritDoc}
   */
  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
  }

  /**
   * Gets the children projections.
   * 
   * @return the list of children projections.
   */
  public List<ChildProjection> getChildren() {
    return children;
  }

  /**
   * Sets the children projections. It will fire a "children" property change
   * event.
   * 
   * @param children
   *          the children projections to set.
   */
  public void setChildren(List<ChildProjection> children) {
    List<ChildProjection> oldValue = null;
    if (getChildren() != null) {
      oldValue = new ArrayList<ChildProjection>(getChildren());
    }
    this.children = children;
    updateParentsAndFireChildrenChanged(oldValue, getChildren());
  }

  /**
   * Adds a child projection.
   * 
   * @param child
   *          the child projection to add. It will fire a "children" property
   *          change event.
   * @return <code>true</code> if the projection was succesfully added.
   */
  public boolean addChild(ChildProjection child) {
    if (children == null) {
      children = new ArrayList<ChildProjection>();
    }
    List<ChildProjection> oldValue = new ArrayList<ChildProjection>(
        getChildren());
    if (children.add(child)) {
      updateParentsAndFireChildrenChanged(oldValue, getChildren());
      return true;
    }
    return false;
  }

  /**
   * Adds a children projection collection. It will fire a "children" property
   * change event.
   * 
   * @param childrenToAdd
   *          the children projections to add.
   * @return <code>true</code> if the children projection collection was
   *         succesfully added.
   */
  public boolean addChildren(Collection<? extends ChildProjection> childrenToAdd) {
    if (children == null) {
      children = new ArrayList<ChildProjection>();
    }
    List<ChildProjection> oldValue = new ArrayList<ChildProjection>(
        getChildren());
    if (children.addAll(childrenToAdd)) {
      updateParentsAndFireChildrenChanged(oldValue, getChildren());
      return true;
    }
    return false;
  }

  /**
   * Removes a child projection. It will fire a "children" property change
   * event.
   * 
   * @param child
   *          the child projection to remove.
   * @return <code>true</code> if the projection was succesfully removed.
   */
  public boolean removeChild(ChildProjection child) {
    if (children != null) {
      List<ChildProjection> oldValue = new ArrayList<ChildProjection>(
          getChildren());
      if (children.remove(child)) {
        updateParentsAndFireChildrenChanged(oldValue, getChildren());
        return true;
      }
      return false;
    }
    return false;
  }

  /**
   * Removes a children projection collection. It will fire a "children"
   * property change event.
   * 
   * @param childrenToRemove
   *          the children projections to remove.
   * @return <code>true</code> if the children projection collection was
   *         succesfully removed.
   */
  public boolean removeChildren(Collection<ChildProjection> childrenToRemove) {
    if (children != null) {
      List<ChildProjection> oldValue = new ArrayList<ChildProjection>(
          getChildren());
      if (children.removeAll(childrenToRemove)) {
        updateParentsAndFireChildrenChanged(oldValue, getChildren());
        return true;
      }
      return false;
    }
    return false;
  }

  /**
   * Fires a property change event.
   * 
   * @param propertyName
   *          the name of the changed property.
   * @param oldValue
   *          the old value of the property.
   * @param newValue
   *          the new value of the property.
   */
  protected void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * This method will set the parent projection to the new children projections
   * and remove the parent of the old removed children projections. It will fire
   * the "children" property change event.
   * 
   * @param oldChildren
   *          the old children collection property.
   * @param newChildren
   *          the new children collection property.
   */
  protected void updateParentsAndFireChildrenChanged(
      List<ChildProjection> oldChildren, List<ChildProjection> newChildren) {
    if (oldChildren != null) {
      for (ChildProjection oldChild : oldChildren) {
        if (newChildren == null || !newChildren.contains(oldChild)) {
          oldChild.setParent(null);
        }
      }
    }
    if (newChildren != null) {
      for (ChildProjection newChild : newChildren) {
        if (oldChildren == null || !oldChildren.contains(newChild)) {
          newChild.setParent(this);
        }
      }
    }
    firePropertyChange("children", oldChildren, newChildren);
  }

  /**
   * Gets the projection's name. It may serve for the projection's view.
   * 
   * @return the projection's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the projection's name. It may serve for the projection's view.
   * 
   * @param name
   *          the projection's name.
   */
  public void setName(String name) {
    if (ObjectUtils.equals(this.name, name)) {
      return;
    }
    String oldValue = getName();
    this.name = name;
    firePropertyChange("name", oldValue, getName());
  }

  /**
   * Gets the projection's description. It may serve for the projection's view.
   * 
   * @return the projection's description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the projection's description. It may serve for the projection's view.
   * 
   * @param description
   *          the projection's description.
   */
  public void setDescription(String description) {
    if (ObjectUtils.equals(this.description, description)) {
      return;
    }
    String oldValue = getDescription();
    this.description = description;
    firePropertyChange("description", oldValue, getDescription());
  }

  /**
   * Equality based on name.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Projection)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    Projection rhs = (Projection) obj;
    return new EqualsBuilder().append(getName(), rhs.getName()).isEquals();
  }

  /**
   * Hash code based on name.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(23, 53).append(name).toHashCode();
  }

  /**
   * based on name.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    if (name != null) {
      return name;
    }
    return "";
  }
}
