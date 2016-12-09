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
        "dark-blue": "#488FE8",
        //"light-blue": "#199CD0",
        "selection-blue": "#2F65CA",

        "dark-grey": "#3D3D3D",
        "dark-medium-dark-grey": "#575757",
        "medium-dark-grey": "#767171",
        "light-medium-dark-grey": "#898383",
        "medium-grey": "#888888",
        "medium-light-grey": "#989898",
        "light-grey": "#AAAAAA",
        "super-light-grey": "#CCCCCC",
        "extra-light-grey": "#EEEEEE",

        "app-foreground": "dark-grey",
        "app-background": "white",
        "text-selected": "white",

        "background-selected": "medium-grey",
        "background-selected-dark": "dark-medium-dark-grey",
        "background-selected-dark-disabled": "background-selected-disabled",
        "background-selected-light": "extra-light-grey",

        "border-separator": "super-light-grey",

        "button-border": "super-light-grey",
        "button-border-hovered": "light-grey",
        "button-text": "medium-dark-grey",

        "label": "dark-medium-dark-grey",

        "default-focused-border": "dark-blue",
        "textfield-focused-border": "default-focused-border",
        "checkbox-border": "default-focused-border",
        "header-cell-border": "transparent",
        "tab-pane-border": "transparent",
        "tab-disabled": "super-light-grey",
        "tab-text": "light-medium-dark-grey",
        "tab-disabled-text": "light-medium-dark-grey",

        "header": "white",
        "header-text": "label",
        "header-text-selected": "label",
        "light-background": "extra-light-grey",
        "dark-background": "dark-grey",

        "table-header-text": "dark-medium-dark-grey",
        "table-border-main": "transparent",
        "table-header-background": "app-background",
        "table-header-separator": "transparent",
        "table-row": "label",
        "table-row-selected": "text-selected",
        "table-row-background-even": "light-background",
        "table-row-background-odd": "white",
        "table-row-background-selected": "medium-light-grey",
        "table-row-background-focused-selected": "table-row-background-selected",
        "table-row-line": "transparent",
        "table-column-line": "transparent",
        "table-focus-indicator": "background-selected-dark",

        "border-lead": "background-selected",
        "border-main": "border-light",

        "section-header-text": "app-foreground",
        "panel-header-text": "medium-dark-grey",

        "window-caption-background": "dark-blue",
        "window-border": "light-grey",
        "window-border-inner": "super-light-grey",

        "tooltip": "light-background",
        "tooltip-text": "label",

        "navigator": "app-foreground",
        "navigator-text": "white",
        "navigator-header": "navigator",
        "navigator-header-text": "white",
        "navigator-selected": "background-selected",
        "navigator-text-selected": "text-selected",

        "application-panel": "extra-light-grey",
        "application-bar": "transparent",
        "application-splitter": "transparent",

        "background-disabled": "extra-light-grey",
        "toolbar": "transparent",

        "splitpane": "transparent",

        "application-title": "panel-header-text",

        "field-selection-background": "selection-blue",
        "field-selection-foreground": "text-selected"
      }
    });
