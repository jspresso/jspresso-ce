/* ************************************************************************

   Copyright:

   License:

   Authors:

************************************************************************ */

qx.Theme.define("org.jspresso.framework.theme.Font",
{
  extend : qx.theme.simple.Font,

  fonts :
  {
    "default": {
      size: ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
      || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ? 12 : 11,
      lineHeight: 1.4,
      family: (qx.core.Environment.get("os.name") === "osx") ? ["Lucida Grande"] :
          ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
          || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ?
              ["Segoe UI", "Candara"] :
              ["Tahoma", "Liberation Sans", "Arial", "sans-serif"]
    },

    "bold": {
      size: ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
      || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ? 12 : 11,
      lineHeight: 1.4,
      family: (qx.core.Environment.get("os.name") === "osx") ? ["Lucida Grande"] :
          ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
          || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ?
              ["Segoe UI", "Candara"] :
              ["Tahoma", "Liberation Sans", "Arial", "sans-serif"],
      bold: true
    },

    "small": {
      size: ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
      || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ? 11 : 10,
      lineHeight: 1.4,
      family: (qx.core.Environment.get("os.name") === "osx") ? ["Lucida Grande"] :
          ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
          || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ?
              ["Segoe UI", "Candara"] :
              ["Tahoma", "Liberation Sans", "Arial", "sans-serif"]
    },

    "monospace": {
      size: 11,
      lineHeight: 1.4,
      family: (qx.core.Environment.get("os.name") === "osx") ? ["Lucida Console", "Monaco"] :
          ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
          || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ?
              ["Consolas"] :
              ["Consolas", "DejaVu Sans Mono", "Courier New", "monospace"]
    },

    "headline": {
      size: ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
      || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ? 14 : 13,
      lineHeight: 1.4,
      family: (qx.core.Environment.get("os.name") === "osx") ? ["Lucida Grande"] :
          ((qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "vista")
          || (qx.core.Environment.get("os.name") === "win" && qx.core.Environment.get("os.version") === "7")) ?
              ["Segoe UI", "Candara"] :
              ["Tahoma", "Liberation Sans", "Arial", "sans-serif"]
    }
  }
});
