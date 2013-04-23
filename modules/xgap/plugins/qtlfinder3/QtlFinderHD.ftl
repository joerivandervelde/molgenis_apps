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
		<td style="padding-left:20px;padding-top:5px;">
			<input type="radio" name="filterSelect"
			onclick=";display('hide', 'diseaseToWorm');display('hide', 'humanGeneToWorm');display('hide', 'wormToDisease');display('show', 'regionToWorm');display('hide', 'regionToQtl');"
			<#if model.selectedSearch == 'regionToWorm'>checked</#if>
			>Search worm probes by genetic region</input>
		</td>
	</tr>
	<tr>
		<td style="padding-left:20px;padding-top:5px;">
			<input type="radio" name="filterSelect" value="regionToQtl" 
			onclick="display('hide', 'diseaseToWorm');display('hide', 'humanGeneToWorm');display('hide', 'wormToDisease');display('hide', 'regionToWorm');display('show', 'regionToQtl');"
			<#if model.selectedSearch == 'regionToQtl'>checked</#if>
			>Search a region for significant QTLs</input>
		</td>
	</tr>
	<tr>
		<td style="padding-left:20px;padding-top:5px;">
			<input type="radio" name="filterSelect" 
			onclick="display('show', 'diseaseToWorm');display('hide', 'humanGeneToWorm');display('hide', 'wormToDisease');display('hide', 'regionToWorm');display('hide', 'regionToQtl');" 
			<#if model.selectedSearch == 'diseaseToWorm'>checked</#if>
			checked>Find worm genes by human disease</input>
		</td>
	</tr>
	<tr>
		<td style="padding-left:20px;padding-top:5px;">
			<input type="radio" name="filterSelect"
			onclick="display('hide', 'diseaseToWorm');display('show', 'humanGeneToWorm');display('hide', 'wormToDisease');display('hide', 'regionToWorm');display('hide', 'regionToQtl');"
			<#if model.selectedSearch == 'humanGeneToWorm'>checked</#if>
			>Find orthologs in worm for your own set of human genes</input>
		</td>
	</tr>
	<tr>
		<td style="padding-left:20px;padding-top:5px;">
			<input type="radio" name="filterSelect" 
			onclick="display('hide', 'diseaseToWorm');display('hide', 'humanGeneToWorm');display('show', 'wormToDisease');display('hide', 'regionToWorm');display('hide', 'regionToQtl');"
			<#if model.selectedSearch == 'wormToDisease'>checked</#if>
			>Scan regions of the worm for possible disease association within human</input>
		</td>
	</tr>
</table>

<#---------------END OF RADIO BUTTON SELECTION--------------->

<#---------------------WORM REGION SEARCH-------------------->

<div id="regionToWorm" <#if model.selectedSearch != "regionToWorm">style="display:none"</#if>>
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr width="200">
			<td style="padding-left:25px;">
				Start:<br /> <input title="starting index" id="regionStart" name="regionStart" type="text" />
			</td>
		
			<td style="padding-left:25px;">
				End:<br /> <input title="ending index" id="regionEnd" name="regionEnd" type="text" />
			</td>	
		
			<td style="padding-left:25px;">
				Chromosome:<br /> 
				<select name="regionChr">
					<option value="1">I</option>
					<option value="2">II</option>
					<option value="3">III</option>
					<option value="4">IV</option>
					<option value="5">V</option>
					<option value="6">X</option>
					<option value="7">MtDNA</option>
				</select>
			</td>	
	
			<td width="290" style="padding-left: 30px;">
				<div class="buttons">
					<button type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__regionSearch';
					document.forms.${screen.name}.submit();">
					<img src="generated-res/img/recordview.png" alt=""/>Submit</button>
				</div>	
					
				<div class="buttons">
			 		<button type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__reset'; 
			 		document.forms.${screen.name}.submit();"><img src="generated-res/img/reset.png" alt=""/>Reset</button>			 	
	    		</div>
					
			</td>
		</tr>
		<tr>
			<td colspan="3" height="70" align="center" style="padding-top:20px;">
				<span style="font-size:12px;">Browse a region of a specific chromosome to find genes there</span>
			</td>
		</tr>
	</table>
</div>


<#-----------------END WORM REGION SEARCH-------------------->

<#-----------------WORM REGION QTL SEARCH-------------------->

<div id="regionToQtl" <#if model.selectedSearch != 'regionToQtl'>style="display:none;"</#if>>
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
			<td style="padding-left:25px;">
			Dataset:<br/><select><option>Select a dataset...</option></select>
			</td>
			<td style="padding-left:25px;">
				Start:<br/><input title="starting index" id="regionStart" name="regionStart" type="text" />
			</td>
			<td style="padding-left:25px;">
				Chromosome:<br /> 
				<select name="regionChr">
					<option value="1">I</option>
					<option value="2">II</option>
					<option value="3">III</option>
					<option value="4">IV</option>
					<option value="5">V</option>
					<option value="6">X</option>
					<option value="7">MtDNA</option>
				</select>
			</td>	
		<tr>
			<td style="padding-left:25px;">
				Threshold:<br/><input title="LOD Score Threshold" id="lodThreshold" name="lodTreshold" type="text" size="4"/>
			</td>
			<td style="padding-left:25px;">
				End:<br/><input title="ending index" id="regionEnd" name="regionEnd" type="text" />
			</td>	
		</tr>
		<tr style="margin-top:8px;">
			<td style="padding-left:25px;padding-top:10px;">
				<div class="buttons">
					<button type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__regionSearch';
					document.forms.${screen.name}.submit();">
					<img src="generated-res/img/recordview.png" alt=""/>Submit</button>
				</div>	
			</td>
			<td style="padding-left:25px;padding-top:10px;">		
				<div class="buttons">
			 		<button type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__reset'; 
			 		document.forms.${screen.name}.submit();"><img src="generated-res/img/reset.png" alt=""/>Reset</button>			 	
	    		</div>
			</td>
		</tr>
		<tr>
			<td colspan="3" height="70" align="center" style="padding-top:20px;">
				<span style="font-size:12px;">Browse a region for QTLs that are above a specified threshold</span>
			</td>
		</tr>		
	</table>
</div>

<#-----------------END WORM REGION QTL SEARCH---------------->


<#---------------DISEASE SELECTION DROPDOWN BAR-------------->

<div id="diseaseToWorm" <#if model.selectedSearch != 'diseaseToWorm'>style="display:none"</#if>>
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
						>${disease} [ ${model.humanToWorm.diseaseToHuman[disease]?size} Gene(s) ]</option> 
					</#list>
				</select><script>$("#Phenotype_select").chosen();</script>
			</td>
		
			<td width="290">
			 	
			 	<div class="buttons">
					<button type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__shop'; 
				 	document.forms.${screen.name}.submit();">
				 	<img src="clusterdemo/icons/shoppingcart.png" alt=""/>Add to Cart</button>
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

<div id="humanGeneToWorm" <#if model.selectedSearch != 'humanGeneToWorm'>style="display:none"</#if>>
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td style="padding-left:25px;">
				<textarea name="enspIds" cols="50" rows="6">Please fill in one or several ENSP identifiers to see if there is an ortholog in C. elegans. Seperate by comma.
			
EXAMPLE: ENSP00000230732, ENSP00000005178, ENSP00000301067	
				</textarea>
			</td>
			
			<td width="290">
			 	<div class="buttons">
					<button type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__humanGeneSearch';
					document.forms.${screen.name}.submit();">
					<img src="generated-res/img/recordview.png" alt=""/>Submit</button>
				</div>		
				
				<div class="buttons">
			 		<button type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__reset'; 
			 		document.forms.${screen.name}.submit();"><img src="generated-res/img/reset.png" alt=""/>Reset</button>			 	
	    		</div>		 	
			</td>
		</tr>
		<tr>
			<td colspan="3" height="70" align="center" style="padding-top:20px;">
				<span style="font-size:12px;">Search for worm genes by submitting human ENSP gene identifiers.</span>
			</td>
		</tr>
	</table>
</div>

<#---------END HUMAN GENE FILL IN FORM (UPLOAD FILE?)------->

<#---------WORM REGION BROWSER------------------------------>

<div id="wormToDisease" <#if model.selectedSearch != 'wormToDisease'>style="display:none"</#if>>
	<table align="center">
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				DROPDOWN DATASET SELECT
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







