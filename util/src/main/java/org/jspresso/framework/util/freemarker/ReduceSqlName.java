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
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("rawtypes")
public class ReduceSqlName implements TemplateMethodModelEx {

  private static final String  WORD_SEP = "_";

  private final int                  maxSize;
  private final Map<String, String>  shortened;
  private final DedupSqlName dedupper;

  /**
   * Constructs a new {@code ReduceSqlName} instance.
   *
   * @param maxSize           the maximum size of the identifiers.
   * means no
   *          limit.
   * @param dedupSqlName the dedup sql name
   */
  public ReduceSqlName(int maxSize, DedupSqlName dedupSqlName) {
    this.maxSize = maxSize;
    shortened = new HashMap<>();
    dedupper = dedupSqlName;
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
    String shortenedKey = sqlColumnName + mandatorySuffix;
    int size = maxSize;
    if (mandatorySuffix.length() > 0) {
      size -= mandatorySuffix.length();
    }
    if (size <= 0 || sqlColumnName.length() <= size) {
      return sqlColumnName + mandatorySuffix;
    }
    if (shortened.containsKey(shortenedKey)) {
      return shortened.get(shortenedKey);
    }
    String[] splitted = sqlColumnName.split(WORD_SEP);
    int charsPerSection = (size / splitted.length) - 1;
    StringBuilder reduced = new StringBuilder();
    if (charsPerSection > 0) {
      int remainingExtraChars = 0;
      for (String section : splitted) {
        if (section.length() < charsPerSection) {
          remainingExtraChars += (charsPerSection - section.length());
        }
      }
      for (String section : splitted) {
        if (section.length() <= charsPerSection) {
          reduced.append(section);
        } else {
          int extraChars = 0;
          if (remainingExtraChars > 0) {
            extraChars = Math.min(remainingExtraChars, section.length() - charsPerSection);
            remainingExtraChars -= extraChars;
          }
          reduced.append(section.substring(0, charsPerSection + extraChars));
        }
        reduced.append(WORD_SEP);
      }
    } else {
      reduced.append(sqlColumnName.substring(0, size - 2)).append(WORD_SEP);
    }
    String reducedAsString = reduced.toString();
    reducedAsString = dedupper.dedup(reducedAsString);
    shortened.put(shortenedKey, reducedAsString + mandatorySuffix);
    return reducedAsString + mandatorySuffix;
  }

}
