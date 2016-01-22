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
package org.jspresso.framework.model.persistence.hibernate.dialect;

import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Customized Oracle 10g dialect.
 *
 * @author Vincent Vandenschrick
 */
public class Oracle10gDialect extends org.hibernate.dialect.Oracle10gDialect {

  // /**
  // * Maps dates differently.
  // * <p>
  // * {@inheritDoc}
  // */
  // @Override
  // protected void registerDateTimeTypeMappings() {
  // registerColumnType(Types.DATE, "date");
  // registerColumnType(Types.TIME, "date");
  // registerColumnType(Types.TIMESTAMP, "date");
  // }

  /**
   * Maps integers differently.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void registerNumericTypeMappings() {
    super.registerNumericTypeMappings();
    registerColumnType(Types.INTEGER, "number($p,0)");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void registerCharacterTypeMappings() {
    super.registerCharacterTypeMappings();
    registerColumnType(Types.VARCHAR, "clob");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void registerLargeObjectTypeMappings() {
    super.registerLargeObjectTypeMappings();
    registerColumnType(Types.VARBINARY, "blob");
  }

  /**
   * Eliminates duplicate aliases.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String transformSelectString(String select) {
    return eliminateDuplicateColumnAliases(select);
  }

  private String eliminateDuplicateColumnAliases(String sql) {
    Set<String> usedAliases = new HashSet<>();
    String[] aliasesSplit = sql.split(" as ");
    StringBuilder buff = new StringBuilder(aliasesSplit[0]);
    if (aliasesSplit.length > 1) {
      for (int i = 1; i < aliasesSplit.length; i++) {
        StringTokenizer aliasTokenizer = new StringTokenizer(aliasesSplit[i],
            ", ", true);
        String alias = aliasTokenizer.nextToken();
        int offset = aliasesSplit[i].indexOf(alias) + alias.length();
        while (usedAliases.contains(alias)) {
          alias = alias.substring(0, alias.length() - 2)
              + RandomStringUtils.randomAlphanumeric(1).toLowerCase() + "_";
        }
        usedAliases.add(alias);
        buff.append(" as ");
        buff.append(alias);
        buff.append(aliasesSplit[i].substring(offset));
      }
    }
    return buff.toString();
  }
}
