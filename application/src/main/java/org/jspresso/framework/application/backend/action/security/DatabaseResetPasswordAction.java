/*
 * Copyright (c) 2005-2017 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.action.security;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.jdbc.core.JdbcTemplate;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * Concrete backend implementation of a reset password action where password is
 * stored in a relational database.
 *
 * @author Vincent Vandenschrick
 */
public class DatabaseResetPasswordAction extends AbstractResetPasswordAction {

  private JdbcTemplate jdbcTemplate;
  private String       updateQuery;

  /**
   * Configures the Spring jdbcTemplate to use to issue the update statement.
   *
   * @param jdbcTemplate
   *          the jdbcTemplate to set.
   */
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Configures the update query to execute to change the password. The prepared
   * statement parameters that will be bound are, in order :
   * <ol>
   * <li><b>&quot;new password&quot;</b> potentially hashed.</li>
   * <li><b>&quot;user name&quot;</b>.</li>
   * </ol>
   *
   * @param updateQuery
   *          the updateQuery to set.
   */
  public void setUpdateQuery(String updateQuery) {
    this.updateQuery = updateQuery;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean changePassword(String username, String generatedPassword) {
    try {
      String generatedPassHash = "";
      if (generatedPassword != null) {
        generatedPassHash = digestAndEncode(generatedPassword.toCharArray());
      }

      int updCount = getJdbcTemplate().update(getUpdateQuery(), generatedPassHash, username);
      if (updCount == 0) {
        throw new ActionException("Could not reset password for user " + username);
      }
    } catch (NoSuchAlgorithmException | IOException ex) {
      throw new ActionException(ex);
    }
    return true;
  }

  /**
   * Gets the jdbcTemplate.
   *
   * @return the jdbcTemplate.
   */
  protected JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  /**
   * Gets the updateQuery.
   *
   * @return the updateQuery.
   */
  protected String getUpdateQuery() {
    return updateQuery;
  }
}
