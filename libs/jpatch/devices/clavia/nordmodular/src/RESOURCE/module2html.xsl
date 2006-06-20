<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [ <!ENTITY nbsp "&#160;"> ]>
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
                xml:lang="en" lang="en">

<xsl:output method = "html"
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
    doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
/>

<!-- prepare document -->
<xsl:template match="/">
<html>
  <head>
    <meta http-equiv="content-type" content="text/xhtml+xml; charset=UTF-8" />
    <title>Module Definitions</title>
  </head>
  <style type="text/css">
  <![CDATA[<!--
  table
  {
    border-collapse:collapse;
  }
  html
  {
    padding:0;
    margin:0;
  }
  body
  {
    margin:10pt;
    padding:0pt;
  }
  td
  {
    vertical-align:top;
    border:solid #C0C0C0 1px;
  }
  td.tdindex
  {
    border:none 0;
  }
  p.group
  {
    font-weight:bold;
  }
  li.liindex
  {
  }
  ul.ulindex
  {
  list-style-type:square;
  list-style-position:inside;
  margin-left: 0;
  padding-left:5pt;
  }
  div.preamble
  {
    border:solid lightgray 2px;
    background-color:#EEEEDD;
    padding-left: 20pt;
  }
  div.preamblecontent
  {
    border:solid lightgray 2px;
    background-color:#C8CDEE;
    margin-top: 20pt;
    padding-left: 10pt;
    padding-top: 2pt;
    padding-bottom: 2pt;
  }
  div.content
  {
    margin-left: 10pt;
    padding-left: 0pt;
  }
  p.property
  {
    font-weight:bold;
  }
  p.value
  {
    margin-left:40pt;
  }
  -->]]>
  </style>
  <body>
    <p><a name="top" id="top"></a></p>
    <xsl:call-template name="preamble" />
    <xsl:call-template name="preamblecontent" />
    <div class="content">
    <xsl:apply-templates select="/modules-definition/structure" />
    <xsl:apply-templates select="/modules-definition/modules" />
    </div>
  </body>
</html>
</xsl:template>

<xsl:template name="preamble">
<div class="preamble">
<h1>Module Definitions Version 1.0</h1>
<h2>31.05.2006</h2>
<p class="property">Editor(s):</p>
<p class="value">
Christian Schneider &lt;<a href="mailto:christianschneider@fastmail.fm">christianschneider@fastmail.fm</a>&gt;
</p>

<h2>Licence</h2>
<pre>Copyright (c)  YEAR  YOUR NAME.
Permission is granted to copy, distribute and/or modify this document
under the terms of the GNU Free Documentation License, Version 1.2
or any later version published by the Free Software Foundation;
with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
Texts.  A copy of the license is included in the section entitled "GNU
Free Documentation License".</pre>
</div>
</xsl:template>

<xsl:template name="preamblecontent">
<div class="preamblecontent">
<p>Navigate: <a href="#hierarchy">Hierarchy</a> | <a href="#modules">Modules</a></p>
</div>
</xsl:template>

<!-- hierarchy -->
<xsl:template match="structure">
    <p><a name="hierarchy" id="hierarchy"></a></p>
    <h1>Hierarchy</h1>
    <xsl:if test="count(group)&gt;0">
    <div>
      <xsl:for-each select="group">
      <p class="group"><xsl:value-of select="@name" /></p>
        <xsl:for-each select="section">
        (
          <xsl:for-each select="insert">
            <xsl:variable name="theID" select="@module-id" />
            <xsl:for-each select="/modules-definition/modules/module[@id=$theID]">
              <a href="#module{@id}"><xsl:value-of select="@short-name" /></a>
            </xsl:for-each>
            <xsl:if test="position()!=count(../insert)">,&nbsp;</xsl:if>
          </xsl:for-each>
        )<br />
        </xsl:for-each>
      </xsl:for-each>
    </div>
    </xsl:if>
</xsl:template>

<!-- modules -->
<xsl:template match="modules">
  <p><a name="modules" id="modules"></a></p>
  <h1>Modules</h1>
  <xsl:apply-templates select="module" />
</xsl:template>

<xsl:template match="module">
  <p><a name="module{@id}" id="module{@id}"></a></p>
  <h2><xsl:value-of select="@short-name" /></h2>
  <p>[<a href="#top">top</a>]</p>
  <table>
    <xsl:for-each select="@*">
    <tr>
    <td><xsl:value-of select="name()" /></td>
    <td><xsl:value-of select="." /></td>
    </tr>
    </xsl:for-each>
    <tr><td>connector(s)</td><td>
    <table>
    <tr><td>type</td><td>id</td><td>signal-type</td><td>name</td></tr>
    <xsl:apply-templates select="input" />
    <xsl:apply-templates select="output" />
    </table>
    </td></tr>
  </table>
</xsl:template>

<xsl:template match="input|output">
<tr>
  <td><xsl:value-of select="name()" /></td>
  <td><xsl:value-of select="@id" />   </td>
  <td><xsl:value-of select="@type" /> </td>
  <td><xsl:value-of select="@name" /> </td>
</tr>
</xsl:template>

</xsl:stylesheet>
