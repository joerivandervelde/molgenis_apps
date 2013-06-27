<#macro overlapResult model screen>
	<#-- RESULT SCREEN WHEN SELECTING WORM HUMAN PLOT 	-->
	<div id="hyperTest" style= "float:left;">
		<table >
			<tr style="">
				<td style="padding-left:25px;padding-top:-15px">
					<h4>Hypergeometric test results for <b>${model.disease}</b>: <u>${model.hyperTestProbability}</u></h4>
				</td>
			</tr>
		</table>		
	</div>	

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
			    <td>WBGene00000001</td> 
			    <td>ENSP00000001</td> 
			    <td>Cancer</td> 
			    <td>1.29e-17</td> 
			</tr> 
			<tr> 
			    <td>WBGene00000002</td> 
			    <td>ENSP00000002</td> 
			    <td>Cancer</td> 
			    <td>1.29e-17</td> 
			</tr> 
			<tr> 
			    <td>WBGene00000003</td> 
			    <td>ENSP00000003</td> 
			    <td>NA</td> 
			    <td>NA</td> 
			</tr> 
			<tr> 
			   <td>WBGene00000004</td> 
			    <td>NA</td> 
			    <td>NA</td> 
			    <td>NA</td> 
			</tr> 
		</tbody> 
	</table> 
	
</#macro>