<#macro qtlLoci model screen>
<#----------------QTL LOCI SEARCH-------------------->
	<table>
		<tr>
			<td colspan="3" height="10" align="center">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td style="padding-left:25px;">
				Probe or Trait name:<br /> <input title="trait name" id="traitInput" name="traitInput" type="text" size="10"/>
			</td>
			
			<td style="padding-left:25px;">
				Threshold:<br /> <input title="LOD Score Threshold" id="lodThreshold2" name="lodThreshold" type="text" size="4"/>
			</td>
			
			<td style="padding-left:25px;">
			Dataset:<br/>
			<select name="regionDataSetSelect"  style="width:50px;">
				<#list model.dataSets as dataset>
					<option value="${dataset}" <#if model.dataSet == dataset>selected="selected"</#if>
					>${dataset}</option> 
				</#list>
			</select>
			</td>
			
			<td width="290" style="padding-top:10px;">
				<div class="buttons">
					<button type="submit" onclick="document.forms.${screen.name}.__action.value = '__qtlfinderhd__traitRegionSearch';
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
			<td colspan="3" height="40" style="padding-top:20px;padding-left:25px;">
				
			</td>
		</tr>		
	</table>
</#macro>
