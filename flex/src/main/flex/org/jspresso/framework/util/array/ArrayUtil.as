/**
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 */

package org.jspresso.framework.util.array {
  public class ArrayUtil {
    
    public static function areUnorderedArraysEqual(a1:Array, a2:Array):Boolean {
      if((a1 && !a2) || (a2 && !a1)) {
        return false;
      } else if(a1.length != a2.length) {
        return false;
      } else {
        for each (var e:Object in a1) {
          if(a2.indexOf(e) < 0) {
            return false;
          }
        }
      }
      return true;
    }
  }
}