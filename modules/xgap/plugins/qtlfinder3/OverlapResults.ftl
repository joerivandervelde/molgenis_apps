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
						    <td>${key}</td> 
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
							    	<a href="" onclick="extraDiseases${gene}.style.display = 'inline';showED${gene}.style.display = 'none'; hideED${gene}.style.display = 'inline'">more...</a>
							    </span>
							    <span id="hideED${gene}" style="display:none;">
							    	<a href="" onclick="extraDiseases${gene}.style.display = 'none';showED${gene}.style.display = 'inline'; hideED${gene}.style.display = 'none'">hide...</a>
							    </span>
							    
								<span id="extraDiseases${gene}" style="display:none;">
									<#list  model.humanToWorm.linkToDisease(gene) as a>
										<#if a == model.humanToWorm.linkToDisease(gene)[1]>
											<#--we do nothing-->
										<#elseif a?starts_with("WBGene")>
											<#--we do nothing-->
										<#else>
											${a}, 
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