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

qx.Theme.define("org.jspresso.framework.theme.Decoration",
    {
      extend: qx.theme.simple.Decoration,

      decorations: {
        "button-box": {
          style: {
            radius: 2,
            width: 1,
            color: "button-border",
            backgroundColor: "button-box-bright"
          }
        },

        "button-box-pressed": {
          include: "button-box",
          style: {
            backgroundColor: "button-box-bright-pressed"
          }
        },

        "button-box-focused": {
          include: "button-box",
          style: {
            color: "background-selected",
            shadowBlurRadius: 2,
            shadowColor: "background-selected",
            shadowLength: 0,
            transition: "box-shadow 200ms ease-out"
          }
        },

        "button-box-pressed-focused": {
          include: "button-box-pressed"
        },

        "checkbox-focused": {
          include: "checkbox",
          style: {
            color: "checkbox-focused-border",
            shadowBlurRadius: 2,
            shadowColor: "checkbox-focused-border",
            shadowLength: 0,
            transition: "box-shadow 200ms ease-out"
          }
        },

        "inset": {
          style: {
            radius: 2,
            width: 1,
            color: "border-light"
          }
        },

        "focused-inset": {
          style: {
            radius: 2,
            width: 1,
            color: "textfield-focused-border",
            shadowBlurRadius: 2,
            shadowColor: "textfield-focused-border",
            shadowLength: 0,
            transition: "box-shadow 200ms ease-out"
          }
        },

        "panel-box": {
          style: {
            radius: 2,
            width: 1,
            inset: true,
            color: "border-main"
          }
        },

        "lead-item": {
          style: {
            width: 2,
            color: "border-lead"
          }
        },

        "table": {
          include: "panel-box",
          style: {
            color: "table-border-main"
          }
        },

        "table-header": {
          include: "button-box",

          style: {
            radius: 0,
            width: [0, 0, 1, 0],
            backgroundColor: "table-header-background",
            color: "table-header-separator"
          }
        },

        "table-header-cell": {
          style: {
            widthLeft: 0,
            widthRight: 1,
            color: "header-cell-border"
          }
        },

        "table-header-cell-first": {
          include: "table-header-cell"
        },

        "table-header-cell-hovered": {
          include: "table-header-cell",
          style: {
            backgroundColor: "button-box-dark"
          }
        },

        "table-header-cell-first-hovered": {
          include: "table-header-cell-first",
          style: {
            backgroundColor: "button-box-dark"
          }
        },

        "table-header-column-button": {
          include: "table-header",
          style: {
            widthRight: 0
          }
        },

        "table-scroller-focus-indicator": {
          style: {
            radius: 2,
            width: 1,
            color: "table-focus-indicator"
          }
        },

        "panel-box-top-angled": {
          include: "panel-box",
          style: {
            radius: [0, 0, 2, 2]
          }
        },

        "panel-box-right-angled": {
          include: "panel-box-top-angled",
          style: {
            radius: [2, 0, 0, 2]
          }
        },

        "panel-box-bottom-angled": {
          include: "panel-box-top-angled",
          style: {
            radius: [2, 2, 0, 0]
          }
        },

        "panel-box-left-angled": {
          include: "panel-box-top-angled",
          style: {
            radius: [0, 0, 2, 2]
          }
        },

        "panel-box-top-only": {
          include: "panel-box",
          style: {
            radius: 0,
            width: [1, 0, 0, 0]
          }
        },

        "panel-box-right-only": {
          include: "panel-box-top-only",
          style: {
            width: [0, 1, 0, 0]
          }
        },

        "panel-box-bottom-only": {
          include: "panel-box-top-only",
          style: {
            width: [0, 0, 1, 0]
          }
        },

        "panel-box-left-only": {
          include: "panel-box-top-only",
          style: {
            width: [0, 0, 0, 1]
          }
        },

        "tab-pane-top-only": {
          include: "panel-box-top-only",
          style: {
            color: "tab-pane-border"
          }
        },

        "tab-pane-right-only": {
          include: "panel-box-right-only",
          style: {
            color: "tab-pane-border"
          }
        },

        "tab-pane-bottom-only": {
          include: "panel-box-bottom-only",
          style: {
            color: "tab-pane-border"
          }
        },

        "tab-pane-left-only": {
          include: "panel-box-left-only",
          style: {
            color: "tab-pane-border"
          }
        },

        "tab-button-top": {
          include: "tabview-page-button-top",
          style: {
            color: "tab-pane-border"
          }
        },

        "tab-button-bottom": {
          include: "tabview-page-button-bottom",
          style: {
            color: "tab-pane-border"
          }
        },

        "tab-button-right": {
          include: "tabview-page-button-right",
          style: {
            color: "tab-pane-border"
          }
        },

        "tab-button-left": {
          include: "tabview-page-button-left",
          style: {
            color: "tab-pane-border"
          }
        },

        "window": {
          style: {
            radius: 2,
            width: 1,
            color: "window-border",
            innerWidth: 1,
            innerColor: "window-border-inner",
            shadowLength: 1,
            shadowBlurRadius: 2,
            shadowColor: "shadow",
            backgroundColor: "app-background"
          }
        },

        "window-caption": {
          style: {
            width: [0, 0, 1, 0],
            color: "window-border-inner",
            backgroundColor: "window-caption-background"
          }
        },

        "header-box": {
          style: {
            //gradientStart: ["header", 0],
            //gradientEnd: ["app-background", 100]
            backgroundColor: "application-bar"
          }
        },

        "accordion-box": {
          style: {
            width: [0, 1, 0, 0],
            color: "border-main"
          }
        },

        "accordion-section-box": {
          style: {
            width: [0, 0],
            color: "border-main",
            transition: "top 300ms ease-in"
          }
        },

        "scroll-knob": {
          style: {
            radius: 2,
            width: 1,
            color: "button-border"
          }
        }
      }
    });
