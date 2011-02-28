/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.preferences;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * A JDBC based preference store.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JdbcPreferenceStore implements IPreferencesStore {

  private String              tableName;
  private String              keyColumnName;
  private String              valueColumnName;
  private Map<String, String> defaultRestrictions;
  private JdbcTemplate        jdbcTemplate;

  private Map<String, String> preferences;

  /**
   * {@inheritDoc}
   */
  public String getPreference(String key) {
    if (jdbcTemplate == null) {
      return null;
    }
    initIfNecessary();
    return preferences.get(key);
  }

  /**
   * {@inheritDoc}
   */
  public void putPreference(String key, String value) {
    if (jdbcTemplate == null) {
      return;
    }
    initIfNecessary();
    String existing = preferences.put(key, value);
    StringBuffer sql = new StringBuffer();
    if (existing != null) {
      sql.append("UPDATE ").append(getTableName()).append(" SET ")
          .append(getValueColumnName()).append(" = ? WHERE ");
    } else {
      sql.append("INSERT INTO ").append(getTableName());
    }
  }

  /**
   * {@inheritDoc}
   */
  public void removePreference(String key) {
    if (jdbcTemplate == null) {
      return;
    }
    initIfNecessary();
    String existing = preferences.remove(key);
    if (existing != null) {
      StringBuffer sql = new StringBuffer("DELETE FROM ")
          .append(getTableName()).append(" WHERE ").append(getKeyColumnName())
          .append(" = ").append(" ?");
      String[] restrictionsValues;
      int[] restrictionsTypes;
      if (defaultRestrictions != null && defaultRestrictions.size() > 0) {
        restrictionsValues = new String[defaultRestrictions.size() + 1];
        restrictionsTypes = new int[defaultRestrictions.size() + 1];
        int i = 1;
        for (Map.Entry<String, String> restriction : defaultRestrictions
            .entrySet()) {
          restrictionsValues[i] = restriction.getValue();
          restrictionsTypes[i] = Types.VARCHAR;
          sql.append(" AND ").append(restriction.getKey()).append(" = ?");
          i++;
        }
      } else {
        restrictionsValues = new String[1];
        restrictionsTypes = new int[1];
      }
      restrictionsValues[0] = key;
      restrictionsTypes[0] = Types.VARCHAR;
      jdbcTemplate
          .update(sql.toString(), restrictionsValues, restrictionsTypes);
    }
  }

  private void initIfNecessary() {
    if (preferences == null) {
      preferences = new HashMap<String, String>();

      StringBuffer sql = new StringBuffer("SELECT ").append(getTableName())
          .append(".").append(getKeyColumnName()).append(", ")
          .append(getValueColumnName()).append(" FROM ").append(getTableName());
      String[] restrictionsValues;
      int[] restrictionsTypes;
      if (defaultRestrictions != null && defaultRestrictions.size() > 0) {
        sql.append(" WHERE ");
        restrictionsValues = new String[defaultRestrictions.size()];
        restrictionsTypes = new int[defaultRestrictions.size()];
        int i = 0;
        for (Map.Entry<String, String> restriction : defaultRestrictions
            .entrySet()) {
          restrictionsValues[i] = restriction.getValue();
          restrictionsTypes[i] = Types.VARCHAR;
          if (i > 0) {
            sql.append(" AND ");
          }
          sql.append(restriction.getKey()).append(" = ?");
          i++;
        }
      } else {
        restrictionsValues = new String[0];
        restrictionsTypes = new int[0];
      }
      jdbcTemplate.query(sql.toString(), restrictionsValues, restrictionsTypes,
          new RowCallbackHandler() {

            public void processRow(ResultSet rs) throws SQLException {
              preferences.put(rs.getString(1), rs.getString(2));
            }
          });
    }
  }

  /**
   * Configures the datasource to which the JdbcPreferenceStore is persisted.
   * 
   * @param dataSource
   *          the datasource to which the JdbcPreferenceStore is persisted.
   */
  public void setDataSource(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  /**
   * Gets the tableName.
   * 
   * @return the tableName.
   */
  public String getTableName() {
    if (keyColumnName == null) {
      keyColumnName = "PREFERENCES";
    }
    return tableName;
  }

  /**
   * Sets the tableName.
   * 
   * @param tableName
   *          the tableName to set.
   */
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /**
   * Gets the defaultRestrictions.
   * 
   * @return the defaultRestrictions.
   */
  public Map<String, String> getDefaultRestrictions() {
    return defaultRestrictions;
  }

  /**
   * Sets the defaultRestrictions. The default restrictions contrains arbitrary
   * string key value pairs that are added to every SQL where clauses.
   * 
   * @param defaultRestrictions
   *          the defaultRestrictions to set.
   */
  public void setDefaultRestrictions(Map<String, String> defaultRestrictions) {
    this.defaultRestrictions = defaultRestrictions;
  }

  /**
   * Gets the prefKeyColumnName.
   * 
   * @return the prefKeyColumnName.
   */
  protected String getKeyColumnName() {
    if (keyColumnName == null) {
      keyColumnName = "PREFERENCE_KEY";
    }
    return keyColumnName;
  }

  /**
   * Sets the prefKeyColumnName.
   * 
   * @param prefKeyColumnName
   *          the prefKeyColumnName to set.
   */
  public void setKeyColumnName(String prefKeyColumnName) {
    this.keyColumnName = prefKeyColumnName;
  }

  /**
   * Gets the prefValueColumnName.
   * 
   * @return the prefValueColumnName.
   */
  protected String getValueColumnName() {
    if (keyColumnName == null) {
      keyColumnName = "PREFERENCE_VALUE";
    }
    return valueColumnName;
  }

  /**
   * Sets the prefValueColumnName.
   * 
   * @param prefValueColumnName
   *          the prefValueColumnName to set.
   */
  public void setValueColumnName(String prefValueColumnName) {
    this.valueColumnName = prefValueColumnName;
  }

}
