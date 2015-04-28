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
            shadowLength: 0
          }
        },

        "button-box-pressed-focused": {
          include: "button-box-pressed",

          style: {
            color: "background-selected",
            shadowBlurRadius: 3,
            shadowColor: "background-selected",
            shadowLength: 0
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
            shadowLength: 0
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

        "accordion-box": {
          style: {
            radius: 2,
            width: 1,
            inset: true,
            color: "border-main",
            shadowBlurRadius: 2,
            shadowColor: "border-light",
            shadowLength: 0
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
        }
      }
    });
