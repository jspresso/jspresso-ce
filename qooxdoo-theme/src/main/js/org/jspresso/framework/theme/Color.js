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
        "dark-blue": "#44509E",
        "light-blue": "#199CD0",

        "dark-grey": "#3D3D3D",
        "medium-dark-grey": "#575757",
        "medium-grey": "#888888",
        "medium-light-grey": "#AAAAAA",
        "light-grey": "#CCCCCC",
        "super-light-grey": "#EEEEEE",

        "app-foreground": "dark-grey",
        "app-background": "white",
        "text-selected": "white",

        "background-selected": "medium-grey",
        "background-selected-dark": "medium-dark-grey",
        "background-selected-dark-disabled": "background-selected-disabled",
        "background-selected-light": "super-light-grey",

        "border-separator": "light-grey",

        "button-border": "light-grey",
        "button-border-hovered": "medium-light-grey",

        "label": "medium-light-grey",

        "default-focused-border": "light-blue",
        "textfield-focused-border": "default-focused-border",
        "checkbox-border": "default-focused-border",
        "header-cell-border": "transparent",
        "tab-pane-border": "transparent",
        "tab-disabled": "medium-light-grey",
        "tab-disabled-text": "white",

        "header": "white",
        "header-text": "label",
        "header-text-selected": "label",
        "light-background": "super-light-grey",
        "dark-background": "dark-grey",

        "table-header-text": "medium-dark-grey",
        "table-border-main": "transparent",
        "table-header-background": "app-background",
        "table-header-separator": "transparent",
        "table-row": "label",
        "table-row-selected": "text-selected",
        "table-row-background-even": "light-background",
        "table-row-background-odd": "white",
        "table-row-background-selected": "background-selected",
        "table-row-background-focused-selected": "table-row-background-selected",
        "table-row-line": "transparent",
        "table-column-line": "transparent",

        "table-focus-indicator": "background-selected-dark",
        "border-lead": "background-selected",

        "border-main": "border-light",

        "section-header-text": "app-foreground",
        "panel-header-text": "label",

        "window-caption-background": "light-blue",
        "window-border": "medium-light-grey",
        "window-border-inner": "light-grey",

        "tooltip": "light-background",
        "tooltip-text": "label",

        "navigator": "app-foreground",
        "navigator-text": "white",
        "navigator-header": "navigator",
        "navigator-header-text": "white",
        "navigator-selected": "background-selected",
        "navigator-text-selected": "text-selected",

        "application-panel": "super-light-grey",
        "application-bar": "white",
        "application-splitter": "transparent",

        "background-disabled": "super-light-grey"
      }
    });
