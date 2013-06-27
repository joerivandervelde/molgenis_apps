<#macro comparePhenotypes model screen>	
<#----------------COMPARE PHENOTYPES-------------------->
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>	
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
		
		<tr>
			<td width="200" style="padding-left:25px;">
				<select class=" ui-widget-content ui-corner-all" id="wormPhenotype" name="wormPhenotype"  style="width:500px;">
					<#list model.humanToWorm.wormToPhenotype?keys as phenotype>
						<option value="${phenotype}" <#if model.phenotype == phenotype>selected="selected"</#if>
						>${phenotype} </option>
					</#list>	
				</select>
			</td>
		</tr>
	</table>
</#macro>		
