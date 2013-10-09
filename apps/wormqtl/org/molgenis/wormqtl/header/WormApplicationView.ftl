
<#include "Layout.ftl"/>
<#-- start with the 'main' screen, which is called 'application'-->

<#assign title=screen.label/>

<#--rendering starts here -->
<@molgenis_header screen/>
<div id="container">
	
<#if screen.target?exists && screen.show=="popup">
	<@layout screen.target/>
<#else>	

<#list screen.children as subscreen>
	<@layout subscreen />
</#list>

</#if>

</div>
<div id="footer" style="text-align:center;margin:0 auto;">
	<div style="height:85px;width:900px;margin:0 auto;padding-top:20px">
		<div style="float:left;"><img height="75" src="clusterdemo/wormqtl/eulogo.gif"></div>
		<div style="float:left;"><img height="75" src="clusterdemo/wormqtl/seventh-framework-programme.png"></div>
		<div style="float:left; text-align:center; padding-left:20px; width:650px;"><b>The research has received funding from the European Community's Health Seventh Framework Programme (FP7/2007-2013) under grant agreement BioSHaRE-EU (nr 261433), PANACEA (nr 222936) and ERASysbio-plus ZonMW project GRAPPLE - Iterative modeling of gene regulatory interactions underlying stress, disease and ageing in C. elegans (project nr. 90201066).</b></div>
	</div>
	<br>
	<br>Please cite <a href="http://poojachopragoel.com/wp-content/uploads/2013/02/coming-soon.jpg" target="_blank">Van der Velde et al (2013)</a>, <a href="http://www.ncbi.nlm.nih.gov/pubmed/23180786" target="_blank">Snoek et al (2013)</a>, <a href="http://www.ncbi.nlm.nih.gov/pubmed/22308096">Arends et al (2012)</a> and <a href="http://www.ncbi.nlm.nih.gov/pubmed/21210979">Swertz et al (2010)</a> on use.</i><br>
	<i>This database was generated using the open source <a href="http://www.molgenis.org">MOLGENIS database generator</a> version ${screen.getVersion()}.
	
</div>
<@molgenis_footer />
