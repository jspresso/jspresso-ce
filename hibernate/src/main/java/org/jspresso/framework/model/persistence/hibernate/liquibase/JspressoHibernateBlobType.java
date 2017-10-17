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
package org.jspresso.framework.model.persistence.hibernate.liquibase;

/**
 * This is a custom implementation of the Liquibase BLOB type to correctly handle MySQL blobs.
 *
 * @author Vincent Vandenschrick
 */

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.BlobType;
import liquibase.ext.hibernate.database.HibernateSpringDatabase;
import liquibase.util.StringUtils;

@DataTypeInfo(name = "blob", aliases = {"longblob", "longvarbinary", "java.sql.Types.BLOB", "java.sql.Types.LONGBLOB",
                                        "java.sql.Types.LONGVARBINARY", "java.sql.Types.VARBINARY",
                                        "java.sql.Types.BINARY", "varbinary", "binary", "image", "tinyblob",
                                        "mediumblob"}, minParameters = 0, maxParameters = 1,
                                        priority = LiquibaseDataType.PRIORITY_DATABASE)
public class JspressoHibernateBlobType extends BlobType {

  public DatabaseDataType toDatabaseDataType(Database database) {
    if (database instanceof HibernateSpringDatabase) {
      String originalDefinition = StringUtils.trimToEmpty(getRawDefinition());
      return new DatabaseDataType(originalDefinition.toUpperCase(), getParameters());
    }
    // use defaults for all the others
    return super.toDatabaseDataType(database);
  }
}
