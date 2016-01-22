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
package org.jspresso.framework.util.sql;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple helper class for formatting SQL names.
 *
 * @author Vincent Vandenschrick
 */
public final class SqlHelper {

  private static final Formatter       DEFAULT_FORMATTER;
  private static final KeyWordProvider DEFAULT_KEY_WORD_PROVIDER;
  private static final String          WORD_SEP = "_";

  static {
    DEFAULT_FORMATTER = new Formatter() {

      @Override
      public String run(String name) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
          if (i > 0 && Character.isLowerCase(name.charAt(i - 1))
              && Character.isUpperCase(name.charAt(i))) {
            result.append(WORD_SEP);
          }
          if ((i == 0 && Character.isJavaIdentifierStart(name.charAt(i)))
              || (i > 0 && Character.isJavaIdentifierPart(name.charAt(i)))) {
            result.append(Character.toUpperCase(name.charAt(i)));
          } else {
            result.append(WORD_SEP);
          }
        }
        return result.toString();
      }
    };

    DEFAULT_KEY_WORD_PROVIDER = new KeyWordProvider() {

      @Override
      public List<String> run() {
        return Arrays.asList("BEGIN", "END", "GROUP", "FUNCTION", "ACTION", "ARRAY", "DATE", "DATA", "DAY", "MONTH",
            "YEAR", "FROM", "TO", "USER");
      }
    };
  }

  private Formatter   formatter        = DEFAULT_FORMATTER;
  private Set<String> reservedKeyWords = new HashSet<>(DEFAULT_KEY_WORD_PROVIDER.run());

  /**
   * Constructs a new {@code SqlHelper} instance.
   *
   */
  public SqlHelper() {
    this(null, null);
  }

  /**
   * Constructs a new {@code SqlHelper} instance.
   *
   * @param formatter the formatter
   * @param keyWordProvider the key word provider
   */
  public SqlHelper(Formatter formatter, KeyWordProvider keyWordProvider) {
    if (formatter != null) {
      this.formatter = formatter;
    }
    if (keyWordProvider != null) {
      this.reservedKeyWords = new HashSet<>(keyWordProvider.run());
    }
  }

  /**
   * Transform to sql.
   *
   * @param camelCaseRoot the camel case root
   * @param camelCaseSuffix the camel case suffix
   * @return the SQL string
   */
  public String transformToSql(String camelCaseRoot, String camelCaseSuffix) {
    String sqlColumnName = formatter.run(camelCaseRoot);
    if (isReserved(sqlColumnName)) {
      sqlColumnName += WORD_SEP;
      if (camelCaseSuffix != null) {
        sqlColumnName += formatter.run(camelCaseSuffix);
      }
    }
    return sqlColumnName;
  }

  /**
   * Sets an additional keyword provider.
   *
   * @param provider
   *          an additional keyword provider.
   */
  public void setAdditionalKeyWordProvider(KeyWordProvider provider) {
    if (provider == null) {
      return;
    }

    reservedKeyWords.addAll(provider.run());
  }

  /**
   * Sets a new column name formatter. If null, the default one is restored.
   *
   * @param formatter
   *          a new formatter.
   */
  public void setFormatter(Formatter formatter) {
    if (formatter != null) {
      this.formatter = formatter;
    } else {
      this.formatter = DEFAULT_FORMATTER;
    }
  }

  /**
   * Sets a new keyword provider. If null, the default one is restored.
   *
   * @param provider
   *          a new keyword provider.
   */
  public void setKeyWordProvider(KeyWordProvider provider) {
    KeyWordProvider currentProvider;
    if (provider != null) {
      currentProvider = provider;
    } else {
      currentProvider = DEFAULT_KEY_WORD_PROVIDER;
    }
    reservedKeyWords = new HashSet<>(currentProvider.run());
  }

  private boolean isReserved(String name) {
    return reservedKeyWords.contains(name.toUpperCase());
  }

  /**
   * Any modification of the provided word before checking, e.g. from camel
   * style to upper case and underscore separated.
   */
  public interface Formatter {

    /**
     * Executes the formatter on the given name.
     *
     * @param name
     *          the name to format
     * @return the formatted name.
     */
    String run(String name);
  }

  /**
   * Provide upper case reserved key words.
   */
  public interface KeyWordProvider {

    /**
     * Gets the list of reserved keywords uppercase.
     *
     * @return the list of reserved keywords uppercase.
     */
    List<String> run();
  }
}
