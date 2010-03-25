/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.automation;

/**
 * Source of automation ids used for record / playback.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IAutomationSource {

  /**
   * Gets a seed to generate fixed ids to mark the generated component. This id
   * can serve to record/replay command sequences.
   * 
   * @return a fixed id to mark the generated component.
   */
  String getAutomationSeed();

  /**
   * Sets a seed to generate fixed ids to mark the generated component. This id
   * can serve to record/replay command sequences.
   * 
   * @param automationSeed
   *          a fixed id to mark the generated component.
   */
  void setAutomationSeed(String automationSeed);
}
