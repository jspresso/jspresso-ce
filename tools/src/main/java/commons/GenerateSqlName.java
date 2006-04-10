/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package commons;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Infers a SQL column name from a property name.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class GenerateSqlName implements TemplateMethodModel {

  /**
   * Infers a SQL column name from a property name.
   * <p>
   * {@inheritDoc}
   */
  public TemplateModel exec(List arguments) throws TemplateModelException {
    try {
      String propertyName = (String) arguments.get(0);
      StringBuffer sqlColumnName = new StringBuffer();
      for (int i = 0; i < propertyName.length(); i++) {
        if (i > 0 && Character.isLowerCase(propertyName.charAt(i - 1))
            && Character.isUpperCase(propertyName.charAt(i))) {
          sqlColumnName.append("_");
        }
        sqlColumnName.append(Character.toUpperCase(propertyName.charAt(i)));
      }
      return new SimpleScalar(sqlColumnName.toString());
    } catch (Exception ex) {
      throw new TemplateModelException("Could not infer SQL column name.", ex);
    }
  }
}
