<#macro org_molgenis_wormqtl_header_HomePage screen>
	<#-- normally you make one big form for the whole plugin-->
	<form method="post" enctype="multipart/form-data" name="${screen.name}" action="">
		<#-- needed in every form: to redirect the request to the right screen-->
		<input type="hidden" name="__target" value="${screen.name}">
		<#-- needed in every form: to define the action. This can be set by the submit button-->
		<input type="hidden" name="__action">
	
		<#-- this shows a title and border -->
		<div class="formscreen">
			<div class="form_header" id="${screen.getName()}">
				${screen.label}
			</div>
		
			<#--optional: mechanism to show messages-->
			<#list screen.getMessages() as message>
				<#if message.success>
					<p class="successmessage">${message.text}</p>
				<#else>
					<p class="errormessage">${message.text}</p>
				</#if>
			</#list>
		
			<div align="center">
				<table width="700px">
					<tr>
						<td colspan="4">
							<div style="height: 10px">&nbsp;</div>
						</td>
					</tr>
					<tr>
						<td colspan="4" align="center">
							<div align="left">
							<!-- <font style='font-size:24px; font-weight:bold;'>xQTL workbench</font>-->
							<#if screen.userIsAdminAndDatabaseIsEmpty == true>
								<table bgcolor="white" border="3" bordercolor="red">
									<tr>
										<td>
											<br><i><font color="red">You are logged in as admin, and the database does not contain any investigations or other users. Automated setup is now possible. Database additions will disable this notice.</font></i><br><br>
											Enter your preferred file storage location, and press 'Load' to validate this path and load the example dataset here. Unvalidated paths are overwritten. In addition, the demo users and permissions are loaded.<br><br>
											The default shown is ./data - consider changing this before continuing. Be aware of permissions your OS grants you on this directory, depending on which user started up the application.<br><br>
											<#if screen.validpath?exists>
												<b>A valid path is present and cannot be overwritten here. To do so, use Settings -> File storage.</b><br><br>
												Path: <font style="font-size:medium; font-family: Courier, 'Courier New', monospace">${screen.validpath}</font>
											<#else>
												Path: <input type="text" size="30" style="border:2px solid black; color:blue; display:inline; font-size:medium; font-family: Courier, 'Courier New', monospace" id="inputBox" name="fileDirPath" value="./data" onkeypress="if(window.event.keyCode==13){document.forms.${screen.name}.__action.value = 'setPathAndLoad';}">
											</#if>
											<input type="submit" value="Load" id="loadExamples" onclick="document.forms.${screen.name}.__action.value = 'setPathAndLoad'; document.forms.${screen.name}.submit();"/>
											<br><br>
										</td>
									</tr>
								</table>
							</#if>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="4">
							<div style="height: 10px">&nbsp;</div>
						</td>
					</tr>
					<tr>
						<td colspan="4" align="center">
							<div align="left">
								<h2>WormQTL - Public archive and analysis web portal for natural variation data in Caenorhabditis spp.</h2>
								WormQTL is an online scalable system for QTL exploration to service the worm community. WormQTL provides many publicly available datasets and welcomes submissions from other worm researchers.
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="4">
							<div style="height: 20px">&nbsp;</div>
						</td>
					</tr>
				
					<tr>
						<td colspan="4" align="center">
							<div align="left">
								<h2>Database exploring</h2>
							</div>
						</td>
					</tr>
					
					<tr>	
						<td align="center">
							<a href="?select=QtlFinderPublic2"><img height="100" width="100" src="clusterdemo/wormqtl/FindQtls.png" /><h3>Find QTLs</h3></a>
						</td>
						<td align="center">
							<a href="?select=GenomeBrowser"><img height="100" width="100" src="clusterdemo/wormqtl/qtl_gb_button.png" /><h3>Genome browser</h3></a>
						</td>
						<td align="center">
							<a href="?select=Investigations"><img height="100" width="100" src="clusterdemo/wormqtl/qtl_data_button.png" /><h3>Browse data</h3></a>
						</td>
						<td align="center">
							<a href="?select=Help"><img height="100" width="100" src="res/img/designgg/helpicon.gif" /><h3>Help</h3></a>
						</td>
					</tr>
					
					<tr>
						<td><span><h4>Explanation</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin porta nisi ut ipsum sodales, at feugiat nisl accumsan.</span></td>
						<td><span><h4>Explanation</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin porta nisi ut ipsum sodales, at feugiat nisl accumsan.</span></td>
						<td><span><h4>Explanation</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin porta nisi ut ipsum sodales, at feugiat nisl accumsan.</span></td>
						<td><span><h4>Explanation</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin porta nisi ut ipsum sodales, at feugiat nisl accumsan.</span></td>
					</tr>
					
					
					<tr>	
						<td colspan="4" align="center">
							<div align="left">
								<h2>Human-worm linking study</h2>
							</div>
						</td>
							
					</tr>
					<tr>
						<td align="center">
							<a href="?select=QtlFinderHD"><img height="100" width="100" src="clusterdemo/wormqtl/humanDisease.png" /><h3>Human Diseases</h3></a>
						</td>	
						<td align="center">		
							<a href="?select=GenomicRegion"><img height="100" width="100" src="clusterdemo/wormqtl/genomicRegion.png" /><h3>Genomic region</h3></a>
						</td>	
						<td align="center">	
							<a href="?select=QtlLoci"><img height="100" width="100" src="clusterdemo/wormqtl/QtlLoci.png" /><h3>QTL Loci</h3></a>
						</td>	
						<td align="center">
							<a href="?select=ComparePhenotypes"><img height="100" width="100" src="clusterdemo/wormqtl/ComparePhenotypes.png" /><h3>Compare Phenotypes</h3></a>
						</td>	
					</tr>
					<tr>
						<td><span><h4>Explanation</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin porta nisi ut ipsum sodales, at feugiat nisl accumsan.</span></td>
						<td><span><h4>Explanation</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin porta nisi ut ipsum sodales, at feugiat nisl accumsan.</span></td>
						<td><span><h4>Explanation</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin porta nisi ut ipsum sodales, at feugiat nisl accumsan.</span></td>
						<td><span><h4>Explanation</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin porta nisi ut ipsum sodales, at feugiat nisl accumsan.</span></td>
					</tr>
					
					
					<tr>
						<td colspan="4">
							<div style="height: 20px">&nbsp;</div>
						</td>
					</tr>
					
					
					<tr>
						<td colspan="4">
							<div style="height: 20px">&nbsp;</div>
						</td>
					</tr>
				
				</table>
			</div>
		</div>
	</form>
</#macro>
