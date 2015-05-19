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
 *
 * @asset(org/jspresso/framework/theme/fonts/*.*)
 */

qx.Theme.define("org.jspresso.framework.theme.Font",
    {
      extend: qx.theme.simple.Font,

      fonts: {
        "default": {
          size: 13,
          lineHeight: 1.4,
          family: ["Arial"],
          sources: [
            {
              family: "Exo",
              bold: false,
              italic: false,
              source: [
                "org/jspresso/framework/theme/fonts/Exo2.0-Regular-webfont.eot",
                "org/jspresso/framework/theme/fonts/Exo2.0-Regular-webfont.svg#exo_2.0",
                "org/jspresso/framework/theme/fonts/Exo2.0-Regular-webfont.ttf",
                "org/jspresso/framework/theme/fonts/Exo2.0-Regular-webfont.woff"
              ]
            },
            {
              family: "Exo",
              bold: true,
              italic: false,
              source: [
                "org/jspresso/framework/theme/fonts/Exo2.0-Bold-webfont.eot",
                "org/jspresso/framework/theme/fonts/Exo2.0-Bold-webfont.svg#exo_2.0bold",
                "org/jspresso/framework/theme/fonts/Exo2.0-Bold-webfont.ttf",
                "org/jspresso/framework/theme/fonts/Exo2.0-Bold-webfont.woff"
              ]
            },
            {
              family: "Exo",
              bold: false,
              italic: true,
              source: [
                "org/jspresso/framework/theme/fonts/Exo2.0-Italic-webfont.eot",
                "org/jspresso/framework/theme/fonts/Exo2.0-Italic-webfont.svg#exo_2.0italic",
                "org/jspresso/framework/theme/fonts/Exo2.0-Italic-webfont.ttf",
                "org/jspresso/framework/theme/fonts/Exo2.0-Italic-webfont.woff"
              ]
            },
            {
              family: "Exo",
              bold: true,
              italic: true,
              source: [
                "org/jspresso/framework/theme/fonts/Exo2.0-BoldItalic-webfont.eot",
                "org/jspresso/framework/theme/fonts/Exo2.0-BoldItalic-webfont.svg#exo_2.0bold_italic",
                "org/jspresso/framework/theme/fonts/Exo2.0-BoldItalic-webfont.ttf",
                "org/jspresso/framework/theme/fonts/Exo2.0-BoldItalic-webfont.woff"
              ]
            }
          ]
        },

        "bold": {
          include: "default",
          bold: true
        },

        "italic": {
          include: "default",
          italic: true
        },

        "bolditalic": {
          include: "default",
          bold: true,
          italic: true
        },

        "small": {
          include: "default",
          size: 11
        },

        "monospace": {
          include: "default",
          size: 12
        },

        "headline": {
          include: "default",
          size: 15
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
