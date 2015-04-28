/* ************************************************************************

   Copyright:

   License:

   Authors:

************************************************************************ */

qx.Theme.define("org.jspresso.framework.theme.Appearance",
{
  extend : qx.theme.simple.Appearance,
  
  appearances :
  {
    "splitpane/splitter": {
      base: true,
      style: function (states) {
        return {
          backgroundColor: "white"
        };
      }
    },

    "menubar": {
      base: true,
      style: function (states) {
        return {
          backgroundColor: "white"
        };
      }
    },

    "toolbar": {
      base: true,
      style: function (states) {
        return {
          backgroundColor: "white"
        };
      }
    },

    "toolbar/part": {
      base: true,
      style: function (states) {
        return {
          height: 30
        };
      }
    },

    "toolbar/part/container": {
      base: true,
      style: function (states) {
        return {
          padding: 2
        };
      }
    },

    "button-frame": {
      base: true,
      style: function (states) {
        return {
          padding: [2, 4],
          margin: 2
        };
      }
    },

    "button": {
      base: true,
      include: "button-frame",
      style: function (states, superStyles) {
        return {
          decorator: states.hovered ? superStyles.decorator : undefined,
          padding: states.hovered ? [2, 4] : [3, 5]
        };
      }
    },

    "textfield": {
      base: true,
      style: function (states) {
        return {
          padding: [2, 4]
        };
      }
    },

    "table": {
      base: true,
      style: function (states) {
        return {
          decorator: "button-box",
          headerCellHeight: 25,
          rowHeight: 25,
          backgroundColor: "white"
        };
      }
    },

    "table-header-cell": {
      base: true,
      style: function (states) {
        var hovered = "";
        if (states.hovered) {
          hovered = "-hovered";
        }
        return {
          decorator: states.first ? "table-header-cell-first" + hovered : "table-header-cell" + hovered
        };
      }
    },

    "groupbox/legend": {
      base: true,
      style: function (states) {
        return {
          padding: 5,
          margin: [0, 5]
        };
      }
    },

    "collapsable-panel": {
      include: "groupbox/frame",
      style: function (states) {
        return {
          showSeparator: true,
          gap: 2,
          decorator: "panel-box",
          margin: 0,
          padding: [0, 1, 0, 1]
        };
      }
    },

    "collapsable-panel/bar/icon": {
      include: "groupbox/legend",
      alias: "groupbox/legend"
    },

    "tree": {
      base: true,
      style: function (states) {
        return {
          decorator: undefined,
          padding: 0
        };
      }
    },

    "splitbutton/button": {
      base: true,
      include: "button-frame",
      style: function (states, superStyles) {
        return {
          padding: states.hovered ? [2, 4] : [3, 5],
          margin: [2, 0, 2, 2],
          decorator: states.hovered ? superStyles.decorator : undefined
        };
      }
    },

    "splitbutton/arrow": {
      base: true,
      include: "button-frame",
      style: function (states, superStyles) {
        return {
          padding: states.hovered ? [2, 4] : [3, 5],
          margin: [2, 2, 2, 0],
          decorator: states.hovered ? superStyles.decorator : undefined
        };
      }
    },

    "splitpane": {
      base: true,
      style: function (states) {
        return {
          padding: 2
        };
      }
    },

    "datefield/button": {
      base: true,
      style: function (states) {
        return {
          margin: 0,
          padding: 0
        };
      }
    },

    "selectbox": {
      base: true,
      alias: "button-frame",
      include: "button-frame",
      style: function (states) {
        return {
          margin: 0,
          padding: [2, 4]
        };
      }
    },

    "tabview/bar": {
      base: true,
      style: function (states) {
        return {
          margin: [2, 2, 0, 2]
        };
      }
    },

    "tabview/pane": {
      base: true,
      style: function (states) {
        return {
          backgroundColor: "background",
          decorator: "button-box-top-angled",
          margin: [0, 2, 2, 2]
        };
      }
    },

    "tabview-page/button": {
      base: true,
      style: function (states) {
        var padding;
        if (states.barTop || states.barBottom) {
          var padding = [4, 8, 4, 6];
        } else {
          var padding = [4, 2, 4, 2];
        }
        return {
          textColor: states.disabled ? "text-disabled" : states.checked ? undefined : "gray",
          padding: padding,
          backgroundColor: states.disabled ? "white" : states.checked ? "button-box-bright" : "white",
          font: states.disabled ? undefined : states.checked ? "bold" : undefined
        };
      }
    }
  }
});
