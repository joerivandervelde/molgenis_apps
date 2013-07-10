<#macro overlapResult model screen>

<p><a href="javascript:Toggle();">Toggle me...</a></p>

<div id="ToggleTarget">All mimsy were the borogoves,<br />And the mome raths outgrabe.</div>

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
						    	<#list model.humanToWorm.linkToDisease(gene) as a>
						    		<#if a?starts_with("WBGene")>
						    		<#--we do nothing, list has wbgene, disease, disease etc...-->
						    		<#else>
						    			<#if a == model.humanToWorm.linkToDisease(gene)[1]>
						    				<span style="font-size:16px;"><i>${a}</i></span>,
						    			<#else>
						    				<span style="font-size:12px;">${a}</span>, 
						    			</#if>	 
						    		</#if>
						    	</#list>
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