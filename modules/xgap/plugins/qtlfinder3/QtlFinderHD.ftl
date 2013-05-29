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

<#--- SCRIPT AND STYLE SECTION --->

<script> <#-- JQuery widget scripts -->
	$(document).ready(function(){
		
		<#-- TAB WIDGET -->
		$( "#tabs" ).tabs({ active: 0 });
			
		$( "#tabs" ).tabs();
		$( ".tabs-bottom .ui-tabs-nav, .tabs-bottom .ui-tabs-nav > *" ).removeClass( "ui-corner-all ui-corner-top" ).addClass( "ui-corner-bottom" );
		$( ".tabs-bottom .ui-tabs-nav" ).appendTo( ".tabs-bottom" );
		
		$( "#tabs" ).tabs({ collapsible: false });
		
		<#-- ACCORDION WIDGET -->
		$( "#accordion" ).accordion();	
		
		<#-- DROPDOWN WIDGET -->
		$("#diseaseSelect").chosen();
		$("#humanPhenotype").chosen();
		$("#wormPhenotype").chosen();
	});
</script>

<style> <#-- JQuery widget style -->
	#tabs { background: #EAEAEA; border:none;}
	.allTabs { height: 30px; background: #13507A; width: auto; }
	.selectTab { height: 30px; background: white; font-size: 12px;}
	#tabs li.ui-tabs-active {background: white; border-bottom: 1;}
	
	#tabs .tabs-spacer { float: left; height: 300px; }
	.tabs-bottom .ui-tabs-nav { clear: left;}
	.tabs-bottom .ui-tabs-nav li {top:-15px; bottom: 0; margin: 0 .2em 1px 0; border-bottom: 1; border-top: 0; }
	.tabs-bottom .ui-tabs-nav li.ui-tabs-active { margin-top: -1px; padding-top: 1px; }
</style>

<#-- TAB AREA -->

<div id="tabs" class="tabs-bottom" >
	<ul class="allTabs">
		<li class="selectTab"><a href="#tabs-1" onclick=$( "#tabs" ).tabs( "option", "active", 0 );>Region Search</a></li>
		<li class="selectTab"><a href="#tabs-2" onclick=$( "#tabs" ).tabs( "option", "active", 1 );>QTL Search</a></li>
		<li class="selectTab"><a href="#tabs-3" onclick=$( "#tabs" ).tabs( "option", "active", 2 );>Disease Search</a></li>
		<li class="selectTab"><a href="#tabs-4" onclick=$( "#tabs" ).tabs( "option", "active", 3 );>Worm pheno and Human pheno</a></li>
	</ul>	
	
	<div class="tabs-spacer"></div>
	
	<#---------------------WORM REGION SEARCH-------------------->
	<div id="tabs-1">
		<div id="accordion">
			<h3>Region search</h3>
			<div>
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
			</div>
			<h3>Region searching by Trait</h3>
			<div>
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
							<span style="font-size:12px;">Submit a probe and a lod score threshold to get genes that are located in the region matching your criteria</span>
						</td>
					</tr>		
				</table>
			</div>
		</div>
	</div>
	
	<#-----------------WORM REGION QTL SEARCH-------------------->
	<div id="tabs-2">
		<table>
			<tr>
				<td colspan="3" height="10" align="center">
					&nbsp;
				</td>
			</tr>
			
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
				Start:<br/><input title="starting index" id="QtlRegionStart" name="QtlRegionStart" type="text" size="10" 
			</td>
			
			<td style="padding-left:25px;">
				Chromosome:<br /> 
				<select id="QtlRegionChr" name="QtlRegionChr">
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
					Threshold:<br/><input title="LOD Score Threshold" id="QtlLodThreshold" name="QtlLodThreshold" type="text" size="4"
				</td>
				<td style="padding-left:25px;">
					End:<br/><input title="ending index" id="QtlRegionEnd" name="QtlRegionEnd" type="text" size="10"
				</td>	
			</tr>
			<tr style="margin-top:8px;">
				<td style="padding-left:25px;padding-top:10px;">
					<div class="buttons">
						<button type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__QtlSearch';
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
				<td colspan="3" height="70" align="center" style="padding-top:20px;padding-left:25px;">
					<span style="font-size:12px;">Browse a region for QTLs that are above a specified threshold</span>
				</td>
			</tr>		
		</table>
	</div>
	
	<#---------------DISEASE SELECTION DROPDOWN BAR-------------->
	<div id="tabs-3">
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
	</div>
	
	<#----------------HUMAN GENE FILL IN FORM-------------------->
	<div id="tabs-4">
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
						<option>WORM PHENOTYPES</option>
					</select>
				</td>
			</tr>
		</table>	
	</div>
	
</div>

<br />
<br />

<#if model.showTable>
	<@resultTable model = model screen = screen />
</#if>

<#import "../qtlfinder2/QtlFinder2.ftl" as qtlf>
<@qtlf.cartAndPlot model=model screen=screen />

<#--end of your plugin-->	
			</div>
		</div>
	</div>
</form>
</#macro>

<#macro resultTable model screen>
<style> <#-- Result table style -->
	#result a:link {
		color: #666;
		font-weight: bold;
		text-decoration:none;
	}
	#result a:visited {
		color: #999999;
		font-weight:bold;
		text-decoration:none;
	}
	#result a:active,
	#result a:hover {
		color: #bd5a35;
		text-decoration:underline;
	}
	#result {
		font-family:Arial, Helvetica, sans-serif;
		color:#666;
		font-size:12px;
		text-shadow: 1px 1px 0px #fff;
		background:#eaebec;
		margin:20px;
		border:#ccc 1px solid;
	
		-moz-border-radius:3px;
		-webkit-border-radius:3px;
		border-radius:3px;
	
		-moz-box-shadow: 0 1px 2px #d1d1d1;
		-webkit-box-shadow: 0 1px 2px #d1d1d1;
		box-shadow: 0 1px 2px #d1d1d1;
	}
	#result th {
		padding:21px 25px 22px 25px;
		border-top:1px solid #fafafa;
		border-bottom:1px solid #e0e0e0;
	
		background: #ededed;
		background: -webkit-gradient(linear, left top, left bottom, from(#ededed), to(#ebebeb));
		background: -moz-linear-gradient(top,  #ededed,  #ebebeb);
	}
	#result th:first-child {
		text-align: left;
		padding-left:20px;
	}
	#result tr:first-child th:first-child {
		-moz-border-radius-topleft:3px;
		-webkit-border-top-left-radius:3px;
		border-top-left-radius:3px;
	}
	#result tr:first-child th:last-child {
		-moz-border-radius-topright:3px;
		-webkit-border-top-right-radius:3px;
		border-top-right-radius:3px;
	}
	#result tr {
		text-align: center;
		padding-left:20px;
	}
	#result td:first-child {
		text-align: left;
		padding-left:20px;
		border-left: 0;
	}
	#result td {
		padding:18px;
		border-top: 1px solid #ffffff;
		border-bottom:1px solid #e0e0e0;
		border-left: 1px solid #e0e0e0;
	
		background: #fafafa;
		background: -webkit-gradient(linear, left top, left bottom, from(#fbfbfb), to(#fafafa));
		background: -moz-linear-gradient(top,  #fbfbfb,  #fafafa);
	}
	#result tr.even td {
		background: #f6f6f6;
		background: -webkit-gradient(linear, left top, left bottom, from(#f8f8f8), to(#f6f6f6));
		background: -moz-linear-gradient(top,  #f8f8f8,  #f6f6f6);
	}
	#result tr:last-child td {
		border-bottom:0;
	}
	#result tr:last-child td:first-child {
		-moz-border-radius-bottomleft:3px;
		-webkit-border-bottom-left-radius:3px;
		border-bottom-left-radius:3px;
	}
	#result tr:last-child td:last-child {
		-moz-border-radius-bottomright:3px;
		-webkit-border-bottom-right-radius:3px;
		border-bottom-right-radius:3px;
	}
	#result tr:hover td {
		background: #f2f2f2;
		background: -webkit-gradient(linear, left top, left bottom, from(#f2f2f2), to(#f0f0f0));
		background: -moz-linear-gradient(top,  #f2f2f2,  #f0f0f0);	
	}
</style>

<table id="result" cellspacing='0' class="table">
	<caption><h3>Diseases mapped to this QTL</h3></caption>
	<thead>
		<tr>
			<th scope="col">Worm genes</th>
			<th scope="col">Human disease</th>
			<th scope="col">Probability</th>
		</tr>
	</thead>
	<tbody>
		<#list model.geneAssociatedDiseases?keys as gene>
			<tr>
				<td>${gene}</td>
				<td>${model.geneAssociatedDiseases[gene][0]}</td>
				<td>0.000001</td>
			<tr>
		</#list>
	</tbody>
</table>

<br />
<br />
<br /> 

</#macro>



