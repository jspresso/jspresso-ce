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
package org.jspresso.framework.util.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper class for string operations.
 *
 * @author Vincent Vandenschrick
 */
public final class StringUtils {

  /**
   * UTF-8 BOM.
   */
  // FEFF because this is the Unicode char represented by the UTF-8 byte order
  // mark (EF BB BF).
  public static final String UTF8_BOM = "\uFEFF";

  private StringUtils() {
    // Helper class constructor
  }

  /**
   * Ensures that all strings in the source collection are whitespace free.
   *
   * @param sourceCollection
   *          the list of strings to check.
   * @return the set of whitespace free strings.
   */
  public static Collection<String> ensureSpaceFree(
      Collection<String> sourceCollection) {
    if (sourceCollection == null) {
      return null;
    }
    Collection<String> result;
    if (sourceCollection instanceof Set<?>) {
      result = ensureSpaceFree((Set<String>) sourceCollection);
    } else if (sourceCollection instanceof List<?>) {
      result = ensureSpaceFree((List<String>) sourceCollection);
    } else {
      result = sourceCollection;
      for (String source : new ArrayList<>(sourceCollection)) {
        result.remove(source);
        result.add(ensureSpaceFree(source));
      }
    }
    return result;
  }

  /**
   * Ensures that all strings in the source list are whitespace free.
   *
   * @param sourceList
   *          the list of strings to check.
   * @return the list of whitespace free strings.
   */
  public static List<String> ensureSpaceFree(List<String> sourceList) {
    if (sourceList == null) {
      return null;
    }
    List<String> result = new ArrayList<>(sourceList.size());
    for (String source : sourceList) {
      result.add(ensureSpaceFree(source));
    }
    return result;
  }

  /**
   * Ensures that all strings in the source list are whitespace free.
   *
   * @param sourceMap
   *          the map of strings to check.
   * @return the list of whitespace free strings.
   */
  public static Map<String, String> ensureSpaceFree(
      Map<String, String> sourceMap) {
    if (sourceMap == null) {
      return null;
    }
    Map<String, String> result = new LinkedHashMap<>(
        sourceMap.size());
    for (Map.Entry<String, String> sourceEntry : sourceMap.entrySet()) {
      result.put(ensureSpaceFree(sourceEntry.getKey()),
          ensureSpaceFree(sourceEntry.getValue()));
    }
    return result;
  }

  /**
   * Ensures that all strings in the source set are whitespace free.
   *
   * @param sourceSet
   *          the list of strings to check.
   * @return the set of whitespace free strings.
   */
  public static Set<String> ensureSpaceFree(Set<String> sourceSet) {
    if (sourceSet == null) {
      return null;
    }
    Set<String> result = new LinkedHashSet<>(sourceSet.size());
    for (String source : sourceSet) {
      result.add(ensureSpaceFree(source));
    }
    return result;
  }

  /**
   * Eliminates any whitespace character from the source string. This is useful
   * to ensure that any XML formatting does not break class names for instance.
   *
   * @param source
   *          the source string.
   * @return the whitespace free string.
   */
  public static String ensureSpaceFree(String source) {
    if (source == null) {
      return null;
    }
    return source.replaceAll("\\s*", "");
  }

  /**
   * Prepends the UTF-8 bom to a source string.
   *
   * @param source
   *          the source to prepend the BOM to.
   * @return the bom-ed string.
   */
  public static String prependUtf8Bom(String source) {
    return UTF8_BOM + source;
  }
}
