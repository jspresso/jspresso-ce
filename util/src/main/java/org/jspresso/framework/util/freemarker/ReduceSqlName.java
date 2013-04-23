/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Reduces the SQL identifiers to apply a maximum length.
 * 
 * @version $LastChangedRevision: 5621 $
 * @author Vincent Vandenschrick
 */
public class ReduceSqlName implements TemplateMethodModelEx {

  private static final String  WORD_SEP = "_";

  private int                  maxSize;
  private Map<String, String>  shortened;
  private Map<String, Integer> deduppers;

  /**
   * Constructs a new <code>SqlNameReductor</code> instance.
   * 
   * @param maxSize
   *          the maximum size of the identifiers. <code>-1</code> means no
   *          limit.
   */
  public ReduceSqlName(int maxSize) {
    this.maxSize = maxSize;
    shortened = new HashMap<String, String>();
    deduppers = new HashMap<String, Integer>();
  }

  /**
   * Reduces an input string.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public TemplateModel exec(List arguments) throws TemplateModelException {
    try {
      String sqlNameToReduce = arguments.get(0).toString();
      String mandatorySuffix = "";
      if (arguments.size() > 1) {
        mandatorySuffix = arguments.get(1).toString();
      }
      return new SimpleScalar(reduceToMaxSize(sqlNameToReduce, mandatorySuffix));
    } catch (Exception ex) {
      throw new TemplateModelException("Could execute reduceSqlName method.",
          ex);
    }
  }

  private String reduceToMaxSize(String sqlColumnName, String mandatorySuffix) {
    int size = maxSize;
    if (mandatorySuffix.length() > 0) {
      size -= mandatorySuffix.length();
    }
    if (size <= 0 || sqlColumnName.length() <= size) {
      return sqlColumnName + mandatorySuffix;
    }
    if (shortened.containsKey(sqlColumnName)) {
      return shortened.get(sqlColumnName);
    }
    String[] splitted = sqlColumnName.split(WORD_SEP);
    int lettersPerSection = (size / splitted.length) - 1;
    StringBuilder reduced = new StringBuilder();
    if (lettersPerSection > 0) {
      for (String section : splitted) {
        reduced.append(section.substring(0, lettersPerSection));
        reduced.append(WORD_SEP);
      }
    } else {
      reduced.append(sqlColumnName.substring(0, size - 2)).append(WORD_SEP);
    }
    if (deduppers.containsKey(reduced)) {
      deduppers.put(reduced.toString(),
          Integer.valueOf(deduppers.get(reduced).intValue() + 1));
    } else {
      deduppers.put(reduced.toString(), Integer.valueOf(0));
    }
    reduced.append(Integer.toHexString(deduppers.get(reduced.toString())
        .intValue()));
    shortened.put(sqlColumnName, reduced.toString() + mandatorySuffix);
    return reduced.toString() + mandatorySuffix;
  }

}
