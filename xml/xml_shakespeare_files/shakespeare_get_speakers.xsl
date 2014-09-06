<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
	<xsl:for-each select="/PLAY/ACT/SCENE/SPEECH/SPEAKER[not(.=preceding::SPEAKER)]" >
		<xsl:sort select="."/>
		<xsl:value-of select="." />~
	</xsl:for-each>					
</xsl:template>
</xsl:stylesheet>