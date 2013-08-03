<#macro compareResults results>
		<div>
		<h3 style="padding-left:25px">Overlap test for
						<@compress single_line=true>
							<span onclick="alert('
												Associated genes having orthologs:
												<#list results.sampleGenes as o>
													${o}<#if o_has_next>, </#if>
													<#if o_index == 24>
														${results.sampleGenes?size-25} more...
														<#break>
													</#if>
												</#list>
												')">
									<font color="blue"><u>
										<#list results.sampleInputs as sampleInput>
											${sampleInput}<#if sampleInput_has_next>; </#if>
										</#list>
										[${results.sampleSource}]
									</u></font>
							</span>
							</@compress>
							</h3>
		<hr style="border-color:#13507A;border-width:thin;">
			<table id="wormHumanTable" width="850" align="center"> 
				<thead> 
					<th>Vs. phenotype</th>
					<th>From source</th>
					<th>Genes overlap</th>
					<th>Overlap details</th>
					<th>P-value</th>
					<th>Single test thres.</th>
					<th>Bonferroni thres.</th>
					<th>Single test signf.?</th>
					<th>Bonferroni signf.?</th>
				</thead> 
				<tbody>
					<#list results.sourceToPopulationSizePruned?keys as source>
							<#list results.sourceToPhenoToSuccessStatesPruned[source]?keys as disease>
											
								<tr> 
								    <td><a style="text-decoration:none;" href="${results.sourceToPhenoToDetails[source][disease][0]}" target="__blank"><#if results.sampleInputs?seq_contains(disease)><font color="gray">${disease}</font><#else>${disease}</#if></td>
								    <td>${source}</td> 
								    <td>${results.sourceToPhenoToSuccesses[source][disease]?c}</td>
								   <td>
										<@compress single_line=true>
										<div onclick="alert('
												Input genes:
												<#list results.sampleGenes as o>
													${o}<#if o_has_next>, </#if>
													<#if o_index == 24>
														${results.sampleGenes?size-25} more...
														<#break>
													</#if>
												</#list>
												\n\n
												${disease} genes: 
												<#list results.sourceToPhenoToGenes[source][disease] as o>
													${o}<#if o_has_next>, </#if>
													<#if o_index == 24>
														${results.sourceToPhenoToGenes[source][disease]?size-25} more...
														<#break>
													</#if>
												</#list>
												\n\n
												Overlapping genes:
												<#list results.sourceToPhenoToOverlappingGenes[source][disease]?keys as o>
													<#if results.sourceToPhenoToOverlappingGenes[source][disease]?values[0]??>
														${o} (ortholog: <#list results.sourceToPhenoToOverlappingGenes[source][disease][o] as orth>${orth}<#if orth_has_next>, </#if></#list>)<#if o_has_next>, </#if>
													<#else>
														${o}<#if o_has_next>, </#if>
													</#if>
													<#if o_index == 24>
														${results.sourceToPhenoToOverlappingGenes[source][disease]?keys?size-25} more...
														<#break>
													</#if>
												</#list>		
											')">
											</@compress>
											<nobr>
											<font color="blue"><u>${results.sampleSizePruned?c} vs ${results.sourceToPhenoToSuccessStatesPruned[source][disease]?c}</u></font>
											<img src="clusterdemo/wormqtl/overlap.png" width="30%" height="30%" />
											</nobr>
										</div>
									</td>
								    <#-- ?string(0.##E0) == scientific annotation, # sets amount of numbers behind comma-->
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