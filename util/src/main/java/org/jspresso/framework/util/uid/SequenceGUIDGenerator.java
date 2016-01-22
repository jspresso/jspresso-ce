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
package org.jspresso.framework.util.uid;

import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

/**
 * A simple GUID generator based on a database sequence.
 *
 * @author Vincent Vandenschrick
 */
public class SequenceGUIDGenerator implements IGUIDGenerator<Long> {

  private DataFieldMaxValueIncrementer sequenceIncrementer;

  /**
   * Uses the sequence incrementer to get the next sequence independently from
   * the underlying DB.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Long generateGUID() {
    return getSequenceIncrementer().nextLongValue();
  }


  /**
   * Sets the sequenceIncrementer.
   *
   * @param sequenceIncrementer the sequenceIncrementer to set.
   */
  public void setSequenceIncrementer(DataFieldMaxValueIncrementer sequenceIncrementer) {
    this.sequenceIncrementer = sequenceIncrementer;
  }


  /**
   * Gets the sequenceIncrementer.
   *
   * @return the sequenceIncrementer.
   */
  protected DataFieldMaxValueIncrementer getSequenceIncrementer() {
    return sequenceIncrementer;
  }
}
