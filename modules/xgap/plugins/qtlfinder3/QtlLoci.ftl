<#macro qtlLoci model screen>
<#----------------QTL LOCI SEARCH-------------------->
	<table align="center" width="800">
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr >
			<td style="padding-left:25px;" width="50">
				Probe name:<br /> <input title="trait name" id="traitInput" name="traitInput" type="text" size="10" <#if model.exampleMode?? && model.exampleMode == true>value="AGIUSA19536"</#if>/>
			</td>
			
			<td style="padding-left:10px;" width="50">
				LOD threshold:<br /> <input title="LOD Score Threshold" id="lodThreshold2" name="lodThreshold" type="text" size="10" <#if model.exampleMode?? && model.exampleMode == true>value="50"</#if>/>
			</td>
			
			<td style="padding-left:10px;" width="50">
			Dataset:<br/>
			<select name="regionDataSetSelect"  style="width:30px;">
				<#list model.qtlSearchInputState.dataSets as dataset>
					<option value="${dataset}" <#if model.qtlSearchInputState.selectedDataSet == dataset>selected="selected"</#if>
					>${dataset}</option> 
				</#list>
			</select>
			</td>
			
			<td width="290" style="padding-top:10px;padding-left:10px;">
				<div class="buttons">
					<button style="color:blue;" type="submit" onclick="document.getElementById('listOfHits').style.display = 'none';document.getElementById('imageSpace').style.display = 'block';document.forms.${screen.name}.__action.value = '__qtlfinderhd__traitRegionSearch';
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
			<td colspan="4" height="10" align="center">
				<br><span style="font-size:12px;">Placeholder for text that is descriptive. Per datasource? Click <a href=""style="text-decoration:none;" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__loadExample';document.forms.${screen.name}.submit();return false;">here</a> to load a tested example query.
				</span>
			</td>
		</tr>		
	</table>
	<br>
</#macro>
