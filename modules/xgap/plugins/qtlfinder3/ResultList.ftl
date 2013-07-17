<#macro resultList model screen>
<#-- RESULT LIST WITH HITS WHEN A SEARCHING FUNCTION IS USED -->
	<#if model.showResults>
		<#if model.hits??>
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
						
						<div class="buttons">
							<table align="center">
								<tr>
									<td>
										<button type="submit" onclick="document.forms.${screen.name}.__action.value = 'gotoCart'; document.forms.${screen.name}.submit();"><img src="generated-res/img/listview.png" alt=""/> View cart (${model.shoppingCart?keys?size})</button>
									</td>
									<td>	
										<div class="buttons"><button class="positive" type="submit" onclick="document.forms.${screen.name}.__action.value = 'plotShoppingCart'; document.forms.${screen.name}.submit();"><img src="clusterdemo/icons/icon_plaintext_plot.png" alt=""/> Plot cart now</button></div>
									</td>
									<td>	
										<div class="buttons"><button class="positive" type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__plotOverlap'; document.forms.${screen.name}.submit();"><img src="clusterdemo/icons/icon_plaintext_plot.png" alt=""/> Plot Human Worm Overlap</button></div>
									</td>	
								</tr>
							</table>	
						</div>				
					<#else>
						<h4>Please note: ${shopped} hits are not shown because they are already in your cart.</h4>
					</#if>
				</#if>
			</div>
			
			<#if shopped gt 0 && shopped == model.hits?size>
				<#-- do not show 'add all hits' button when there is nothing to be added -->
				
			<#else>
				<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'shopAll'; document.forms.${screen.name}.submit();"><img src="generated-res/img/run.png" alt=""/><img src="clusterdemo/icons/shoppingcart.png" alt=""/> Add all hits to cart</button></div>
				<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'gotoCart'; document.forms.${screen.name}.submit();"><img src="generated-res/img/listview.png" alt=""/> View cart (${model.shoppingCart?keys?size})</button></div>				
				<br><br>
			</#if>
			
			<br>
	
			<#--<input type="submit" id="shopAll" onclick="$(this).closest('form').find('input[name=__action]').val('shopAll');" value="Add all to cart" /><script>$("#shopAll").addClass('grayButton').button();</script><br><br>-->
			<#--<input type="submit" class="shop" value="" onclick="document.forms.${screen.name}.__action.value = 'shopAll'; document.forms.${screen.name}.submit();"><b><i>Add all to cart</b></i><br><br>-->
			
			<#assign counter = 1>
			
			
			<#list model.hits?keys as name>
				<#if model.shoppingCart?keys?seq_contains(name)>
					<#--<input type="submit" class="unshop" value="" onclick="document.forms.${screen.name}.__action.value = 'unshop'; document.forms.${screen.name}.__shopMeName.value = '${name}'; document.forms.${screen.name}.submit();">-->
					<#--<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'unshop';document.forms.${screen.name}.__shopMeName.value = '${name}'; document.forms.${screen.name}.submit();"><img src="generated-res/img/select.png" alt=""/> (remove)</button></div>-->
				<#else>
					<#assign counter = counter+1 >
					<div id="probeReport" style="padding-bottom:15px;<#if counter%2==0>background:white;</#if>">
						<div class="buttons" style="padding-top:15px;">
							<button type="submit" onclick="document.forms.${screen.name}.__action.value = 'shop'; 
							document.forms.${screen.name}.__shopMeId.value = '${model.hits[name].id?c}'; 
							document.forms.${screen.name}.__shopMeName.value = '${name}'; 
							document.forms.${screen.name}.submit();">
							<img src="clusterdemo/icons/shoppingcart.png" alt=""/> Add to cart</button>
						</div>
						<#--<input type="submit" class="shop" value="" onclick="document.forms.${screen.name}.__action.value = 'shop'; document.forms.${screen.name}.__shopMeId.value = '${model.hits[name].id?c}'; document.forms.${screen.name}.__shopMeName.value = '${name}'; document.forms.${screen.name}.submit();">-->
			
						<div  style="font-size:100%;float:left;vertical-align:center;">
							${model.hits[name].get(typefield)} 
							<a class="tip" target="_self" href="">${name}
							<span style="font-size:80%;">
									Worm gene: 
									<#if model.hits[name].get('ReportsFor_name')?? && model.hits[name].get('ReportsFor_name')?is_string && model.hits[name].get('ReportsFor_name')?length gt 0>
										${model.hits[name].reportsFor_name}
									<#elseif model.hits[name].symbol?? && model.hits[name].symbol?length gt 0>
										${model.hits[name].symbol}
									</#if>
									<br />
									Human protein ortholog:
									<#list model.humanToWorm.humanToWorm?keys as key>
										<#if model.hits[name].get('ReportsFor_name')?? && model.hits[name].get('ReportsFor_name')?is_string && model.hits[name].get('ReportsFor_name')?length gt 0>
											<#if model.humanToWorm.humanToWorm[key] == model.hits[name].reportsFor_name><#assign hprotein = key/>${key}</#if>
										<#elseif model.hits[name].symbol?? && model.hits[name].symbol?length gt 0>
											<#if model.humanToWorm.humanToWorm[key] == model.hits[name].symbol><#assign hprotein = key/>${key}</#if>
										</#if>
									</#list>
								</span>
							</a>
							<div style="font-size:80%">
							<#if model.hits[name].get('ReportsFor_name')?? && model.hits[name].get('ReportsFor_name')?is_string && model.hits[name].get('ReportsFor_name')?length gt 0>	
										<#if model.humanToWorm.linkToDisease(model.hits[name].reportsFor_name, 'OMIM')[1] == "No ortholog available">
										<#else>OMIM: ${model.humanToWorm.linkToDisease(model.hits[name].reportsFor_name, 'OMIM')[1]}<br />
										</#if>	
										<#if model.humanToWorm.linkToDisease(model.hits[name].reportsFor_name, 'DGA')[1] == "No ortholog available">
										<#else>DGA: ${model.humanToWorm.linkToDisease(model.hits[name].reportsFor_name, 'DGA')[1]}<br />
										</#if>		
										<#if model.humanToWorm.linkToDisease(model.hits[name].reportsFor_name, 'GWAS')[1] == "No ortholog available">
										<#else>GWAS: ${model.humanToWorm.linkToDisease(model.hits[name].reportsFor_name, 'GWAS')[1]}	
										</#if>
									<#elseif model.hits[name].symbol?? && model.hits[name].symbol?length gt 0>
										<#if model.humanToWorm.linkToDisease(model.hits[name].symbol, 'OMIM')[1] == "No ortholog available">
										<#else>OMIM: ${model.humanToWorm.linkToDisease(model.hits[name].symbol, 'OMIM')[1]}<br />
										</#if>
										<#if model.humanToWorm.linkToDisease(model.hits[name].symbol, 'DGA')[1] == "No ortholog available">
										<#else>DGA: ${model.humanToWorm.linkToDisease(model.hits[name].symbol, 'DGA')[1]}<br />	
										</#if>
										<#if model.humanToWorm.linkToDisease(model.hits[name].symbol, 'GWAS')[1] == "No ortholog available">
										<#else>GWAS: ${model.humanToWorm.linkToDisease(model.hits[name].symbol, 'GWAS')[1]}
										</#if>
									</#if>
							</div>	
						</div>
		
						<#-- IN HOVER
				
						<#if model.hits[name].get('ReportsFor_name')?? && model.hits[name].get('ReportsFor_name')?is_string && model.hits[name].get('ReportsFor_name')?length gt 0>
								- <a target="_blank" href="http://www.wormbase.org/species/c_elegans/gene/${model.hits[name].get('ReportsFor_name')}">WormBase</a>
							<#elseif model.hits[name].symbol?? && model.hits[name].symbol?length gt 0>
								- <a target="_blank" href="http://www.wormbase.org/species/c_elegans/gene/${model.hits[name].symbol}">WormBase</a>
							</#if>
				
						-->	
						<#if (model.hits[name].get('ReportsFor_name')?? && model.hits[name].get('ReportsFor_name')?is_string && model.hits[name].get('ReportsFor_name')?length gt 0) || (model.hits[name].symbol?? && model.hits[name].symbol?length gt 0)><#if model.probeToGene[name]?? && model.probeToGene[name]?length gt 0><div style=" text-align: center; float:right; padding-left:30px; position:relative; right:10px;" onmouseover="return overlib('<#if model.probeToGene[name].description??>${model.probeToGene[name].description?replace("'", "")?replace(" / ", "<br>")}<#else>No data available</#if>', CAPTION, 'Ontological terms')" onmouseout="return nd();"><img src="res/img/designgg/helpicon.gif" width="25" height="25"/><br><b>Ontologies</b></div></#if></#if>
				
						<br>
						<br>
					</div>
			
				</#if>
			</#list>
		</#if>
	</#if>	
</#macro>