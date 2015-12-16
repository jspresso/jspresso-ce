<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  exclude-result-prefixes="#default">

  <xsl:import href="urn:docbkx:stylesheet"/>
  <xsl:import href="urn:docbkx:stylesheet/highlight.xsl"/>
  <xsl:import href="html-titlepages.xsl" />

  <xsl:param name="graphicsize.extension">1</xsl:param>
  <xsl:param name="tablecolumns.extension">1</xsl:param>
  <xsl:param name="use.extensions">1</xsl:param>
  <xsl:param name="ignore.image.scaling">1</xsl:param>
  <xsl:param name="formal.title.placement">figure after</xsl:param>
  <xsl:param name="generate.index">1</xsl:param>
  <xsl:param name="ulink.show">0</xsl:param>
  <xsl:param name="highlight.source">1</xsl:param>
  <xsl:param name="chapter.autolabel">I</xsl:param>
  <xsl:param name="section.autolabel">1</xsl:param>
  <xsl:param name="section.autolabel.max.depth">3</xsl:param>
  <xsl:param name="section.label.includes.component.label">1</xsl:param>
  <xsl:param name="html.cellpadding">5</xsl:param>

  <xsl:param name="shade.verbatim" select="1"/>
  <xsl:attribute-set name="shade.verbatim.style">
    <xsl:attribute name="padding">4pt</xsl:attribute>
    <xsl:attribute name="border-width">0.5pt</xsl:attribute>
    <xsl:attribute name="border-style">solid</xsl:attribute>
    <xsl:attribute name="border-color">#575757</xsl:attribute>
  </xsl:attribute-set>
  
</xsl:stylesheet>