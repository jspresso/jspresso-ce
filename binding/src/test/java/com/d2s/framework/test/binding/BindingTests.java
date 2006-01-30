/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.test.binding;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IMvcBinder;
import com.d2s.framework.binding.basic.BasicCollectionConnector;
import com.d2s.framework.binding.basic.BasicCompositeConnector;
import com.d2s.framework.binding.basic.BasicValueConnector;
import com.d2s.framework.binding.bean.BeanConnector;
import com.d2s.framework.binding.bean.IBeanConnectorFactory;
import com.d2s.framework.sample.backend.domain.City;
import com.d2s.framework.sample.backend.domain.Department;
import com.d2s.framework.sample.backend.domain.Employee;
import com.d2s.framework.test.model.AbstractModelTest;

/**
 * Test case for DefaultMvcBinder.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BindingTests extends AbstractModelTest {

  /**
   * Tests connectors operations on simple property binding.
   */
  public void testSimplePropertyBinding() {
    IBeanConnectorFactory beanConnectorFactory = (IBeanConnectorFactory) getApplicationContext()
        .getBean("beanConnectorFactory");
    BeanConnector cityModelConnector = beanConnectorFactory
        .createBeanConnector("model", City.class);

    BasicCompositeConnector cityViewConnector = new BasicCompositeConnector(
        "view");
    BasicValueConnector cityNameViewConnector = new BasicValueConnector("name");
    cityViewConnector.addChildConnector(cityNameViewConnector);

    IMvcBinder mvcBinder = (IMvcBinder) getApplicationContext().getBean(
        "mvcBinder");
    mvcBinder.bind(cityViewConnector, cityModelConnector);

    City paris = (City) createEntityInstance(City.class);
    paris.setName("Paris");

    cityModelConnector.setConnectorValue(paris);

    assertEquals("View value not initialized.", "Paris", cityNameViewConnector
        .getConnectorValue());

    paris.setName("Paname");
    assertEquals("View value not updated.", "Paname", cityNameViewConnector
        .getConnectorValue());

    cityNameViewConnector.setConnectorValue("Parigi");
    assertEquals("Model value not updated.", "Parigi", paris.getName());

    mvcBinder.bind(cityViewConnector, null);

    cityNameViewConnector.setConnectorValue("Paris");
    assertEquals("Model value still updated.", "Parigi", paris.getName());

    paris.setName("Paname");
    assertEquals("View value still updated.", "Paris", cityNameViewConnector
        .getConnectorValue());
  }

  /**
   * Tests connectors operations on child reference connectors.
   */
  public void testReferenceBinding() {
    IBeanConnectorFactory beanConnectorFactory = (IBeanConnectorFactory) getApplicationContext()
        .getBean("beanConnectorFactory");

    BeanConnector employeeModelConnector = beanConnectorFactory
        .createBeanConnector("model", Employee.class);

    BasicCompositeConnector employeeViewConnector = new BasicCompositeConnector(
        "view");
    BasicCompositeConnector departmentViewConnector = new BasicCompositeConnector(
        "department");
    BasicValueConnector departmentNameViewConnector = new BasicValueConnector(
        "name");
    employeeViewConnector.addChildConnector(departmentViewConnector);
    departmentViewConnector.addChildConnector(departmentNameViewConnector);

    IMvcBinder mvcBinder = (IMvcBinder) getApplicationContext().getBean(
        "mvcBinder");
    mvcBinder.bind(employeeViewConnector, employeeModelConnector);

    Department it = (Department) createEntityInstance(Department.class);
    it.setName("I.T.");
    Department hr = (Department) createEntityInstance(Department.class);
    hr.setName("H.R.");
    Employee vincent = (Employee) createEntityInstance(Employee.class);
    it.addToDepartmentEmployees(vincent);

    employeeModelConnector.setConnectorValue(vincent);

    assertEquals("View value not initialized.", "I.T.",
        departmentNameViewConnector.getConnectorValue());

    it.setName("Information Technology");
    assertEquals("View value not updated.", "Information Technology",
        departmentNameViewConnector.getConnectorValue());

    departmentNameViewConnector.setConnectorValue("I.Tech");
    assertEquals("Model value not updated.", "I.Tech", it.getName());

    hr.addToDepartmentEmployees(vincent);
    assertEquals("View value not updated.", "H.R.", departmentNameViewConnector
        .getConnectorValue());

    departmentViewConnector.setConnectorValue(it);
    assertEquals("View value not updated.", "I.Tech",
        departmentNameViewConnector.getConnectorValue());
    assertEquals("Model value not updated.", it, vincent.getDepartment());

    it.removeFromDepartmentEmployees(vincent);
    assertNull("View value not reset to null.", departmentViewConnector
        .getConnectorValue());
    assertNull("View value not reset to null.", departmentNameViewConnector
        .getConnectorValue());

    departmentViewConnector.setConnectorValue(hr);
    assertTrue("Model value not updated.", hr.getDepartmentEmployees()
        .contains(vincent));
  }

  /**
   * Tests connectors operations on child collection connectors.
   */
  public void testCollectionBinding() {
    IBeanConnectorFactory beanConnectorFactory = (IBeanConnectorFactory) getApplicationContext()
        .getBean("beanConnectorFactory");

    BeanConnector departmentModelConnector = beanConnectorFactory
        .createBeanConnector("model", Department.class);

    BasicCompositeConnector departmentViewConnector = new BasicCompositeConnector(
        "view");

    BasicCompositeConnector employeeViewConnectorPrototype = new BasicCompositeConnector(
        "employeeViewPrototype");
    BasicValueConnector employeeNameViewConnectorPrototype = new BasicValueConnector(
        "name");
    BasicValueConnector employeeAgeViewConnectorPrototype = new BasicValueConnector(
        "age");
    employeeViewConnectorPrototype
        .addChildConnector(employeeNameViewConnectorPrototype);
    employeeViewConnectorPrototype
        .addChildConnector(employeeAgeViewConnectorPrototype);

    IMvcBinder mvcBinder = (IMvcBinder) getApplicationContext().getBean(
        "mvcBinder");

    BasicCollectionConnector departmentEmployeesViewConnector = new BasicCollectionConnector(
        "departmentEmployees", mvcBinder, employeeViewConnectorPrototype);
    departmentViewConnector.addChildConnector(departmentEmployeesViewConnector);

    mvcBinder.bind(departmentViewConnector, departmentModelConnector);

    Department it = (Department) createEntityInstance(Department.class);
    Employee vincent = (Employee) createEntityInstance(Employee.class);
    vincent.setName("Vincent");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -30);
    cal.add(Calendar.DAY_OF_YEAR, -10);
    vincent.setBirthDate(cal.getTime());

    departmentModelConnector.setConnectorValue(it);
    assertEquals("View value not initialized.", 0,
        departmentEmployeesViewConnector.getChildConnectorKeys().size());

    vincent.setDepartment(it);
    assertEquals("View value not updated.", 1, departmentEmployeesViewConnector
        .getChildConnectorKeys().size());
    assertEquals("Child view connector not updated", "Vincent",
        ((ICompositeValueConnector) departmentEmployeesViewConnector
            .getChildConnector(0)).getChildConnector("name")
            .getConnectorValue());
    assertEquals("Child view connector not updated", new Integer(30),
        ((ICompositeValueConnector) departmentEmployeesViewConnector
            .getChildConnector(0)).getChildConnector("age").getConnectorValue());

    vincent.transformNameToUppercase();
    assertEquals("Child view connector not updated", "VINCENT",
        ((ICompositeValueConnector) departmentEmployeesViewConnector
            .getChildConnector(0)).getChildConnector("name")
            .getConnectorValue());

    cal.add(Calendar.YEAR, 2);
    vincent.setBirthDate(cal.getTime());
    assertEquals("Child view connector not updated", new Integer(28),
        ((ICompositeValueConnector) departmentEmployeesViewConnector
            .getChildConnector(0)).getChildConnector("age").getConnectorValue());

    Employee stephanie = (Employee) createEntityInstance(Employee.class);
    stephanie.setName("Stephanie");
    cal.add(Calendar.YEAR, 4);
    stephanie.setBirthDate(cal.getTime());

    Set<Employee> itEmployees = new HashSet<Employee>();
    itEmployees.add(stephanie);
    it.setDepartmentEmployees(itEmployees);

    assertEquals("Child view connector not updated", "Stephanie",
        ((ICompositeValueConnector) departmentEmployeesViewConnector
            .getChildConnector(0)).getChildConnector("name")
            .getConnectorValue());
    assertEquals("Child view connector not updated", new Integer(24),
        ((ICompositeValueConnector) departmentEmployeesViewConnector
            .getChildConnector(0)).getChildConnector("age").getConnectorValue());

    it.addToDepartmentEmployees(vincent);
    vincent.setName("Vincent");
    stephanie.setDepartment(null);

    assertEquals("Child view connector not updated", "Vincent",
        ((ICompositeValueConnector) departmentEmployeesViewConnector
            .getChildConnector(0)).getChildConnector("name")
            .getConnectorValue());
    assertEquals("Child view connector not updated", new Integer(28),
        ((ICompositeValueConnector) departmentEmployeesViewConnector
            .getChildConnector(0)).getChildConnector("age").getConnectorValue());
    assertEquals("View value not updated.", 1, departmentEmployeesViewConnector
        .getChildConnectorKeys().size());

    Department hr = (Department) createEntityInstance(Department.class);
    departmentModelConnector.setConnectorValue(hr);
    assertEquals("View value not updated.", 0, departmentEmployeesViewConnector
        .getChildConnectorKeys().size());

    hr.addToDepartmentEmployees(stephanie);
    assertEquals("Child view connector not updated", "Stephanie",
        ((ICompositeValueConnector) departmentEmployeesViewConnector
            .getChildConnector(0)).getChildConnector("name")
            .getConnectorValue());
    assertEquals("Child view connector not updated", new Integer(24),
        ((ICompositeValueConnector) departmentEmployeesViewConnector
            .getChildConnector(0)).getChildConnector("age").getConnectorValue());
  }

  /**
   * The suite method implemented dynamically.
   * 
   * @return the test suite.
   */
  public static Test suite() {
    /*
     * the dynamic way
     */
    return new TestSuite(BindingTests.class);
  }
}
