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
			<#if shopped gt 0 && shopped == model.hits?size>
			<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'gotoCart'; document.forms.${screen.name}.submit();"><img src="generated-res/img/listview.png" alt=""/> View cart (${model.shoppingCart?keys?size})</button></div>				
			<br>
			</#if>
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
					<div style="padding-bottom:15px;<#if counter%2==0>background:white;</#if>">
						<div class="buttons" style="padding-top:15px;"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'shop'; document.forms.${screen.name}.__shopMeId.value = '${model.hits[name].id?c}'; document.forms.${screen.name}.__shopMeName.value = '${name}'; document.forms.${screen.name}.submit();"><img src="clusterdemo/icons/shoppingcart.png" alt=""/> Add to cart</button></div>
						<#--<input type="submit" class="shop" value="" onclick="document.forms.${screen.name}.__action.value = 'shop'; document.forms.${screen.name}.__shopMeId.value = '${model.hits[name].id?c}'; document.forms.${screen.name}.__shopMeName.value = '${name}'; document.forms.${screen.name}.submit();">-->
			
						<div style="font-size:100%;float:left;">${model.hits[name].get(typefield)} <a target="_self" href="?__target=${screen.name}&select=${screen.name}&__action=__entity__report__for__${name}"><b>${name}<#if model.hits[name].label?? && model.hits[name].label?length gt 0> / ${model.hits[name].label}</#if></b></a>
							<#if model.hits[name].get('ReportsFor_name')?? && model.hits[name].get('ReportsFor_name')?is_string && model.hits[name].get('ReportsFor_name')?length gt 0>reports for <a target="_blank" href="molgenis.do?select=Genes&__target=Genes&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=Gene_name&__filter_operator=EQUALS&__filter_value=${model.hits[name].reportsFor_name}">${model.hits[name].reportsFor_name}</a><#elseif model.hits[name].symbol?? && model.hits[name].symbol?length gt 0>(${model.hits[name].symbol})</#if>
							
							<#if model.hits[name].get('ReportsFor_name')?? && model.hits[name].get('ReportsFor_name')?is_string && model.hits[name].get('ReportsFor_name')?length gt 0>
								- <a target="_blank" href="http://www.wormbase.org/species/c_elegans/gene/${model.hits[name].get('ReportsFor_name')}">WormBase</a>
							<#elseif model.hits[name].symbol?? && model.hits[name].symbol?length gt 0>
								- <a target="_blank" href="http://www.wormbase.org/species/c_elegans/gene/${model.hits[name].symbol}">WormBase</a>
							</#if>
							
							<#if model.hits[name].description??> <br> <#if model.hits[name].description?length gt 60>${model.hits[name].description?substring(0, 60)} <#else>${model.hits[name].description}</#if> <a target="_blank" href="molgenis.do?select=${model.hits[name].get(typefield)}s&__target=${model.hits[name].get(typefield)}s&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=${model.hits[name].get(typefield)}_name&__filter_operator=EQUALS&__filter_value=${name}">...more</a><#else> <br> </#if>
						
						</div>
				
						<#if (model.hits[name].get('ReportsFor_name')?? && model.hits[name].get('ReportsFor_name')?is_string && model.hits[name].get('ReportsFor_name')?length gt 0) || (model.hits[name].symbol?? && model.hits[name].symbol?length gt 0)><#if model.probeToGene[name]?? && model.probeToGene[name]?length gt 0><div style=" text-align: center; float:right; padding-left:30px; position:relative; right:10px;" onmouseover="return overlib('<#if model.probeToGene[name].description??>${model.probeToGene[name].description?replace("'", "")?replace(" / ", "<br>")}<#else>No data available</#if>', CAPTION, 'Ontological terms')" onmouseout="return nd();"><img src="res/img/designgg/helpicon.gif" width="25" height="25"/><br><b>Ontologies</b></div></#if></#if>
				
						<br>
						<br>
					</div>
			
				</#if>
			</#list>
		</#if>
	</#if>	
</#macro>