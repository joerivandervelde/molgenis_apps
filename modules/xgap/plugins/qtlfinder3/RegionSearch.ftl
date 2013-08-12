<#macro regionSearch model screen>
<#---------------------WORM REGION SEARCH-------------------->
	<table align="center" width="800">
		<tr>
			<td colspan="4" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td style="padding-left:10px;" width="50">
				Chromosome:<br> 
				<select id="regionChr" name="regionChr">
					<#list model.regionSearchInputState.chromosomes?keys as key>
					<option value="${key}" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__regionChrChange';document.forms.${screen.name}.submit();" <#if model.regionSearchInputState.selectedChromosome == key>SELECTED</#if>>${key}</option>
					</#list>
				</select>
			</td>
			<td style="padding-left:25px;" width="50">
				Start bp:<br> <input title="starting index" id="regionStart" name="regionStart" type="text" value="${model.regionSearchInputState.selectedStartBp?c}" size="10"/>
			</td>
			<td style="padding-left:10px;" width="50">
				End bp:<br> <input title="ending index" id="regionEnd" name="regionEnd" type="text" value="${model.regionSearchInputState.selectedEndBp?c}" size="10"/>
			</td>
			<td style="padding-left: 10px;padding-top:10px;" width="200">
				<div class="buttons">
					<button style="color:blue;" type="submit" onclick="document.getElementById('listOfHits').style.display = 'none';document.getElementById('imageSpace').style.display = 'block';
					document.forms.${screen.name}.__action.value = '__qtlfinderhd__regionSearch';
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
			<td>
			</td>
			<td colspan="2">
				<div id="slider"></div>
			</td>
			<td>
			</td>
		</tr>	
		
		<tr>
			<td colspan="4" height="10" align="center">
				<br>
				<span style="font-size:12px;">Select a region of the <i>C. elegans</i> genome by submitting a chromosome and a start and end position(bp) via the slider or by typing it manually. Click <a href=""style="text-decoration:none;" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__loadExample';document.forms.${screen.name}.submit();return false;">here</a> to load a tested example query.
				</span>
			</td>
		</tr>
	</table>
	<br>
	<#-- <input type="checkbox" id="QtlCheckBox" name="QtlCheckBox" onclick="allowQtlSearch();">QTL Region</option> -->
	
	<#-- maybe merge the two in the future to allow QTL search constrained by Region search... hide for now-->
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
						<#list model.qtlSearchInputState.dataSets as dataset>
							<option value="${dataset}" <#if model.qtlSearchInputState.selectedDataSet == dataset>selected="selected"</#if>
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
