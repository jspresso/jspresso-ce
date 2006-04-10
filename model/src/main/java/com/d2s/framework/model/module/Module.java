/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.module;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.util.bean.ElementClass;
import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * A module is a central element in the application architecture. It serves as
 * an entry point on the domain model. Modules are organized as a tree structure
 * since they can (optionally) provide modules. A module can be seen as a window
 * on the business grouping processes forming a business activity (like master
 * data management, customer contract handling, ...). Each module can
 * (optionally) provide a projected object serving as model root for trigerring
 * grouped processes.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class Module implements IPropertyChangeCapable {

  private PropertyChangeSupport propertyChangeSupport;
  private String                name;
  private String                description;
  private List<SubModule>       subModules;

  /**
   * Constructs a new <code>Module</code> instance.
   */
  public Module() {
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
   * Gets the subModules modules.
   * 
   * @return the list of subModules modules.
   */
  @ElementClass(SubModule.class)
  public List<SubModule> getSubModules() {
    return subModules;
  }

  /**
   * Sets the subModules modules. It will fire a "subModules" property change
   * event.
   * 
   * @param subModules
   *          the subModules modules to set.
   */
  public void setSubModules(List<SubModule> subModules) {
    List<SubModule> oldValue = null;
    if (getSubModules() != null) {
      oldValue = new ArrayList<SubModule>(getSubModules());
    }
    this.subModules = subModules;
    updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
  }

  /**
   * Adds a child module.
   * 
   * @param child
   *          the child module to add. It will fire a "subModules" property
   *          change event.
   * @return <code>true</code> if the module was succesfully added.
   */
  public boolean addSubModule(SubModule child) {
    if (subModules == null) {
      subModules = new ArrayList<SubModule>();
    }
    List<SubModule> oldValue = new ArrayList<SubModule>(getSubModules());
    if (subModules.add(child)) {
      updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
      return true;
    }
    return false;
  }

  /**
   * Adds a subModules module collection. It will fire a "subModules" property
   * change event.
   * 
   * @param subModulesToAdd
   *          the subModules modules to add.
   * @return <code>true</code> if the subModules module collection was
   *         succesfully added.
   */
  public boolean addSubModules(Collection<? extends SubModule> subModulesToAdd) {
    if (subModules == null) {
      subModules = new ArrayList<SubModule>();
    }
    List<SubModule> oldValue = new ArrayList<SubModule>(getSubModules());
    if (subModules.addAll(subModulesToAdd)) {
      updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
      return true;
    }
    return false;
  }

  /**
   * Removes a child module. It will fire a "subModules" property change event.
   * 
   * @param subModule
   *          the child module to remove.
   * @return <code>true</code> if the module was succesfully removed.
   */
  public boolean removeSubModule(SubModule subModule) {
    if (subModules != null) {
      List<SubModule> oldValue = new ArrayList<SubModule>(getSubModules());
      if (subModules.remove(subModule)) {
        updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
        return true;
      }
      return false;
    }
    return false;
  }

  /**
   * Removes a subModules module collection. It will fire a "subModules"
   * property change event.
   * 
   * @param childrenToRemove
   *          the subModules modules to remove.
   * @return <code>true</code> if the subModules module collection was
   *         succesfully removed.
   */
  public boolean removeSubModules(Collection<SubModule> childrenToRemove) {
    if (subModules != null) {
      List<SubModule> oldValue = new ArrayList<SubModule>(getSubModules());
      if (subModules.removeAll(childrenToRemove)) {
        updateParentsAndFireSubModulesChanged(oldValue, getSubModules());
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
   * This method will set the parent module to the new subModules modules and
   * remove the parent of the old removed subModules modules. It will fire the
   * "subModules" property change event.
   * 
   * @param oldChildren
   *          the old subModules collection property.
   * @param newChildren
   *          the new subModules collection property.
   */
  protected void updateParentsAndFireSubModulesChanged(
      List<SubModule> oldChildren, List<SubModule> newChildren) {
    if (oldChildren != null) {
      for (SubModule oldChild : oldChildren) {
        if (newChildren == null || !newChildren.contains(oldChild)) {
          oldChild.setParent(null);
        }
      }
    }
    if (newChildren != null) {
      for (SubModule newChild : newChildren) {
        if (oldChildren == null || !oldChildren.contains(newChild)) {
          newChild.setParent(this);
        }
      }
    }
    firePropertyChange("subModules", oldChildren, newChildren);
  }

  /**
   * Gets the module's name. It may serve for the module's view.
   * 
   * @return the module's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the module's name. It may serve for the module's view.
   * 
   * @param name
   *          the module's name.
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
   * Gets the module's description. It may serve for the module's view.
   * 
   * @return the module's description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the module's description. It may serve for the module's view.
   * 
   * @param description
   *          the module's description.
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
    if (!(obj instanceof Module)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    Module rhs = (Module) obj;
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
