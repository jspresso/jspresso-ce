/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.sample.backend.domain;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import com.d2s.framework.model.entity.AbstractEntityExtension;

/**
 * Helper class computing extended properties for Person entities.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class PersonExtension extends AbstractEntityExtension<Person> {

  private Integer age = null;

  /**
   * Constructs a new <code>PersonExtension</code> instance.
   * 
   * @param extendedPerson
   *          The extended person instance.
   */
  public PersonExtension(Person extendedPerson) {
    super(extendedPerson);
    extendedPerson.addPropertyChangeListener("birthDate",
        new PropertyChangeListener() {

          public void propertyChange(@SuppressWarnings("unused")
          PropertyChangeEvent evt) {
            Integer oldAge = age;
            age = null;
            getEntity().firePropertyChange("age", oldAge,
                com.d2s.framework.util.bean.IPropertyChangeCapable.UNKNOWN);
          }
        });
  }

  /**
   * Computes the person age.
   * 
   * @return The person age.
   */
  public Integer getAge() {
    if (age != null) {
      return age;
    }
    if (getEntity().getBirthDate() != null) {
      age = new Integer((int) ((new Date().getTime() - getEntity()
          .getBirthDate().getTime()) / (1000L * 60 * 60 * 24 * 365)));
    }
    return age;
  }
}
