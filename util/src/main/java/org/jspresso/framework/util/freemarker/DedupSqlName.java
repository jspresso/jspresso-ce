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
 * Dedups the SQL identifiers.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("rawtypes")
public class DedupSqlName implements TemplateMethodModelEx {

  private final boolean              alwaysAppendIndex;
  private final Map<String, Integer> deduppers;

  /**
   * Constructs a new {@code DedupSqlName} instance.
   * @param alwaysAppendIndex Does it always append index even on 1st use of the identifier
   */
  public DedupSqlName(boolean alwaysAppendIndex) {
    this.alwaysAppendIndex = alwaysAppendIndex;
    deduppers = new HashMap<>();
  }

  /**
   * Reduces an input string.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public TemplateModel exec(List arguments) throws TemplateModelException {
    try {
      String sqlNameToDedup = arguments.get(0).toString();
      return new SimpleScalar(dedup(sqlNameToDedup));
    } catch (Exception ex) {
      throw new TemplateModelException("Could execute dedupSqlName method.", ex);
    }
  }

  String dedup(String sqlName) {
    if (deduppers.containsKey(sqlName)) {
      deduppers.put(sqlName, deduppers.get(sqlName) + 1);
    } else {
      deduppers.put(sqlName, 0);
    }
    String dedupper = Integer.toHexString(deduppers.get(sqlName));
    if (!alwaysAppendIndex && "0".equals(dedupper)) {
      dedupper = "";
    }
    return sqlName.substring(0, sqlName.length() - dedupper.length()) + dedupper;
  }

}
