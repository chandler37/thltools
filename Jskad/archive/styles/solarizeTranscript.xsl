<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    
    <xsl:param name="TITLE_TYPE" select="'AVDB_TITLE'"/>
    <xsl:param name="TRANSCRIPT_FRAGMENT_TYPE" select="'TRANSCRIPT_FRAGMENT'"/>
    
    <xsl:template match="/">
        <xsl:apply-templates select="TITLE"/>
    </xsl:template>
    
    <xsl:template match="TITLE">
        <add>
            <xsl:variable name="title.id" select="@id"/>
            <xsl:apply-templates select="METADATA">
                <xsl:with-param name="title.id" select="$title.id"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="TEXT/S">
                <xsl:with-param name="title.id" select="$title.id"/>
            </xsl:apply-templates>
        </add>
    </xsl:template>
    
    <xsl:template match="METADATA">
        <xsl:param name="title.id" select="''"/>
        <doc>
            <field name="id"><xsl:value-of select="$title.id"/></field>
            <field name="thdl_type"><xsl:value-of select="$TITLE_TYPE"/></field>
            <field name="speechType"><xsl:value-of select="speechType"/></field>
            <field name="language"><xsl:value-of select="language"/></field>
            <field name="culturalRegion"><xsl:value-of select="culturalRegion"/></field>
            <field name="title_en"><xsl:value-of select="name"/></field>
            <field name="caption_en"><xsl:value-of select="caption"/></field>
            <field name="speechType"><xsl:value-of select="speechType"/></field>
            <field name="transcriptFilename"><xsl:value-of select="transcript"/></field>
            <!-- make plan for videos; incl. caressing #s so that you can search for ranges -->
        </doc>
    </xsl:template>
    
<!-- Here's what a chunk of metadata looks like:

      <speechType>Conversation</speechType>
      <language>Tibetan</language>
      <culturalRegion>dbus</culturalRegion>
      <name>A Lucky Dream: &lt;i&gt;Three's Company #01&lt;/i&gt;</name>
      <caption>CAPTION HEAD</caption>
      <transcript id="2502">00008_01-a-lucky-dream_09.xml</transcript>
      <video id="6613">			
         <mediaDescription>Audio</mediaDescription>			
         <connectionSpeed>medium</connectionSpeed>			
         <size>3225818</size>			
         <duration>00:02:14</duration>			
         <name>00008_lucky-dream.mp3</name>		
      </video>
      <video id="6612">			
         <mediaDescription>Video</mediaDescription>			
         <connectionSpeed>medium</connectionSpeed>			
         <size>5009798</size>			
         <duration>00:11:11</duration>			
         <name>00008_lucky-dream.mp4</name>		
      </video>
      <video id="6611">			
         <mediaDescription>Video</mediaDescription>			
         <connectionSpeed>fast</connectionSpeed>			
         <size>13556712</size>			
         <duration>00:11:11</duration>			
         <name>00008_lucky-dream.mp4</name>		
      </video>
-->    

    <xsl:template match="S">
        <xsl:param name="title.id" select="''"/>
        <doc>
            <field name="id"><xsl:value-of select="concat($title.id, '_', @id)"/></field>
            <field name="thdl_type"><xsl:value-of select="$TRANSCRIPT_FRAGMENT_TYPE"/></field>
            <field name="form_bo"><xsl:value-of select="FORM[@xml:lang='bo']"/></field>
            <field name="form_bo-Latn"><xsl:value-of select="FORM[@xml:lang='bo-Latn']"/></field>
            <field name="transl_en"><xsl:value-of select="TRANSL[@xml:lang='en']"/></field>
            <field name="transl_zh"><xsl:value-of select="TRANSL[@xml:lang='zh']"/></field>
            <xsl:if test="AUDIO/@start">
                <field name="start_f"><xsl:value-of select="AUDIO/@start"/></field>
            </xsl:if>
            <xsl:if test="AUDIO/@end">
                <field name="end_f"><xsl:value-of select="AUDIO/@end"/></field>
            </xsl:if>
        </doc>
    </xsl:template>
    
<!-- Here's what a chunk of transcript looks like:

      <S who="N400005" id="d148e29">
         <FORM xml:lang="bo">དེ་རིང་གནམ་ཡག་པོ་ར་ཅིག་༿འདྲ་ཅིག༾མི་འདུག་གས།</FORM>
         <FORM xml:lang="bo-Latn">de ring gnam yag po ra cig {'dra cig}mi 'dug gas/</FORM>
         <TRANSL xml:lang="en">Isn't it a nice day today?</TRANSL>
         <TRANSL xml:lang="zh">今天的天气多好啊, 是吧!</TRANSL>
         <AUDIO end="8.925999997392298" start="7.63"/>
      </S>
-->
    
</xsl:stylesheet>
