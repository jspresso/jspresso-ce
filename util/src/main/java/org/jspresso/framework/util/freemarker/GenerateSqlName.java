/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.util.freemarker;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Infers a SQL column name from a property name.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
  @SuppressWarnings("unchecked")
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
