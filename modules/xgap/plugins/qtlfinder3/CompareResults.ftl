<#macro compareResults results model>

		<#-- amount of genes shown per input, results and overlap-->
		<#assign cutoff = 100>

		<div>
		<h3 style="padding-left:25px">Overlap test for
						<@compress single_line=true>
							<span onclick="alert('
												Associated genes having orthologs:
												<#list results.sampleGenes as o>
													${o}<#if o_has_next>, </#if>
													<#if o_index == cutoff-1>
														${results.sampleGenes?size-cutoff} more...
														<#break>
													</#if>
												</#list>
												')">
									<font style="cursor: pointer;" color="blue"><u>
										<#list results.sampleInputs as sampleInput>
											${sampleInput}<#if sampleInput_has_next>; </#if>
										</#list>
										<#if model.diseaseMapping != "All Human Sources">
										[${results.sampleSource}]
										</#if>
									</u></font>
							</span>
							</@compress>
							</h3>
	
			<hr style="border-color:#13507A;border-width:thin;" />
			<div id="hiddenTableColumnExplanation" style="display:none">
				<a onclick="document.getElementById('notHiddenTableColumnExplanation').style.display='inline';document.getElementById('hiddenTableColumnExplanation').style.display='none';" style="float:right;padding-right:12px;cursor:pointer;"><img style="cursor:pointer;" height="40" width="40" src="res/img/designgg/helpicon_pressed.gif" /></a>
				
				<ul>
					<li>
								<b>Vs. phenotype</b> <br>
								
								This is the phenotype for which this row is reporting.<br> 
								Your selected phenotype(s) showed overlap (in any degree) with this <i>Vs. phenotype</i>.
					</li>			
								<br>
					<li>			
								<b>From source</b> <br>
								The data source where this <i>Vs. phenotype</i> is from. 
					</li>			
								<br>
					<li>		
								<b>Genes overlap </b> <br>
								The amount of overlapping genes between the selected phenotype(s) and this <i>Vs. phenotype</i>.
					</li>			
								<br>			
					<li>		
								<b>Overlap details </b> <br>
								The genes that are associated with the selected phenotype(s) 
								and the <i>Vs. phenotype</i>. When clicked, it also shows exactly which genes are overlapping. Groups of genes shown are limited to ${cutoff} gene names.
					</li>			
								<br>
					<li>		
								<b>P-value </b> <br>
								The gene overlap is significance based on a hypergeometric test.
					</li>			
								<br>
					<li>		
								<b>Single test threshold </b> <br>
								The p-value threshold for individually performed tests.
					</li>			
								<br>
					<li>		
								<b>Bonferroni threshold </b> <br>
								The p-value threshold with strict multiple testing correction. 
					</li>			
								<br>
					<li>		
								<b>Single test significant </b> <br>
								Is the p-value significant for single test threshold?
					</li>			
								<br>
					<li>		
								<b>Bonferroni significant </b> <br>
								Is the p-value significant after multiple testing correction?
					</li>
				</ul>
						
			</div>
			
			
			<div id="notHiddenTableColumnExplanation" style="display:inline;">
				<a onclick="document.getElementById('hiddenTableColumnExplanation').style.display='inline';document.getElementById('notHiddenTableColumnExplanation').style.display='none';" style="float:right;padding-right:12px;cursor:pointer;"><img style="cursor:pointer;" height="40" width="40" src="res/img/designgg/helpicon.gif" /></a>	
			</div>
		
			<div class="buttons">
			 	<button  id="setFilterToYes" style="margin-left:10px;float:left;display:none;cursor:pointer;">Hide non-significant hits</button>			 	
	    	</div>	
	    	
	    	<div class="buttons">
			 	<button  id="setFilterToNo" style="margin-left:10px;display:inline;float:left;cursor:pointer;">Show all hits</button>			 	
	    	</div>
				
			<br><br><br>
				
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
								    <#if model.diseaseMapping == "All Human Sources">
								    	<td><a style="text-decoration:none;" href="${results.sourceToPhenoToDetails[source][disease][0]}" target="__blank"><#if results.sampleInputs?seq_contains(disease + ' [' + source + ']')><font color="gray">${disease}</font><#else>${disease}</#if></td>
								    <#else>
								    	<td><a style="text-decoration:none;" href="${results.sourceToPhenoToDetails[source][disease][0]}" target="__blank"><#if results.sampleInputs?seq_contains(disease)><font color="gray">${disease}</font><#else>${disease}</#if></td>
								    </#if>	
								    <td>${source}</td> 
								    <td>${results.sourceToPhenoToSuccesses[source][disease]?c}</td>
								   <td>
										
										<#assign body = "<!DOCTYPE html><html><head><title>Overlap details for " + disease + "</title></head><body><b>Input genes for ">
										<#list results.sampleInputs as sampleInput>
											<#assign body = body + sampleInput><#if sampleInput_has_next><#assign body = body + "; "></#if>
										</#list>
										<#assign body = body +":</b><br>">
										
												<#list results.sampleGenes as o>
													<#assign body = body + o><#if o_has_next><#assign body = body + ", "></#if>
													<#if o_index == cutoff-1>
														<#assign body = body + (results.sampleGenes?size-cutoff)?c + " more...">
														<#break>
													</#if>
												</#list>
												<#assign body = body + "<br><br>">
												<#assign body = body + "<b>" + disease + " genes:</b><br>">
												<#list results.sourceToPhenoToGenes[source][disease] as o>
													<#assign body = body + o><#if o_has_next><#assign body = body + ", "></#if>
													<#if o_index == cutoff-1>
														<#assign body = body + (results.sourceToPhenoToGenes[source][disease]?size-cutoff)?c + " more...">
														<#break>
													</#if>
												</#list>
												<#assign body = body + "<br><br><b>Overlapping genes:</b><br>">
												<#list results.sourceToPhenoToOverlappingGenes[source][disease]?keys as o>
													<#if results.sourceToPhenoToOverlappingGenes[source][disease]?values[0]??>
														<#assign body = body + o + " (ortholog: "><#list results.sourceToPhenoToOverlappingGenes[source][disease][o] as orth><#assign body = body + orth><#if orth_has_next><#assign body = body + ", "></#if></#list><#assign body = body + ")"><#if o_has_next><#assign body = body + ", "></#if>
													<#else>
														<#assign body = body + o><#if o_has_next><#assign body = body + ", "></#if>
													</#if>
													<#if o_index == cutoff-1>
														<#assign body = body + (results.sourceToPhenoToOverlappingGenes[source][disease]?keys?size-cutoff)?c + " more...">
														<#break>
													</#if>
												</#list>		
										
										<#assign body = body + "</body></html>">
										
											<a href="#" onclick="var generate = window.open('', '', 'width=500,height=500,resizable=yes,toolbar=no,location=no,scrollbars=yes');  generate.document.write('${body}'); generate.document.close(); return false;">
												<nobr>
												<font style="cursor: pointer;" color="blue"><u>${results.sampleSizePruned?c} vs ${results.sourceToPhenoToSuccessStatesPruned[source][disease]?c}</u></font>
												<img src="clusterdemo/wormqtl/overlap.png" width="23px" height="16px" />
												</nobr>
											</a>
											
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