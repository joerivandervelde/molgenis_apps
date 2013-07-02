<#macro compareResults model screen>
	<#if model.allOverlaps??>
		<table id="wormHumanTable"> 
			<thead> 
				<tr> 
		    		<th>Worm phenotype</th> 
				    <th>Human Phenotype</th> 
				    <th>Number of genes overlapping</th> 
				</tr> 
			</thead> 
			<tbody> 
				<#list model.allOverlaps?keys as humanPhenotype>
					<#if model.allOverlaps[humanPhenotype] gt 0>
						<tr>
						  	<td>${model.selectedWormPhenotype}</td> 
						    <td>${humanPhenotype}</td> 
						    <td>${model.allOverlaps[humanPhenotype]}</td>  
						</tr>
					</#if>
				</#list>
			</tbody> 
		</table>
	</#if>	
	
	
	
	<br>
	<br>
</#macro>