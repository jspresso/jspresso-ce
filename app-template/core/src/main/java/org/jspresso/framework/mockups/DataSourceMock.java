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
package org.jspresso.framework.mockups;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * Mock for DataSource.
 *
 * @author Vincent Vandenschrick
 */
public class DataSourceMock implements DataSource {

  /**
   * Mock method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public PrintWriter getLogWriter() {
    return null;
  }

  /**
   * Mock method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setLogWriter(PrintWriter out) {
    // NO-OP
  }

  /**
   * Mock method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setLoginTimeout(int seconds) {
    // NO-OP
  }

  /**
   * Mock method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int getLoginTimeout() {
    return 0;
  }

  /**
   * Mock method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public <T> T unwrap(Class<T> iface) {
    return null;
  }

  /**
   * Mock method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isWrapperFor(Class<?> iface) {
    return false;
  }

  /**
   * Mock method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Connection getConnection() {
    return null;
  }

  /**
   * Mock method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Connection getConnection(String username, String password) {
    return null;
  }

  /**
   * Mock method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("javadoc")
  public Logger getParentLogger() {
    return null;
  }

}
