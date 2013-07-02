<#macro comparePhenotypes model screen>	
<#----------------COMPARE PHENOTYPES-------------------->
	<table align="center">
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>	
		<#--
		<tr>
			<td width="200" style="padding-left:25px;">
				<select class=" ui-widget-content ui-corner-all" id="humanPhenotype" name="humanPhenotype"  style="width:500px;">
					<#list model.humanToWorm.diseaseToHuman?keys as disease>
						<option value="${disease}" <#if model.disease == disease>selected="selected"</#if>
						>${disease} [ ${model.humanToWorm.diseaseToHuman[disease]?size} human protein(s) with ortholog ]</option> 
					</#list>
				</select>
			</td>			
		</tr>
		-->
		<tr>
			<td width="200" style="padding-left:25px;">
				<select class=" ui-widget-content ui-corner-all" id="wormPhenotype" name="wormPhenotype"  style="width:500px;">
					<#list model.humanToWorm.wormToPhenotype?keys as phenotype>
						<option value="${phenotype}" <#if model.selectedWormPhenotype == phenotype>selected="selected"</#if>
						>${phenotype} </option>
					</#list>	
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
				Worm and human phenotypes showing the largest overlap will be returned in a result table.
				</span>
			</td>
		</tr>
	</table>
	<br>
</#macro>		
