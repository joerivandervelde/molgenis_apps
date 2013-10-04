<#macro humanDisease model screen>	
<#---------------DISEASE SELECTION DROPDOWN BAR-------------->
	<table align="center">
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td width="100" align="center" style="vertical-align:middle;">
				Mapping:
				${model.diseaseMapping}
			</td>
			<td width="200" style="padding-left:25px;">
				<select data-placeholder="Select one or multiple diseases" multiple class=" ui-widget-content ui-corner-all" id="diseaseSelect" name="diseaseSelect"  style="width:500px;">
					<#if model.diseaseMapping == "All Human Sources">
						<#list model.humanToWorm.humanSourceNames() as source>
							<#list model.humanToWorm.humanDiseasesWithOrthology(source) as disease>
								<option value="${disease}||%||${source}" <#if model.diseaseSearchInputState.selectedDiseases?? && model.diseaseSearchInputState.selectedDiseases?seq_contains(disease)>SELECTED</#if>>[${source}] ${disease}</option>
							</#list>
						</#list>
					<#elseif model.humanToWorm.humanSourceNames()?seq_contains(model.diseaseMapping)>
						<#list model.humanToWorm.humanDiseasesWithOrthology(model.diseaseMapping) as disease>
							<option value="${disease}" <#if model.diseaseSearchInputState.selectedDiseases?? && model.diseaseSearchInputState.selectedDiseases?seq_contains(disease)>SELECTED</#if>>${disease}</option> 
						</#list>
					<#else>
						<#list model.humanToWorm.wormPhenotypesWithOrthology(model.diseaseMapping) as disease>
							<option value="${disease}" <#if model.diseaseSearchInputState.selectedDiseases?? && model.diseaseSearchInputState.selectedDiseases?seq_contains(disease)>SELECTED</#if>>${disease}</option> 
						</#list>
					</#if>
				</select>
			</td>
			<td width="290" style="padding-left:10px;">
				<div class="buttons">
					<button style="color:blue;" type="submit" id="search" onclick="document.getElementById('listOfHits').style.display = 'none';document.getElementById('imageSpace').style.display = 'block';document.forms.${screen.name}.__action.value = '__qtlfinderhd__diseaseSearch'; 
					document.forms.${screen.name}.submit();"><img src="generated-res/img/recordview.png" alt=""/>Search</button>
		 		</div>			
		 		
				<div class="buttons">
		 			<button type="button" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__reset'; 
		 			document.forms.${screen.name}.submit();"><img src="generated-res/img/reset.png" alt=""/>Reset</button>			 	
    			</div>
    			
			</td>
		</tr>
		<tr>
			<td colspan="5" height="70" align="center" style="padding-top:20px;">
				<span style="font-size:12px;">Make a selection of one or multiple diseases to find ortholog genes in <i>C. elegans</i>.s Click <a href=""style="text-decoration:none;" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__loadExample';document.forms.${screen.name}.submit();return false;">here</a> to load an example query.
				<br>Click <a target="_blank" href="WormQTLHD_toolTutorial.pdf#page=1">here</a> to see a tutorial on how to get results from this tool.
				</span>
			</td>
		</tr>
	</table>
	
</#macro>