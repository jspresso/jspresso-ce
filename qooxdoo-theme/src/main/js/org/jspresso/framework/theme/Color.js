/**
 * Copyright (c) 2005-2015 Vincent Vandenschrick. All rights reserved.
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

qx.Theme.define("org.jspresso.framework.theme.Color",
    {
      extend: qx.theme.simple.Color,

      colors: {
        "border-separator": "#CCCCCC",

        "button-border": "#CCCCCC",
        "button-border-hovered": "#BBBBBB",

        "table-row": "gray",
        "table-row-selected": "text-selected",
        "table-row-background-even": "white",
        "table-row-background-odd": "light-background",

        "border-main": "border-light",

        "header": "white",
        "header-text": "gray",
        "header-text-selected": "gray",
        "light-background": "#F2F2F2",
        "dark-background": "#E2E2E2",

        "app-foreground": "#4568BB",
        "app-background": "white",
        "text-selected": "white",

        "window-caption-background": "light-background",
        "window-border": "#979797",
        "window-border-inner": "#B7B7B7",

        "background-selected": "#6694E3",
        "background-selected-dark": "app-foreground",
        "background-selected-dark-disabled": "background-selected-disabled",

        "table-focus-indicator": "background-selected",
        "border-lead": "background-selected",

        "table-row-background-focused-selected": "background-selected-dark",
        "table-row-background-focused": "background-selected-dark",
        "table-row-background-selected": "background-selected-dark",

        "tooltip": "light-background",
        "tooltip-text": "gray",

        "label": "gray",

        "navigator": "background-selected-dark",
        "navigator-text": "white",
        "navigator-header": "navigator",
        "navigator-header-text": "white",
        "navigator-selected": "background-selected",
        "navigator-text-selected": "white"
      }
    });
