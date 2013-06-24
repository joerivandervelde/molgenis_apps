<#macro QtlFinderHD screen>

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
			
			<#-- Call macro's start-->
			
			<@javaScript />
			<@style />
			
			<@qtlLoci model = model screen = screen />
		
			<@humanDisease model = model screen = screen />
			<@regionSearch model = model screen = screen />
			
			<@comparePhenotypes model = model screen = screen /> 
			
			<@rl.resultList model=model screen=screen/>
			
			<#--
			<@resultTable model = model screen = screen />
			
			<#-- Call macro's end -->
			
		</div>
	</form>
</#macro>

<#macro javaScript>
<#--- SCRIPTS --->
<script>
	$(document).ready(function(){
	
		<#-- TABLE WIDGET -->
		$( "#wormHumanTable" ).dataTable();
		
		<#-- DROPDOWN WIDGET -->
		$("#diseaseSelect").chosen();
		$("#humanPhenotype").chosen();
		$("#wormPhenotype").chosen();
	
		<#-- QTL CHECK BOX -->
		function allowQtlSearch()
		{
  			if(document.getElementById('QtlCheckBox').checked){
    			var elem = document.getElementById("elem");
				elem.style.display = "show";
  			}else{
    			elem.style.display = "none";
    		}	
		}	
	});
</script>
</#macro>

<#macro style>
<#-- RESULT TABLE STYLE -->
	<style> 
		table.dataTable tr.odd{
			background:#EAEAEA;
		}
		
		table.dataTable tr.even{
			 	background:#FFFFFF;
		}
		
		table.dataTable tr.even td.sorting_1 {
			background:#F0F0F0;
		}
		
		table.dataTable tr.odd td.sorting_1 {
			background:#D8D8D8;
		}
	</style>
</#macro>

<#macro regionSearch model screen>
<#---------------------WORM REGION SEARCH-------------------->
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr width="200">
			<td style="padding-left:25px;">
				Start:<br /> <input title="starting index" id="regionStart" name="regionStart" type="text" size="10"/>
			</td>
		
			<td style="padding-left:25px;">
				End:<br /> <input title="ending index" id="regionEnd" name="regionEnd" type="text" size="10"/>
			</td>	
		
			<td style="padding-left:25px;">
				Chromosome:<br /> 
				<select id="regionChr" name="regionChr">
					<option value="1">I</option>
					<option value="2">II</option>
					<option value="3">III</option>
					<option value="4">IV</option>
					<option value="5">V</option>
					<option value="6">X</option>
					<option value="7">MtDNA</option>
				</select>
			</td>	
	
			<td width="290" style="padding-left: 30px;padding-top:10px;">
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
			<td colspan="3" height="70" style="padding-top:20px;padding-left:25px;">
				<span style="font-size:12px;">Browse a region of a specific chromosome to find genes there</span>
			</td>
		</tr>
	</table>
	
	<input type="checkbox" id="QtlCheckBox" name="QtlCheckBox" onclick="allowQtlSearch();">QTL Region</option>
	
	<div id="elem" style="display:none;">
		<table>
			<tr>
				<td colspan="3" height="10" align="center">
					&nbsp;
				</td>
			</tr>
			
			<tr>
				<td style="padding-left:25px;">
					Dataset:<br/>
					<select name="dataSetSelect"  style="width:50px;">
						<#list model.dataSets as dataset>
							<option value="${dataset}" <#if model.dataSet == dataset>selected="selected"</#if>
							>${dataset}</option> 
						</#list>
					</select>
				</td>
			
				<td style="padding-left:25px;">
					Threshold:<br/><input title="LOD Score Threshold" id="QtlLodThreshold" name="QtlLodThreshold" type="text" size="4"
				</td>
			</tr>
		</table>
	</div>	
</#macro>


<#macro qtlLoci model screen>
<#----------------QTL LOCI SEARCH-------------------->
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td style="padding-left:25px;">
				Probe or Trait name:<br /> <input title="trait name" id="traitInput" name="traitInput" type="text" size="10"/>
			</td>
			
			<td style="padding-left:25px;">
				Threshold:<br /> <input title="LOD Score Threshold" id="lodThreshold2" name="lodThreshold" type="text" size="4"/>
			</td>
			
			<td style="padding-left:25px;">
			Dataset:<br/>
			<select name="regionDataSetSelect"  style="width:50px;">
				<#list model.dataSets as dataset>
					<option value="${dataset}" <#if model.dataSet == dataset>selected="selected"</#if>
					>${dataset}</option> 
				</#list>
			</select>
			</td>
			
			<td width="290" style="padding-top:10px;">
				<div class="buttons">
					<button type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__traitRegionSearch';
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
			<td colspan="3" height="40" style="padding-top:20px;padding-left:25px;">
				
			</td>
		</tr>		
	</table>
</#macro>


<#macro humanDisease model screen>	
<#---------------DISEASE SELECTION DROPDOWN BAR-------------->
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td width="200" style="padding-left:25px;">
				<select class=" ui-widget-content ui-corner-all" id="diseaseSelect" name="diseaseSelect"  style="width:500px;">
					<#list model.humanToWorm.diseaseToHuman?keys as disease>
						<option value="${disease}" <#if model.disease == disease>selected="selected"</#if>
						>${disease} [ ${model.humanToWorm.diseaseToHuman[disease]?size} human protein(s) with ortholog ]</option> 
					</#list>
				</select>
			</td>
			<td width="290">
		 		<div class="buttons">
					<button type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__diseaseSearch'; 
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
				<span style="font-size:12px;">Search for worm genes via human disease worm gene association.</span><br/> 
				<span style="font-size:12px;">A Hyper geometric test will also be performed, calculating the significance of the disease - worm ortholog match.</span>
			</td>
		</tr>
	</table>
	
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td style="padding-left:25px;">
				<textarea name="enspIds" cols="50" rows="5">Please fill in one or several ENSP identifiers to see if there is an ortholog in C. elegans. Seperate by comma.
			
EXAMPLE: ENSP00000230732, ENSP00000005178, ENSP00000301067</textarea>
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
</#macro>
	

<#macro comparePhenotypes model screen>	
<#----------------COMPARE PHENOTYPES-------------------->
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>	
		<tr>
			<td width="200" style="padding-left:25px;">
				<select class=" ui-widget-content ui-corner-all" id="humanPhenotype" name="humanPhenotype"  style="width:500px;">
					<#list model.humanToWorm.diseaseToHuman?keys as disease>
						<option value="${disease}" <#if model.disease == disease>selected="selected"</#if>
						>${disease} [ ${model.humanToWorm.diseaseToHuman[disease]?size} human protein(s) with ortholog ]</option> 
					</#list>
				</select>
			</td>
		</tr>
		
		<tr>
			<td width="200" style="padding-left:25px;">
				<select class=" ui-widget-content ui-corner-all" id="wormPhenotype" name="wormPhenotype"  style="width:500px;">
					<#list model.humanToWorm.wormToPhenotype?keys as phenotype>
						<option value="${phenotype}" <#if model.phenotype == phenotype>selected="selected"</#if>
						>${phenotype} </option>
					</#list>	
				</select>
			</td>
		</tr>
	</table>
</#macro>		



<#macro resultTable model screen>
<#-- RESULT TABLE -->
	<table id="wormHumanTable"> 
		<thead> 
			<tr> 
	    		<th>Worm Gene</th> 
			    <th>Human Gene</th> 
			    <th>Human Phenotype</th> 
			    <th>P-value</th> 
			</tr> 
		</thead> 
		<tbody> 
			<tr> 
			    <td>WBGene00000001</td> 
			    <td>ENSP00000001</td> 
			    <td>Cancer</td> 
			    <td>1.29e-17</td> 
			</tr> 
			<tr> 
			    <td>WBGene00000002</td> 
			    <td>ENSP00000002</td> 
			    <td>Cancer</td> 
			    <td>1.29e-17</td> 
			</tr> 
			<tr> 
			    <td>WBGene00000003</td> 
			    <td>ENSP00000003</td> 
			    <td>NA</td> 
			    <td>NA</td> 
			</tr> 
			<tr> 
			   <td>WBGene00000004</td> 
			    <td>NA</td> 
			    <td>NA</td> 
			    <td>NA</td> 
			</tr> 
		</tbody> 
	</table> 
</#macro>
