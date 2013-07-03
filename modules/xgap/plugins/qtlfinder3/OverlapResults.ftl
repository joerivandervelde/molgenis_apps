<#macro overlapResult model screen>
	<#-- RESULT SCREEN WHEN SELECTING WORM HUMAN PLOT 
	<div id="hyperTest" style= "float:left;">
		<table >
			<tr style="">
				<td style="padding-left:25px;padding-top:-15px">
					<h4>Hypergeometric test results for <b>${model.disease}</b>: <u>${model.hyperTestProbability}</u></h4>
				</td>
			</tr>
		</table>		
	</div>	
		-->
	<#-- RESULT TABLE -->
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
			<tr>
				<td>
				<td>
				<td>
				<td>
			<tr>	
		</tbody> 
	</table> 
	
	<br>
	<br>
</#macro>