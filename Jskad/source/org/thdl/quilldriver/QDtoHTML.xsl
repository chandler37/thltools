<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns:jskad="xalan://org.thdl.tib.text">
	
<xsl:output method="html"/>
<xsl:template match="qd">
<html>
<head>
<style>
<xsl:value-of select="jskad:TibetanHTML.getStyles(28)" disable-output-escaping="yes"/>
</style>
</head>
<body>
<xsl:apply-templates select="metadata/title" />
<xsl:apply-templates select="text" />
<xsl:apply-templates select="metadata/workhistory" />
</body>
</html>
</xsl:template>

<xsl:template match="title">
<xsl:value-of select="." />
<p />
</xsl:template>

<xsl:template match="text">
<xsl:apply-templates select="node()" />
</xsl:template>

<xsl:template match="who">
<p />
<xsl:variable name="idvar" select="@id" />
<xsl:variable name="name" select="../../metadata/speakers/speaker[@iconid=$idvar]" />
<u><xsl:value-of select="jskad:TibetanHTML.getHTML($name)" disable-output-escaping="yes"/></u>
</xsl:template>

<xsl:template match="text()">
<xsl:value-of select="jskad:TibetanHTML.getIndentedHTML(.)" disable-output-escaping="yes"/>
</xsl:template>

<xsl:template match="workhistory">
<p />
<b>Work History</b>
<p/>
<table width="100%" border="1">
<tr>
<th>Name</th>
<th>Task</th>
<th>Start Time</th>
<th>Duration</th>
</tr>
<xsl:apply-templates select="work"/>
</table>
</xsl:template>

<xsl:template match="work">
<tr>
<td><xsl:value-of select="name" disable-output-escaping="yes"/></td>
<td><xsl:value-of select="task" disable-output-escaping="yes"/></td>
<td><xsl:value-of select="start" /></td>
<td><xsl:value-of select="duration" /> minutes</td>
</tr>
</xsl:template>

</xsl:stylesheet>