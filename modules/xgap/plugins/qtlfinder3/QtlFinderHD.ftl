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
			
			<#assign allDataTypes = "__ALL__DATATYPES__SEARCH__KEY">
			<#--====================================-->	
		
			<#-- imports -->	
			<#import "../qtlfinder3/ResultList.ftl" as rl>
			<#import "../qtlfinder3/HumanDisease.ftl" as hd>
			<#import "../qtlfinder3/RegionSearch.ftl" as rs>
			<#import "../qtlfinder3/ComparePhenotypes.ftl" as cp>
			<#import "../qtlfinder3/QtlLoci.ftl" as ql>
			<#import "../qtlfinder3/OverlapResults.ftl" as or>
			<#import "../qtlfinder3/ShoppingCart.ftl" as sc>
			<#import "../qtlfinder3/MultiPlot.ftl" as mp>
			
			<#-- macro's-->	
			<@styleAndScript />
			<@browseSearch />	
			
			<#if model.screenType == "humanDisease">
				<@hd.humanDisease model = model screen = screen />
			</#if>
			
			<#if model.screenType == "genomicRegion">
				<@rs.regionSearch model = model screen = screen />
			</#if>
			
			<#if model.screenType == "qtlLoci">
				<@ql.qtlLoci model = model screen = screen />
			</#if>
			
			<#if model.screenType == "comparePhenotypes">
				<@cp.comparePhenotypes model = model screen = screen /> 
			</#if>
		
			<#if model.screenType == "shoppingCart">
				<@sc.shoppingCart model = model screen = screen />
			</#if>
			
			<@rl.resultList model = model screen = screen/>
			
			<#if model.multiplot??>
				<@mp.multiPlot model=model screen=screen />
			</#if>
		
			
			
			
			<#-- <@or.overlapResult model = model screen = screen /> -->
				
		</div>
	</form>
</#macro>

<#macro browseSearch>
	<table align="center" id="browse">
		<tr>
			<td align="center">
				<a href="molgenis.do?__target=QtlFinderHD&select=QtlFinderHD&screen=humanDisease"><b>Human Diseases</b></a>
			</td>	
			<td align="center">		
				<a href="molgenis.do?__target=QtlFinderHD&select=QtlFinderHD&screen=genomicRegion"><b>Genomic region</b></a>
			</td>	
			<td align="center">	
				<a href="molgenis.do?__target=QtlFinderHD&select=QtlFinderHD&screen=qtlLoci"><b>QTL Loci</b></a>
			</td>	
			<td align="center">
				<a href="molgenis.do?__target=QtlFinderHD&select=QtlFinderHD&screen=comparePhenotypes"><b>Compare Phenotypes</b></a>
			</td>	
		</tr>	
	</table>
	<hr style="border-color:#13507A;border-width:thin;">
</#macro>

<#macro styleAndScript>
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
		
		
		#browse td {padding-left:15px;padding-top:5px;}
		#browse	a {color:#13507A;}      
		#browse a:visited {color:#13507A;}
		#browse a:hover {color:#4682b4;}
		#browse a:active {color:#0000FF;}
		
	</style>
</#macro>
