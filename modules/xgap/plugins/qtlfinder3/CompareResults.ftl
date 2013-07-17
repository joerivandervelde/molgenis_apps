<#macro compareResults model screen>
	<#if model.allOverlaps??>
		<table id="wormHumanTable" width="850" align="center"> 
			<thead> 
				<tr> 
		    		<th>Worm phenotype</th> 
				    <th>Human Phenotype</th> 
				    <th>Number of genes overlapping</th> 
				    <th>P-value</th>
				</tr> 
			</thead> 
			<tbody>
				<#if model.showWorm> 
					<#list model.allOverlaps?keys as humanPhenotype>
						<#if model.allOverlaps[humanPhenotype] gt 0>
							<tr>
							  	<td>${model.selectedWormPhenotype}</td> 
							    <td>${humanPhenotype}</td> 
							    <td align="center">${model.allOverlaps[humanPhenotype]}</td>
							    <td>${model.allProbabilities[humanPhenotype]}</td>  
							</tr>					
						</#if>
					</#list>
				<#else>
					<#list model.allOverlaps?keys as wormPhenotype>
						<#if model.allOverlaps[wormPhenotype] gt 0>
							<tr>
							  	<td>${wormPhenotype}</td> 
							    <td>${model.selectedHumanPhenotype}</td> 
							    <td align="center">${model.allOverlaps[wormPhenotype]}</td>
							    <td>${model.allProbabilities[wormPhenotype]}</td>  
							</tr>	
						</#if>
					</#list>
				</#if>	
			</tbody> 
		</table>
	</#if>	
	<br><br>
</#macro>