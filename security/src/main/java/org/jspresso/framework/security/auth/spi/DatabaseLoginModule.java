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
package org.jspresso.framework.security.auth.spi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;

import org.jboss.security.auth.spi.DatabaseServerLoginModule;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * An extended database login module to complement the user principal with
 * custom values.
 *
 * @author Vincent Vandenschrick
 */
public class DatabaseLoginModule extends DatabaseServerLoginModule {

  /**
   * Complete identity and feed shared context.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings({"ConstantConditions", "unchecked"})
  @Override
  public boolean login() throws LoginException {
    if (super.login() && getIdentity() instanceof UserPrincipal) {
      String username = getUsername();
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(dsJndiName);
        conn = ds.getConnection();
        // prepare the query
        ps = conn.prepareStatement(principalsQuery);
        ps.setString(1, username);
        rs = ps.executeQuery();
        if (rs.next()) {
          ResultSetMetaData rsmd = rs.getMetaData();
          for (int i = 2; i < rsmd.getColumnCount() + 1; i++) {
            String customKey = rsmd.getColumnLabel(i);
            Object customValue = rs.getObject(i);
            ((UserPrincipal) getIdentity()).putCustomProperty(
                customKey.toLowerCase(), customValue);
          }
        }
      } catch (Throwable ex) {
        // This should never happen
        throw new NestedRuntimeException(ex);
      } finally {
        if (rs != null) {
          try {
            rs.close();
          } catch (SQLException ex) {
            // Can't do much here
          }
        }
        if (ps != null) {
          try {
            ps.close();
          } catch (SQLException ex) {
            // Can't do much here
          }
        }
        if (conn != null) {
          try {
            conn.close();
          } catch (SQLException ex) {
            // Can't do much here
          }
        }
      }
      // Fixes bug #1175
      if (sharedState.get("javax.security.auth.login.name") instanceof String) {
        sharedState.put("javax.security.auth.login.name", getIdentity());
      }
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String createPasswordHash(String username, String password,
      String digestOption) throws LoginException {
    if (password == null) {
      return null;
    }
    if (password.length() == 0) {
      return password;
    }
    return super.createPasswordHash(username, password, digestOption);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean validatePassword(String inputPassword,
      String expectedPassword) {
    if (inputPassword != null && inputPassword.length() == 0) {
      return true;
    }
    if (inputPassword == null && expectedPassword == null) {
      return true;
    }
    return super.validatePassword(inputPassword, expectedPassword);
  }
}
