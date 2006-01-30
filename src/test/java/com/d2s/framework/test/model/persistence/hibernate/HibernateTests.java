/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.test.model.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.test.model.AbstractModelTest;
import com.d2s.framework.test.model.domain.Department;
import com.d2s.framework.test.model.domain.Employee;

/**
 * Tests for hibernate persistence layer used in conjunction with proxy
 * entities.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class HibernateTests extends AbstractModelTest {

  private IApplicationSession applicationSession;
  private HibernateTemplate   hibernateTemplate;
  private TransactionTemplate transactionTemplate;

  /**
   * Tests rollback of persistent state.
   */
  public void testCreateRollback() {
    cleanup();
    final Department itDept = (Department) createEntityInstance(Department.class);
    transactionTemplate.execute(new TransactionCallback() {

      public Object doInTransaction(TransactionStatus status) {
        Department itDeptMerged = (Department) applicationSession
            .cloneInUnitOfWork(itDept);
        itDeptMerged.setName("I.T. Department");
        hibernateTemplate.persist(itDeptMerged);
        status.setRollbackOnly();
        return null;
      }
    });
    assertFalse("Saved entity is considered persistent after rollback.", itDept
        .isPersistent());
    assertNull("Version has not been succesfully rollbacked.", itDept
        .getVersion());
    assertNull(
        "Transient object properties has not been rollbacked to pre-transaction state.",
        itDept.getName());
    assertTrue("Saved entity is considered not dirty after rollback.",
        applicationSession.isDirty(itDept));
  }

  /**
   * Tests rollback of updated in-memory entity state.
   */
  public void testUpdateRollback() {
    cleanup();
    final Department itDept = (Department) createEntityInstance(Department.class);
    itDept.setName("I.T. Department");
    final Employee vincent = (Employee) createEntityInstance(Employee.class);
    vincent.setName("Vincent");
    vincent.setGender("M");
    itDept.addToDepartmentEmployees(vincent);
    hibernateTemplate.persist(itDept);
    transactionTemplate.execute(new TransactionCallback() {

      public Object doInTransaction(TransactionStatus status) {
        List<IEntity> entitiesToMerge = new ArrayList<IEntity>();
        entitiesToMerge.add(itDept);
        entitiesToMerge.add(vincent);
        List<IEntity> mergedEntities = applicationSession
            .cloneInUnitOfWork(entitiesToMerge);
        Department itDeptMerged = (Department) mergedEntities.get(0);
        Employee vincentMerged = (Employee) mergedEntities.get(1);
        Employee stephanie = (Employee) createEntityInstance(Employee.class);
        stephanie.setName("Stephanie");
        stephanie.setGender("F");
        itDeptMerged.addToDepartmentEmployees(stephanie);
        itDeptMerged.removeFromDepartmentEmployees(vincentMerged);
        itDeptMerged.setName("Updated");
        hibernateTemplate.saveOrUpdate(itDeptMerged);
        status.setRollbackOnly();
        return null;
      }
    });
    assertEquals("Collection property unsuccessfully rollbacked.", 1, itDept
        .getDepartmentEmployees().size());
    assertEquals("Collection property unsuccessfully rollbacked.", "Vincent",
        itDept.getDepartmentEmployees().iterator().next().getName());
    assertEquals("Simple property unsuccessfully rollbacked.",
        "I.T. Department", itDept.getName());
  }

  /**
   * Tests rollback of updated in-memory entity state.
   */
  public void testRefresh() {
    cleanup();
    final Department itDept = (Department) createEntityInstance(Department.class);
    itDept.setName("I.T. Department");
    hibernateTemplate.persist(itDept);
    itDept.setName("torefresh");

    transactionTemplate.execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        hibernateTemplate.refresh(itDept);
        return null;
      }
    });
    assertEquals("Simple property unsuccessfully refreshed.",
        "I.T. Department", itDept.getName());
  }

  private void cleanup() {
    hibernateTemplate.execute(new HibernateCallback() {

      public Object doInHibernate(Session session) {
        session.createQuery(
            "delete from com.d2s.framework.sample.backend.domain.Employee")
            .executeUpdate();
        session.createQuery(
            "delete from com.d2s.framework.sample.backend.domain.Department")
            .executeUpdate();
        session.createQuery(
            "delete from com.d2s.framework.sample.backend.domain.Person")
            .executeUpdate();
        session.createQuery(
            "delete from com.d2s.framework.sample.backend.domain.City")
            .executeUpdate();
        return null;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    applicationSession = (IApplicationSession) getApplicationContext().getBean(
        "applicationSession");
    hibernateTemplate = new HibernateTemplate(
        (SessionFactory) getApplicationContext().getBean(
            "hibernateSessionFactory"));
    transactionTemplate = new TransactionTemplate(
        (PlatformTransactionManager) getApplicationContext().getBean(
            "transactionManager"));
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
    return new TestSuite(HibernateTests.class);
  }
}
