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
 * @author Vincent Vandenschrick
 */
public class JdbcPreferencesStore implements IPreferencesStore {

  private String              storePath;
  private Map<String, String> preferences;
  private JdbcTemplate        jdbcTemplate;

  private String              tableName;
  private String              keyColumnName;
  private String              valueColumnName;
  private String              pathColumnName;
  private Map<String, String> defaultRestrictions;

  /**
   * Constructs a new {@code JdbcPreferenceStore} instance.
   */
  public JdbcPreferencesStore() {
    setStorePath(GLOBAL_STORE);
  }

  /**
   * Sets the path of this store.
   *
   * @param storePath
   *          the preferences store path.
   */
  @Override
  public void setStorePath(String... storePath) {
    if (storePath != null && storePath.length > 0) {
      StringBuilder buff = new StringBuilder();
      for (String aStorePath : storePath) {
        buff.append(aStorePath).append('.');
      }
      this.storePath = buff.toString();
    } else {
      this.storePath = GLOBAL_STORE;
    }
    preferences = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
  @Override
  public void putPreference(String key, String value) {
    if (jdbcTemplate == null) {
      return;
    }
    initIfNecessary();
    String existing = preferences.put(key, value);
    StringBuilder sql = new StringBuilder();
    String[] restrictionsColumns;
    String[] restrictionsValues;
    int[] restrictionsTypes;
    if (defaultRestrictions != null && defaultRestrictions.size() > 0) {
      restrictionsColumns = new String[defaultRestrictions.size() + 3];
      restrictionsValues = new String[defaultRestrictions.size() + 3];
      restrictionsTypes = new int[defaultRestrictions.size() + 3];
      int i = 3;
      for (Map.Entry<String, String> restriction : defaultRestrictions
          .entrySet()) {
        restrictionsColumns[i] = restriction.getKey();
        restrictionsValues[i] = restriction.getValue();
        restrictionsTypes[i] = Types.VARCHAR;
        i++;
      }
    } else {
      restrictionsColumns = new String[3];
      restrictionsValues = new String[3];
      restrictionsTypes = new int[3];
    }

    restrictionsColumns[0] = getValueColumnName();
    restrictionsValues[0] = value;
    restrictionsTypes[0] = Types.VARCHAR;
    restrictionsColumns[1] = getKeyColumnName();
    restrictionsValues[1] = key;
    restrictionsTypes[1] = Types.VARCHAR;
    restrictionsColumns[2] = getPathColumnName();
    restrictionsValues[2] = storePath;
    restrictionsTypes[2] = Types.VARCHAR;

    if (existing != null) {
      sql.append("UPDATE ").append(getTableName()).append(" SET ")
          .append(getValueColumnName()).append(" = ? WHERE ");
      for (int i = 1; i < restrictionsColumns.length; i++) {
        if (i > 1) {
          sql.append(" AND ");
        }
        sql.append(restrictionsColumns[i]).append(" = ?");
      }
    } else {
      sql.append("INSERT INTO ").append(getTableName()).append(" (");
      for (int i = 0; i < restrictionsColumns.length; i++) {
        if (i > 0) {
          sql.append(", ");
        }
        sql.append(restrictionsColumns[i]);
      }
      sql.append(") VALUES (");
      for (int i = 0; i < restrictionsColumns.length; i++) {
        if (i > 0) {
          sql.append(", ");
        }
        sql.append("?");
      }
      sql.append(")");
    }
    jdbcTemplate.update(sql.toString(), restrictionsValues, restrictionsTypes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePreference(String key) {
    if (jdbcTemplate == null) {
      return;
    }
    initIfNecessary();
    String existing = preferences.remove(key);
    if (existing != null) {
      StringBuilder sql = new StringBuilder("DELETE FROM ")
          .append(getTableName()).append(" WHERE ").append(getKeyColumnName())
          .append(" = ? AND ").append(getPathColumnName()).append(" = ?");

      String[] restrictionsValues;
      int[] restrictionsTypes;
      if (defaultRestrictions != null && defaultRestrictions.size() > 0) {
        restrictionsValues = new String[defaultRestrictions.size() + 2];
        restrictionsTypes = new int[defaultRestrictions.size() + 2];
        int i = 1;
        for (Map.Entry<String, String> restriction : defaultRestrictions
            .entrySet()) {
          restrictionsValues[i] = restriction.getValue();
          restrictionsTypes[i] = Types.VARCHAR;
          sql.append(" AND ").append(restriction.getKey()).append(" = ?");
          i++;
        }
      } else {
        restrictionsValues = new String[2];
        restrictionsTypes = new int[2];
      }
      restrictionsValues[0] = key;
      restrictionsTypes[0] = Types.VARCHAR;
      restrictionsValues[1] = storePath;
      restrictionsTypes[1] = Types.VARCHAR;
      jdbcTemplate
          .update(sql.toString(), restrictionsValues, restrictionsTypes);
    }
  }

  private void initIfNecessary() {
    if (preferences == null) {
      preferences = new HashMap<>();

      StringBuilder sql = new StringBuilder("SELECT ").append(getKeyColumnName())
          .append(", ").append(getValueColumnName()).append(" FROM ")
          .append(getTableName()).append(" WHERE ").append(getPathColumnName())
          .append(" = ?");
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
          if (i > 0) {
            sql.append(" AND ");
          }
          sql.append(restriction.getKey()).append(" = ?");
          i++;
        }
      } else {
        restrictionsValues = new String[1];
        restrictionsTypes = new int[1];
      }
      restrictionsValues[0] = storePath;
      restrictionsTypes[0] = Types.VARCHAR;
      jdbcTemplate.query(sql.toString(), restrictionsValues, restrictionsTypes,
          new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
              preferences.put(rs.getString(1), rs.getString(2));
            }
          });
    }
  }

  /**
   * Configures the data source to which the JdbcPreferenceStore is persisted.
   *
   * @param dataSource
   *          the data source to which the JdbcPreferenceStore is persisted.
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
    if (tableName == null) {
      tableName = "PREFERENCES";
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
   * Sets the defaultRestrictions. The default restrictions contains arbitrary
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
   * @param keyColumnName
   *          the prefKeyColumnName to set.
   */
  public void setKeyColumnName(String keyColumnName) {
    this.keyColumnName = keyColumnName;
  }

  /**
   * Gets the prefValueColumnName.
   *
   * @return the prefValueColumnName.
   */
  protected String getValueColumnName() {
    if (valueColumnName == null) {
      valueColumnName = "PREFERENCE_VALUE";
    }
    return valueColumnName;
  }

  /**
   * Sets the prefValueColumnName.
   *
   * @param valueColumnName
   *          the prefValueColumnName to set.
   */
  public void setValueColumnName(String valueColumnName) {
    this.valueColumnName = valueColumnName;
  }

  /**
   * Sets the pathColumnName.
   *
   * @param pathColumnName
   *          the pathColumnName to set.
   */
  public void setPathColumnName(String pathColumnName) {
    this.pathColumnName = pathColumnName;
  }

  /**
   * Gets the pathColumnName.
   *
   * @return the pathColumnName.
   */
  protected String getPathColumnName() {
    if (pathColumnName == null) {
      pathColumnName = "PREFERENCE_PATH";
    }
    return pathColumnName;
  }

}
