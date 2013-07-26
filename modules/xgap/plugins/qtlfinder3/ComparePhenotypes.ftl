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
				<td width="200" style="padding-left:25px;">
					<select multiple class=" ui-widget-content ui-corner-all" id="comparePheno" name="comparePheno"  style="width:500px;">
				
					<#if model.humanToWorm.humanSourceNames()?seq_contains(model.diseaseMapping)>
						<#list model.humanToWorm.humanDiseasesWithOrthology(model.diseaseMapping) as phenotype>
							<option value="${phenotype}"
							>${phenotype} </option>
						</#list>	
					<#else>
						<#list model.humanToWorm.wormPhenotypesWithOrthology(model.diseaseMapping) as phenotype>
							<option value="${phenotype}"
							>${phenotype} </option>
						</#list>
					</#if>
					
						
					</select>
				</td>
			
			
			
				<td width="290" style="padding-left:10px;">
					<div class="buttons">
						<button style="color:blue;" type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__comparePhenotypes'; 
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
	<br>
</#macro>		
