/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.persistence.hibernate.dialect;

import org.hibernate.dialect.MySQL5Dialect;

/**
 * Mysql5 InnoDB dialect.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class MySQL5InnoDBDialect extends MySQL5Dialect {

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTableTypeString() {
    return " type=InnoDB";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasSelfReferentialForeignKeyBug() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supportsCascadeDelete() {
    return true;
  }
}
