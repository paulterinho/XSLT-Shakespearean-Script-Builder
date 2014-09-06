<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">    
    <xsl:param name="who"/>    
    <xsl:template match="/">        
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simple"
                    page-height="29.7cm" 
                    page-width="21cm"
                    margin-top="1cm" 
                    margin-bottom="2cm" 
                    margin-left="2.5cm" 
                    margin-right="2.5cm">
                    <fo:region-body margin-top="3cm"/>
                    <fo:region-before extent="3cm"/>
                    <fo:region-after extent="1.5cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            
            
            <fo:page-sequence master-reference="simple">
                <fo:flow flow-name="xsl-region-body">
                    <xsl:apply-templates select="PLAY"/>
                </fo:flow>
            </fo:page-sequence>
            
        </fo:root> 
    </xsl:template>
    
    <xsl:template match="PLAY">
        <fo:block font-family="sans-serif" >
            <xsl:apply-templates select="TITLE"/>           
            <xsl:apply-templates select="ACT"/>   
            </fo:block>
    </xsl:template>
    
    <xsl:template match="ACT">
        <fo:block >
            <xsl:apply-templates select="TITLE"/>  
            <xsl:apply-templates select="SCENE"/>  
        </fo:block>
    </xsl:template>
    
    <xsl:template match="SCENE">
        <fo:block margin-left="30pt">
            <xsl:apply-templates select="TITLE"/>   
            <xsl:apply-templates select="SPEECH"/>   
        </fo:block>
    </xsl:template>
    
    <xsl:template match="TITLE">       
        <fo:block font-size="18pt" 
            font-family="sans-serif" 
            line-height="24pt"
            space-after.optimum="15pt"
            color="black"
            text-align="left"
            padding-top="3pt">
            <xsl:value-of select="."/>
        </fo:block> 
    </xsl:template>
   
      
    <xsl:template match="SPEECH">
        <fo:block font-size="12pt" 
            space-after.optimum="3pt"
            text-align="justify">
            <xsl:value-of select="SPEAKER"/>: 
            <xsl:apply-templates select="LINE"/>          
            
        </fo:block>
    </xsl:template>
    
    <xsl:template match="LINE">
        <xsl:choose>
            <!--<xsl:when test="../SPEAKER=$who">-->
            <xsl:when test="./preceding-sibling::SPEAKER=$who">            
                <fo:block 
                    margin-left="30pt"
                    color="gray"
                    space-after.optimum="3pt"
                    background-color="yellow"
                    text-align="justify">
                    <xsl:value-of select="."/>    
                </fo:block>
            </xsl:when>
            <xsl:otherwise>
                <fo:block 
                    margin-left="30pt"
                    color="gray"
                    space-after.optimum="3pt"
                    text-align="justify">
                    speaker,<xsl:value-of select="../SPEAKER"/>,<xsl:value-of select="$who"/>,<xsl:value-of select="."/>    
                </fo:block>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
   
</xsl:stylesheet>


