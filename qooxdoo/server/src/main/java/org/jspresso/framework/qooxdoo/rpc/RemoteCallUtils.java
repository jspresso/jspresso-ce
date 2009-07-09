/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.qooxdoo.rpc;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Refines the way Qooxdoo rpc handles JSON types.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteCallUtils extends net.sf.qooxdoo.rpc.RemoteCallUtils {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected Class resolveClassHint(String requestedTypeName, Class targetType)
      throws Exception {
    Class clazz = super.resolveClassHint(requestedTypeName, targetType);
    if (clazz == null) {
      return Class.forName(requestedTypeName);
    }
    return clazz;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected Map filter(Object obj, Map map) {
    Map filteredMap = super.filter(obj, map);
    filteredMap.put("class", obj.getClass().getName());
    return filteredMap;
  }

  /**
   * Handles Lists.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Object fromJava(Object obj) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    if (obj instanceof List<?>) {
      List<?> list = (List<?>) obj;
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("class", "qx.data.Array");
      jsonObject.put("array", fromJava(list.toArray()));
      return jsonObject;
    } else if (obj instanceof BigDecimal) {
      return super.fromJava(new Double(((BigDecimal) obj).doubleValue()));
    } else if (obj instanceof BigInteger) {
      return super.fromJava(new Long(((BigInteger) obj).longValue()));
      // } else if (obj instanceof RemoteValueState) {
      // Object value = ((RemoteValueState) obj).getValue();
      // JSONObject jsonObject = (JSONObject) super.fromJava(obj);
      // if (value instanceof byte[]) {
      // jsonObject.put("value", RemotePeerRegistryServlet
      // .computeDownloadUrl(((RemoteValueState) obj).getGuid()));
      // }
      // return jsonObject;
      // } else if (obj instanceof RemoteValueCommand) {
      // Object value = ((RemoteValueCommand) obj).getValue();
      // JSONObject jsonObject = (JSONObject) super.fromJava(obj);
      // if (value instanceof byte[]) {
      // jsonObject
      // .put("value", RemotePeerRegistryServlet
      // .computeDownloadUrl(((RemoteValueCommand) obj)
      // .getTargetPeerGuid()));
      // }
      // return jsonObject;
    }
    return super.fromJava(obj);
  }

  /**
   * Handles qx.data.Array <-> lists. Handles polymorphism in arrays.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object toJava(Object obj, Class targetType) {
    if (obj instanceof JSONObject) {
      JSONObject jsonObject = (JSONObject) obj;
      String requestedTypeName = jsonObject.optString("class", null);
      if (requestedTypeName != null) {
        if ("qx.data.Array".equals(requestedTypeName)) {
          JSONArray jsonArray = jsonObject.optJSONArray("array");
          if (jsonArray != null) {
            Object array = toJava(jsonArray, Object[].class);
            if (array instanceof Object[]) {
              List<Object> returnedList = new ArrayList<Object>();
              for (Object o : ((Object[]) array)) {
                returnedList.add(o);
              }
              return returnedList;
            }
          }
        } else {
          Class clazz = null;
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
}
