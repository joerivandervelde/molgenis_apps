<#macro QtlFinderHD screen>
<!-- normally you make one big form for the whole plugin-->
<form method="post" enctype="multipart/form-data" name="${screen.name}" action="">
	<!--needed in every form: to redirect the request to the right screen-->
	<input type="hidden" name="__target" value="${screen.name}">
	<!--needed in every form: to define the action. This can be set by the submit button-->
	<input type="hidden" name="__action">
	<!--need to be set to "true" in order to force a download-->
	<input type="hidden" name="__show">
	
	<input type="hidden" name="__shopMeName">
	<input type="hidden" name="__shopMeId">
	
<!-- this shows a title and border -->
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

<#---------------RADIO BUTTON SELECTION--------------->

<table>
	<tr>
		<td style="padding-left:20px;padding-top:20px;">
			<input type="radio" name="humanToWorm" value="wormToDisease" 
			onclick="display('hide', 'diseaseToWorm');display('hide', 'humanToWorm');display('show', 'wormToDisease');"
			>Search database for worm phenotypes</input>
		</td>
	</tr>
	<tr>
		<td style="padding-left:20px;padding-top:5px;">
			<input type="radio" name="humanToWorm" value="wormToDisease" 
			onclick="display('hide', 'diseaseToWorm');display('hide', 'humanToWorm');display('show', 'wormToDisease');"
			>Search worm probes by genetic region</input>
		</td>
	</tr>
	<tr>
		<td style="padding-left:20px;padding-top:5px;">
			<input type="radio" name="humanToWorm" value="diseaseToWorm" 
			onclick="display('show', 'diseaseToWorm');display('hide', 'humanToWorm');display('hide', 'wormToDisease');" 
			checked>Find worm genes by human disease</input>
		</td>
	</tr>
	<tr>
		<td style="padding-left:20px;padding-top:5px;">
			<input type="radio" name="humanToWorm" value="humanToWorm"
			onclick="display('hide', 'diseaseToWorm');display('show', 'humanToWorm');display('hide', 'wormToDisease');"
			>Find orthologs in worm for your own set of human genes</input>
		</td>
	</tr>
	<tr>
		<td style="padding-left:20px;padding-top:5px;">
			<input type="radio" name="humanToWorm" value="wormToDisease" 
			onclick="display('hide', 'diseaseToWorm');display('hide', 'humanToWorm');display('show', 'wormToDisease');"
			>Scan regions of the worm for possible disease association within human</input>
		</td>
	</tr>
</table>

<#---------------END OF RADIO BUTTON SELECTION--------------->


<#---------------DISEASE SELECTION DROPDOWN BAR-------------->

<div class="diseaseToWorm" style="display:show;">
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td width="200" style="padding-left:25px;">
				<select class=" ui-widget-content ui-corner-all" id="Phenotype_select" name="diseaseSelect"  style="width:500px;">
					<#list model.humanToWorm.diseaseToHuman?keys as disease>
						<option value="${disease}" <#if model.disease == disease>selected="selected"</#if>
						>${disease} ... ( ${model.humanToWorm.diseaseToHuman[disease]?size} Gene(s) )</option> 
					</#list>
				</select><script>$("#Phenotype_select").chosen();</script>
			</td>
		
			<td width="290">
			 	
			 	<div class="buttons">
					<button type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__shop'; 
				 	document.forms.${screen.name}.submit();">
				 	<img src="clusterdemo/icons/shoppingcart.png" alt=""/> Add to Cart</button>
			 	</div>
				
				<div class="buttons">
			 		<button type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__reset'; 
			 		document.forms.${screen.name}.submit();"><img src="generated-res/img/reset.png" alt=""/>Reset</button>			 	
	    		</div>
			 	
			</td>
		</tr>
		<tr>
			<td colspan="3" height="70" align="center" style="padding-top:20px;">
				<span style="font-size:12px;">Search for worm genes via human disease worm gene association</span>
			</td>
		</tr>
	</table>
</div>

<#-----------END DISEASE SELECTION DROPDOWN BAR-------------->

<#-----------HUMAN GENE FILL IN FORM (UPLOAD FILE?)---------->

<div class="humanToWorm">
	<table align="center">
		<tr>
			<td>
			Search Bar 1
			</td>
		</tr>
	</table>
</div>

<#---------END HUMAN GENE FILL IN FORM (UPLOAD FILE?)------->

<#---------WORM REGION BROWSER------------------------------>

<div class="wormToDisease">
	<table align="center">
		<tr>
			<td>
			Search Bar 2
			</td>
		</tr>
	</table>
</div>

<#---------END WORM REGION BROWSER-------------------------->



<#import "../qtlfinder2/QtlFinder2.ftl" as qtlf>

<@qtlf.cartAndPlot model=model screen=screen/>



<#--end of your plugin-->	
			</div>
		</div>
	</div>
</form>
</#macro>







