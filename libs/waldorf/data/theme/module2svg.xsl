<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/2000/svg"
    xmlns:svg="http://www.w3.org/2000/svg"
    xmlns:xlink="http://www.w3.org/1999/xlink">
>
<!--
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:html="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/2000/svg"
    exclude-result-prefixes="html"
> -->

    <xsl:output 
          method="xml" 
          doctype-public="-//W3C//DTD SVG 1.0//EN" 
          doctype-system="http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd" 
          encoding="ISO-8859-1" 
          media-type="image/svg+xml" 
          version="1.0" 
          indent="yes"
      />

<xsl:template name="generateHeight">
    <xsl:param name="margin" />
    <xsl:param name="moduleList" />
    <xsl:param name="pos" />
    <xsl:param name="totalHeight" />
    
    <xsl:choose>
      <xsl:when test="$pos &lt;= count($moduleList)">
        <xsl:variable name="module" select="$moduleList[$pos]" />
        <xsl:call-template name="generateHeight">
            <xsl:with-param name="margin" select="$margin" />
		    <xsl:with-param name="moduleList" select="$moduleList" />
		    <xsl:with-param name="pos" select="($pos)+1" />
		    <xsl:with-param name="totalHeight" select="($totalHeight)+($margin)+($module/@height)" />
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
		  <xsl:attribute name="height">
		  	<xsl:value-of select="($totalHeight)" />
		  </xsl:attribute>
      </xsl:otherwise>
    </xsl:choose>
</xsl:template>
      
<xsl:template match="/">

<svg xmlns:xlink="http://www.w3.org/1999/xlink">

  <xsl:attribute name="version">1.0</xsl:attribute>
  <xsl:attribute name="width">
    <xsl:for-each select="modules/module">
      <xsl:sort select="@width" order="descending" data-type="number" />
      <xsl:if test="position()=1">
	      <xsl:value-of select="(@width)+20" />
      </xsl:if>
    </xsl:for-each>
  </xsl:attribute>
  
  <xsl:call-template name="generateHeight">
    <xsl:with-param name="margin" select="number(20)" />
    <xsl:with-param name="moduleList" select="modules/module" />
    <xsl:with-param name="pos" select="number(1)" />
    <xsl:with-param name="totalHeight" select="number(100)" />
  </xsl:call-template>
  <!--
  <xsl:attribute name="height">
    <xsl:for-each select="modules/module">
      <xsl:sort select="@height" order="descending" data-type="number" />
      <xsl:if test="position()=1">
	      <xsl:value-of select="((@height)+20)*count(../module)" />
      </xsl:if>
    </xsl:for-each>  
  </xsl:attribute>
-->

<defs>
  <style type="text/css">
   <![CDATA[
     text { font-family:Helvetica,,sans-serif;font-size:11px;font-weight:normal;
     alignment-baseline:top;
     }
     text.modName { fill:black; font-size:12px;}
     
     rect.unknownComponent
     {
       stroke-width:1px;
       stroke:black;
       stroke-opacity:.4;
       fill:#00AA00;
       fill-opacity:.2;
       stroke-dasharray: 3, 1;
     }
      .textDisplay
      {
         fill:white;
         fill-opacity:1;
         stroke:black;
         stroke-width:1px;
      }
   ]]>
   
   <xsl:variable name="userCSS" select="modules/style/text()" />
   
   <!-- fix module css class -->
   <xsl:variable name="fixCSS1">
     <xsl:choose>
       <xsl:when test="contains($userCSS,'module')">
         <xsl:value-of select="concat(substring-before($userCSS, 'module'),'.module',substring-after($userCSS,'module'))" />
       </xsl:when>
       <xsl:otherwise><xsl:value-of select="$userCSS" /></xsl:otherwise>
     </xsl:choose>
   </xsl:variable>
   <xsl:variable name="fixCSS2">
     <xsl:choose>
       <xsl:when test="contains($fixCSS1,'knob')">
         <xsl:value-of select="concat(substring-before($fixCSS1, 'knob'),'.knob',substring-after($fixCSS1,'knob'))" />
       </xsl:when>
       <xsl:otherwise><xsl:value-of select="$fixCSS1" /></xsl:otherwise>
     </xsl:choose>
   </xsl:variable>
   <xsl:variable name="fixCSS3">
     <xsl:choose>
       <xsl:when test="contains($fixCSS2,'label')">
         <xsl:value-of select="concat(substring-before($fixCSS2, 'label'),'.label',substring-after($fixCSS1,'label'))" />
       </xsl:when>
       <xsl:otherwise><xsl:value-of select="$fixCSS2" /></xsl:otherwise>
     </xsl:choose>
   </xsl:variable>
   
   <xsl:variable name="fixCSS4">
     <xsl:choose>
       <xsl:when test="contains($fixCSS3,'textDisplay')">
         <xsl:value-of select="concat(substring-before($fixCSS3, 'textDisplay'),'.textDisplay',substring-after($fixCSS3,'textDisplay'))" />
       </xsl:when>
       <xsl:otherwise><xsl:value-of select="$fixCSS3" /></xsl:otherwise>
     </xsl:choose>
   </xsl:variable>
   <xsl:value-of select="$fixCSS4" /> 
  </style>
   <radialGradient id = "knobFill" cx = "75%" cy = "75%" r = "100%">
      <stop stop-color = "#DDDDDD" offset = "0%"/>
      <stop stop-color = "teal" offset = "75%"/>
      <stop stop-color = "white" offset = "100%"/>
   </radialGradient>
</defs>


<g transform="translate(10,10)">
  <xsl:call-template name="modules">
    <xsl:with-param name="moduleList" select="modules/module" />
    <xsl:with-param name="pos" select="number(1)" />
    <xsl:with-param name="y" select="number(20)" />
  </xsl:call-template>
</g>
</svg>
</xsl:template>

<xsl:template name="modules">
  <xsl:param name="moduleList" />
  <xsl:param name="pos" />
  <xsl:param name="y" />
  
  <xsl:if test="$pos&lt;=count($moduleList)">
    <xsl:variable name="module" select="$moduleList[$pos]" />
    <g transform="translate(0,{$y})">
      <xsl:apply-templates select="$module" />
    </g>
	  <xsl:call-template name="modules">
	    <xsl:with-param name="moduleList" select="$moduleList" />
	    <xsl:with-param name="pos" select="($pos)+1" />
	    <xsl:with-param name="y" select="($y)+($module/@height)+(20)" />
	  </xsl:call-template>
  </xsl:if>
</xsl:template>

<xsl:template match="module">

 <text x="5" y="-5" class="modName"><tspan style="font-weight:bold;"><xsl:apply-templates select="name" /></tspan> (
 <xsl:for-each select="@*">
   <xsl:value-of select="name(.)" />=<xsl:value-of select="." />;
 </xsl:for-each>)
 
 </text>
   <rect>
      <xsl:attribute name="width">
        <xsl:value-of select="@width" />
      </xsl:attribute>
      
      <xsl:attribute name="height">
        <xsl:value-of select="@height" />
      </xsl:attribute>
      <xsl:attribute name="stroke">
        <xsl:value-of select="'rgb(0,0,0)'" />
      </xsl:attribute>
      <xsl:attribute name="stroke-width">
        <xsl:value-of select="1" />
      </xsl:attribute>
      <xsl:if test="not(contains(@style,'fill'))">
	      <xsl:attribute name="fill">
	        <xsl:value-of select="'#DDDDDD'" />
	      </xsl:attribute>
      </xsl:if>
      <xsl:choose>
        <xsl:when test="string-length(@class)&gt;0">
          <xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="class">module</xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:if test="string-length(@style)&gt;0">
	      <xsl:attribute name="style">
	        <xsl:value-of select="@style" />
	      </xsl:attribute>
      </xsl:if>
   </rect>
   
   <xsl:for-each select="./*">
      <xsl:choose>
         <xsl:when test="name()='connector'">     
	         <xsl:apply-templates select="." />   
	     </xsl:when>
         <xsl:when test="name()='knob'">     
	         <xsl:apply-templates select="." />   
	     </xsl:when>
         <xsl:when test="name()='image'">     
	         <xsl:apply-templates select="." />   
	     </xsl:when>
         <xsl:when test="name()='label'">     
	         <xsl:apply-templates select="." />   
	     </xsl:when>
         <xsl:when test="name()='textDisplay'">     
	         <xsl:apply-templates select="." />   
	     </xsl:when>
         <xsl:when test="name()='button'">     
	         <xsl:apply-templates select="." />   
	     </xsl:when>
         <xsl:when test="name()='slider'">     
	         <xsl:apply-templates select="." />   
	     </xsl:when>
	     <xsl:otherwise>
	        <xsl:call-template name="compFallback">
	          <xsl:with-param name="node" select="." />
	        </xsl:call-template>
	        
	     </xsl:otherwise>
      </xsl:choose>
   </xsl:for-each>

</xsl:template>

<xsl:template name="compFallback">
  <xsl:param name="node" />
  
  <xsl:if test="string-length($node/@x)&gt;0 and string-length($node/@y)&gt;0">

    <xsl:choose>
      <xsl:when test="string-length($node/@width)&gt;0 and string-length($node/@height)&gt;0">
		<rect class="unknownComponent" x="{@x}" y="{@y}" width="{@width}" height="{@height}" />
	  </xsl:when>
      <xsl:when test="string-length($node/@size)&gt;0">
		<rect class="unknownComponent" x="{@x}" y="{@y}" width="{@size}" height="{@size}" />
	  </xsl:when>
    </xsl:choose>

  </xsl:if>
  
</xsl:template>

<xsl:template match="label">
	<text>
      <xsl:attribute name="x"><xsl:value-of select="@x" /></xsl:attribute>
      <xsl:attribute name="y"><xsl:value-of select="@y" /></xsl:attribute>
      <xsl:attribute name="style"><xsl:value-of select="@style" /></xsl:attribute>
      <xsl:choose>
        <xsl:when test="string-length(@class)&gt;0">
          <xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="class">label</xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>
	
	  <xsl:apply-templates />
	</text>
</xsl:template>

<xsl:template name="copy-svg">
  <xsl:param name="node" />
  
    <xsl:copy>
        <xsl:copy-of select="*|@*" />
        <xsl:apply-templates select="node()"/>
	    <xsl:for-each select="$node/*">
		   <xsl:call-template name="copy-svg">
		     <xsl:with-param name="node" select="." />
		   </xsl:call-template>
	    </xsl:for-each>
    </xsl:copy>
    
</xsl:template>

<xsl:template match="svg:svg">
<xsl:variable name="clipId" select="'rectangle'+generate-id()" />

<clipPath id="{$clipId}">
<rect x="0" y="0" width="{@width}" height="{@height}" />
</clipPath>

<g clip-path="url(#{$clipId})">
	    <xsl:for-each select="./*">
		   <xsl:call-template name="copy-svg">
		     <xsl:with-param name="node" select="." />
		   </xsl:call-template>
	    </xsl:for-each>
</g>
</xsl:template>

<xsl:template match="image">
  <xsl:choose>
    <xsl:when test="string-length(@xlink:href)&gt;0">
      <xsl:choose>
        <xsl:when test="starts-with(@xlink:href, 'url(#')">    
	        <xsl:variable name="imgid" select="substring-before(substring-after(@xlink:href, 'url(#'), ')')" />
        	<xsl:variable name="href" select="/modules/defs/image[@id=$imgid]/@xlink:href" />
        	
			<image x="{@x}" y="{@y}" width="{@width}" height="{@height}" xlink:href="{$href}" />
			
        </xsl:when>
        <xsl:otherwise>
		  <image x="{@x}" y="{@y}" width="{@width}" height="{@height}"
		     xlink:href="{@xlink:href}" />
	     </xsl:otherwise>
	  </xsl:choose>
    </xsl:when>
    <xsl:otherwise>
      <g transform="translate({@x},{@y})">
        <xsl:apply-templates select="svg:svg" />
      </g>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="connector">
 
<xsl:choose>
  <xsl:when test="(./connector/@type)='output'">
  <rect>
      <xsl:attribute name="x">
        <xsl:value-of select="@x" />
      </xsl:attribute>
      <xsl:attribute name="y">
        <xsl:value-of select="@y" />
      </xsl:attribute>
      <xsl:attribute name="width">
        <xsl:value-of select="@size" />
      </xsl:attribute>
      <xsl:attribute name="height">
        <xsl:value-of select="@size" />
      </xsl:attribute>
      <xsl:choose>
        <xsl:when test="string-length(@class)&gt;0">
          <xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="class">connector</xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:attribute name="stroke">
        <xsl:value-of select="'#000'" />
      </xsl:attribute>
      <xsl:attribute name="stroke-width">
        <xsl:value-of select="0.75" />
      </xsl:attribute>
    <xsl:if test="string-length(@style)&gt;0">
	    <xsl:attribute name="style">
		<xsl:value-of select="@style" />
	    </xsl:attribute>
    </xsl:if>
  </rect>
  </xsl:when>
  <xsl:otherwise>
  <xsl:variable name="radius" select="@size div 2"/>
  <circle>
      <xsl:attribute name="cx">
        <xsl:value-of select="@x + $radius" />
      </xsl:attribute>
      <xsl:attribute name="cy">
        <xsl:value-of select="@y + $radius" />
      </xsl:attribute>
      <xsl:choose>
        <xsl:when test="string-length(@class)&gt;0">
          <xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="class">connector</xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:attribute name="r">
        <xsl:value-of select="$radius" />
      </xsl:attribute>
      <xsl:attribute name="stroke">
        <xsl:value-of select="'#000'" />
      </xsl:attribute>
      <xsl:attribute name="stroke-width">
        <xsl:value-of select="0.75" />
      </xsl:attribute>
    <xsl:if test="string-length(@style)&gt;0">
	    <xsl:attribute name="style">
		<xsl:value-of select="@style" />
	    </xsl:attribute>
    </xsl:if>
  </circle>
   
  </xsl:otherwise>
</xsl:choose>
</xsl:template>

<xsl:template match="button">
  <rect fill="#AAA" style="fill-opacity:.8;stroke:black;stroke-width:1px;stroke-opacity:1;"
   class="{@class}" x="{@x}" y="{@y}" width="{(@width)-1}" height="{(@height)-1}" />
   
   <xsl:if test="count(./btn)&gt;0">
     <text x="{(@x)+2}" y="{(@y)+10}"><xsl:value-of select="./btn[position()=1]" /></text>
   </xsl:if>
   
</xsl:template>

<xsl:template match="slider">
  <rect fill="#AAA" style="fill-opacity:.8;stroke:black;stroke-width:1px;stroke-opacity:1;"
   class="{@class}" x="{@x}" y="{@y}" width="{(@width)-1}" height="{(@height)-1}" />
</xsl:template>

<xsl:template match="textDisplay">
<g transform="translate({@x}, {@y})">
  <rect>
    <xsl:attribute name="x"><xsl:value-of select="'0'" /></xsl:attribute>
    <xsl:attribute name="y"><xsl:value-of select="'0'" /></xsl:attribute>
    <xsl:attribute name="width"><xsl:value-of select="(@width)-2" /></xsl:attribute>
    <xsl:attribute name="height"><xsl:value-of select="(@height)-2" /></xsl:attribute>
    
     <xsl:choose>
       <xsl:when test="string-length(@class)&gt;0" >
         <xsl:attribute name="class"><xsl:value-of select="@class" /></xsl:attribute>
       </xsl:when>
       <xsl:otherwise>
        	<xsl:attribute name="class">textDisplay</xsl:attribute>
      </xsl:otherwise>
     </xsl:choose>
  </rect>
  
  <xsl:if test="count(./parameter)&gt;0 and string-length(./parameter/@alt)&gt;0">
    <xsl:variable name="clipid" select="'tclip'+(generate-id())" />
    <clipPath id="{$clipid}">
		<rect x="0" y="0" width="{@width}" height="{@height}" />
	</clipPath>
    <text clip-path="url(#{$clipid})" x="2" y="8">(<xsl:value-of select="./parameter/@alt" />)</text>
  </xsl:if>
</g>
</xsl:template>

<xsl:template match="knob">
  <xsl:variable name="radius" select="@size div 2"/>
  <circle>
      <xsl:attribute name="cx">
        <xsl:value-of select="@x + $radius" />
      </xsl:attribute>
      <xsl:attribute name="cy">
        <xsl:value-of select="@y + $radius" />
      </xsl:attribute>
      <xsl:choose>
        <xsl:when test="string-length(@class)&gt;0">
          <xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="class">knob</xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:attribute name="r">
        <xsl:value-of select="$radius" />
      </xsl:attribute>
      <xsl:attribute name="fill">
        <xsl:value-of select="'url(#knobFill)'" />
      </xsl:attribute>
      <xsl:attribute name="stroke">
        <xsl:value-of select="'#000'" />
      </xsl:attribute>
      <xsl:attribute name="stroke-width">
        <xsl:value-of select="0.75" />
      </xsl:attribute>
    <xsl:if test="string-length(@style)&gt;0">
	    <xsl:attribute name="style">
		<xsl:value-of select="@style" />
	    </xsl:attribute>
    </xsl:if>
  </circle>
  <line x1="{@x + $radius}" y1="{@y + $radius}" 
        x2="{@x + $radius}" y2="{@y}"
        stroke="#000" stroke-width="1" />
</xsl:template>

</xsl:stylesheet>   