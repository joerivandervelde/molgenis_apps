<#macro comparePhenotypes model screen>	
<#----------------COMPARE PHENOTYPES-------------------->
	<div class="buttons" style="text-align:center;">
		<input type="radio" name="listSelect" id="worm" 
		onclick="wormPhenotypeTable.style.display='block';humanPhenotypeTable.style.display='none';"
		checked>Worm phenotype list</input>
		
		<input type="radio" name="listSelect" id="human" 
		onclick="wormPhenotypeTable.style.display='none';humanPhenotypeTable.style.display='block';"
		>Human phenotype list</input>
	</div>	

	<div id="wormPhenotypeTable" style="display:block;">
		<table align="center">
			<tr>
				<td colspan="3" height="10" align="center">
					&nbsp;
				</td>
			</tr>	
			
			<tr>
				<td width="200" style="padding-left:25px;">
					<select class=" ui-widget-content ui-corner-all" id="wormPhenotype" name="wormPhenotype"  style="width:500px;">
						<#list model.humanToWorm.wormPhenotypesWithOrthology(model.phenotypeMapping) as phenotype>
							<option value="${phenotype}" <#if model.selectedWormPhenotype?? && model.selectedWormPhenotype == phenotype>selected="selected"</#if>
							>${phenotype} </option>
						</#list>	
					</select>
				</td>
			
			
			
				<td width="290" style="padding-left:10px;">
					<div class="buttons">
						<button style="color:blue;" type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__comparePhenotypesWorm'; 
						document.forms.${screen.name}.submit();"><img src="generated-res/img/recordview.png" alt=""/>Compare</button>
			 		</div>				
			 		
					<div class="buttons">
			 			<button type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__reset'; 
			 			document.forms.${screen.name}.submit();"><img src="generated-res/img/reset.png" alt=""/>Reset</button>			 	
	    			</div>
				</td>	
			</tr>	
			<tr>
				<td colspan="3" height="70" align="center" style="padding-top:20px;">
					<span style="font-size:12px;">(<i>for example</i>: high incidence male progeny or embryonic arrest)<br>
					The genes that are associated with the selected phenotype will be compared with every human phenotype in the current database. 
					All worm and human phenotypes will be returned in a result table showing how they overlap.
					</span>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="humanPhenotypeTable" style="display:none;">	
		<table align="center">
			<tr>
				<td colspan="3" height="10" align="center">
					&nbsp;
				</td>
			</tr>	
			
			<tr>
				<td width="200" style="padding-left:25px;">
					<select class=" ui-widget-content ui-corner-all" id="humanPhenotype" name="humanPhenotype"  style="width:500px;">
						<#list model.humanToWorm.humanDiseasesWithOrthology(model.diseaseMapping) as disease>
							<option value="${disease}" <#if model.disease?? && model.disease == disease>selected="selected"</#if>>${disease}</option> 
						</#list>
					</select>
				</td>			

				<td width="290" style="padding-left:10px;">
					<div class="buttons">
						<button style="color:blue;" type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__comparePhenotypesHuman'; 
						document.forms.${screen.name}.submit();"><img src="generated-res/img/recordview.png" alt=""/>Compare</button>
			 		</div>				
			 		
					<div class="buttons">
			 			<button type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__reset'; 
			 			document.forms.${screen.name}.submit();"><img src="generated-res/img/reset.png" alt=""/>Reset</button>			 	
	    			</div>
				</td>	
			</tr>	
			<tr>
				<td colspan="3" height="70" align="center" style="padding-top:20px;">
					<span style="font-size:12px;">(<i>for example</i>: primary breast cancer or celiac disease)<br>
					The genes that are associated with the selected phenotype will be compared with every worm phenotype in the current database. 
					All human and worm phenotypes will be returned in a result table showing how they overlap.
					</span>
				</td>
			</tr>
		</table>
	</div>	
	<br>
</#macro>		
