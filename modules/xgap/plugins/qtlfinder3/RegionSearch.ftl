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
