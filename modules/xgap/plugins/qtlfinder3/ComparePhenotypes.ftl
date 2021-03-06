<#macro comparePhenotypes model screen>	
<#----------------COMPARE PHENOTYPES-------------------->
	<div id="wormPhenotypeTable" style="display:block;">
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
					<select data-placeholder="Select one or multiple diseases" multiple class=" ui-widget-content ui-corner-all" id="comparePheno" name="comparePheno"  style="width:500px;">
					<#if model.diseaseMapping == "All Human Sources">
						<#list model.humanToWorm.humanSourceNames() as source>
							<#list model.humanToWorm.humanDiseasesWithOrthology(source) as disease>
								<option value="${disease}||%||${source}" <#if model.diseaseSearchInputState.selectedDiseases?? && model.diseaseSearchInputState.selectedDiseases?seq_contains(disease)>SELECTED</#if>>[${source}] ${disease}</option>
							</#list>
						</#list>
					<#elseif model.humanToWorm.humanSourceNames()?seq_contains(model.diseaseMapping)>
						<#list model.humanToWorm.humanDiseasesWithOrthology(model.diseaseMapping) as phenotype>
							<option value="${phenotype}" <#if model.diseaseSearchInputState.selectedDiseases?? && model.diseaseSearchInputState.selectedDiseases?seq_contains(phenotype)>SELECTED</#if>>${phenotype} </option>
						</#list>	
					<#else>
						<#list model.humanToWorm.wormPhenotypesWithOrthology(model.diseaseMapping) as phenotype>
							<option value="${phenotype}" <#if model.diseaseSearchInputState.selectedDiseases?? && model.diseaseSearchInputState.selectedDiseases?seq_contains(phenotype)>SELECTED</#if>
							>${phenotype} </option>
						</#list>
					</#if>
					
						
					</select>
				</td>
			
			
			
				<td width="290" style="padding-left:10px;">
					<div class="buttons">
						<button style="color:blue;" type="submit" id="search" onclick="document.getElementById('listOfHits').style.display = 'none';document.getElementById('imageSpace').style.display = 'block';document.forms.${screen.name}.__action.value = '__qtlfinderhd__comparePhenotypes'; 
						document.forms.${screen.name}.submit();"><img src="generated-res/img/recordview.png" alt=""/>Compare</button>
			 		</div>				
			 		
					<div class="buttons">
			 			<button type="button" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__reset'; 
			 			document.forms.${screen.name}.submit();"><img src="generated-res/img/reset.png" alt=""/>Reset</button>			 	
	    			</div>
				</td>	
			</tr>	
			<tr>
				<td colspan="4" height="70" align="center" style="padding-top:20px;">
					<span style="font-size:12px;">Make a selection of one or multiple diseases to do a gene overlap test against all the diseases and phenotypes in the database. Click <a href=""style="text-decoration:none;" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__loadExample';document.forms.${screen.name}.submit();return false;">here</a> to load an example query.
					Click <a target="_blank" href="WormQTLHD_toolTutorial.pdf#page=7">here</a> to see a tutorial on how to get results from this tool.
					</span>
				</td>
			</tr>
		</table>
	</div>
</#macro>		
