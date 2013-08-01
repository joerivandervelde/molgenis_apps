<#macro humanDisease model screen>	
<#---------------DISEASE SELECTION DROPDOWN BAR-------------->
	<table align="center">
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td width="200" style="padding-left:25px;">
				<select data-placeholder="Select one or multiple diseases" multiple class=" ui-widget-content ui-corner-all" id="diseaseSelect" name="diseaseSelect"  style="width:500px;">
					<#if model.humanToWorm.humanSourceNames()?seq_contains(model.diseaseMapping)>
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
		 			<button type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__reset'; 
		 			document.forms.${screen.name}.submit();"><img src="generated-res/img/reset.png" alt=""/>Reset</button>			 	
    			</div>
			</td>
		</tr>
		<tr>
			<td colspan="3" height="70" align="center" style="padding-top:20px;">
				<span style="font-size:12px;">(<i>for example</i>: Selecting primary breast cancer or celiac disease)<br>
				Gene hits, for example on <a href="http://www.geneontology.org/">Geno Ontology</a> or anatomy terms, will show the probes and related terms for the genes that are orthologous to the human genes associated with the selected disease.
				</span>
			</td>
		</tr>
	</table>
	<br>
</#macro>