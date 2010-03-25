/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Infers a SQL column name from a property name.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @author Pierre-m Raoul (atao)
 */
public class GenerateSqlName implements TemplateMethodModel {

  private static final String          WORD_SEP = "_";
  private static final KeyWordProvider DEFAULT_KEY_WORD_PROVIDER;
  private static final Formatter       DEFAULT_FORMATTER;
  private Set<String>                  reservedKeyWords;
  private Formatter                    formatter;

  static {
    DEFAULT_FORMATTER = new Formatter() {

      public String run(String name) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < name.length(); i++) {
          if (i > 0 && Character.isLowerCase(name.charAt(i - 1))
              && Character.isUpperCase(name.charAt(i))) {
            result.append(WORD_SEP);
          }
          result.append(Character.toUpperCase(name.charAt(i)));
        }
        return result.toString();
      }
    };

    DEFAULT_KEY_WORD_PROVIDER = new KeyWordProvider() {

      public List<String> run() {
        return Arrays.asList(new String[] {"BEGIN", "END", "GROUP", "FUNCTION",
      "ACTION", "ARRAY", "DATE", "DATA", "DAY", "MONTH", "YEAR"});
      }
    };
  }

  /**
   * Constructs a new <code>GenerateSqlName</code> instance.
   */
  public GenerateSqlName() {
    reservedKeyWords = new HashSet<String>(DEFAULT_KEY_WORD_PROVIDER.run());
    formatter = DEFAULT_FORMATTER;
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

  /**
   * Infer a SQL column name from a property name. By default: - use camel
   * parser to separate words with "_" - check for sql reserved key words
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public TemplateModel exec(List arguments) throws TemplateModelException {

    String sqlColumnName = formatter.run(arguments.get(0).toString());
    if (isReserved(sqlColumnName)) {
      sqlColumnName += WORD_SEP;
      if (arguments.size() > 1 && arguments.get(1) != null) {
        sqlColumnName += formatter.run(arguments.get(1).toString());
      }
    }
    try {
      return new SimpleScalar(sqlColumnName);
    } catch (Exception ex) {
      throw new TemplateModelException("Could not infer SQL column name.", ex);
    }
  }

  private boolean isReserved(String name) {
    return reservedKeyWords.contains(name.toUpperCase());
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
    reservedKeyWords = new HashSet<String>(currentProvider.run());
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

}
