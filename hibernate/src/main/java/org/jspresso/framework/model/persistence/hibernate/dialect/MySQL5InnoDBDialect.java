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
package org.jspresso.framework.model.persistence.hibernate.dialect;

import java.sql.Types;

/**
 * Mysql5 InnoDB dialect.
 *
 * @author Vincent Vandenschrick
 */
public class MySQL5InnoDBDialect extends
    org.hibernate.dialect.MySQL5InnoDBDialect {

  /**
   * Constructs a new {@code MySQL5InnoDBDialect} instance. Overrides
   * tinyblob definition by replacing it with varbinary until 64 length.
   */
  public MySQL5InnoDBDialect() {
    super();
    registerColumnType(Types.VARBINARY, 63, "varbinary($l)");
    // for Hibernate 4 between 4.0 and 4.3, the Mysql mapping changed from bit to boolean - tinyint(1).
    // This was restored in Hibernate > 4.3 but for 4.2, we need to override this mapping.
    registerColumnType(Types.BOOLEAN, "bit");
  }
}
