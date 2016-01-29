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
package org.jspresso.framework.model.component.query;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;

import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.util.io.SerializationUtil;

/**
 * Helper class to manage {@code IQueryComponent}
 * serialization and deserialization.
 *
 * @author Maxime
 */
public abstract class QueryComponentSerializationUtil {

  /**
   * Serialize a query component content to a Base64 form.
   *
   * @param query component to serialize
   * @param complement additional map of string to serialize
   * @return the string representation of the query component
   * @throws IOException the iO exception
   */
  public static String serializeFilter(IQueryComponent query,
      LinkedHashMap<String, Serializable> complement)
  throws IOException {

    // temporary map (too heavy to be serialized, will use a simple table)
    LinkedHashMap<String, Serializable> map = new LinkedHashMap<>();
    for (String key : query.keySet()) {
      Object value = query.get(key);
      
      // ignore empty comparable query structures
      if (value instanceof ComparableQueryStructure) {
        ComparableQueryStructure cqs = (ComparableQueryStructure)value;
        if (cqs.getInfValue() ==null && cqs.getSupValue()==null)
          continue;
      }
      
      if (value instanceof QueryComponent) {
        QueryComponent qc = (QueryComponent) value;
        Serializable[] delegate = componentToTable(qc);
        if (delegate != null) {
          map.put(key, delegate);
        }
      }
      else if (value instanceof EnumQueryStructure) {
        Serializable delegate = queryStructureToString((EnumQueryStructure) value);
        if (delegate != null) {
          map.put(key, delegate);
        }
      }
      else if (value == null || value instanceof Serializable) {
        map.put(key, (Serializable) value);
      }
    }

    // manage additional fields
    for (String k : complement.keySet())
      map.put(k, complement.get(k));

    // prepare non heavy table
    Serializable[] simple = new Serializable[map.keySet().size() * 2];
    int i = 0;
    for (String key : map.keySet()) {
      simple[i++] = key;
      simple[i++] = map.get(key);
    }

    byte[] data = SerializationUtil.serialize(simple, true);

    return SerializationUtil.toBase64String(data); // data.length
  }

  private static Serializable[] componentToTable(QueryComponent query) {
    if (query.size() == 0) {
      return null;
    }
    Serializable[] delegate = new Serializable[query.size() * 2];
    int i = 0;
    for (String k : query.keySet()) {
      delegate[i++] = k;
      Object v = query.get(k);
      if (v == null) {
        delegate[i++] = null;
      }
      else if (v instanceof QueryComponent) {
        delegate[i++] = componentToTable((QueryComponent) query.get(k));
      }
      else if (v instanceof EnumQueryStructure) {
        delegate[i++] = queryStructureToString((EnumQueryStructure) v);
      }
      else if (v instanceof Serializable) {
        delegate[i++] = (Serializable) query.get(k);
      }
    }
    return delegate;
  }

  private static String queryStructureToString(EnumQueryStructure value) {
    Set<EnumValueQueryStructure> selectedValues = value.getSelectedEnumerationValues();
    if (selectedValues.isEmpty())
      return null;

    StringBuilder sb = new StringBuilder("[[");
    for (EnumValueQueryStructure ev : selectedValues) {
      sb.append(ev.getValue());
      sb.append("§");
    }
    sb.append("]]");
    return sb.toString();
  }


  /**
   * Deserialize a base 64 representation of a query component
   * representation and hydrate the given query component instance.
   *
   * @param filterBase64 the base 64 representation of a query component.
   * @return the serializable [ ]
   * @throws IOException the iO exception
   * @throws ClassNotFoundException the class not found exception
   */
  public static Serializable[] deserializeFilter(String filterBase64) throws IOException, ClassNotFoundException {

    Serializable[] filters =
        (Serializable[]) SerializationUtil.deserializeFromBase64(filterBase64, true);

    return filters;
  }


}
