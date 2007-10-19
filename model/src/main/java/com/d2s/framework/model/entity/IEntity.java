/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.io.Serializable;

import com.d2s.framework.model.component.IComponent;

/**
 * This interface must be implemented by all persistent entities in the
 * application domain. It establishes the minimal contract of an entity which is
 * providing id accessors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntity extends IComponent {

  /**
   * constant for identifier property <code>ID</code>.
   */
  String ID      = "id";

  /**
   * constant for version property <code>VERSION</code>.
   */
  String VERSION = "version";

  /**
   * It is important to declare here so that ORM (hibernate for instance)
   * detects it has to delegate to the underlying instance when proxiing.
   * 
   * @param o
   *            the instance to compare to.
   * @return true if both instances are equal.
   */
  boolean equals(Object o);

  /**
   * Gets the interface or class establishing the entity contract.
   * 
   * @return the entity contract.
   */
  Class<? extends IEntity> getContract();

  /**
   * Gets the id used to uniquely identify an entity (surrogate key). The id is
   * assigned to the entity instance as soon as the entity is created in memory
   * an is afterwards made immutable so that <code>equals()</code> and
   * <code>hashCode()</code> can safely rely on it whenever they are transient
   * or not. It also establishes the minimal contract of a versionable entity
   * which is providing version accessors to handle access concurrency.
   * 
   * @return The id of the entity.
   */
  Serializable getId();

  /**
   * Gets the version of this entity.
   * 
   * @return the entity version.
   */
  Integer getVersion();

  /**
   * It is important to declare here so that ORM (hibernate for instance)
   * detects it has to delegate to the underlying instance when proxiing.
   * 
   * @return the hashcode.
   */
  int hashCode();

  /**
   * Gets wether this entity has already been saved in the backing store.
   * 
   * @return true if the entity is not transient.
   */
  boolean isPersistent();
}
