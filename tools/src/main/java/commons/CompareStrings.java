/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package commons;

import java.util.List;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.NumberModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * Compares two strings using java rules.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CompareStrings implements TemplateMethodModelEx {

  private BeansWrapper wrapper;

  /**
   * Constructs a new <code>CompareStrings</code> instance.
   * 
   * @param wrapper
   *          the beans wrapper.
   */
  public CompareStrings(BeansWrapper wrapper) {
    this.wrapper = wrapper;
  }

  /**
   * Compares two strings using java rules.
   * <p>
   * {@inheritDoc}
   */
  public TemplateModel exec(List arguments) throws TemplateModelException {
    try {
      String first = ((TemplateScalarModel) arguments.get(0)).getAsString();
      String second = ((TemplateScalarModel) arguments.get(1)).getAsString();
      return new NumberModel(new Integer(first.compareTo(second)), wrapper);
    } catch (Exception ex) {
      throw new TemplateModelException("Could execute compareStrings method.",
          ex);
    }
  }
}
