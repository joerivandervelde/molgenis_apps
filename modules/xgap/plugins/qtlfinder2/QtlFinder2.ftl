<#macro QtlFinder2 screen>

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
		
			<#if model.query??>
				<#assign query = model.query>
			<#else>
				<#assign query = "">
			</#if>
			<#--====================================-->	
		
			<#-- Imports -->
			<#import "../qtlfinder3/ResultList.ftl" as rl>
			<#import "../qtlfinder3/ShoppingCart.ftl" as sc>
			<#import "../qtlfinder3/ProbeReport.ftl" as pr>
			<#import "../qtlfinder3/MultiPlot.ftl" as mp>

			<#-- Macro's -->
			<@qtlFinder model=model screen=screen/>	
			
			<#if model.showResults>
				<@rl.resultList model=model screen=screen />
			</#if>
			
			<#if model.cartView>
				<@sc.shoppingCart model=model screen=screen />
			</#if>
			
			<#if model.report??>
				<@pr.probeReport model=model screen=screen />
			</#if>
			
			<#if model.multiplot??>
				<@mp.multiPlot model=model screen=screen />
			</#if>
			
		</div>
	</form>
</#macro>

<#macro qtlFinder model screen>
	<#assign allDataTypes = "__ALL__DATATYPES__SEARCH__KEY">
	
	<br><br>
	
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td width="290" align="right">
				<select class=" ui-widget-content ui-corner-all" id="Phenotype_select" name="dataTypeSelect"  style="width:220px;">
					<option value=${allDataTypes} <#if model.selectedAnnotationTypeAndNr?? && model.selectedAnnotationTypeAndNr == allDataTypes>selected="selected"</#if>>All data (${model.annotationTypeAndNr[allDataTypes]})</option>
					<#list model.annotationTypeAndNr?keys as key>
						<#if key != allDataTypes>
							<option value="${key}" <#if model.selectedAnnotationTypeAndNr?? && model.selectedAnnotationTypeAndNr == key>selected="selected"</#if>>${key} (${model.annotationTypeAndNr[key]})</option>
						</#if>
					</#list>
				</select><script>$("#Phenotype_select").chosen();</script>
			</td>
			<td width="250" align="center">
				<input type="text" name="query" class="searchBox" value="${query}" >
			</td>
			<td width="290" align="left">
				<#--<input type="submit" id="search" onclick="$(this).closest('form').find('input[name=__action]').val('search');" value="Search" /><script>$("#search").addClass('grayButton').button();</script>-->
				 <div class="buttons"><button style="color:blue;" type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = 'search'; document.forms.${screen.name}.submit();"><img src="generated-res/img/recordview.png" alt=""/>Search</button></div>
				 
				 <div class="buttons"><button type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = 'reset'; document.forms.${screen.name}.submit();"><img src="generated-res/img/reset.png" alt=""/>Reset</button></div>
	    </button>
			</td>
		</tr>
		<tr>
			<td colspan="3" height="70" align="center">
				<span style="font-size:12px;">(<i>for example:</i> ctl, daf, pgp-7, gst-27, Y65B4BR, K02B12, WBGene00021562, WBGene00006727, acetylcholine, luciferase ... )
				<br>Gene hits, for example on <a href="http://www.geneontology.org/">Geno Ontology</a> or anatomy terms, will show the probes and related terms for that gene.
				</span>
			</td>
		</tr>
	</table>
</#macro>