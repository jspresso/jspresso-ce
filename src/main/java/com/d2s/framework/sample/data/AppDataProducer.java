/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.sample.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.model.persistence.hibernate.EntityProxyInterceptor;
import com.d2s.framework.sample.backend.domain.City;
import com.d2s.framework.sample.backend.domain.Department;
import com.d2s.framework.sample.backend.domain.Employee;
import com.d2s.framework.sample.backend.domain.Project;

/**
 * Simple utility class to produce test data.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AppDataProducer {

  private BeanFactory    applicationContext;
  private IEntityFactory entityFactory;

  /**
   * Constructs a new <code>AppDataProducer</code> instance.
   * 
   * @param applicationContext
   *            the application context to use.
   */
  public AppDataProducer(BeanFactory applicationContext) {
    this.applicationContext = applicationContext;
    entityFactory = (IEntityFactory) applicationContext
        .getBean("basicEntityFactory");
  }

  /**
   * Creates som test data.
   * 
   * @return the test data created.
   */
  public List<Department> createTestData() {
    HibernateTemplate hibernateTemplate = (HibernateTemplate) applicationContext
        .getBean("hibernateTemplate");
    EntityProxyInterceptor entityInterceptor = new EntityProxyInterceptor();
    entityInterceptor.setEntityFactory(entityFactory);
    hibernateTemplate.setEntityInterceptor(entityInterceptor);

    List<Department> departments = new ArrayList<Department>();
    Department it = createEntityInstance(Department.class);
    it.setName("I.T. Dept.");
    it.setDescription("This is the Information Technology\ndepartment.");
    Department hr = createEntityInstance(Department.class);
    hr.setName("H.R. Dept.");
    hr.setDescription("This is the Human Resources\ndepartment.");

    City paris14 = createEntityInstance(City.class);
    paris14.setName("Paris XIV");
    paris14.setCountry("France");
    paris14.setZip("75001");

    Employee vincent = createEntityInstance(Employee.class);
    vincent.setName("Vincent");
    vincent.setGender("M");
    vincent.setVegetarian(Boolean.FALSE);
    vincent.setAnnualSalary(new Integer(100));
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -30);
    cal.add(Calendar.DAY_OF_YEAR, -10);
    vincent.setBirthDate(cal.getTime());
    vincent.setDepartment(it);
    vincent.setBirthCity(paris14);

    Employee stephanie = createEntityInstance(Employee.class);
    stephanie.setName("Stephanie");
    stephanie.setGender("F");
    stephanie.setVegetarian(Boolean.TRUE);
    stephanie.setAnnualSalary(new Integer(75));
    cal.add(Calendar.YEAR, 4);
    stephanie.setBirthDate(cal.getTime());
    stephanie.setManager(vincent);
    stephanie.setDepartment(it);

    Employee sibylle = createEntityInstance(Employee.class);
    sibylle.setName("Sibylle");
    sibylle.setGender("F");
    sibylle.setVegetarian(Boolean.FALSE);
    sibylle.setAnnualSalary(new Integer(50));
    cal.add(Calendar.YEAR, 5);
    sibylle.setBirthDate(cal.getTime());
    sibylle.setManager(vincent);
    sibylle.setDepartment(it);

    Employee apolline = createEntityInstance(Employee.class);
    apolline.setName("Apolline");
    apolline.setGender("F");
    apolline.setVegetarian(Boolean.TRUE);
    cal.add(Calendar.YEAR, 2);
    apolline.setBirthDate(cal.getTime());
    apolline.setManager(vincent);
    apolline.setDepartment(it);

    Project accounting = createEntityInstance(Project.class);
    accounting.setName("Accounting Project");
    accounting.addToProjectMembers(vincent);
    accounting.addToProjectMembers(sibylle);

    Project timeSheet = createEntityInstance(Project.class);
    timeSheet.setName("TimeSheet Project");
    timeSheet.addToProjectMembers(vincent);
    timeSheet.addToProjectMembers(stephanie);

    Project expenses = createEntityInstance(Project.class);
    expenses.setName("Expenses Project");
    expenses.addToProjectMembers(vincent);
    expenses.addToProjectMembers(sibylle);
    expenses.addToProjectMembers(apolline);

    it.setManager(vincent);
    it.addToDepartmentProjects(accounting);
    it.addToDepartmentProjects(timeSheet);
    it.addToDepartmentProjects(expenses);

    hibernateTemplate.saveOrUpdate(paris14);

    departments.add(it);
    departments.add(hr);

    return departments;
  }

  /**
   * Creates a new entity instance from its interface contract.
   * 
   * @param <T>
   *            the concrete entity class.
   * @param entityContract
   *            The entity contract.
   * @return The new entity instance.
   */
  protected <T extends IEntity> T createEntityInstance(Class<T> entityContract) {
    return entityFactory.createEntityInstance(entityContract);
  }
}
