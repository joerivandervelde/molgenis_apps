<#macro diseaseReport model screen>
	<#if model.diseaseMapping == "DGA">
		<#if model.disease??>
			<#assign disease = "${model.disease}">
			<div id="diseaseReport" style="padding-left:25px;">
				<h3 align="center">${disease}</h3>
				<hr width="600" align="center"/>
				<b>Wormgenes</b>: <#list model.humanToWorm.convert(disease) as wormGenes>${wormGenes}, </#list><br><br>
				<b>Human proteins</b>: <#list model.humanToWorm.diseaseToHuman[disease] as humanProteins>${humanProteins}, </#list><br><br>
				<b>SNPs</b>: <#list model.humanToWorm.diseaseToHuman[disease] as humanProteins>${model.humanToWorm.getGwasDisease(humanProtein)}</#list><br><br>
				<#-- Studies:<#list model.humanToWorm.gwasStudy[disease] as studyData><#if studyData?starts_with("rs")><#else>${studyData}, </#if></#list> -->
				<b>Probes</b>: probe 1, probe 2 [Add probes to cart]<br><br>
			</div>
			<hr width="600" align="center"/>
		</#if>
	<#elseif model.diseaseMapping == "OMIM">
		<#if model.diseases??>
			<#list model.diseases as disease>
				<div id="diseaseReport" style="padding-left:25px;">
				<h3 align="center">${disease}</h3>
				<hr width="600" align="center"/>
				<b>Wormgenes</b>: <#list model.humanToWorm.convert(disease) as wormGenes>${wormGenes}, </#list><br><br>
				<b>Human proteins</b>: <#list model.humanToWorm.diseaseToHuman[disease] as humanProteins>${humanProteins}, </#list><br><br>
				<b>SNPs</b>: <#list model.humanToWorm.diseaseToHuman[disease] as humanProteins>${model.humanToWorm.getGwasDisease(humanProtein)}</#list><br><br>
				<#-- Studies:<#list model.humanToWorm.gwasStudy[disease] as studyData><#if studyData?starts_with("rs")><#else>${studyData}, </#if></#list> -->
				<b>Probes</b>: probe 1, probe 2 [Add probes to cart]<br><br>
			</div>
			</#list>
			<hr width="600" align="center"/>
		</#if>
	</#if>		 
</#macro>