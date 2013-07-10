<#macro overlapResult model screen>

</body>
</html>
	<table id="wormHumanTable"> 
		<thead> 
			<tr> 
	    		<th>Worm Gene</th> 
			    <th>Human Gene</th> 
			    <th>Human Phenotype</th> 
			    <th>P-value</th> 
			</tr> 
		</thead> 
		<tbody> 
			<#list model.humanToWorm.humanToWorm?keys as key>
				<#list model.genes as gene>
					<#if gene == model.humanToWorm.humanToWorm[key]>
						<tr>
				  			<td>${gene}</td> 
						    <td><a href="http://www.ensembl.org/Homo_sapiens/Search/Results?species=Homo_sapiens;idx=;q=${key}" target="_blank">${key}</a></td> 
						    <td>
						    	<span style="font-size:16px;">
						    		<#list model.humanToWorm.linkToDisease(gene) as a>
							    		<#if a?starts_with("WBGene")>
							    			<#--we do nothing, list has wbgene, disease, disease etc...-->
							    		<#else>
							    			<#if a == model.humanToWorm.linkToDisease(gene)[1]>
							    				${a} 
							    			</#if>	 
							    		</#if>
							    	</#list>
							    </span>
							    <br>	
							    
							    <span id="showED${gene}" style="display:inline;">
							    	<a style="color:blue;cursor:pointer;" onclick="extraDiseases${gene}.style.display = 'inline';showED${gene}.style.display = 'none'; hideED${gene}.style.display = 'inline'">more...</a>
							    </span>
							    <span id="hideED${gene}" style="display:none;">
							    	<a style="color:blue;cursor:pointer;" onclick="extraDiseases${gene}.style.display = 'none';showED${gene}.style.display = 'inline'; hideED${gene}.style.display = 'none'">hide...</a>
							    </span>
							    
								<span id="extraDiseases${gene}" style="display:none;">
									<#list  model.humanToWorm.linkToDisease(gene) as a>
										<#if a == model.humanToWorm.linkToDisease(gene)[1]>
											<#--we do nothing-->
										<#elseif a?starts_with("WBGene")>
											<#--we do nothing-->
										<#else>
											<#if a == model.humanToWorm.linkToDisease(gene)[model.humanToWorm.linkToDisease(gene)?size - 1]>
												${a}
											<#else>
												${a},
											</#if> 
										</#if>
									</#list>
								</span>
						    </td> 
						    <td>NA</td>
						</tr>		
					</#if>	
				</#list>
			</#list>
		</tbody> 
	</table> 	
	<br><br>
</#macro>