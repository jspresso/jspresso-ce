/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.test.binding;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.d2s.framework.binding.IMvcBinder;
import com.d2s.framework.binding.basic.BasicCollectionConnector;
import com.d2s.framework.binding.basic.BasicCompositeConnector;
import com.d2s.framework.binding.basic.BasicValueConnector;
import com.d2s.framework.binding.bean.BeanConnector;
import com.d2s.framework.binding.bean.IBeanConnectorFactory;
import com.d2s.framework.binding.masterdetail.IMasterDetailBinder;
import com.d2s.framework.binding.ui.swing.CollectionConnectorListModel;
import com.d2s.framework.binding.ui.swing.CollectionConnectorTableModel;
import com.d2s.framework.binding.ui.swing.IListSelectionModelBinder;
import com.d2s.framework.binding.ui.swing.JFormattedFieldConnector;
import com.d2s.framework.binding.ui.swing.JTextFieldConnector;
import com.d2s.framework.test.model.AbstractModelTest;
import com.d2s.framework.test.model.domain.Department;
import com.d2s.framework.test.model.domain.Employee;

/**
 * Test case for DefaultMvcBinder.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class GuiBindingExample extends AbstractModelTest {

  /**
   * Tests connectors operations on gui connectors.
   */
  public void testCollectionBinding() {
    IBeanConnectorFactory beanConnectorFactory = (IBeanConnectorFactory) getApplicationContext()
        .getBean("beanConnectorFactory");

    IMvcBinder mvcBinder = (IMvcBinder) getApplicationContext().getBean(
        "mvcBinder");
    IMasterDetailBinder masterDetailBinder = (IMasterDetailBinder) getApplicationContext()
        .getBean("masterDetailBinder");

    IListSelectionModelBinder listSelectionModelBinder = (IListSelectionModelBinder) getApplicationContext()
        .getBean("listSelectionModelBinder");

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

    BasicCollectionConnector departmentEmployeesViewConnector = new BasicCollectionConnector(
        "departmentEmployees", mvcBinder, employeeViewConnectorPrototype);

    departmentViewConnector.addChildConnector(departmentEmployeesViewConnector);

    mvcBinder.bind(departmentViewConnector, departmentModelConnector);

    BasicCompositeConnector managerViewConnector = new BasicCompositeConnector(
        "manager");
    BasicCollectionConnector managedEmployeesViewConnector = new BasicCollectionConnector(
        "managedEmployees", mvcBinder, employeeViewConnectorPrototype);
    managerViewConnector.addChildConnector(managedEmployeesViewConnector);

    BeanConnector managerModelConnector = beanConnectorFactory
        .createBeanConnector("manager", Employee.class);

    mvcBinder.bind(managerViewConnector, managerModelConnector);

    masterDetailBinder.bind(departmentViewConnector
        .getChildConnector("departmentEmployees"), managerViewConnector);

    BasicCompositeConnector employeeViewConnector = new BasicCompositeConnector(
        "employee");

    JTextField employeeNameJTextField = new JTextField();
    JTextFieldConnector employeeNameConnector = new JTextFieldConnector("name",
        employeeNameJTextField);
    employeeViewConnector.addChildConnector(employeeNameConnector);

    JTextField employeeAnnualSalaryJTextField = new JTextField();
    JFormattedFieldConnector employeeAnnualSalaryConnector = new JFormattedFieldConnector(
        "annualSalary", employeeAnnualSalaryJTextField, NumberFormat
            .getCurrencyInstance());
    employeeViewConnector.addChildConnector(employeeAnnualSalaryConnector);

    BeanConnector employeeModelConnector = beanConnectorFactory
        .createBeanConnector("employee", Employee.class);

    masterDetailBinder.bind(managerModelConnector
        .getChildConnector("managedEmployees"), employeeModelConnector);

    mvcBinder.bind(employeeViewConnector, employeeModelConnector);

    Department it = (Department) createEntityInstance(Department.class);

    Employee vincent = (Employee) createEntityInstance(Employee.class);
    vincent.setName("Vincent");
    vincent.setAnnualSalary(new Integer(100));
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -30);
    cal.add(Calendar.DAY_OF_YEAR, -10);
    vincent.setBirthDate(cal.getTime());
    vincent.setDepartment(it);

    Employee stephanie = (Employee) createEntityInstance(Employee.class);
    stephanie.setName("Stephanie");
    stephanie.setAnnualSalary(new Integer(75));
    cal.add(Calendar.YEAR, 4);
    stephanie.setBirthDate(cal.getTime());
    stephanie.setDepartment(it);

    Employee sibylle = (Employee) createEntityInstance(Employee.class);
    sibylle.setName("Sibylle");
    sibylle.setAnnualSalary(new Integer(50));
    cal.add(Calendar.YEAR, 5);
    sibylle.setBirthDate(cal.getTime());
    sibylle.setManager(vincent);
    sibylle.setDepartment(it);

    Employee apolline = (Employee) createEntityInstance(Employee.class);
    apolline.setName("Apolline");
    cal.add(Calendar.YEAR, 2);
    apolline.setBirthDate(cal.getTime());
    apolline.setManager(vincent);
    apolline.setDepartment(it);

    departmentModelConnector.setConnectorValue(it);

    JDialog testFrame = new JDialog((JFrame) null, "GUI connectors test", true);
    testFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    List<String> columnConnectorKeys = new ArrayList<String>();
    columnConnectorKeys.add("id");
    columnConnectorKeys.add("name");
    columnConnectorKeys.add("birthDate");
    JTable employeeTable = new JTable(new CollectionConnectorTableModel(
        departmentEmployeesViewConnector, columnConnectorKeys));
    listSelectionModelBinder.bindSelectionModel(
        departmentEmployeesViewConnector, employeeTable.getSelectionModel(),
        null);
    JScrollPane employeeTableScrollPane = new JScrollPane(employeeTable);

    JList employeeList = new JList(new CollectionConnectorListModel(
        departmentEmployeesViewConnector));
    listSelectionModelBinder.bindSelectionModel(
        departmentEmployeesViewConnector, employeeList.getSelectionModel(),
        null);
    JScrollPane employeeListScrollPane = new JScrollPane(employeeList);

    JTable managedEmployeeTable = new JTable(new CollectionConnectorTableModel(
        managedEmployeesViewConnector, columnConnectorKeys));
    listSelectionModelBinder.bindSelectionModel(managedEmployeesViewConnector,
        managedEmployeeTable.getSelectionModel(), null);
    JScrollPane managedEmployeeTableScrollPane = new JScrollPane(
        managedEmployeeTable);

    JPanel employeeDetailJPanel = new JPanel();
    employeeDetailJPanel.setLayout(new GridLayout(1, 0));
    employeeDetailJPanel.add(employeeNameJTextField);
    employeeDetailJPanel.add(employeeAnnualSalaryJTextField);

    testFrame.getContentPane().add(employeeTableScrollPane, BorderLayout.NORTH);
    testFrame.getContentPane().add(employeeListScrollPane, BorderLayout.EAST);
    testFrame.getContentPane().add(managedEmployeeTableScrollPane,
        BorderLayout.CENTER);
    testFrame.getContentPane().add(employeeDetailJPanel, BorderLayout.SOUTH);
    testFrame.pack();
    testFrame.setSize(800, 600);
    testFrame.setVisible(true);
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
    return new TestSuite(GuiBindingExample.class);
  }

  /**
   * Main method.
   * 
   * @param args
   *          arguments.
   */
  public static void main(String[] args) {
    GuiBindingExample tests = new GuiBindingExample();
    try {
      tests.setUp();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    tests.testCollectionBinding();
  }
}
