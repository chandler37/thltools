<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="text" encoding="utf-8"/>
	
	<xsl:param name="prefix" select="''"/>
	
	<xsl:template match="/">
		<xsl:apply-templates select="//transcript"/>
	</xsl:template>
	
	<xsl:template match="transcript">
		<xsl:value-of select="$prefix"/><xsl:value-of select="."/><xsl:text>
</xsl:text>
	</xsl:template>
</xsl:stylesheet>
