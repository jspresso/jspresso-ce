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
package org.jspresso.framework.application.frontend.command.remote;

/**
 * Starts the application.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteStartCommand extends RemoteCommand {

  private static final long serialVersionUID = -8122101348450476262L;

  private String   language;
  private int      timezoneOffset;
  private String[] keysToTranslate;
  private String   version;
  private String   clientType;

  /**
   * Gets the language.
   *
   * @return the language.
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Sets the language.
   *
   * @param language
   *          the language to set.
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Gets the timezoneOffset.
   *
   * @return the timezoneOffset.
   */
  public int getTimezoneOffset() {
    return timezoneOffset;
  }

  /**
   * Sets the timezoneOffset.
   *
   * @param timezoneOffset
   *          the timezoneOffset to set.
   */
  public void setTimezoneOffset(int timezoneOffset) {
    this.timezoneOffset = timezoneOffset;
  }

  /**
   * Gets the keysToTranslate.
   *
   * @return the keysToTranslate.
   */
  public String[] getKeysToTranslate() {
    return keysToTranslate;
  }

  /**
   * Sets the keysToTranslate.
   *
   * @param keysToTranslate
   *          the keysToTranslate to set.
   */
  public void setKeysToTranslate(String... keysToTranslate) {
    this.keysToTranslate = keysToTranslate;
  }

  /**
   * Gets the version.
   *
   * @return the version.
   */
  public String getVersion() {
    return version;
  }

  /**
   * Sets the version.
   *
   * @param version
   *          the version to set.
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * Gets client type.
   *
   * @return the client type
   */
  public String getClientType() {
    return clientType;
  }

  /**
   * Sets client type.
   *
   * @param clientType the client type
   */
  public void setClientType(String clientType) {
    this.clientType = clientType;
  }
}
