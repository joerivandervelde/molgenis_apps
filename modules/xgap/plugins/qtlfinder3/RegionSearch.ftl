<#macro regionSearch model screen>
<#---------------------WORM REGION SEARCH-------------------->
	<table align="center" width="800">
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td style="padding-left:25px;" width="50">
				Start bp:<br> <input title="starting index" id="regionStart" name="regionStart" type="text" size="10"/>
			</td>
		

			<td style="padding-left:10px;" width="50">
				End bp:<br> <input title="ending index" id="regionEnd" name="regionEnd" type="text" size="10"/>
			</td>	
		
			<td style="padding-left:10px;" width="50">
				Chromosome:<br> 
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
		
			<td style="padding-left: 10px;padding-top:10px;" width="200">
				<div class="buttons">
					<button style="color:blue;" type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__regionSearch';
					document.forms.${screen.name}.submit();">
					<img src="generated-res/img/recordview.png" alt=""/>Search</button>
				</div>	
					
				<div class="buttons">
			 		<button type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__reset'; 
			 		document.forms.${screen.name}.submit();"><img src="generated-res/img/reset.png" alt=""/>Reset</button>			 	
	    		</div>			
			</td>
		</tr>
		<tr>
		<td colspan="3" height="10" align="center">
			<br>
			<span style="font-size:12px;">(<i>for example:</i> start bp = 1000, end bp = 25000, chromosome = 4)<br>
			Gene hits, for example on <a href="http://www.geneontology.org/">Geno Ontology</a> or anatomy terms, will show the probes and related terms for the genes that are located in the selected region.
			</span>
		</td>
		</tr>
	</table>
	<br>
	<#-- <input type="checkbox" id="QtlCheckBox" name="QtlCheckBox" onclick="allowQtlSearch();">QTL Region</option> -->
	
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
