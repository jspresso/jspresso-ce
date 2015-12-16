<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  exclude-result-prefixes="#default">

  <xsl:import href="urn:docbkx:stylesheet"/>
  <xsl:import href="urn:docbkx:stylesheet/highlight.xsl"/>
  <xsl:import href="fo-titlepages.xsl" />

  <xsl:param name="paper.type">A4</xsl:param>
  <xsl:param name="fop1.extensions">1</xsl:param>
  <xsl:param name="tablecolumns.extension">1</xsl:param>
  <xsl:param name="graphicsize.extension">1</xsl:param>
  <xsl:param name="use.extensions">1</xsl:param>
  <xsl:param name="formal.title.placement">
    figure after
    example after
    equation after
    table after
    procedure after
    task after
  </xsl:param>
  <xsl:param name="generate.index">1</xsl:param>
  <xsl:param name="ulink.show">0</xsl:param>
  <xsl:param name="shade.verbatim">1</xsl:param>
  <xsl:param name="highlight.source">1</xsl:param>
  <xsl:param name="body.start.indent">1em</xsl:param>
  <xsl:param name="alignment">left</xsl:param>
  <xsl:param name="body.font.master">11</xsl:param>
  <xsl:param name="body.font.family">sans-serif</xsl:param>
  <xsl:param name="chapter.autolabel">I</xsl:param>
  <xsl:param name="section.autolabel">1</xsl:param>
  <xsl:param name="section.autolabel.max.depth">3</xsl:param>
  <xsl:param name="section.label.includes.component.label">1</xsl:param>
  <xsl:param name="title.color">#884444</xsl:param>

  <xsl:param name="page.margin.inner">
    <xsl:choose>
      <xsl:when test="$double.sided != 0">0.75in</xsl:when>
      <xsl:otherwise>0.5in</xsl:otherwise>
    </xsl:choose>
  </xsl:param>
  <xsl:param name="page.margin.outer">
    <xsl:choose>
      <xsl:when test="$double.sided != 0">0.75in</xsl:when>
      <xsl:otherwise>0.5in</xsl:otherwise>
    </xsl:choose>
  </xsl:param>

  <xsl:attribute-set
    name="monospace.verbatim.properties">
    <xsl:attribute name="font-size">
      <xsl:value-of select="$body.font.master * 0.8"></xsl:value-of>
      <xsl:text>pt</xsl:text>
    </xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="shade.verbatim.style">
    <xsl:attribute name="border">thin black solid</xsl:attribute>
    <xsl:attribute name="padding">4pt</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set
    name="formal.title.properties">
    <xsl:attribute name="text-align">center</xsl:attribute>
    <xsl:attribute name="font-size">
      <xsl:value-of select="$body.font.master * 0.8"></xsl:value-of>
      <xsl:text>pt</xsl:text>
    </xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="component.title.properties">
    <xsl:attribute name="color"><xsl:value-of select="$title.color"/></xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="section.title.properties">
    <xsl:attribute name="space-before.minimum">2em</xsl:attribute>
    <xsl:attribute name="space-before.optimum">4em</xsl:attribute>
    <xsl:attribute name="space-before.maximum">4em</xsl:attribute>
    <xsl:attribute name="hyphenate">false</xsl:attribute>
    <xsl:attribute name="color"><xsl:value-of select="$title.color"/></xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="section.level1.properties">
    <xsl:attribute name="break-after">page</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="xref.properties">
    <xsl:attribute name="text-decoration">underline</xsl:attribute>
    <xsl:attribute name="color">blue</xsl:attribute>
  </xsl:attribute-set> 

  <xsl:attribute-set name="table.properties">
    <xsl:attribute name="keep-together.within-column">
      <xsl:choose>
        <xsl:when test="@tabstyle='splitable'">auto</xsl:when>
        <xsl:otherwise>always</xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:template name="table.row.properties">
      <xsl:if test="ancestor::thead">
          <xsl:attribute name="background-color">#EEEEEE</xsl:attribute>
      </xsl:if>
  </xsl:template>
  <xsl:attribute-set name="table.cell.padding">
    <xsl:attribute name="padding-left">0pt</xsl:attribute>
    <xsl:attribute name="padding-right">5pt</xsl:attribute>
    <xsl:attribute name="padding-top">2pt</xsl:attribute>
    <xsl:attribute name="padding-bottom">2pt</xsl:attribute>
  </xsl:attribute-set>

  
  <!-- 
  <xsl:template name="hyphenate-url">
    <xsl:param name="url" select="''"/>
    <xsl:choose>
      <xsl:when test="$ulink.hyphenate = ''">
        <xsl:value-of select="$url"/>
      </xsl:when>
      <xsl:when test="string-length($url) &gt; 1">
        <xsl:variable name="char" select="substring($url, 1, 1)"/>
        <xsl:value-of select="$char"/>
        <xsl:if test="contains($ulink.hyphenate.chars, $char)">
          <xsl:if test="not($char = '/' and substring($url,2,1) = '/')">
            <xsl:copy-of select="$ulink.hyphenate"/>
          </xsl:if>
        </xsl:if>
        <xsl:call-template name="hyphenate-url">
          <xsl:with-param name="url" select="substring($url, 2)"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$url"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  -->
  
  <xsl:param name="cell.hyphenate.chars">.,-&gt;&lt;</xsl:param>
  <xsl:template name="hyphenate-cell">
    <xsl:param name="content" select="''"/>
    <xsl:choose>
      <xsl:when test="string-length($content) &gt; 2">
        <xsl:variable name="char" select="substring($content, 1, 1)"/>
        <xsl:variable name="nextChar" select="substring($content, 2, 2)"/>
        <xsl:if test="contains($cell.hyphenate.chars, $char) and not(' ' = $nextChar)">
          <xsl:text>&#x200B;</xsl:text>
        </xsl:if>
        <xsl:value-of select="$char"/>
        <xsl:call-template name="hyphenate-cell">
          <xsl:with-param name="content" select="substring($content, 2)"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$content"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="entry//text()">
    <xsl:call-template name="hyphenate-cell">
      <xsl:with-param name="content" select="."/>
    </xsl:call-template>
  </xsl:template>
  
</xsl:stylesheet>