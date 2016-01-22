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
package org.jspresso.framework.util.format {

import mx.utils.StringUtil;

import org.jspresso.framework.util.lang.DateDto;

public class DateUtils {
  //noinspection JSFieldCanBeLocal
  private static var _df:DateFormatter = new DateFormatter();

  /**
   * Format Strings
   */
  public static const FMT_FULL_DATE_TIME:String = 'MM/DD/YYYY L:NN A';
  public static const FMT_FULL_DATE:String = 'MM/DD/YYYY';
  public static const FMT_ISO_DATE_TIME:String = 'YYYY-MM-DD JJ:NN:SS';
  public static const FMT_ISO_DATE:String = 'YYYY-MM-DD';
  public static const FMT_ISO_TIME:String = 'JJ:NN:SS';
  public static const FMT_SHORT_DATE_TIME:String = 'MM/DD/YY L:NN A';
  public static const FMT_SHORT_DATE:String = 'MM/DD/YY';
  public static const FMT_SHORT_DATE_DAY:String = 'MM/DD/YY (EEE)';
  public static const FMT_MINI_DATE_TIME:String = 'MM/DD L:NN A';
  public static const FMT_MINI_DATE:String = 'MM/DD';
  public static const FMT_MINI_DATE_DAY:String = 'MM/DD (EEE)';
  public static const FMT_MILITARY_TIME:String = 'JJ:NN';
  public static const FMT_FRIENDLY_DATE:String = 'DD MMM YYYY';
  public static const FMT_FRIENDLY_TIME:String = 'L:NN A';

  /**
   * Time facts
   */
  public static const SECONDS_IN_HOUR:int = 3600;
  public static const SECONDS_IN_DAY:int = 86400;
  public static const MILISECONDS_IN_DAY:int = 86400000;
  public static const MINUTES_IN_DAY:int = 1440;

  //Date.getMonth() is 0 based, others values are not
  public static const SHORT_MONTHS:Array = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov',
                                            'Dec'];
  public static const MONTHS:Array = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August',
                                      'September', 'October', 'November', 'December'];

  public static const SHORT_DAYS:Array = ['', 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  public static const DAYS:Array = ['', 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

  public function DateUtils() {
    //Call me in Saran and call me static!
  }

  public static function parseIsoDate(value:String):Date {
    value = value.replace(/-/g, "/");
    value = value.replace("T", " ");
    value = value.replace("Z", " GMT-0000");
    return new Date(Date.parse(value));
  }

  public static function parseDate(value:String, inputFormat:String):Date {
    if (value == null || value.length == 0) {
      return null;
    }
    if (value.length <= 2 && value.length != inputFormat.length) {
      if (value.length == 1) {
        return parseDate(value, "D");
      }
      return parseDate(value, "DD");
    }

    var mask:String;
    var temp:String;
    var dateString:String = "";
    var monthString:String = "";
    var yearString:String = "";
    var j:int = 0;

    var n:int = inputFormat.length;
    for (var i:int = 0; i < n; i++, j++) {
      temp = "" + value.charAt(j);
      mask = "" + inputFormat.charAt(i);

      if (mask == "M") {
        if (isNaN(Number(temp)) || temp == " ") {
          j--;
        } else {
          monthString += temp;
        }
      } else if (mask == "D") {
        if (isNaN(Number(temp)) || temp == " ") {
          j--;
        } else {
          dateString += temp;
        }
      } else if (mask == "Y") {
        if (isNaN(Number(temp)) || temp == " ") {
          j--;
        } else {
          yearString += temp;
        }
      } else if (isNaN(Number(mask)) && !isNaN(Number(temp)) && temp != " ") {
        j--;
      }
    }

    temp = "" + value.charAt(inputFormat.length - i + j);
    if (!(temp == "") && (temp != " ")) {
      return null;
    }

    var monthNum:Number = Number(monthString);
    var dayNum:Number = Number(dateString);
    var yearNum:Number = Number(yearString);

    var today:Date = new Date();
    if (isNaN(yearNum) || yearString.length == 0) {
      yearNum = today.getFullYear();
    }
    if (isNaN(monthNum) || monthString.length == 0) {
      monthNum = today.getMonth() + 1;
    }
    if (isNaN(dayNum) || dateString.length == 0) {
      dayNum = today.getDate();
    }

    if (yearString.length == 2 && yearNum < 70) {
      yearNum += 2000;
    }

    var newDate:Date = new Date(yearNum, monthNum - 1, dayNum);

    if (dayNum != newDate.getDate() || (monthNum - 1) != newDate.getMonth()) {
      return null;
    }

    return newDate;
  }

  public static function parseTime(value:String, guessPMBelow:int = 0):Date {
    value = StringUtil.trim(value.toUpperCase());
    var dt:Date = new Date(/*2000,0,1*/);
    dt.time = 0;
    var time:Object;
    var isMil:Boolean = false;
    //standard time regex
    var matches:Array;
    var reg:RegExp = /^(1[012]|[1-9])(:[0-5]\d)?(:[0-5]\d)?(.?\d{3})?( ?[AaPp][Mm]?)?$/;
    matches = reg.exec(value);
    if (!matches) {
      //military time regex
      reg = /^(2[0-4]|1\d|0?\d)(:?[0-5]\d)?(:?[0-5]\d)?(.?\d{3})?$/;
      isMil = true;
      matches = reg.exec(value);
    }
    if (!matches) {
      //could not parse
      return null;
    }
    time = {
      hours: Number(matches[1]),
      minutes: matches[2] ? Number(String(matches[2]).replace(':', '')) : 0,
      seconds: matches[3] ? Number(String(matches[3]).replace(':', '')) : 0,
      milliseconds: matches[4] ? Number(String(matches[4]).replace('.', '')) : 0,
      ampm: null
    };
    if (isMil) {
      //processing military format
      dt.setHours(time.hours, time.minutes, time.seconds, time.milliseconds);
    } else {
      //processing common format
      if (matches[4]) {
        //user indicated AM/PM
        if (String(matches[4]).indexOf('P') != -1) {
          //PM
          time.hours = time.hours == 12 ? 12 : time.hours + 12;
        } else if (time.hours == 12) {
          time.hours = 0;
        }
      } else if (guessPMBelow > 0) {
        //will have to guess PM
        time.hours = time.hours < guessPMBelow ? time.hours + 12 : time.hours;
      }
    }
    dt.setHours(time.hours, time.minutes, time.seconds, time.milliseconds);
    return dt;
  }

  public static function format(value:Object, format:String = null):String {
    var ret:String = '';
    if (value == null) {
      return ret;
    }
    _df.formatString = (format == null) ? DateUtils.FMT_ISO_DATE_TIME : format;
    ret = _df.format(value);
    if (ret.length == 0) {
      ret = _df.format(parseIsoDate(value.toString()));
    }
    return ret;
  }

  public static function toSeconds(value:Object):Number {
    var d:Date;
    if (value is Date) {
      d = value as Date;
    } else {
      d = parseIsoDate(value.toString());
    }
    if (!d) {
      return -1;
    } else {
      return (d.getHours() * 3600) + (d.getMinutes() * 60) + d.getSeconds();
    }
  }

  public static function toMinutes(value:Object):Number {
    var sec:Number = toSeconds(value);
    if (sec == -1) {
      return -1;
    } else {
      return sec / 60;
    }
  }

  public static function toMilliseconds(value:Object):Number {
    var sec:Number = toSeconds(value);
    if (sec == -1) {
      return -1;
    } else {
      return sec * 1000;
    }
  }

  public static function toHours(value:Object):Number {
    var sec:Number = toSeconds(value);
    if (sec == -1) {
      return -1;
    } else {
      return sec / DateUtils.SECONDS_IN_HOUR;
    }
  }

  public static function dayOfYear(dt:Date = null):int {
    if (!dt) {
      dt = new Date();
    }
    dt.setHours(0, 0, 0, 0);
    var firstDay:Date = new Date(dt.getFullYear(), 0, 1);
    return Math.floor((dt.getTime() - firstDay.getTime()) / DateUtils.MILISECONDS_IN_DAY) + 1;
  }

  public static function fromDateDto(source:DateDto):Date {
    if (source) {
      return new Date(source.year, source.month, source.date, source.hour, source.minute, source.second, source.millisecond);
    }
    return null;
  }

  public static function fromDate(source:Date):DateDto {
    if (source) {
      var dateDto:DateDto = new DateDto();
      dateDto.year = source.fullYear;
      dateDto.month = source.month;
      dateDto.date = source.date;
      dateDto.hour = source.hours;
      dateDto.minute = source.minutes;
      dateDto.second = source.seconds;
      dateDto.millisecond = source.milliseconds;
      return dateDto;
    }
    return null;
  }
}
}
