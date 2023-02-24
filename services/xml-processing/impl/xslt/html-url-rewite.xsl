<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:html="http://www.w3.org/1999/xhtml"
  exclude-result-prefixes="html" >

  <xsl:output method = "html" omit-xml-declaration = "yes"/> 

<!-- URI of base   page -->
  <xsl:param name="baseURI">
  	<xsl:text>/</xsl:text>
  </xsl:param>  

 <!-- name of GET param-->
  <xsl:param name="param-name">
	  <xsl:text>url</xsl:text>
  </xsl:param>
  
   <!-- used as prefix in GET param value-->
  <xsl:param name="link-prefix"/>
  

	<!-- drop html, head, body tags -->  
  <xsl:template match="html:html | html:body">
  	<xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="html:head">
 	 <xsl:comment>
  		<xsl:text>Parameter list: </xsl:text>
  		<xsl:text>baseURI=[</xsl:text><xsl:value-of select="$baseURI"/>  	
  		<xsl:text>] param-name=[</xsl:text><xsl:value-of select="$param-name"/>  	
  		<xsl:text>] link-prefix=[</xsl:text><xsl:value-of select="$link-prefix"/>  	  	
  		<xsl:text>]</xsl:text>
  	</xsl:comment> 
  </xsl:template>

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>



  <xsl:template match="a">
    <xsl:call-template  name = "anchor"/>
  </xsl:template>

	<xsl:template match="html:a" name="anchor">
		<xsl:variable name="url" select="@href"/>
		<xsl:element name="a" namespace="http://www.w3.org/1999/xhtml">
			<xsl:attribute name="href">
				<xsl:choose>
         		 		<!-- ignore absolute, mailto, news, javascript URLs -->
					<xsl:when test="contains($url, '://') or starts-with($url, 'mailto:') or starts-with($url, 'news:') or starts-with($url, 'javascript:')">
						<xsl:value-of select="$url"/>
					</xsl:when>				
   				
            				<!-- replace url -->
					<xsl:otherwise>			
						<xsl:variable name="new-url" select="translate($url, '?','&amp;')"/>
						<xsl:value-of select="$baseURI"/>
						<xsl:choose>				
							<xsl:when test="contains($baseURI,'?')">	
								<xsl:text>&amp;</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>?</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:value-of select="$param-name"/>
						<xsl:text>=</xsl:text>
						<xsl:value-of select="$link-prefix"/>
						<xsl:value-of select="$new-url"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:copy-of select="*"/> 
			<xsl:value-of select="text()"/> 
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>

