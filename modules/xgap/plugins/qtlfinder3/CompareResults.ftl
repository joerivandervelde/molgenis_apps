<#macro compareResults results>
		<div>
		<h3 style="padding-left:25px;"> Overlap test for <b>
		<#list results.samplePhenotypesOrGenes as samplePhenotypesOrGenes>
			<#if !samplePhenotypesOrGenes_has_next>${samplePhenotypesOrGenes}<#else>${samplePhenotypesOrGenes}; </#if>
		</#list>
		</b> from source <b>${results.sampleSource}</b><h3>
		<hr style="border-color:#13507A;border-width:thin;">
			<table id="wormHumanTable" width="850" align="center"> 
				<thead> 
					<th>Vs. phenotype</th>
					<th>From source</th>
					<th>Gene overlap</th>
					<th>Out of</th>
					<th>P-value</th>
					<th>Single test thres.</th>
					<th>Bonferroni thres.</th>
					<th>Single test signf.?</th>
					<th>Bonferroni signf.?</th>
				</thead> 
				<tbody>
					<#list results.sourceToPopulationSize?keys as source>
							<#list results.sourceToPhenoToSuccessStatesPruned[source]?keys as disease>
												
								<tr> 
								    <td><a style="text-decoration:none;" href=""><#if results.samplePhenotypesOrGenes?seq_contains(disease)><font color="gray">${disease}</font><#else>${disease}</#if></td>
								    <td>${source}</td> 
								    <td>${results.sourceToPhenoToSuccesses[source][disease]?c}</td>
								    <td>${results.sourceToPhenoToSuccessStatesPruned[source][disease]?c} vs ${results.sampleSizePruned?c}</td>
									<td>${results.sourceToPhenoToPval[source][disease]?string("0.##E0")}</td>
									<td>${results.baseThreshold?c}</td>
									<td>${results.sourceToBonferroniThreshold[source]?string("0.##E0")}</td>
									<td><#if results.sourceToPhenoToPval[source][disease] lt results.baseThreshold><font color="green">YES</font><#else><font color="red">NO</font></#if></td>
									<td><#if results.sourceToPhenoToPval[source][disease] lt results.sourceToBonferroniThreshold[source]><font color="green">YES</font><#else><font color="red">NO</font></#if></td>

								</tr>
								
							</#list>				
					</#list>
				</tbody> 
			</table>
		</div>
</#macro>