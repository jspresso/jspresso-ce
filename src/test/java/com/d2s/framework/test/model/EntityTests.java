/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.test.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.d2s.framework.sample.backend.domain.City;
import com.d2s.framework.sample.backend.domain.Department;
import com.d2s.framework.sample.backend.domain.Employee;
import com.d2s.framework.sample.backend.domain.Person;
import com.d2s.framework.sample.backend.domain.Project;

/**
 * Test case for entities.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityTests extends AbstractModelTest {

  /**
   * The suite method implemented dynamically.
   * 
   * @return the test suite.
   */
  public static Test suite() {
    /*
     * the dynamic way
     */
    return new TestSuite(EntityTests.class);
  }

  /**
   * Tests one-to-many properties operations.
   */
  public void testBidirectionalManyToManyProperties() {
    Project eai = createEntityInstance(Project.class);
    Project accounting = createEntityInstance(Project.class);

    Employee vincent = createEntityInstance(Employee.class);
    Employee stephanie = createEntityInstance(Employee.class);

    eai.addToProjectMembers(vincent);
    assertTrue("Collection property does not contain added element.", eai
        .getProjectMembers().contains(vincent));
    assertTrue("Reverse collection property not updated.", vincent
        .getEmployeeProjects().contains(eai));

    accounting.addToProjectMembers(vincent);
    assertTrue("Collection property does not contain added element.",
        accounting.getProjectMembers().contains(vincent));
    assertTrue("Reverse collection property not updated.", vincent
        .getEmployeeProjects().contains(accounting));
    assertTrue("Previous collection property changed.", eai.getProjectMembers()
        .contains(vincent));
    assertTrue("Previous reverse collection property changed.", vincent
        .getEmployeeProjects().contains(eai));

    eai.addToProjectMembers(stephanie);
    assertTrue("Collection property does not contain added element.", eai
        .getProjectMembers().contains(stephanie));
    assertTrue("Reverse collection property not updated.", stephanie
        .getEmployeeProjects().contains(eai));
    assertTrue("Previous collection property changed.", eai.getProjectMembers()
        .contains(vincent));
    assertTrue("Previous reverse collection property changed.", vincent
        .getEmployeeProjects().contains(eai));

    eai.setProjectMembers(null);
    assertTrue("Collection property still contains removed element.", eai
        .getProjectMembers().size() == 0);
    assertFalse("Reverse collection property not updated.", stephanie
        .getEmployeeProjects().contains(eai));
    assertFalse("Reverse collection property not updated.", vincent
        .getEmployeeProjects().contains(eai));
    assertTrue("Reverse collection property not updated.", vincent
        .getEmployeeProjects().contains(accounting));
  }

  /**
   * Tests bidirectional many-to-one properties.
   */
  public void testBidirectionalManyToOne() {
    Employee vincent = createEntityInstance(Employee.class);
    Employee stephanie = createEntityInstance(Employee.class);
    Employee sibylle = createEntityInstance(Employee.class);

    stephanie.setManager(vincent);
    assertEquals("Reference property not correctly set.", vincent, stephanie
        .getManager());
    assertTrue("Reverse collection property not correctly updated.", vincent
        .getManagedEmployees().contains(stephanie));

    stephanie.setManager(sibylle);
    assertTrue("Reverse collection property not correctly updated.", sibylle
        .getManagedEmployees().contains(stephanie));
    assertEquals("Reference property not correctly set.", sibylle, stephanie
        .getManager());
    assertFalse("Reverse collection property not correctly updated.", vincent
        .getManagedEmployees().contains(stephanie));

    stephanie.setManager(null);
    assertNull("Reference property not correctly set to null.", stephanie
        .getManager());
    assertFalse("Reverse collection property not correctly updated.", sibylle
        .getManagedEmployees().contains(stephanie));
  }

  /**
   * Tests one-to-many properties operations.
   */
  public void testBidirectionalOneToManyProperties() {
    Department it = createEntityInstance(Department.class);
    Department hr = createEntityInstance(Department.class);
    assertEquals("Collection property not properly initialized.", 0, it
        .getDepartmentEmployees().size());

    Employee vincent = createEntityInstance(Employee.class);
    it.addToDepartmentEmployees(vincent);
    assertTrue("Collection property does not contain added element.", it
        .getDepartmentEmployees().contains(vincent));
    assertEquals("Reverse reference property not set.", it, vincent
        .getDepartment());

    hr.addToDepartmentEmployees(vincent);
    assertTrue("Collection property does not contain added element.", hr
        .getDepartmentEmployees().contains(vincent));
    assertEquals("Reverse reference property not set.", hr, vincent
        .getDepartment());
    assertFalse("Old collection property still contains element.", it
        .getDepartmentEmployees().contains(vincent));

    hr.removeFromDepartmentEmployees(vincent);
    assertFalse("Collection property still contains added element.", it
        .getDepartmentEmployees().contains(vincent));
    assertNull("Reverse reference property not updated to null.", vincent
        .getDepartment());
  }

  /**
   * Tests bidirectional one-to-one properties.
   */
  public void testBidirectionalOneToOne() {
    Person vincent = createEntityInstance(Person.class);
    Person stephanie = createEntityInstance(Person.class);
    Person sibylle = createEntityInstance(Person.class);

    stephanie.setMarriedTo(vincent);
    assertEquals("Reference property not correctly set.", vincent, stephanie
        .getMarriedTo());
    assertEquals("Reverse reference property not correctly updated.", stephanie
        .getMarriedTo(), vincent);

    vincent.setMarriedTo(sibylle);
    assertEquals("Reference property not correctly set.", vincent, sibylle
        .getMarriedTo());
    assertEquals("Reverse reference property not correctly updated.", sibylle
        .getMarriedTo(), vincent);
    assertNull("Old reverse reference property not correctly updated.",
        stephanie.getMarriedTo());

    vincent.setMarriedTo(null);
    assertNull("Reference property not correctly set to null.", vincent
        .getMarriedTo());
    assertNull("Reverse reference property not correctly updated to null.",
        sibylle.getMarriedTo());
  }

  /**
   * Tests that a collection property 'set' is equivallent to 'adds'.
   */
  public void testCollectionPropertySetter() {
    Department it = createEntityInstance(Department.class);
    Department hr = createEntityInstance(Department.class);
    Employee vincent = createEntityInstance(Employee.class);
    Employee stephanie = createEntityInstance(Employee.class);
    Employee sibylle = createEntityInstance(Employee.class);
    it.addToDepartmentEmployees(vincent);
    it.addToDepartmentEmployees(stephanie);
    Set<Employee> itEmployees = it.getDepartmentEmployees();
    Set<Employee> newItEmployees = new HashSet<Employee>();
    newItEmployees.add(stephanie);
    newItEmployees.add(sibylle);

    it.setDepartmentEmployees(newItEmployees);
    assertTrue("Collection reference changed.", itEmployees == it
        .getDepartmentEmployees());
    assertTrue("Collection property does not contain added element.", it
        .getDepartmentEmployees().contains(stephanie));
    assertTrue("Collection property does not contain added element.", it
        .getDepartmentEmployees().contains(sibylle));
    assertFalse("Collection property still contains old element.", it
        .getDepartmentEmployees().contains(vincent));
    assertEquals("Reverse reference property not correctly updated.", it,
        stephanie.getDepartment());
    assertNull("Reverse reference property not correctly set to null.", vincent
        .getDepartment());

    hr.setDepartmentEmployees(it.getDepartmentEmployees());
    assertFalse("Collection reference directly assigned.", hr
        .getDepartmentEmployees() == it.getDepartmentEmployees());
    Collection<Employee> hrEmployees = hr.getDepartmentEmployees();
    assertTrue("Collection property does not contain added element.", hr
        .getDepartmentEmployees().contains(stephanie));
    assertFalse("Collection property still contains old element.", it
        .getDepartmentEmployees().contains(stephanie));
    assertEquals("Reverse reference property not correctly updated.", hr,
        stephanie.getDepartment());

    hr.setDepartmentEmployees(null);
    assertTrue("Collection reference changed.", hrEmployees == hr
        .getDepartmentEmployees());
    assertNull("Reverse reference property not correctly set to null.",
        stephanie.getDepartment());
  }

  /**
   * Tests computed properties.
   */
  public void testComputedProperties() {
    Person vincent = createEntityInstance(Person.class);
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -30);
    cal.add(Calendar.DAY_OF_YEAR, -10);
    vincent.setBirthDate(cal.getTime());
    assertEquals("Could not get computed property.", new Integer(30), vincent
        .getAge());

    cal.add(Calendar.YEAR, 2);
    vincent.setBirthDate(cal.getTime());
    assertEquals("Computed property not properly updated.", new Integer(28),
        vincent.getAge());
  }

  /**
   * Tests entity serializability.
   * 
   * @throws Exception
   *             Any exception occuring.
   */
  public void testEntitySerializability() throws Exception {
    Department it = createEntityInstance(Department.class);
    it.setName("I.T. Dept");
    Employee vincent = createEntityInstance(Employee.class);
    vincent.setName("Vincent");
    Employee stephanie = createEntityInstance(Employee.class);
    stephanie.setName("Stephanie");
    it.addToDepartmentEmployees(vincent);
    it.addToDepartmentEmployees(stephanie);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(it);
    oos.flush();

    byte[] store = baos.toByteArray();

    ByteArrayInputStream bais = new ByteArrayInputStream(store);
    ObjectInputStream ois = new ObjectInputStream(bais);
    Department itClone = (Department) ois.readObject();

    assertFalse("Entity and its clone are same instance.", it == itClone);
    assertTrue("Entity and its clone are not equal.", it.equals(itClone));
    assertEquals("Entity clone has different field values", it.getName(),
        itClone.getName());

    assertTrue("Entity did not clone its associations.", it
        .getDepartmentEmployees().equals(itClone.getDepartmentEmployees()));

    assertTrue("Entity did not clone its references.", (itClone
        .getDepartmentEmployees().iterator().next()).getDepartment().equals(
        itClone));
  }

  /**
   * Tests service execution on entities.
   * 
   * @throws Exception
   *             Any exception occuring.
   */
  public void testEntityService() throws Exception {
    Person vincent = createEntityInstance(Person.class);
    vincent.setName("Vincent");
    vincent.transformNameToUppercase();
    assertEquals("Service did not perform succesfully.", vincent.getName()
        .toUpperCase(), vincent.getName());
  }

  /**
   * Tests that internal id is set on creation.
   */
  public void testIdentityOnCreate() {
    City paris = createEntityInstance(City.class);
    assertNotNull("Synthetic key (Id) was not set on creation.", paris.getId());
  }

  /**
   * Tests simple property operations.
   */
  public void testSimpleProperties() {
    City paris = createEntityInstance(City.class);
    paris.setName("Paris");
    assertEquals("Simple property was not set correctly.", "Paris", paris
        .getName());
  }

  /**
   * Tests unidirectional many-to-one properties.
   */
  public void testUnidirectionalManyToOne() {
    Person vincent = createEntityInstance(Person.class);
    City paris = createEntityInstance(City.class);
    vincent.setBirthCity(paris);
    assertEquals("Reference property not correctly set.", paris, vincent
        .getBirthCity());
  }
}
