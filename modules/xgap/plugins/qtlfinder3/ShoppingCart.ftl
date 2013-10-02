<#macro shoppingCart model screen>
	<#-- SHOPPING CART VIEW -->
	<#if model.shoppingCart?? && model.shoppingCart?size gt 0>
		<br>
			<h3>Shopping cart</h3>
		<br>

		<table id="shoppingCartList">
			<thead align="left">
				<th>Probe name</th>
				<#--<th>Location</th>-->
				<th>Orthology</th>
				<th>Disease associations</th>
				<#-- <th>Ontologies</th> -->
			</thead>
			
			<tbody>
				<#list model.shoppingCart?keys as name>	
					<tr>
						<td width="240px" style="padding-bottom:10px;padding-top:10px;vertical-align:middle;">
							<a style="text-decoration:none;" target="__blank" href="molgenis.do?select=${model.shoppingCart[name].get(typefield)}s&__target=${model.shoppingCart[name].get(typefield)}s&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=${model.shoppingCart[name].get(typefield)}_name&__filter_operator=EQUALS&__filter_value=${model.shoppingCart[name].name}"><b>${model.shoppingCart[name].get("name")}</b></a>
								
							<div class="buttons" style="padding-top:15px;">
								<button type="submit" onclick="document.forms.${screen.name}.__action.value = 'unshop';
								document.forms.${screen.name}.__shopMeName.value = '${name}'; 
								document.forms.${screen.name}.submit();">
								<img src="generated-res/img/cancel.png" alt=""/> Remove</button>
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
							<#if model.shoppingCart[name].get('ReportsFor_name')?? && model.shoppingCart[name].get('ReportsFor_name')?is_string && model.shoppingCart[name].get('ReportsFor_name')?length gt 0>
								Ce: <a style="text-decoration:none;" href="http://www.wormbase.org/species/c_elegans/gene/${model.shoppingCart[name].get('ReportsFor_name')}" target="__blank">${model.shoppingCart[name].get('ReportsFor_name')}</a><br>
							</#if>
							<#if model.shoppingCart[name].get('ReportsFor_name')?? && model.shoppingCart[name].get('ReportsFor_name')?is_string && model.shoppingCart[name].get('ReportsFor_name')?length gt 0>
								<#if model.humanToWorm.wormGeneToHumanGene(model.shoppingCart[name].reportsFor_name)??>
									<#assign humanOrtho = model.humanToWorm.wormGeneToHumanGene(model.shoppingCart[name].reportsFor_name)>
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
							<#if model.humanToWorm.wormProbeToDataSourceToHumanDiseases(model.shoppingCart[name].name)?has_content>
								<#list model.humanToWorm.wormProbeToDataSourceToHumanDiseases(model.shoppingCart[name].name)?keys as source>
									<b>${source}</b>: 		
									<#list model.humanToWorm.wormProbeToDataSourceToHumanDiseases(model.shoppingCart[name].name)[source] as disease>
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
				
				</#list>			
			</tbody>
		</table>	
		
	<#else>	
		<br />
			<h3>Your shopping cart is empty.</h3>
		<br />	
	</#if>
	<br><br>	
</#macro>