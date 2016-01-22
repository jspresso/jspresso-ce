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
package org.jspresso.framework.qooxdoo.rpc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Refines the way Qooxdoo rpc handles JSON types.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteCallUtils extends net.sf.qooxdoo.rpc.RemoteCallUtils {

  private final static ThreadLocal<BidiMap<String, String>> CODEC = new ThreadLocal<>();

  /**
   * Handles Lists.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public Object fromJava(Object obj)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, JSONException {
    BidiMap<String, String> codec = CODEC.get();
    boolean initialCall = false;
    if (codec == null) {
      initialCall = true;
      codec = new DualHashBidiMap<>();
      CODEC.set(codec);
    }
    try {
      Object returnValue;
      if (obj instanceof List<?>) {
        List<?> list = (List<?>) obj;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(encode("class"), encode("qx.data.Array"));
        jsonObject.put(encode("array"), fromJava(list.toArray()));
        returnValue = jsonObject;
      } else if (obj instanceof BigDecimal) {
        returnValue = super.fromJava(((BigDecimal) obj).doubleValue());
      } else if (obj instanceof BigInteger) {
        returnValue = super.fromJava(((BigInteger) obj).longValue());
      } else {
        returnValue = super.fromJava(obj);
      }
      if (initialCall) {
        JSONObject wrapper = new JSONObject();
        wrapper.put("codec", new JSONObject(codec));
        wrapper.put("payload", returnValue);
        returnValue = wrapper;
      }
      return returnValue;
    } finally {
      if (initialCall) {
        CODEC.remove();
      }
    }
  }

  /**
   * Handles qx.data.Array <-> lists. Handles polymorphism in arrays.
   * <p/>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object toJava(Object obj, @SuppressWarnings("rawtypes") Class targetType) {
    if (obj instanceof JSONObject) {
      JSONObject jsonObject = (JSONObject) obj;
      String requestedTypeName = jsonObject.optString("class", null);
      if (requestedTypeName != null) {
        if ("qx.data.Array".equals(requestedTypeName)) {
          JSONArray jsonArray = jsonObject.optJSONArray("array");
          if (jsonArray != null) {
            Object array = toJava(jsonArray, Object[].class);
            if (array instanceof Object[]) {
              List<Object> returnedList = new ArrayList<>();
              Collections.addAll(returnedList, ((Object[]) array));
              return returnedList;
            }
          }
        } else {
          Class<?> clazz = null;
          try {
            clazz = resolveClassHint(requestedTypeName, targetType);
          } catch (Throwable t) {
            // NO-OP
          }
          if (clazz != null && targetType.isAssignableFrom(clazz)) {
            return super.toJava(obj, clazz);
          }
        }
      }
    }
    return super.toJava(obj, targetType);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  protected Map filter(Object obj, Map map) {
    Map<String, Object> filteredMap = super.filter(obj, map);
    filteredMap.put("class", encode(obj.getClass().getName()));
    // Prevents recursion on JSON serialization
    filteredMap.remove("parent");
    Map<String, Object> encodedMap = new HashMap();
    for (Map.Entry<String, Object> entry : filteredMap.entrySet()) {
      encodedMap.put(encode(entry.getKey()), entry.getValue());
    }
    return encodedMap;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Class<?> resolveClassHint(String requestedTypeName, @SuppressWarnings("rawtypes") Class targetType)
      throws Exception {
    Class<?> clazz = super.resolveClassHint(requestedTypeName, targetType);
    if (clazz == null) {
      return Class.forName(requestedTypeName);
    }
    return clazz;
  }

  /**
   * Make the Rpc java lib more permissive, i.e. do not impose any signature
   * constraint on public method.
   * <p/>
   * {@inheritDoc}
   */

  @Override
  protected boolean throwsExpectedException(Method method) {
    // Removes the exception constraints on method signature.
    return true;
  }

  /**
   * Encode string.
   *
   * @param original
   *     the original
   * @return the string
   */
  protected String encode(String original) {
    BidiMap<String, String> codec = CODEC.get();
    String key = codec.getKey(original);
    if (key == null) {
      key = Integer.toHexString(codec.size());
      codec.put(key, original);
    }
    return key;
  }
}
