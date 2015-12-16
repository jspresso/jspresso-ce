/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.security;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.jspresso.framework.action.ActionBusinessException;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.security.UserPrincipal;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Concrete backend implementation of a change password action where password is
 * stored in a relational database.
 * 
 * @author Vincent Vandenschrick
 */
public class DatabaseChangePasswordAction extends AbstractChangePasswordAction {

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
   * <li><b>&quot;current password&quot;</b> potentially hashed.</li>
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
  protected boolean changePassword(UserPrincipal userPrincipal,
      String currentPassword, String newPassword) {
    try {
      String newPassHash = "";
      if (newPassword != null) {
        newPassHash = digestAndEncode(newPassword.toCharArray());
      }
      String currentPassHash = "";
      if (currentPassword != null) {
        currentPassHash = digestAndEncode(currentPassword.toCharArray());
      }

      int updCount = getJdbcTemplate().update(getUpdateQuery(), newPassHash, userPrincipal.getName(), currentPassHash);
      if (updCount == 0) {
        throw new ActionBusinessException("Current password is not valid.",
            "password.current.invalid");
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
