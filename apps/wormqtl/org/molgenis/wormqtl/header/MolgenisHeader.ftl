<#--Date:        November 11, 2009
 * Template:	PluginScreenFTLTemplateGen.ftl.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenFTLTemplateGen 3.3.2-testing
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
-->
<#macro org_molgenis_wormqtl_header_MolgenisHeader screen>

<div style="height:10px;">&nbsp;</div>

<a href="molgenis.do?select=Home"><img src="clusterdemo/bg/banerHD.png" /></a>

<table style="width: 100%;">
	<tr>
		<td align="right">
			<font style="font-size:14px;">
				<#-->| <a href="api/REST/">JSON api</a> | <a href="api/SOAP/">SOAP api</a> | <a href="api/REST/">REST api</a> | -->
				<#--a target="_blank" href="http://www.molgenis.org/wiki/xQTL">Help</a> | <a href="generated-doc/fileformat.html">Exchange format</a> | <a href="api/R/">R api</a> | <a href="api/find/">Find api</a> | ${screen.userLogin}-->
				<a href="molgenis.do?select=Home">Home</a>  
				<a href="wormqtl_contact.html">Contact</a> 
				About
				<b>Article in NAR</b>
			</font>
		</td>
		<#-- disabled shopping cart button until further notice -->
		<#--<a href="molgenis.do?__target=QtlFinderPublic2&select=QtlFinderPublic2&screen=shoppingCart" type="submit"><img align="right" height="40" width="60" src="clusterdemo/wormqtl/shopping_cart_logo.png" alt="Shopping Cart"></a>-->
	</tr>
</table>


<div style="height:10px;">&nbsp;</div>

	
</#macro>
