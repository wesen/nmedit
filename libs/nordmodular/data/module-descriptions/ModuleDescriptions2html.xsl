<?xml version="1.0" encoding="utf-8"?>
<!-- <!DOCTYPE xsl:stylesheet [ <!ENTITY nbsp "&#160;"> ]> -->
<!--
Copyright (c)  Christian Schneider.
Permission is granted to copy, distribute and/or modify this document
under the terms of the GNU Free Documentation License, Version 1.2
or any later version published by the Free Software Foundation;
with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
Texts.  A copy of the license is included in the section entitled "GNU
Free Documentation License".
-->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns="http://www.w3.org/1999/xhtml"
	            xmlns:md="http://nmedit.sf.net/ns/ModuleDescriptions"
                xml:lang="en" lang="en">

<xsl:output method = "html"
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
    doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
/>

<!-- prepare document -->
<xsl:template match="/">
<html>
  <head>
	<meta http-equiv="Content-type" content="text/html+xhtml; charset=UTF-8" />
    <title>Module Descriptions</title>
  </head>
  <style type="text/css">
  /* <![CDATA[<!-- */
  * {
    font-family:sans-serif;
  }
  body
  {
    font-family:sans-serif;
    font-size:11px;
  }
  div.properties
  {
    margin-left:20px;
  }
  h1
  {
    font-family:sans-serif;
    font-size:20px;
    font-weigh:bold;
  }
  h2
  {
    font-family:sans-serif;
    font-size:16px;
    font-weigh:bold;
  }
  div.header
  {
     border:solid lightgray 2px;
     background-color: #F0F0F0;
     padding: 10px;
     padding-top: 4px;
     padding-bottom: 20px;
  }
  div.dvModuleHeader
  {
     background-color:#E0E0E0;
     font-size:12px;
     padding:2px;
     padding-left: 4px;
     margin-top:10px;
    border: solid gray 1px;
  }
  .dvModSubCategoryConnector
  {
     background-color:#FFC1C2;
     padding: 2px;
    border: solid lightgray 1px;
  }
  .dvModSubCategoryParameter
  {
     background-color:#C3D4FF;
     padding: 2px;
    border: solid lightgray 1px;
  }
  .dvModSubCategoryAttribute
  {
     background-color:#D9FFBA;
     padding: 2px;
    border: solid lightgray 1px;
     text-align:left;
  }
  table
  {
     border-collapse:collapse;
     width:100%;
     background-color:#F3F3F3;
  }
  td
  {
    vertical-align:top;
    border: solid lightgray 1px;
    text-align: center;
  }
  thead tr
  {
  	background-color:#F0F0F0;
  }
  td.annotation
  {
    text-align: left;
    background-color: #E0F0E0;
    padding-bottom:10px;
  }
  code
  {
    font-family:'Lucida Console','Courier New',monospace;
    font-size:12px;
  }
  th {
    vertical-align:top;
    background-color:#F0E0E0;
    border: solid lightgray 1px;
  }
  tr.important
  {
    background-color:#E4E4E4;
  }
  img
  {}
  img.imageresource
  { margin-left:10px; }
  img.tableicon
  { border:0 none; }
  td.tablecell
  { 
     text-align:left;
     padding-left:4px;
     padding-right:4px;
     vertical-align:middle;
     padding-top:2px;
     padding-bottom:2px;
     height:18px;
     line-height:14px;
  }
  /* -->]]> */
  </style>
  <body>
	  <xsl:apply-templates select="md:ModuleDescriptions/md:header" />
	  
	<h1>Table of Contents</h1>
	 
	<ol>
		<li><a href="#annotation">Annotation</a></li>
		<li><a href="#modules">Modules</a></li>
		<li><a href="#definitions">Definitions</a></li>
	</ol>
	
	<a id="annotation"></a>
	<h1>1. Annotation</h1>
	  <xsl:apply-templates select="md:ModuleDescriptions/md:annotation" />
	  
	<a id="modules"></a>
	<h1>2. Modules</h1>
	  <xsl:apply-templates select="md:ModuleDescriptions/md:body" />
	  
	<a id="definitions"></a>
	<h1>3. Definitions</h1>
	  <xsl:apply-templates select="md:ModuleDescriptions/md:defs" />
	  
      <br />
      <br />
	  - end of file -
  </body>
</html>
</xsl:template>

<xsl:template match="md:vendor" >
	<strong>Vendor: </strong>
	<xsl:choose>
	    <xsl:when test="string-length(text())&gt;0">
			<xsl:apply-templates select="text()" />
		</xsl:when>
		<xsl:otherwise>
			- not specified -
		</xsl:otherwise>
	</xsl:choose>
	<br />
	<strong>URL: </strong>
	<xsl:choose>
	    <xsl:when test="string-length(@url)&gt;0">
			<a href="{@url}"><xsl:apply-templates select="@url" /></a>
		</xsl:when>
		<xsl:otherwise>
			- not specified -
		</xsl:otherwise>
	</xsl:choose>
	<br />
</xsl:template>

<xsl:template match="md:property" >
	<strong><xsl:value-of select="@name" />: </strong>
	<xsl:choose>
	    <xsl:when test="string-length(text())&gt;0">
			<xsl:apply-templates select="text()" />
		</xsl:when>
		<xsl:otherwise>
			- not specified -
		</xsl:otherwise>
	</xsl:choose>
	<br />
</xsl:template>

<xsl:template match="md:header" >
<div class="header">
	<h1>Module Descriptions </h1>
	
	<h2>Vendor</h2>
    <div class="properties">
	<xsl:apply-templates select="md:vendor" />
    </div>
	
	<h2>Device</h2>
	<div class="properties"> 
	
		<strong>Model: </strong>
		<xsl:choose>
		    <xsl:when test="string-length(md:device/md:model/text())&gt;0">
				<xsl:apply-templates select="md:device/md:model" />
			</xsl:when>
			<xsl:otherwise>
				- not specified -
			</xsl:otherwise>
		</xsl:choose>
		<br />
		<xsl:apply-templates select="md:device/md:vendor" />
		
        <xsl:if test="count(md:device/md:version)&gt;0">
			<xsl:for-each select="md:device/md:version">
				<strong><xsl:value-of select="@type" />: </strong>
				<xsl:apply-templates select="text()" />
				<br />
			</xsl:for-each>
		</xsl:if>

		<xsl:apply-templates select="md:device/md:property" />
    	</div>
	
	<xsl:if test="count(md:property)&gt;0">
		<h2>Properties</h2>
		<div class="properties"> 
		<xsl:apply-templates select="md:property" />
		</div>
	</xsl:if>
	
	<h2>Info</h2>
	<div class="properties">
		Generated by ModuleDescriptions2html.xsl for format version 1.3.<br />
		Format version of this document: <xsl:value-of select="/md:ModuleDescriptions/@version"/> <br />
		XSL stylesheet version: 2006-12-15
	</div>
</div>
</xsl:template>

<xsl:template match="md:annotation" >
	<xsl:apply-templates select="md:section" />
</xsl:template>
	
<xsl:template match="md:title" >
	<h2><xsl:apply-templates /></h2>
</xsl:template>
	
<xsl:template match="md:section" >
<div style="margin-left:20px">
	<xsl:apply-templates />
</div>
</xsl:template>
	
<xsl:template match="md:pre" >
	<pre><xsl:apply-templates /></pre>
</xsl:template>
	
<xsl:template match="md:list" >
<xsl:if test="count(md:item)&gt;0">3.


<ul>
	<xsl:for-each select="md:item">
		<li><xsl:apply-templates select="text()|md:code|md:mail|md:link" /></li>
	</xsl:for-each>
</ul>
</xsl:if>
</xsl:template>
	
<xsl:template match="md:code" >
	<code><xsl:apply-templates /></code>
</xsl:template>

<xsl:template match="md:mail" >
	<a href="mailto:{@mailto}"><xsl:apply-templates /></a>
</xsl:template>

<xsl:template match="md:link" >
	<a href="{@href}"><xsl:apply-templates /></a>
</xsl:template>

<xsl:template match="md:defs" >
	<xsl:choose>
		<xsl:when test="count(md:def-signal) = 1">
			<div style="margin-left:20px">
			<h2>Signal Types</h2>
			<table style="width:200px;">
				<tr>
					<th style="width:50px;">index</th> <th>type</th>
				</tr>
			<xsl:for-each select="md:def-signal/md:signal">
				<tr>
					<td><xsl:value-of select="@index" /></td>
					<td><xsl:value-of select="@type" /></td>
				</tr>
			</xsl:for-each>
			</table>
			</div>
		</xsl:when>
		<xsl:when test="count(md:def-signal)&gt;1">
			<div style="margin-left:20px">
			<h2>Signal Types</h2>
			- concurrent definitions found -
			</div>
		</xsl:when>
	</xsl:choose>
	
	<xsl:if test="count(md:def-type)&gt;0">
		<div style="margin-left:20px">
		<h2>Custom Types</h2>
		<xsl:for-each select="md:def-type">
			<strong><xsl:value-of select="@name" />: </strong>
			<br />
			
			<table style="width:200px;">
				<tr>
					<th style="width:50px;">index</th> <th>type</th>
				</tr>
			<xsl:for-each select="md:enumeration">
				<tr>
					<td><xsl:value-of select="@index" />:</td>
					<td><xsl:value-of select="@value" /></td>
				</tr>
			</xsl:for-each>
			</table>
			<br />
		</xsl:for-each>
		</div>
	</xsl:if>
	
</xsl:template>

<xsl:template match="md:body" >
	<a id="modtable"></a>
	
	<!-- print table -->	
	<xsl:variable name="data" select="md:module" /> 

	<!-- don't know why this does not work... -->

	<!--
	<xsl:variable name="data">
	  <xsl:for-each select="md:module">
	    <xsl:sort select="@name" data-type="text"
			order="ascending" case-order="lower-first" />
	       <xsl:copy-of select="." /> 
	  </xsl:for-each>
	</xsl:variable>-->

	<strong>Number of modules: </strong><xsl:value-of select="count($data)" /><br />
	
	<xsl:call-template name="createModuleTable">
		<xsl:with-param name="data" select="$data" />
		<xsl:with-param name="cols" select="number(5)" />
	</xsl:call-template>

	<xsl:for-each select="$data">
	    <xsl:sort select="@name" data-type="text"
			order="ascending" case-order="lower-first" />
		<xsl:apply-templates select="." mode="doc" />
	</xsl:for-each>
</xsl:template>

<xsl:template match="md:module" mode="doc">
<div>
	<xsl:variable name="aID" select="generate-id(.)" />
	<a id="{$aID}"></a>
	<div class="dvModuleHeader">
		<strong><xsl:value-of select="@name" /></strong>, Category:<xsl:value-of select="@category" />
		, index:<xsl:value-of select="@index" />
		(<a href="#modtable">up</a>)
	</div>
	
	<div style="margin-left:20px; margin-bottom:20px;margin-top:10px;">
			
		<xsl:if test="count(md:comment) = 1">
		<p><strong>Comment: </strong><xsl:apply-templates select="md:comment" /></p>
		</xsl:if>

		<xsl:if test="count(md:image)&gt;0">
			<p><strong>images:</strong> </p>
			<xsl:for-each select="md:image">
				<xsl:sort select="@type" />
				
				<xsl:value-of select="@type" /> 
				<img class="imageresource" src="{@src}" width="{@width}" height="{@height}" /> 
				<br />
			</xsl:for-each>
			<br />
		</xsl:if>

		<xsl:if test="count(md:connector)&gt;0">
			<div class="dvModSubCategoryConnector"><strong>connectors</strong> (<xsl:value-of select="count(md:connector)" />)</div>
			<table>
				<thead><tr><th>name</th><th>type</th><th>index</th><th>signal</th><th>class</th></tr></thead>
				<tbody>
					<xsl:apply-templates select="md:connector" mode="doc">
						<xsl:sort select="@type" />
					</xsl:apply-templates>
				</tbody>
			</table>
		</xsl:if>
		<xsl:if test="count(md:parameter)&gt;0">
			<br />
			<div class="dvModSubCategoryParameter"><strong>parameters</strong> (<xsl:value-of select="count(md:parameter)" />)</div>
			<table>
				<thead><tr><th>name</th><th>index</th>
					<th>min-value <br /> 
					<span style="font-weight:normal;">(default:0)</span></th>
					<th>max-value <br /> <span style="font-weight:normal;">(default:127)</span></th>
					<th>default-value <br /> <span style="font-weight:normal;">(default:0)</span></th><th>class</th></tr></thead>
				<tbody>
					<xsl:apply-templates select="md:parameter" mode="doc">
						<xsl:sort select="@class" />
					</xsl:apply-templates>
				</tbody>
			</table>
		</xsl:if>
		<xsl:if test="count(md:attribute)&gt;0">
			<br />
			<xsl:call-template name="printAttributeTable">
				<xsl:with-param name="attributes" select="md:attribute" />
			</xsl:call-template>
		</xsl:if>
	</div>
</div>
</xsl:template>
	
<xsl:template name ="printAttributeTable">
	<xsl:param name="attributes" />
	<xsl:if test="count($attributes)&gt;0">
	<div class="dvModSubCategoryAttribute"><strong>attributes</strong>
		(<xsl:value-of select="count($attributes)" />)</div>
	<table>
		<thead>
			<tr>
			<th>name</th>
			<th>type</th>
			<th>value</th>
			</tr>
		</thead>
		<tbody>
			<xsl:apply-templates select="$attributes" mode="doc" />
		</tbody>
	</table>
	</xsl:if>
</xsl:template>

<xsl:template match="md:attribute" mode="doc">
	<tr>
		<td style="width:200px;"><strong><xsl:value-of select="@name" /></strong></td>
		<td>
			<xsl:choose>
				<xsl:when test="@type">
					<xsl:value-of select="@type" />
				</xsl:when>
				<xsl:otherwise>
					string
				</xsl:otherwise>
			</xsl:choose>
		</td>
		<td>
			<xsl:choose>
				<xsl:when test="@value">
					<xsl:value-of select="@value" />
				</xsl:when>
				<xsl:when test="text()">
					<xsl:value-of select="text()" />
				</xsl:when>
				<xsl:otherwise>
					- no value -
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</tr>
</xsl:template>
	
<xsl:template match="md:parameter" mode="doc">
	<tr class="important">
		<td style="width:240px;"><strong><xsl:value-of select="@name" /></strong></td>
		<td>
			<xsl:call-template name="valueOrDash">
				<xsl:with-param name="val" select="@index" />
			</xsl:call-template>
		</td>
		<td>
			<!-- 
			<xsl:choose>
				<xsl:when test="@minValue"><xsl:value-of select="@minValue" /></xsl:when>
				<xsl:otherwise>0 (default)</xsl:otherwise>
			</xsl:choose>
				-->
			<xsl:call-template name="valueOrDash">
				<xsl:with-param name="val" select="@minValue" />
			</xsl:call-template>
		</td>
		<td>
			<!-- 
			<xsl:choose>
				<xsl:when test="@maxValue"><xsl:value-of select="@maxValue" /></xsl:when>
				<xsl:otherwise>127 (default)</xsl:otherwise>
			</xsl:choose>
				-->
			<xsl:call-template name="valueOrDash">
				<xsl:with-param name="val" select="@maxValue" />
			</xsl:call-template>
		</td>
		<td>
			<!--
			<xsl:choose>
				<xsl:when test="@defaultValue"><xsl:value-of select="@defaultValue" /></xsl:when>
				<xsl:otherwise>0 (default)</xsl:otherwise>
			</xsl:choose>
				-->
			<xsl:call-template name="valueOrDash">
				<xsl:with-param name="val" select="@defaultValue" />
			</xsl:call-template>
		</td>
		<td>
			<xsl:call-template name="valueOrDash">
				<xsl:with-param name="val" select="@class" />
			</xsl:call-template>
		</td>
	</tr>
	
	<xsl:if test="count(md:comment) = 1">
	<tr>
		<td></td>
		<td class="annotation" colspan="4">
			<strong>Comment: </strong><xsl:apply-templates select="md:comment" />
		</td>
	</tr>
	</xsl:if>
	
	<xsl:if test="@format-id">
	<tr>
		<td></td>
		<td class="annotation" colspan="4">
			<strong>format-id: </strong>
			<xsl:value-of select="@format-id" />
		</td>
	</tr>
	</xsl:if>
	
	<xsl:if test="@formatter">
	<tr>
		<td></td>
		<td class="annotation" colspan="4">
			<strong>formatter: </strong>
			<xsl:value-of select="@formatter" />
		</td>
	</tr>
	</xsl:if>
	
	<xsl:if test="count(md:attribute)&gt;0">
		<tr>
			<td></td>
			<td colspan="5">
				<xsl:call-template name="printAttributeTable">
					<xsl:with-param name="attributes" select="md:attribute" />
				</xsl:call-template>
			</td>
		</tr>
	</xsl:if>
</xsl:template>
	
<xsl:template name="valueOrDash">
	<xsl:param name="val" select="/.." />
	<xsl:choose>
		<xsl:when test="$val and string-length($val) &gt; 0">
			<xsl:value-of select="$val" />
		</xsl:when>
		<xsl:otherwise>-</xsl:otherwise>
	</xsl:choose>
</xsl:template>
	
<xsl:template match="md:connector" mode="doc">
<tr class="important">
	<td style="width:240px;"><strong><xsl:value-of select="@name" /></strong></td>
	<td>
		<xsl:call-template name="valueOrDash">
			<xsl:with-param name="val" select="@type" />
		</xsl:call-template>
	</td>
	<td>
		<xsl:call-template name="valueOrDash">
			<xsl:with-param name="val" select="@index" />
		</xsl:call-template>
	</td>
	<td>
		<xsl:call-template name="valueOrDash">
			<xsl:with-param name="val" select="@signal" />
		</xsl:call-template>
	</td>
	<td>
		<xsl:call-template name="valueOrDash">
			<xsl:with-param name="val" select="@class" />
		</xsl:call-template>
	</td>
</tr>
	<xsl:if test="count(md:comment) = 1">
	<tr>
		<td></td>
		<td class="annotation" colspan="4">
			<strong>Comment: </strong><xsl:apply-templates select="md:comment" />
		</td>
	</tr>
	</xsl:if>
	<xsl:if test="count(md:attribute)&gt;0">
    <tr>
		<td></td>
		<td colspan="4">
			<xsl:call-template name="printAttributeTable">
				<xsl:with-param name="attributes" select="md:attribute" />
			</xsl:call-template>
		</td>
	</tr>
	</xsl:if>
</xsl:template>
	
<xsl:template match="md:module" mode="tablecell">
	<xsl:variable name="image" select="md:image[@type='icon16x16']" />
	
	<xsl:for-each select="$image[position()=1]">
		<img class="tableicon" src="{@src}" width="{@width}" height="{@height}" />
        &#160;
	</xsl:for-each>
	<a href="#{generate-id()}">
		<xsl:value-of select="@name" /> 
	</a> (<xsl:value-of select="@index" />) 
</xsl:template>
	
<xsl:template name="createModuleTable">
	<xsl:param name="data" /> 
	<xsl:param name="cols" />

	<xsl:if test="count($data)&gt;0 and $cols&gt;0">
	<table>
		<xsl:variable name="rowcount" select="ceiling(count($data) div $cols)" />
		<xsl:call-template name="printRow">
			<xsl:with-param name="data" select="$data" />
			<xsl:with-param name="cols" select="$cols" />
			<xsl:with-param name="rowcount" select="$rowcount" />
			<xsl:with-param name="row" select="number(0)" />
		</xsl:call-template>
	</table>
	</xsl:if>
</xsl:template>
	
<xsl:template name="printRow">
	<xsl:param name="data" />
	<xsl:param name="cols" />
	<xsl:param name="rowcount" select="number(0)" />
	<xsl:param name="row" select="number(0)" />
	
	<xsl:if test="$row  &lt; $rowcount">
		<xsl:variable name="start" select="$row * $cols + 1" />
		<xsl:variable name="end" select="(($row + 1) * $cols) " />
		<!-- print row -->
		<tr>
			<xsl:for-each select="$data">
				<xsl:sort select="@category" datatype="text" />
				<xsl:sort select="@index"/>
				<xsl:sort select="@name" datatype="text" />
				<xsl:if test="position()&gt;=$start and position()&lt;=$end">
				<td class="tablecell">
					<xsl:apply-templates select="." mode="tablecell" />
				</td>
				</xsl:if>
			</xsl:for-each>
			<xsl:variable name="remaining" select="$end - count($data)" />
			<xsl:if test="$remaining&gt;0">
				<xsl:call-template name="printEmptyCells">
					<xsl:with-param name="num" select="$remaining" />
				</xsl:call-template>
			</xsl:if>
		</tr>
		
		<!-- next row -->
		<xsl:call-template name="printRow">
			<xsl:with-param name="data" select="$data" />
			<xsl:with-param name="cols" select="$cols" />
			<xsl:with-param name="rowcount" select="$rowcount" />
			<xsl:with-param name="row" select="$row+1" />
		</xsl:call-template>
	</xsl:if>
</xsl:template>
	
<xsl:template name="printEmptyCells">
	<xsl:param name="num" select="number(0)" />

	<xsl:if test="$num&gt;0">
		<td>-</td>
		<xsl:call-template name="printEmptyCells">
			<xsl:with-param name="num" select="$num - 1" />
		</xsl:call-template>
	</xsl:if>
</xsl:template>
	


</xsl:stylesheet>
