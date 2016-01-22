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
package org.jspresso.framework.binding.model;

import java.util.regex.Pattern;

/**
 * This gate opens and closes based on the value of a string property matching a
 * regular expression.
 *
 * @author Vincent Vandenschrick
 */
public class RegexPropertyModelGate extends AbstractPropertyModelGate<String> {

  private String regexpPattern;

  /**
   * Configures the regular expression to match the property value against. The
   * gate will open if the property value matches the regex unless the
   * {@code openOnTrue} property has been set to false.
   *
   * @param regexpPattern
   *          the regexpPattern to set.
   */
  public void setRegexpPattern(String regexpPattern) {
    this.regexpPattern = regexpPattern;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean shouldOpen(String propertyValue) {
    return propertyValue != null && regexpPattern != null
        && Pattern.matches(regexpPattern, propertyValue);
  }
}
