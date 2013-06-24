<#macro plugins_qtlfinder3_ShoppingCart screen>

	<#-- normally you make one big form for the whole plugin -->
	<form method="post" enctype="multipart/form-data" name="${screen.name}" action="">
	
		<#-- needed in every form: to redirect the request to the right screen -->
		<input type="hidden" name="__target" value="${screen.name}">
		
		<#--needed in every form: to define the action. This can be set by the submit button -->
		<input type="hidden" name="__action">
		
		<#-- need to be set to "true" in order to force a download -->
		<input type="hidden" name="__show">
		
		<input type="hidden" name="__shopMeName">
		<input type="hidden" name="__shopMeId">
		
		<#-- this shows a title and border -->
		<div class="formscreen">
			<div class="form_header" id="${screen.getName()}">
			${screen.label}
			</div>
			
			<#--optional: mechanism to show messages-->
			<#list screen.getMessages() as message>
				<#if message.success>
					<p class="successmessage">${message.text}</p>
				<#else>
					<p class="errormessage">${message.text}</p>
				</#if>
			</#list>
			
			<#--====================================-->
			<#if screen.myModel?exists>
				<#assign modelExists = true>
				<#assign model = screen.myModel>
			<#else>
				No model. An error has occurred.
				<#assign modelExists = false>
			</#if>
		
			<#--====================================-->	
			<#if model.query??>
				<#assign query = model.query>
			<#else>
				<#assign query = "">
			</#if>
			
			<#-- imports -->
			<br colspan="3" height="10" align="center" >
				&nbsp;
			</br>
			
			<#-- macro's -->
			<@shoppingCart model=model screen=screen />
			
		</div>
	</form>
</#macro>

<#macro shoppingCart model screen>
	<#-- SHOPPING CART VIEW -->
	
	<#assign allDataTypes = "__ALL__DATATYPES__SEARCH__KEY">
	
	<#if model.cartView>
		<#if model.shoppingCart?? && model.shoppingCart?size gt 0>
				
			<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'emptyShoppingCart'; document.forms.${screen.name}.submit();"><img src="generated-res/img/cancel.png" alt=""/> Clear cart</button></div>
			<div class="buttons"><button class="positive" type="submit" onclick="document.forms.${screen.name}.__action.value = 'plotShoppingCart'; document.forms.${screen.name}.submit();"><img src="clusterdemo/icons/icon_plaintext_plot.png" alt=""/> Plot cart now</button></div>
			<div class="buttons"><button class="positive" type="submit" onclick="document.forms.${screen.name}.__action.value = 'plotOverlap'; document.forms.${screen.name}.submit();"><img src="clusterdemo/icons/icon_plaintext_plot.png" alt=""/> Plot Human Worm Overlap</button></div>
			
			<br><br><br>
		
			<h3>Shopping cart</h3>
		
			<br>
	
			<#assign counterShop = 1 >
			
			<#list model.shoppingCart?keys as name>
			
				<#assign counterShop = counterShop+1 >
				<div style="padding-bottom:15px;<#if counterShop%2==0>background:white;</#if>">
				
				<div class="buttons" style="padding-top:15px;"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'unshop'; document.forms.${screen.name}.__shopMeName.value = '${name}'; document.forms.${screen.name}.submit();"><img src="generated-res/img/cancel.png" alt=""/> Remove</button></div>
				
				<div style="font-size:100%;float:left;">${model.shoppingCart[name].get(typefield)} <a target="_self" href="?__target=${screen.name}&select=${screen.name}&__action=__entity__report__for__${name}"><b>${name}<#if model.shoppingCart[name].label?? && model.shoppingCart[name].label?length gt 0> / ${model.shoppingCart[name].label}</#if></b></a>
			
					<#if model.shoppingCart[name].get('ReportsFor_name')?? && model.shoppingCart[name].get('ReportsFor_name')?is_string && model.shoppingCart[name].get('ReportsFor_name')?length gt 0>reports for <a target="_blank" href="molgenis.do?select=Genes&__target=Genes&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=Gene_name&__filter_operator=EQUALS&__filter_value=${model.shoppingCart[name].reportsFor_name}">${model.shoppingCart[name].reportsFor_name}</a><#elseif model.shoppingCart[name].symbol?? && model.shoppingCart[name].symbol?length gt 0>(${model.shoppingCart[name].symbol})</#if>
					
					<#if model.shoppingCart[name].get('ReportsFor_name')?? && model.shoppingCart[name].get('ReportsFor_name')?is_string && model.shoppingCart[name].get('ReportsFor_name')?length gt 0>
						- <a target="_blank" href="http://www.wormbase.org/species/c_elegans/gene/${model.shoppingCart[name].get('ReportsFor_name')}">WormBase</a>
					<#elseif model.shoppingCart[name].symbol?? && model.shoppingCart[name].symbol?length gt 0>
						- <a target="_blank" href="http://www.wormbase.org/species/c_elegans/gene/${model.shoppingCart[name].symbol}">WormBase</a>
					</#if>
					
					<#if model.shoppingCart[name].description??> <br> <#if model.shoppingCart[name].description?length gt 60>${model.shoppingCart[name].description?substring(0, 60)} <#else>${model.shoppingCart[name].description}</#if> <a target="_blank" href="molgenis.do?select=${model.shoppingCart[name].get(typefield)}s&__target=${model.shoppingCart[name].get(typefield)}s&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=${model.shoppingCart[name].get(typefield)}_name&__filter_operator=EQUALS&__filter_value=${name}">...more</a><#else> <br> </#if></div>
				
					<#if (model.shoppingCart[name].get('ReportsFor_name')?? && model.shoppingCart[name].get('ReportsFor_name')?is_string && model.shoppingCart[name].get('ReportsFor_name')?length gt 0) || (model.shoppingCart[name].symbol?? && model.shoppingCart[name].symbol?length gt 0)><#if model.probeToGene[name]?? && model.probeToGene[name]?length gt 0><div style="text-align: center; float:right; padding-left:30px; position:relative; right:10px;" onmouseover="return overlib('<#if model.probeToGene[name].description??>${model.probeToGene[name].description?replace("'", "")?replace(" / ", "<br>")}<#else>No data available</#if>', CAPTION, 'Ontological terms')" onmouseout="return nd();"><img src="res/img/designgg/helpicon.gif" width="25" height="25"/><br><b>Ontologies</b></div></#if></#if>
				
					<br><br>
					
				</div>
				
				</#list>
		<#else>
			
			<div class="buttons"><button type="submit"><img src="generated-res/img/cancel.png" alt=""/> Clear cart</button></div>
			<div class="buttons"><button class="positive" type="submit" onclick="document.forms.${screen.name}.__action.value = 'plotShoppingCart'; document.forms.${screen.name}.submit();"><img src="clusterdemo/icons/icon_plaintext_plot.png" alt=""/> Plot cart now</button></div>
			<div class="buttons"><button class="positive" type="submit" onclick="document.forms.${screen.name}.__action.value = 'plotOverlap'; document.forms.${screen.name}.submit();"><img src="clusterdemo/icons/icon_plaintext_plot.png" alt=""/> Plot Human Worm Overlap</button></div>
			
			<br><br><br>
			
			<h3>Your shopping cart is empty.</h3>
			
			<br>
		
		</#if>
	</#if>	
</#macro>