<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  exclude-result-prefixes="#default">

  <xsl:import href="file:///@DOCBOOK_XSL_HOME@/html/chunk.xsl" />
  <xsl:import href="html-titlepages.xsl" />

  <xsl:param name="html.stylesheet">../css/docbook.css</xsl:param>
  <xsl:param name="graphicsize.extension">1</xsl:param>
  <xsl:param name="use.extensions">1</xsl:param>
  <xsl:param name="ignore.image.scaling">1</xsl:param>
  <xsl:param name="formal.title.placement">figure after</xsl:param>
  <xsl:param name="generate.index">1</xsl:param>
  <xsl:param name="ulink.show">0</xsl:param>
  <xsl:param name="highlight.source">1</xsl:param>
  <xsl:param name="chapter.autolabel">I</xsl:param>
  <xsl:param name="section.autolabel">1</xsl:param>
  <xsl:param name="section.label.includes.component.label">1</xsl:param>

</xsl:stylesheet>