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
				<#list model.allOverlaps?keys as humanPhenotype>
					<#if model.allOverlaps[humanPhenotype] gt 0>
						<tr>
						  	<td>${model.selectedWormPhenotype}</td> 
						    <td align="center">${humanPhenotype}</td> 
						    <td align="center">${model.allOverlaps[humanPhenotype]}</td>
						    <td>${model.allProbabilities[humanPhenotype]}</td>  
						</tr>					
					<#else>	
						<tr>
						  	<td>${model.selectedWormPhenotype}</td> 
						    <td align="center">${humanPhenotype}</td> 
						    <td align="center">No overlap</td>
						    <td align="center">NA</td>   
						</tr>
					</#if>
				</#list>
			</tbody> 
		</table>
	</#if>	
	<br>
	<br>
</#macro>