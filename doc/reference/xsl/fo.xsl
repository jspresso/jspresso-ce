<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  exclude-result-prefixes="#default">

  <xsl:import href="file:///@DOCBOOK_XSL_HOME@/fo/docbook.xsl" />
  <xsl:import href="fo-titlepages.xsl" />

  <xsl:param name="paper.type">A4</xsl:param>
  <xsl:param name="fop1.extensions">1</xsl:param>
  <xsl:param name="graphicsize.extension">1</xsl:param>
  <xsl:param name="use.extensions">1</xsl:param>
  <xsl:param name="formal.title.placement">figure after</xsl:param>
  <xsl:param name="generate.index">1</xsl:param>
  <xsl:param name="ulink.show">0</xsl:param>
  <xsl:param name="shade.verbatim">1</xsl:param>
  <xsl:param name="highlight.source">1</xsl:param>
  <xsl:param name="body.start.indent">0pt</xsl:param>
  <xsl:param name="alignment">left</xsl:param>
  <xsl:param name="body.font.master">11</xsl:param>
  <xsl:param name="chapter.autolabel">I</xsl:param>
  <xsl:param name="section.autolabel">1</xsl:param>
  <xsl:param name="section.label.includes.component.label">1</xsl:param>

  <xsl:attribute-set name="monospace.properties">
    <xsl:attribute name="font-family">
      <xsl:value-of select="$monospace.font.family"></xsl:value-of>
    </xsl:attribute>
    <xsl:attribute name="font-size">8</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set
    name="monospace.verbatim.properties"
    use-attribute-sets="verbatim.properties monospace.properties">
    <xsl:attribute name="font-size">6</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set
    name="formal.title.properties"
    use-attribute-sets="normal.para.spacing">
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="hyphenate">false</xsl:attribute>
    <xsl:attribute name="space-after.minimum">0.4em</xsl:attribute>
    <xsl:attribute name="space-after.optimum">0.6em</xsl:attribute>
    <xsl:attribute name="space-after.maximum">0.8em</xsl:attribute>
    <xsl:attribute name="text-align">center</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="section.level1.properties">
    <xsl:attribute name="break-after">page</xsl:attribute>
  </xsl:attribute-set>

</xsl:stylesheet>