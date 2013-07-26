<#macro compareResults model screen>
		<div>	
			<table id="wormHumanTable" width="850" align="center"> 
				<thead> 
					<tr> 
			    		<th>Worm phenotype</th> 
					    <th>Human Phenotype</th> 
						<th>Source</th>
					    <th>Number of genes overlapping</th> 
					    <th>P-value</th>
					</tr> 
				</thead> 
				<tbody>
					<#list model.allOverlaps?keys as source>
							<#list model.allOverlaps[source]?keys as disease>
								
								<tr>
								  	<td>[TODO: LIST OF SELECTED PHENOTYPES]</td> 
								    <td>${disease}</td>
								    <td>${source}</td> 
								    <td align="center">${model.getAllOverlaps()[source][disease]}</td>
								    <td>${model.getAllProbabilities()[source][disease]}</td>  
								</tr>
							</#list>				
					</#list>
				</tbody> 
			</table>
		</div>
</#macro>