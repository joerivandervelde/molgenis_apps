<#macro reportScreen model screen>
<#-- SHOW RESULT LIST AND SHOPPING CART ON THE SAME LOCATION -->
	<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'gotoSearch'; document.forms.${screen.name}.submit();"><img src="generated-res/img/listview.png" alt=""/> View hits (${model.hits?size})</button></div>
	<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'gotoCart'; document.forms.${screen.name}.submit();"><img src="generated-res/img/listview.png" alt=""/> View cart (${model.shoppingCart?keys?size})</button></div>
	<div class="buttons"><button class="positive" type="submit" onclick="document.forms.${screen.name}.__action.value = 'plotShoppingCart'; document.forms.${screen.name}.submit();"><img src="clusterdemo/icons/icon_plaintext_plot.png" alt=""/> Plot QTLs</button></div>
	
	<#if model.screenType == "genomicRegion" || model.screenType == "qtlLoci">
		<div class="buttons"><button class="positive" type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__plotOverlap'; document.forms.${screen.name}.submit();"><img src="clusterdemo/icons/icon_plaintext_plot.png" alt=""/> Plot Overlap</button></div>
	</#if>
	
	<div class="buttons"><button type="submit"><img src="generated-res/img/cancel.png" alt=""/> Clear cart</button></div>

	<br><br>

	<#if model.showResults>
		<@rl.resultList model = model screen = screen/>
	</#if>	
	
	<#if model.cartView>
		<@sc.shoppingCart model = model screen = screen />
	</#if>

	<#if model.allOverlaps??>
		<@cr.compareResults model = model screen = screen />
	</#if>	
</#macro>