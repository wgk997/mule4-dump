<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:uni_inc="http://xmlns.oracle.com/CreateUnicenterIncident"
                xmlns:uni_ofco="http://xmlns.oracle.com/CreateOnlineFormsChangeOrder">
    <!--
    This XSL will match any of these elements that have zero child nodes and remove them from the SOAP request.
    That way we can treat empty elements as nil="true" but keep the benefits of validation.

    If you supply an empty <FuelSystem_Fuel/> element, it will be as if you omitted it. If you supply a non empty
    element, you will have to supply enough minOccurs="1" elements to be valid.
    -->
    <xsl:template match="uni_inc:FuelSystem_Fuel[not(node())]"/>
    <xsl:template match="uni_inc:FuelSystem_MeterReading[not(node())]"/>
    <xsl:template match="uni_inc:FuelSystem_TrakSync[not(node())]"/>
    <xsl:template match="uni_ofco:cab_approval[not(node())]"/>

    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
