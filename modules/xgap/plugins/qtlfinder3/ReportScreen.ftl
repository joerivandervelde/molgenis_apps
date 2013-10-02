<#macro reportScreen model screen>
<#-- SHOW RESULT LIST AND SHOPPING CART ON THE SAME LOCATION -->
	<#if model.hits??>
		
		<#if model.shoppingCart?size gt 100><div align="center"><h3><font>Attention, you have selected many probes: ${model.shoppingCart?size}. This will take approximately ${model.shoppingCart?size * 0.6} seconds.  </font> </h3></div></#if>
		<br>
		
		<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'gotoSearch'; document.forms.${screen.name}.submit();"><img src="generated-res/img/listview.png" alt=""/> View hits (${model.hits?size})</button></div>
		<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'gotoCart'; document.forms.${screen.name}.submit();"><img src="generated-res/img/listview.png" alt=""/> View cart (${model.shoppingCart?keys?size})</button></div>
		
		<div class="buttons"><button class="positive" type="submit" onclick="document.getElementById('listOfHits').style.display = 'none';document.getElementById('imageSpace').style.display = 'block';document.forms.${screen.name}.__action.value = 'plotShoppingCart'; document.forms.${screen.name}.submit();"><img src="clusterdemo/icons/icon_plaintext_plot.png" alt=""/> Plot QTLs</button></div>
		
		<#if model.screenType == "humanDisease">
			<#-- <div class="buttons"><button class="positive" disabled="disabled" type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__plotOverlap'; document.forms.${screen.name}.submit();"><img src="clusterdemo/icons/icon_plaintext_plot.png" alt=""/><font style="color:gray;"> Plot Overlap</font></button></div> -->
		<#else>
			<div class="buttons"><button class="positive" type="submit" onclick="document.getElementById('listOfHits').style.display = 'none';document.getElementById('imageSpace').style.display = 'block';document.forms.${screen.name}.__action.value = '__qtlfinderhd__plotOverlap'; document.forms.${screen.name}.submit();"><img src="clusterdemo/icons/icon_plaintext_plot.png" alt=""/> Disease enrichment</button></div>
		</#if>
		
		<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'emptyShoppingCart';document.forms.${screen.name}.submit();"><img src="generated-res/img/cancel.png" alt=""/> Clear cart</button></div>
		
		<br>
	</#if>	
	
	<br>
	<div id="imageSpace" align="center" style="display:none;"><img src="clusterdemo/icons/spinner.gif" width="200px" height="200px"/></div>
	<br>
		
	<div id="listOfHits" style="display:block;">	
		<#if model.showResults>	
			<@rl.resultList model = model screen = screen/>
			<br><br>		
		</#if>	
			
		<#if model.cartView>
			<@sc.shoppingCart model = model screen = screen />
			<br><br>
		</#if>
	
		<#if model.phenoCompareResults.results?? && model.screenType="comparePhenotypes">
			<@cr.compareResults results = model.phenoCompareResults.results model = model />
			<br><br>
		</#if>	
		<#if model.regionSearchResults.results?? && model.screenType="genomicRegion">
			<@cr.compareResults results = model.regionSearchResults.results model = model />
			<br><br>
		</#if>
		<#if model.qtlSearchResults.results?? && model.screenType="qtlLoci">
			<@cr.compareResults results = model.qtlSearchResults.results model = model />
			<br><br>
		</#if>
				
	</div>				
</#macro>