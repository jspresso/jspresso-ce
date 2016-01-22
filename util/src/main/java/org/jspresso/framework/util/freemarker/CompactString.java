/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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

import java.util.Arrays;
import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * Compacts an input string. It only keeps the first char plus:
 * <ul>
 * <li>upper case chars in case of of a camelcase string</li>
 * <li>chars immediately after an underscore in case of a sql identifier</li>
 * </ul>
 * The result is made uppercase.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("rawtypes")
public class CompactString implements TemplateMethodModelEx {

  private static final char UNDERSCORE = '_';

  /**
   * Constructs a new {@code CompactString} instance.
   */
  public CompactString() {
    // Empty constructor.
  }

  /**
   * Compacts an input string.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public TemplateModel exec(List arguments)
      throws TemplateModelException {
    try {
      String toCompact = ((TemplateScalarModel) arguments.get(0)).getAsString();
      if (toCompact == null || toCompact.length() == 0) {
        return new SimpleScalar("");
      }
      StringBuilder result = new StringBuilder();
      result.append(Character.toUpperCase(toCompact.charAt(0)));
      for (int i = 1; i < toCompact.length(); i++) {
        char prev = toCompact.charAt(i - 1);
        char curr = toCompact.charAt(i);
        if ((Character.isUpperCase(curr) && Character.isLowerCase(prev))
            || prev == UNDERSCORE) {
          result.append(Character.toUpperCase(curr));
        }
      }
      return new SimpleScalar(result.toString());
    } catch (Exception ex) {
      throw new TemplateModelException("Could execute compactString method.",
          ex);
    }
  }

  /**
   * Test main method.
   *
   * @param args
   *          main args. unused.
   * @throws TemplateModelException
   *           whenever an unexpected exception occurs.
   */
  public static void main(String... args) throws TemplateModelException {
    new CompactString().exec(Arrays
        .asList(new SimpleScalar("TEAM_TEAM_MEMBERS")));
  }
}
