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
package org.jspresso.framework.model.persistence.hibernate.entity.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.util.uid.ByteArray;

/**
 * A user type to be able to use byte arrays as entity identifiers.
 *
 * @author Vincent Vandenschrick
 */
public class ByteArrayType implements UserType {


  private static final Logger LOG = LoggerFactory.getLogger("org.hibernate.type");

  /**
   * {@inheritDoc}
   */
  @Override
  public int[] sqlTypes() {
    return new int[]{Types.VARBINARY};
  }

  /**
   * Returns false.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isMutable() {
    return false;
  }

  /**
   * Returns {@code Bytes.class}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Class<?> returnedClass() {
    return ByteArray.class;
  }

  /**
   * Simply compares the underlying Bytes.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object x, Object y) {
    return (x == y) || (x != null && y != null && x.equals(y));
  }

  /**
   * Returns shallow copy.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Object deepCopy(Object value) {
    return value;
  }

  /**
   * Constructs a Bytes object out of a byte array.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
      throws SQLException {
    byte[] bytes = rs.getBytes(names[0]);
    if (rs.wasNull()) {
      if (LOG.isTraceEnabled()) {
        LOG.trace("Found [null] as column [{0}]", names);
      }
      return null;
    }
    ByteArray value = new ByteArray(bytes);
    if (LOG.isTraceEnabled()) {
      LOG.trace("Found [{}] as column [{0}]", value, names);
    }
    return value;
  }

  /**
   * Constructs a byte array out of a Bytes object.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
      throws SQLException {
    if (value == null) {
      if (LOG.isTraceEnabled()) {
        LOG.trace("binding parameter [{}] - <null>", index);
      }
      st.setNull(index, Types.VARBINARY);
    } else {
      if (LOG.isTraceEnabled()) {
        LOG.trace("binding parameter [{}] - {}", index, value);
      }
      st.setBytes(index, ((ByteArray) value).getBytes());
    }
  }

  /**
   * Uses Bytes hashcode.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int hashCode(Object x) {
    return x.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Serializable disassemble(Object value) {
    return (Serializable) value;
  }

  /**
   * {@inheritDoc}
   */

  @Override
  public Object assemble(Serializable cached, Object owner) {
    return cached;
  }

  /**
   * {@inheritDoc}
   */

  @Override
  public Object replace(Object original, Object target, Object owner) {
    return original;
  }

}
