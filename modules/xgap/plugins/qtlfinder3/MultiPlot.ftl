<#macro multiPlot model screen>

	<#assign plotWidth = (1024+50)>
	<#assign plotHeight = (768+50)>
	
	<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'gotoSearch'; document.forms.${screen.name}.submit();"><img src="generated-res/img/listview.png" alt=""/> View hits (${model.hits?size})</button></div>	
	<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'gotoCart'; document.forms.${screen.name}.submit();"><img src="generated-res/img/listview.png" alt=""/> View cart (${model.shoppingCart?keys?size})</button></div>
	<div class="buttons"><button type="submit" onclick="document.forms.${screen.name}.__action.value = 'emptyShoppingCart'; document.forms.${screen.name}.submit();"><img src="generated-res/img/cancel.png" alt=""/> Clear cart</button></div>
			
	<br><br><br>
		
	<table cellpadding="30">
		<tr>
			<td>
				<h2>Results for my selected hits:</h2>
				
				<h3>(get a <a href="?__target=${screen.name}&select=${screen.name}&p=${model.permaLink}">permanent link to these results</a>)</h3>
				
				<br>
				
				<table>
					<tr>
						<td>
							<#if model.multiplot.plot??>
							<i>Heatplot, click to enlarge:</i><br>
								<#assign html = "<html><head><title>QTL multiplot</title></head><body><img src=tmpfile/" + model.multiplot.plot + "></body></html>">
								<a href="#" onclick="var generate = window.open('', '', 'width=${plotWidth?c},height=${plotHeight?c},resizable=yes,toolbar=no,location=no,scrollbars=yes');  generate.document.write('${html}'); generate.document.close(); return false;">
									<img src="tmpfile/${model.multiplot.plot}" width="220" height="200">
								</a>
							</#if>
						</td>
						<td>
							<#if model.multiplot.cisTransPlot??>
							<i>Cis-trans plot, click to enlarge:</i><br>
								<#assign html = "<html><head><title>QTL cis-trans plot</title></head><body><img src=tmpfile/" + model.multiplot.cisTransPlot + "></body></html>">
								<a href="#" onclick="var generate = window.open('', '', 'width=${plotWidth?c},height=${plotHeight?c},resizable=yes,toolbar=no,location=no,scrollbars=yes');  generate.document.write('${html}'); generate.document.close(); return false;">
									<img src="tmpfile/${model.multiplot.cisTransPlot}" width="220" height="200">
								</a>
							</#if>
						</td>
						<td>
							<#if model.multiplot.regularPlot??>
							<i>Profile plot, click to enlarge:</i><br>
								<#assign html = "<html><head><title>QTL profile plot</title></head><body><img src=tmpfile/" + model.multiplot.regularPlot + "></body></html>">
								<a href="#" onclick="var generate = window.open('', '', 'width=${plotWidth?c},height=${plotHeight?c},resizable=yes,toolbar=no,location=no,scrollbars=yes');  generate.document.write('${html}'); generate.document.close(); return false;">
									<img src="tmpfile/${model.multiplot.regularPlot}" width="220" height="200">
								</a>
							</#if>
						</td>
					</tr>
					<tr>
						<td>
						<br><br>
						<#if model.multiplot.plot??>
							<span style="font-size:15px;font-weight:bold;">Legend, click to enlarge:</span><br/><br/>
								<#assign html = "<html><head><title>Legend</title></head><body><img src=clusterdemo/wormqtl/legend.png></body></html>">
								<a href="#" onclick="var generate = window.open('', '', 'width=1000,height=650,resizable=yes,toolbar=no,location=no,scrollbars=yes');  generate.document.write('${html}'); generate.document.close(); return false;">
									<img src="clusterdemo/wormqtl/legend.png" width="220" height="170">
								</a>
							</#if>
						</td>
						<td colspan="2">
						<br><br>
							<span style="font-size:15px;font-weight:bold;">More downloads:</span><br/>
							<br>Get the <a target="_blank" href="tmpfile/${model.multiplot.cytoNetwork}">Cytoscape network</a> for this plot. (<a target="_blank" href="http://wiki.cytoscape.org/Cytoscape_User_Manual/Creating_Networks#Import_Free-Format_Table_Files">how-to import</a>)
							<br>Get the <a target="_blank" href="tmpfile/${model.multiplot.cytoNodes}">Cytoscape nodes</a> for this plot. (<a target="_blank" href="http://cytoscape.org/manual/Cytoscape2_6Manual.html#Import Attribute Table Files">how-to import</a>)
							<br>Note: includes <b>significant results only</b>. (LOD > 3.5)
							<br><i>Save both files. Import network (has LOD scores), then node <br>attributes (chrom, bploc, dataset).</i> <a target="_blank" href="clusterdemo/wormqtl/cyto_example.png">Example visualization</a>
							<br>
							<br>Get the generated <a target="_blank" href="tmpfile/${model.multiplot.srcData}">source data</a> for these plots.
							<br>Get the generated <a target="_blank" href="tmpfile/${model.multiplot.plot?replace(".png",".R")}">multiplot plot R script</a>.
							<br>Get the generated <a target="_blank" href="tmpfile/${model.multiplot.cisTransPlot?replace(".png",".R")}">cistrans R plot script</a>.
							<br>Get the generated <a target="_blank" href="tmpfile/${model.multiplot.regularPlot?replace(".png",".R")}">profile R plot script</a>.
						</td>
					</tr>
					<tr>
						<td colspan="3">
						
							<br><br>
							<span style="font-size:15px;font-weight:bold;">Hits plotted:</span><br/>
							<div style="overflow: auto; width: 780px; max-height: 400px;">
								<table border="0">
									<#list model.multiplot.matches?values as d>
										<tr>
											<td>
												<a target="_blank" href="molgenis.do?select=${d.get(typefield)}s&__target=${d.get(typefield)}s&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=${d.get(typefield)}_name&__filter_operator=EQUALS&__filter_value=${d.name}">${d.name}<#if d.label?? && d.label?length gt 0> / ${d.label}</#if></a>
											</td>
											<td>
												[<a target="_self" href="?__target=${screen.name}&select=${screen.name}&__action=__entity__report__for__${d.name}">explore deeper</a>]
											</td>
											<td>
												<#if d.description??>&nbsp;&nbsp;&nbsp;&nbsp;<#if d.description?length gt 50>${d.description?substring(0, 50)}...<#else>${d.description}</#if></#if><br>
											</td>
											<td>
												<#if d.get('ReportsFor_name')?? && d.get('ReportsFor_name')?is_string && d.get('ReportsFor_name')?length gt 0>
													<a target="_blank" href="http://www.wormbase.org/species/c_elegans/gene/${d.get('ReportsFor_name')}">WormBase</a>
												<#elseif d.symbol?? && d.symbol?length gt 0>
													<a target="_blank" href="http://www.wormbase.org/species/c_elegans/gene/${d.symbol}">WormBase</a>
												</#if>
											</td>
										</tr>
									</#list>
								</table>
							</div>
							<br>
							<span style="font-size:15px;font-weight:bold;">From datasets:</span><br/>
							<table border="0">
								<#list model.multiplot.datasets?values as d>
									<tr>
										<td>
											<nobr><b>${d.id}</b>: <a target="_blank" href="molgenis.do?select=Datas&__target=Datas&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=Data_name&__filter_operator=EQUALS&__filter_value=${d.name}">${d.name}</a></nobr>
										</td>
										<td>
											<#if d.description??>${d.description}</#if>
										</td>
									</tr>
								</#list>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</#macro>