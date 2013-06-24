<#macro probeReport screen model>
	
	<#assign plotWidth = (1024+50)>
	<#assign plotHeight = (768+50)>

	<#import "../reportbuilder/ReportBuilder.ftl" as rb>	
		<#assign r = model.report.entity>
		
		<h1>${r.get(typefield)} <a target="_blank" href="molgenis.do?select=${r.get(typefield)}s&__target=${r.get(typefield)}s&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=${r.get(typefield)}_name&__filter_operator=EQUALS&__filter_value=${r.name}">${r.name}<#if r.label?? && r.label?length gt 0> / ${r.label}</#if></a></h1>
		
		<#if model.qtls?? && model.qtls?size gt 0><#-- should have them really -->
			<br>
		
			<div id="${r.name}_closeme" style="float: left; padding: 5px; border: 1px solid #999; width: 400px; height: 400px; text-align:center; ">
				<#list model.qtls as qtl>
				
					Hit #${qtl_index+1}
					<br>Max. <#if qtl.plot??><#if qtl.plot?starts_with('eff')>effect size<#else>LOD score</#if><#else>value</#if>: ${qtl.peakValue}<br>in <a target="_blank" href="molgenis.do?select=Datas&__target=Datas&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=Data_name&__filter_operator=EQUALS&__filter_value=${qtl.matrix.name}">${qtl.matrix.name}</a><br><br>
					<#if qtl.plot??>
						<#assign html = "<html><head><title>QTL plot</title></head><body><img src=tmpfile/" + qtl.plot + "></body></html>">
						<a href="#" onclick="var generate = window.open('', '', 'width=${plotWidth?c},height=${plotHeight?c},resizable=yes,toolbar=no,location=no,scrollbars=yes');  generate.document.write('${html}'); generate.document.close(); return false;">
							<img src="tmpfile/${qtl.plot}" width="320" height="240">
						</a><br>
					<#else>
						<br><br><br><i>No plot available.</i><br><br><br><br>
					</#if>
					<a href="#QTL${qtl_index+1}">View <#if qtl.plot??><#if qtl.plot?starts_with('eff')>effect<#else>QTL</#if><#else>value</#if> details <img src="generated-res/img/filter.png" /></a>
					
					<#if qtl_has_next>
					</div><div style="float: left; padding: 5px; border: 1px solid #999; width: 400px; height: 400px; text-align:center; ">
					</#if>
				</#list>
			</div>
			
		</#if>
	
	
		<div style="clear: both; padding: 0px; border: 0px solid #999; width: 850px; height: 60px; text-align:left; "></div>
		<h1><#if model.qtls?? && model.qtls?size gt 0>Details<#else><i>No plots or details available</i></#if></h1>
		
		
		<#list model.qtls as qtl>
			<table cellpadding="30">
				<tr>
					<td>
						<h3 id="QTL${qtl_index+1}">#${qtl_index+1} - <#if qtl.plot??><#if qtl.plot?starts_with('eff')>Effect<#else>QTL</#if><#else>Values</#if> in data matrix <a target="_blank" href="molgenis.do?select=Datas&__target=Datas&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=${qtl.matrix.id}">${qtl.matrix.name}</a>. Basic information:</h3>
						<table cellpadding="3" border="1" style="width:700px;">
							<tr class="form_listrow0">
								<td colspan="2">
									<b>Plot<b>
								</td>
							</tr>
							<tr class="form_listrow1">
								<td align="center"  colspan="2">
								
									<#if qtl.plot??>
										<i>Click to enlarge</i><br>
										<#assign html = "<html><head><title>QTL plot</title></head><body><img src=tmpfile/" + qtl.plot + "></body></html>">
										<a href="#" onclick="var generate = window.open('', '', 'width=${plotWidth?c},height=${plotHeight?c},resizable=yes,toolbar=no,location=no,scrollbars=yes');  generate.document.write('${html}'); generate.document.close(); return false;">
											<img src="tmpfile/${qtl.plot}" width="160" height="120">
										</a>
									<#else>
										<i>No plot available</i><br>
									</#if>
								</td>
							</tr>
							<tr class="form_listrow0">
								<td colspan="2">
									<b>Highest <#if qtl.plot??><#if qtl.plot?starts_with('eff')>effect size<#else>LOD score</#if><#else>value</#if><b>
								</td>
							</tr>
							<tr class="form_listrow1">
								<td>
									Value: 
								</td>
								<td>
									${qtl.peakValue}
								</td>
							</tr>
							<tr class="form_listrow1">
								<td>
									<#if qtl.plot??>Marker:<#else>Trait:</#if>
								</td>
								<td>
									<#if qtl.plot??><a target="_blank" href="molgenis.do?select=Markers&__target=Markers&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=Marker_name&__filter_operator=EQUALS&__filter_value=${qtl.peakMarker}">${qtl.peakMarker}</a><#else>${qtl.peakMarker}</#if>
									<#if qtl.markerAnnotations?keys?seq_contains(qtl.peakMarker)>at bp ${qtl.markerAnnotations[qtl.peakMarker].bpstart?c}<#if qtl.markerAnnotations[qtl.peakMarker].cm??>, cM ${qtl.markerAnnotations[qtl.peakMarker].cm}</#if></#if>
									[<a target="_self" href="?__target=${screen.name}&select=${screen.name}&__action=__entity__report__for__${qtl.peakMarker}">explore deeper</a>]
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td><h3>#${qtl_index+1} - <#if qtl.plot??><#if qtl.plot?starts_with('eff')>Effect<#else>QTL</#if><#else>Value</#if> advanced information:</h3>
						<div style="overflow: auto; max-height: 400px; width: 720px;">
							<table cellpadding="3" border="1" style="width:700px;">
								<tr class="form_listrow0">
									<td colspan="5">
										<b>All <#if qtl.plot??><#if qtl.plot?starts_with('eff')>effect sizes<#else>LOD scores</#if><#else>Values</#if></b>
									</td>
								</tr>
								<tr class="form_listrow1">
									<td>
										<i><#if qtl.plot??>Marker<#else>Trait</#if> name</i>
									</td>
									<td>
										<i><#if qtl.plot??><#if qtl.plot?starts_with('eff')>Effect size<#else>LOD score</#if><#else>Value</#if></i>
									</td>
									<td>
										<i><#if qtl.plot??>Marker<#else>Trait</#if> cM pos.</i>
									</td>
									<td>
										<i><#if qtl.plot??>Marker<#else>Trait</#if> basepair pos.</i>
									</td>
									<td>
										<i><#if qtl.plot??>Marker<#else>Trait</#if> chromosome</i>
									</td>
								</tr>
							<#list qtl.markers as m>
								<tr class="form_listrow1">
									<td>
										<#if qtl.plot??><a target="_blank" href="molgenis.do?select=Markers&__target=Markers&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=Marker_name&__filter_operator=EQUALS&__filter_value=${m}">${m}</a><#else>${m}</#if>
										[<a target="_self" href="?__target=${screen.name}&select=${screen.name}&__action=__entity__report__for__${m}">explore deeper</a>]
									</td>
									<td>
											${qtl.valuesForMarkers[m_index]}
									</td>
									<td>
											<#if qtl.markerAnnotations?keys?seq_contains(m) && qtl.markerAnnotations[m].cm??>${qtl.markerAnnotations[m].cm?c}</#if>
									</td>
									<td>
											<#if qtl.markerAnnotations?keys?seq_contains(m)>${qtl.markerAnnotations[m].bpstart?c}</#if>
									</td>
									<td>
											<#if qtl.markerAnnotations?keys?seq_contains(m) && qtl.markerAnnotations[m].chromosome_name??><a target="_blank" href="molgenis.do?select=Chromosomes&__target=Chromosomes&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=Chromosome_name&__filter_operator=EQUALS&__filter_value=${qtl.markerAnnotations[m].chromosome_name}">${qtl.markerAnnotations[m].chromosome_name}</a></#if>
									</td>
								</tr>
								<#if m_index = 1000>
								<tr class="form_listrow1">
									<td colspan="5">
										<b>Limited at 1000, total size is ${qtl.markers?size?c}</b>
									</td>
								</tr>
								<#break>
								</#if>
							</#list>
							</table>
							<h3>Data matrix where this <#if qtl.plot??><#if qtl.plot?starts_with('eff')>effect<#else>QTL</#if><#else>value</#if> was found:</h3>
							<@rb.printEntity r=qtl.matrix/>
						</div>
					</td>
				</tr>
			</table>
		</#list>
		
		<div style="height: 40px; width: 800px; float: left;"></div><br>
		
		<h1>Additional</h1>
		
		<table cellpadding="30">
			<tr>
				<td>
					<h2>Record information</h2>
					<@rb.printEntity r=r/>
				</td>
			</tr>
			
		<#if model.report.matrices?size gt 0>
			<tr>
				<td>
					<#list model.report.matrices as ml>
						<br><br><br><h2>Present in data matrix <i>"<a target="_blank" href="molgenis.do?select=Datas&__target=Datas&__comebacktoscreen=${screen.name}&__action=filter_set&__filter_attribute=Data_name&__filter_operator=EQUALS&__filter_value=${ml.data.name}">${ml.data.name}</a>"</i></h2>
						<h3>Matrix record information:</h3>
						<@rb.printEntity r=ml.data/>
						
						<br>
						<table cellpadding="3" border="1" style="width:700px;">
							<tr class="form_listrow1">
								<td>
									Total number of rows
								</td>
								<td>
									${ml.totalRows?c}
								</td>
							</tr>
							<tr class="form_listrow1">
								<td>
									Total number of columns
								</td>
								<td>
									${ml.totalCols?c}
								</td>
							</tr>
							<tr class="form_listrow1">
								<td>
									"${r.name}" present at row index
								</td>
								<td>
									<#if ml.rowIndex == -1>Not present in rows<#else>${ml.rowIndex}</#if>
								</td>
							</tr>
							<tr class="form_listrow1">
								<td>
									"${r.name}" present at column index
								</td>
								<td>
									<#if ml.colIndex == -1>Not present in columns<#else>${ml.colIndex}</#if>
								</td>
								
							</tr>
						</table>
							
							
						<h3>Values of ${r.name} in "${ml.data.name}":</h3>
						<h4>Row plot</h4>
						<#if ml.rowImg??>
						<table>
							<tr>
								<td>
									<i>Click to enlarge</i>
								</td>
							</tr>
						</table>
						
						<#assign html = "<html><head><title>Row plot</title></head><body><img src=tmpfile/" + ml.rowImg + "></body></html>">
						<a href="#" onclick="var generate = window.open('', '', 'width=850,height650,resizable=yes,toolbar=no,location=no,scrollbars=yes');  generate.document.write('${html}'); generate.document.close(); return false;">
							<img src="tmpfile/${ml.rowImg}" width="160" height="120">
						</a>
						
						<#else>
							<#if ml.rowIndex == -1>
								${r.name} not in row values, no row plot was made.
							<#else>
								<#if ml.totalRows gt 1000>
									More than 1000 rows in the matrix, no plot was made.
								<#else>
									Image creation failed. Maybe you have missing values, or a wrong value type.
								</#if>
							</#if>
						</#if>
							
						<h4>Column plot</h4>
						<#if ml.colImg??>
						<table>
							<tr>
								<td>
									<i>Click to enlarge</i>
								</td>
							</tr>
						</table>
						
						<#assign html = "<html><head><title>Column plot</title></head><body><img src=tmpfile/" + ml.colImg + "></body></html>">
						<a href="#" onclick="var generate = window.open('', '', 'width=850,height650,resizable=yes,toolbar=no,location=no,scrollbars=yes');  generate.document.write('${html}'); generate.document.close(); return false;">
							<img src="tmpfile/${ml.colImg}" width="160" height="120">
						</a>
						
						<#else>
							<#if ml.colIndex == -1>
								${r.name} not in column values, no column plot was made.
							<#else>
								<#if ml.totalCols gt 1000>
									More than 1000 columns in the matrix, no plot was made.
								<#else>
									Image creation failed. Maybe you have missing values, or a wrong value type.
								</#if>
							</#if>
						</#if>
							
							
						<br>
						
						<h3>Spearman correlation results</h3>
						
						<h4>Row correlations</h4>
						
						<#if ml.rowCorr??>
						<div style="overflow: auto; max-height: 400px; width: 720px;">
							<table border="1" cellpadding="3" style="width: 700px;">
								<tr class="form_listrow0">
									<td>
										<b>Other rows in "${ml.data.name}"</b>
									</td>
									<td>
										<b>Spearman's rho</b>
									</td>
								</tr>
							<#list ml.rowCorr?keys as key>
								<tr class="form_listrow1">
									<td>
										${key} [<a target="_self" href="?__target=${screen.name}&select=${screen.name}&__action=__entity__report__for__${key}">explore deeper</a>]
									</td>
									<td>
										<#if ml.rowCorr[key]??>${ml.rowCorr[key]}<#else>N/A</#if>
									</td>
								</tr>
							</#list>
							</table>
						</div>
						<#else>
							<#if ml.rowIndex == -1>
								${r.name} not in row values, no row correlations were calculated.
							<#else>
								<#if (ml.totalCols * ml.totalRows) gt 1000000>
									Less than 2 elements to compare, or more than 1 million values in the matrix, no correlations were calculated.
								<#else>
									Correlation failed.
								</#if>
							</#if>
						</#if>
						
						<h4>Column correlations</h4>
							
						<#if ml.colCorr??>
						<div style="overflow: auto; max-height: 400px; width: 720px;">
							<table border="1" cellpadding="3" style="width: 700px;">
								<tr class="form_listrow0">
									<td>
										<b>Other columns in "${ml.data.name}"</b>
									</td>
									<td>
										<b>Spearman's rho</b>
									</td>
								</tr>
							<#list ml.colCorr?keys as key>
								<tr class="form_listrow1">
									<td>
										<a target="_self" href="?__target=${screen.name}&select=${screen.name}&__action=__entity__report__for__${key}">${key}</a>
									</td>
									<td>
										<#if ml.colCorr[key]??>${ml.colCorr[key]}<#else>N/A</#if>
									</td>
								</tr>
							</#list>
							</table>
						</div>
						<#else>
							<#if ml.colIndex == -1>
								${r.name} not in column values, no column correlations were calculated.
							<#else>
								<#if (ml.totalCols * ml.totalRows) gt 1000000>
									Less than 2 elements to compare, or more than 1 million values in the matrix, no correlations were calculated.
								<#else>
									Correlation failed.
								</#if>
							</#if>
						</#if>
					</#list>
				</td>
			</tr>
		</#if>
	</table>	
</#macro>	