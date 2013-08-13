<#macro resultList model screen>
<#-- RESULT LIST WITH HITS WHEN A SEARCHING FUNCTION IS USED -->
	
	<#--find out how many items have been 'shopped'-->
	<#assign shopped = 0>
	<#list model.hits?keys as name>
		<#if model.shoppingCart?keys?seq_contains(name)>
			<#assign shopped = shopped+1>
		</#if>
	</#list>
	
		<div style="text-align:center;">
			<h3>Found <#if model.hits?? && model.hits?size == 100>many<#else>${model.hits?size}</#if> hits.</h3>
			
			<h4>
				<#if model.shortenedQuery??>
					<br>Your query was too specific for any hits, so it was shortened to <u>${model.shortenedQuery}</u>.
				</#if>
				
				<#if model.hits?? && model.hits?size == 100 && model.shortenedQuery??>
					<br>These results were limited to the first 100.
				<#elseif model.hits?? && model.hits?size == 100>
					<br>Your results were limited to the first 100. Please be more specific.
				<#else>
			
				</#if>
			</h4>
			
			<#if shopped gt 0>
				<#if shopped == model.hits?size>
					<h4>All ${shopped} hits are currently in your cart.</h4>	
				<#else>
					<h4>Please note: ${shopped} hits are not shown because they are already in your cart.</h4>
				</#if>
			</#if>
		</div>
		
		<#if shopped gt 0 && shopped == model.hits?size>
			<#-- do not show 'add all hits' button when there is nothing to be added -->
			
		<#else>
			<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'shopAll'; document.forms.${screen.name}.submit();"><img src="generated-res/img/run.png" alt=""/><img src="clusterdemo/icons/shoppingcart.png" alt=""/> Add all hits to cart</button></div>
			<br><br>
		</#if>
		
		<br>
		
		<#if shopped == model.hits?size>
			<#-- do not show table -->
		<#else>
			<table id="hitList">
				<thead align="left">
					<th>Probe name</th>
					<#--<th>Location</th>-->
					<th>Orthology</th>
					<th>Disease associations</th>
					<#-- <th>Ontologies</th> -->
				</thead>
				
				<tbody>
					<#list model.hits?keys as name>
						<#if model.shoppingCart?keys?seq_contains(name)>
								<#--<input type="submit" class="unshop" value="" onclick="document.forms.${screen.name}.__action.value = 'unshop'; document.forms.${screen.name}.__shopMeName.value = '${name}'; document.forms.${screen.name}.submit();">-->
								<#--<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'unshop';document.forms.${screen.name}.__shopMeName.value = '${name}'; document.forms.${screen.name}.submit();"><img src="generated-res/img/select.png" alt=""/> (remove)</button></div>-->
						<#else>
							<tr>
								<td width="240px" style="padding-bottom:10px;padding-top:10px;vertical-align:middle;">
									<a style="text-decoration:none;" target="__blank" href="molgenis.do?select=${model.hits[name].get(typefield)}s&__target=${model.hits[name].get(typefield)}s&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=${model.hits[name].get(typefield)}_name&__filter_operator=EQUALS&__filter_value=${model.hits[name].name}"><b>${model.hits[name].get("name")}</b></a>
									
									<div class="buttons" style="display:inline;">
										<button type="submit" onclick="document.forms.${screen.name}.__action.value = 'shop'; 
										document.forms.${screen.name}.__shopMeId.value = '${model.hits[name].id?c}'; 
										document.forms.${screen.name}.__shopMeName.value = '${name}'; 
										document.forms.${screen.name}.submit();">
										<img src="clusterdemo/icons/shoppingcart.png" alt=""/> Add to cart</button>
									</div>
									 
								</td>
								
								<#-- 
								
								<td width="150px">
									Chr: <#if model.hits[name].chromosome_name??>${model.hits[name].get("chromosome_name")}<br /><#else>no data</#if> 
									StartBp: <#if model.hits[name].bpStart??>${model.hits[name].get("bpStart")}<br /><#else>no data</#if>
									EndBp: <#if model.hits[name].bpEnd??>${model.hits[name].get("bpEnd")}<br /><#else>no data</#if>
								</td>
								
								-->
								
								<td width="175	px" style="padding-bottom:10px;vertical-align:middle;">
									<#if model.hits[name].get('ReportsFor_name')?? && model.hits[name].get('ReportsFor_name')?is_string && model.hits[name].get('ReportsFor_name')?length gt 0>
										Ce: <a style="text-decoration:none;" href="http://www.wormbase.org/species/c_elegans/gene/${model.hits[name].reportsFor_name}" target="__blank">${model.hits[name].reportsFor_name}</a><br>
									<#elseif model.hits[name].symbol?? && model.hits[name].symbol?length gt 0>
										Ce: <a style="text-decoration:none;" href="http://www.wormbase.org/species/c_elegans/gene/${model.hits[name].symbol}" target="__blank">${model.hits[name].symbol}</a><br>
									</#if>
									<#if model.hits[name].get('ReportsFor_name')?? && model.hits[name].get('ReportsFor_name')?is_string && model.hits[name].get('ReportsFor_name')?length gt 0>
										<#if model.humanToWorm.wormGeneToHumanGene(model.hits[name].reportsFor_name)??>
											<#assign humanOrtho = model.humanToWorm.wormGeneToHumanGene(model.hits[name].reportsFor_name)>
										<#else>
											<#assign humanOrtho = "No ortholog">
										</#if>
										<#if humanOrtho??>
											<#if humanOrtho?is_string && humanOrtho?contains("ortholog")>
												Hs: ${humanOrtho}
											<#else>
												Hs: <a style="text-decoration:none;" href="http://www.genenames.org/data/hgnc_data.php?match=${humanOrtho}" target="__blank">${humanOrtho}</a>
											</#if>
										</#if>
									<#elseif model.hits[name].symbol?? && model.hits[name].symbol?length gt 0>
										<#if model.humanToWorm.wormGeneToHumanGene(model.hits[name].symbol)??>
											<#assign humanOrtho = model.humanToWorm.wormGeneToHumanGene(model.hits[name].symbol)>
										<#else>
											<#assign humanOrtho = "No ortholog">
										</#if>
										<#if humanOrtho??>
											<#if humanOrtho?is_string && humanOrtho?contains("ortholog")>
												Hs: ${humanOrtho}
											<#else>
												Hs: <a style="text-decoration:none;" href="http://www.genenames.org/data/hgnc_data.php?match=${humanOrtho}" target="__blank">${humanOrtho}</a>
											</#if>
										</#if>
									</#if>
								</td>
								<td style="padding-bottom:10px;vertical-align:middle;">
									<#if model.humanToWorm.wormProbeToDataSourceToHumanDiseases(model.hits[name].name)?has_content>
										<#list model.humanToWorm.wormProbeToDataSourceToHumanDiseases(model.hits[name].name)?keys as source>
											<b>${source}</b>: 		
											<#list model.humanToWorm.wormProbeToDataSourceToHumanDiseases(model.hits[name].name)[source] as disease>
												${disease}<#if disease_has_next>, </#if> 
											</#list>
											<br>
										</#list>
									<#elseif model.humanToWorm.wormProbeToDataSourceToWormDiseases(model.hits[name].name)?has_content>
										<#list model.humanToWorm.wormProbeToDataSourceToWormDiseases(model.hits[name].name)?keys as source>
											<b>${source}</b>:
											<#list model.humanToWorm.wormProbeToDataSourceToWormDiseases(model.hits[name].name)[source] as disease>
												${disease}<#if disease_has_next>, </#if> 
											</#list>
											<br>
										</#list>		
									<#else>
										No disease associations
									</#if>
								</td>
								
								<#-- Ontologies...
								
								<td>
									<#if (model.hits[name].get('ReportsFor_name')?? && model.hits[name].get('ReportsFor_name')?is_string && model.hits[name].get('ReportsFor_name')?length gt 0) || (model.hits[name].symbol?? && model.hits[name].symbol?length gt 0)><#if model.probeToGene[name]?? && model.probeToGene[name]?length gt 0><div style=" text-align: center; float:right; padding-left:30px; position:relative; right:10px;" onmouseover="return overlib('<#if model.probeToGene[name].description??>${model.probeToGene[name].description?replace("'", "")?replace(" / ", "<br>")}<#else>No data available</#if>', CAPTION, 'Ontological terms')" onmouseout="return nd();"><img src="res/img/designgg/helpicon.gif" width="25" height="25"/><br><b>Ontologies</b></div></#if></#if>
								</td>
								
								-->	
							</tr>
						</#if>	
					</#list>			
				</tbody>
			</table>	
				
		</#if>	
		<br><br>
</#macro>