/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.freemarker;

import java.util.List;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BooleanModel;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * Tells wether an object is instance of a class.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class InstanceOf implements TemplateMethodModelEx {

  private BeansWrapper wrapper;

  /**
   * Constructs a new <code>InstanceOf</code> instance.
   * 
   * @param wrapper
   *            the beans wrapper.
   */
  public InstanceOf(BeansWrapper wrapper) {
    this.wrapper = wrapper;
  }

  /**
   * Tells wether an object is instance of a class.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public TemplateModel exec(List arguments) throws TemplateModelException {
    try {
      Object target = ((AdapterTemplateModel) arguments.get(0))
          .getAdaptedObject(Object.class);
      Class<?> clazz = Class.forName(((TemplateScalarModel) arguments.get(1))
          .getAsString());
      return new BooleanModel(new Boolean(clazz.isAssignableFrom(target
          .getClass())), wrapper);
    } catch (Exception ex) {
      throw new TemplateModelException("Could execute instanceof method.", ex);
    }
  }
}
