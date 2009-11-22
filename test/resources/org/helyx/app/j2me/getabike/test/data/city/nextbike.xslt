<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="xml" />
	
	<xsl:template match="/">
		<cities>
			<xsl:apply-templates/>
		</cities>
	</xsl:template>
	
	<xsl:template match="/markers/country/city">  

		<city type="NEXTBIKE" 
		      serviceName="NextBike" normalizer="SIMPLE"
			  webSite="http://nextbike.de" 
			  stationList="http://nextbike.de/m/maps.php" 
			  stationDetails="">
			  <xsl:choose>
			  	<xsl:when test="../@domain='de'">
				  <xsl:attribute name="country">DEUTCHLAND</xsl:attribute>
			  	</xsl:when>
			  	<xsl:when test="../@domain='at'">
				  <xsl:attribute name="country">AUSTRIA</xsl:attribute>
			  	</xsl:when>
			  	<xsl:when test="../@domain='nz'">
				  <xsl:attribute name="country">NEWZEALAND</xsl:attribute>
			  	</xsl:when>
			  	<xsl:when test="../@domain='la'">
				  <xsl:attribute name="country">AUSTRIA</xsl:attribute>
			  	</xsl:when>
			  	<xsl:when test="../@domain='ch'">
				  <xsl:attribute name="country">SWITZERLAND</xsl:attribute>
			  	</xsl:when>
			  </xsl:choose>
			  <xsl:attribute name="key">
			  	<xsl:value-of select="upper-case(@name)" />
			  </xsl:attribute>
			  <xsl:attribute name="name">
			  	<xsl:value-of select="@name" />
			  </xsl:attribute>
	
			  <support details="false" localization="true" bonus="false" tpe="false" state="false" />
		
		</city>

	</xsl:template>

</xsl:stylesheet>