/* ************************************************************************

 Copyright:

 License:

 Authors:

 ************************************************************************ */

qx.Theme.define("org.jspresso.framework.theme.Decoration",
    {
      extend: qx.theme.simple.Decoration,

      decorations: {
        "button-box": {
          style: {
            radius: 3,
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
             shadowBlurRadius: 3,
             shadowColor: "background-selected",
             shadowLength: 0,
             transition: "all 200ms ease-out"
          }
        },

        "button-box-pressed-focused": {
          include: "button-box-pressed",

          style: {
            /*
             color: "background-selected",
             shadowBlurRadius: 3,
             shadowColor: "background-selected",
             shadowLength: 0,
             transition: "all 200ms ease-out"
             */
          }
        },

        "checkbox-focused": {
          include: "checkbox",
          style: {
            color: "background-selected",
            shadowBlurRadius: 3,
            shadowColor: "background-selected",
            shadowLength: 0,
            transition: "all 200ms ease-out"
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
            color: "background-selected",
            shadowBlurRadius: 2,
            shadowColor: "background-selected",
            shadowLength: 0,
            transition: "all 200ms ease-out"
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

        "header-box": {
          style: {
            gradientStart: ["header", 0],
            gradientEnd: ["app-background", 100],
            transition: "all 300ms ease-in"
          }
        },

        "accordion-box": {
          style: {
            radius: 2,
            width: 1,
            inset: true,
            color: "border-main",
            shadowBlurRadius: 2,
            shadowColor: "border-light",
            shadowLength: 0,
            transition: "all 300ms ease-in"
          }
        },

        "table-header": {
          include: "button-box",

          style: {
            radius: 0,
            width: [0, 0, 2, 0]
          }
        },

        "table-header-cell": {
          style: {
            widthLeft: 0,
            widthRight: 1,
            color: "button-border"
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

        "window": {
          style: {
            radius: 2,
            width: 1,
            color: "window-border",
            innerWidth: 2,
            innerColor: "window-border-inner",
            shadowLength: 1,
            shadowBlurRadius: 3,
            shadowColor: "shadow",
            backgroundColor: "app-background"
          }
        },

        "window-caption": {
          style: {
            width: [0, 0, 2, 0],
            color: "window-border-inner",
            backgroundColor: "window-caption-background"
          }
        }
      }
    });
