/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.test.util.bean;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.d2s.framework.test.D2STestCase;
import com.d2s.framework.util.bean.IAccessor;
import com.d2s.framework.util.bean.IAccessorFactory;

/**
 * Tests for AccessorFactory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AccessorFactoryTests extends D2STestCase {

  /**
   * Tests the setValue method.
   * 
   * @see com.d2s.framework.util.bean.DefaultPropertyAccessor#setValue(Object,
   *      Object)
   */
  public void testSetValue() {
    IAccessorFactory accessorFactory = (IAccessorFactory) getApplicationContext()
        .getBean("accessorFactory");
    JLabel label = new JLabel();
    IAccessor accessor = accessorFactory.createPropertyAccessor("text",
        JLabel.class);

    try {
      accessor.setValue(label, "TestValue");
      assertEquals(
          "Failed to update the underlying bean property using the accessor.",
          "TestValue", label.getText());
    } catch (IllegalAccessException ex) {
      fail("IllegalAccessException");
    } catch (InvocationTargetException ex) {
      fail("InvocationTargetException");
    } catch (NoSuchMethodException ex) {
      fail("NoSuchMethodException");
    }
  }

  /**
   * Tests the getValue method.
   * 
   * @see com.d2s.framework.util.bean.DefaultPropertyAccessor#getValue(Object)
   */
  public void testGetValue() {
    IAccessorFactory accessorFactory = (IAccessorFactory) getApplicationContext()
        .getBean("accessorFactory");
    JLabel label = new JLabel("TestValue");
    IAccessor accessor = accessorFactory.createPropertyAccessor("text",
        JLabel.class);

    try {
      assertEquals(
          "Failed to read the underlying bean property using the accessor.",
          "TestValue", accessor.getValue(label));
    } catch (IllegalAccessException ex) {
      fail("IllegalAccessException");
    } catch (InvocationTargetException ex) {
      fail("InvocationTargetException");
    } catch (NoSuchMethodException ex) {
      fail("NoSuchMethodException");
    }
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
    return new TestSuite(AccessorFactoryTests.class);
  }
}
