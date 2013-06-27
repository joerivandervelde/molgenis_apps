<#macro humanDisease model screen>	
<#---------------DISEASE SELECTION DROPDOWN BAR-------------->
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td width="200" style="padding-left:25px;">
				<select class=" ui-widget-content ui-corner-all" id="diseaseSelect" name="diseaseSelect"  style="width:500px;">
					<#list model.humanToWorm.diseaseToHuman?keys as disease>
						<option value="${disease}" <#if model.disease == disease>selected="selected"</#if>
						>${disease} [ ${model.humanToWorm.diseaseToHuman[disease]?size} human protein(s) with ortholog ]</option> 
					</#list>
				</select>
			</td>
			<td width="290">
				<div class="buttons">
					<button style="color:blue;" type="submit" id="search" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__diseaseSearch'; 
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
				<span style="font-size:12px;">Search for worm genes via human disease worm gene ortholog linking.</span><br/> 
			</td>
		</tr>
	</table>
</#macro>