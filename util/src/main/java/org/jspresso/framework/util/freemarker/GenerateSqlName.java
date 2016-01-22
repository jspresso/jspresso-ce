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

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import org.jspresso.framework.util.sql.SqlHelper;

/**
 * Infers a SQL column name from a property name.
 *
 * @author Vincent Vandenschrick
 * @author Pierre-m Raoul (atao)
 */
public class GenerateSqlName implements TemplateMethodModelEx {

  private SqlHelper.Formatter formatter;
  private SqlHelper.KeyWordProvider keyWordProvider;

  /**
   * Sets a new column name formatter.
   *
   * @param formatter
   *          a new formatter.
   */
  public void setFormatter(SqlHelper.Formatter formatter) {
    this.formatter = formatter;
  }

  /**
   * Sets a new keyword provider.
   *
   * @param keyWordProvider
   *          a new keyword provider.
   */
  public void setKeyWordProvider(SqlHelper.KeyWordProvider keyWordProvider) {
    this.keyWordProvider = keyWordProvider;
  }

  /**
   * Infer a SQL column name from a property name. By default: - use camel
   * parser to separate words with "_" - check for sql reserved key words
   * {@inheritDoc}
   */
  @SuppressWarnings("rawtypes")
  @Override
  public TemplateModel exec(List arguments) throws TemplateModelException {

    String root = arguments.get(0).toString();
    String suffix = null;
    if (arguments.size() > 1 && arguments.get(1) != null) {
      suffix = arguments.get(1).toString();
    }

    String sqlColumnName = new SqlHelper(formatter, keyWordProvider).transformToSql(root, suffix);
    try {
      return new SimpleScalar(sqlColumnName);
    } catch (Exception ex) {
      throw new TemplateModelException("Could not infer SQL column name.", ex);
    }
  }
}
