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
package org.jspresso.framework.util.lang;

import java.io.Serializable;

/**
 * A date DTO that is insensible to timezones.
 *
 * @author Vincent Vandenschrick
 */
public class DateDto implements Serializable {

  private static final long serialVersionUID = 8543631200164065569L;

  private int year;
  private int month;
  private int date;
  private int hour;
  private int minute;
  private int second;
  private int millisecond;

  /**
   * Gets the year.
   *
   * @return the year.
   */
  public int getYear() {
    return year;
  }

  /**
   * Sets the year.
   *
   * @param year
   *     the year to set.
   */
  public void setYear(int year) {
    this.year = year;
  }

  /**
   * Gets the month.
   *
   * @return the month.
   */
  public int getMonth() {
    return month;
  }

  /**
   * Sets the month.
   *
   * @param month
   *     the month to set.
   */
  public void setMonth(int month) {
    this.month = month;
  }

  /**
   * Sets the date.
   *
   * @param date
   *     the date to set.
   */
  public void setDate(int date) {
    this.date = date;
  }

  /**
   * Gets the date.
   *
   * @return the date.
   */
  public int getDate() {
    return date;
  }

  /**
   * Gets the hour.
   *
   * @return the hour.
   */
  public int getHour() {
    return hour;
  }

  /**
   * Sets the hour.
   *
   * @param hour
   *     the hour to set.
   */
  public void setHour(int hour) {
    this.hour = hour;
  }

  /**
   * Gets the minute.
   *
   * @return the minute.
   */
  public int getMinute() {
    return minute;
  }

  /**
   * Sets the minute.
   *
   * @param minute
   *     the minute to set.
   */
  public void setMinute(int minute) {
    this.minute = minute;
  }

  /**
   * Gets the second.
   *
   * @return the second.
   */
  public int getSecond() {
    return second;
  }

  /**
   * Sets the second.
   *
   * @param second
   *     the second to set.
   */
  public void setSecond(int second) {
    this.second = second;
  }

  /**
   * Gets millisecond.
   *
   * @return the millisecond
   */
  public int getMillisecond() {
    return millisecond;
  }

  /**
   * Sets millisecond.
   *
   * @param millisecond
   *     the millisecond
   */
  public void setMillisecond(int millisecond) {
    this.millisecond = millisecond;
  }

  /**
   * {@inheritDoc}
   *
   * @param obj
   *     the obj
   * @return the boolean
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof DateDto) {
      return ((DateDto) obj).year == year && ((DateDto) obj).month == month
          && ((DateDto) obj).date == date && ((DateDto) obj).hour == hour
          && ((DateDto) obj).minute == minute
          && ((DateDto) obj).second == second
          && ((DateDto) obj).millisecond == millisecond;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * @return the int
   */
  @Override
  public int hashCode() {
    return year * 37 + month * 43 + date * 7 + hour * 13 + minute * 23 + second
        * 17 + millisecond * 53;
  }
}
