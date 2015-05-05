/* ************************************************************************

 Copyright:

 License:

 Authors:

 ************************************************************************ */

qx.Theme.define("org.jspresso.framework.theme.Font",
    {
      extend: qx.theme.simple.Font,

      fonts: {
        "default": {
          size: ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
          || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ? 13 : 12,
          lineHeight: 1.4,
          family: (qx.core.Environment.get("os.name") === "osx") ? ["Lucida Grande"] :
              ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
              || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ?
                  ["Segoe UI", "Candara"] :
                  ["Tahoma", "Liberation Sans", "Arial", "sans-serif"]
        },

        "bold": {
          include: "default",
          bold: true
        },

        "small": {
          include: "default",
          size: ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
          || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ? 12 : 11
        },

        "monospace": {
          include: "default",
          size: 12
        },

        "headline": {
          include: "default",
          size: ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
          || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ? 16 : 15
        },

        "headline-bold": {
          include: "headline",
          bold: true
        },

        "header": {
          include: "bold",
          size: 30
        }

      }
    });
