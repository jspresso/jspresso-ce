/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.jspresso.framework.action.ActionBusinessException;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.security.UserPrincipal;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Changes a user password in a database table.
 * 
 * @version $LastChangedRevision: 2097 $
 * @author Vincent Vandenschrick
 */
public class DatabaseChangePasswordAction extends AbstractChangePasswordAction {

  private JdbcTemplate jdbcTemplate;
  private String       updateQuery;

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean changePassword(UserPrincipal userPrincipal,
      String currentPassword, String newPassword) {
    try {
      String newPassHash = digestAndEncode(newPassword.toCharArray());
      String currentPassHash = digestAndEncode(currentPassword.toCharArray());

      int updCount = getJdbcTemplate().update(getUpdateQuery(),
          new Object[] {newPassHash, userPrincipal.getName(), currentPassHash});
      if (updCount == 0) {
        throw new ActionBusinessException("Current password is not valid.",
            "password.current.invalid");
      }
    } catch (NoSuchAlgorithmException ex) {
      throw new ActionException(ex);
    } catch (UnsupportedEncodingException ex) {
      throw new ActionException(ex);
    } catch (IOException ex) {
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
   * Sets the jdbcTemplate.
   * 
   * @param jdbcTemplate
   *          the jdbcTemplate to set.
   */
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Gets the updateQuery.
   * 
   * @return the updateQuery.
   */
  protected String getUpdateQuery() {
    return updateQuery;
  }

  /**
   * Sets the updateQuery.
   * 
   * @param updateQuery
   *          the updateQuery to set.
   */
  public void setUpdateQuery(String updateQuery) {
    this.updateQuery = updateQuery;
  }
}
