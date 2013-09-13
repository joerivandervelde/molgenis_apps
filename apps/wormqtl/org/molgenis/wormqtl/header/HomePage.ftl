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
			
			<@homepageStyle />
			
			<div align="center">
				<table width="700px" id="browse">
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
								<h2>WormQTL<sup>HD</sup> - A comprehensive web database for linking human disease to natural variation data in C. elegans</h2>
								WormQTL<sup>HD</sup> is an online scalable system for QTL exploration to service the worm community. WormQTL<sup>HD</sup> provides many publicly available datasets and welcomes submissions from other worm researchers.
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
							<hr style="border-color:#13507A;border-width:thin;">
								<h2>Human-worm linking study</h2>
							</div>
						</td>
							
					</tr>
					
					<tr>
						<td align="center">
							<a class="tip" href="molgenis.do?__target=QtlFinderHD&select=QtlFinderHD&screen=humanDisease"><img height="100" width="100" src="clusterdemo/wormqtl/humanDisease.png" /><h3><b>Disease</b>2<b>QTL</b></h3>
							<span>Explore novel causal genes for a human phenotype (disease) of interest by using worm (e)QTL findings</span>
							</a>
						</td>	
						<td align="center">		
							<a class="tip" href="molgenis.do?__target=QtlFinderHD&select=QtlFinderHD&screen=genomicRegion"><img height="100" width="100" src="clusterdemo/wormqtl/genomicRegion.png" /><h3><b>Region</b>2<b>disease</b></h3>
							<span>Select regions of the worm genome and link (e)QTLs to human disease phenotypes</span>
							</a>
						</td>	
						<td align="center">	
							<a class="tip" href="molgenis.do?__target=QtlFinderHD&select=QtlFinderHD&screen=qtlLoci"><img height="100" width="100" src="clusterdemo/wormqtl/QtlLoci.png" /><h3><b>QTL</b>2<b>disease</b></h3>
							<span>Select specific (e)QTLs from the WormQTL<sup>HD</sup> database and find if any can be mapped to a human disease</span>
							</a>
						</td>	
						<td align="center">
							<a class="tip" href="molgenis.do?__target=QtlFinderHD&select=QtlFinderHD&screen=comparePhenotypes"><img height="100" width="100" src="clusterdemo/wormqtl/ComparePhenotypes.png" /><h3><b>ComparePheno</b></h3>
							<span>Compare human and worm phenotypes and look for evolutionary conserved gene networks</span></a>
						</td>	
					</tr>
					
					<tr>
						<td colspan="4" align="center">
							<div align="left">
							<hr style="border-color:#13507A;border-width:thin;">
								<h2>Database exploring</h2>
							</div>
						</td>
					</tr>
					
					<tr>	
						<td align="center">
							<a class="tip" href="?select=QtlFinderPublic2"><img height="100" width="100" src="clusterdemo/wormqtl/FindQtls.png" /><h3>Find QTLs</h3>
							<span>Explore the WormQTL<sup>HD</sup> database, search for genes you are interested in and find QTLs</span>
							</a>
						</td>
						<td align="center">
							<a class="tip" href="?select=GenomeBrowser"><img height="100" width="100" src="clusterdemo/wormqtl/qtl_gb_button.png" /><h3>Genome browser</h3>
							<span>Browse the genome of <i>C. elegans</i></span>
							</a>
						</td>
						<td align="center">
							<a class="tip" href="?select=Investigations"><img height="100" width="100" src="clusterdemo/wormqtl/qtl_data_button.png" /><h3>Browse data</h3>
							<span>Scroll through all the different data sets</span>
							</a>
						</td>
						<td align="center">
							<a class="tip" href="molgenis.do?__target=QtlFinderHD&select=QtlFinderHD&screen=showHelp"><img height="100" width="100" src="res/img/designgg/helpicon.gif" /><h3>Help</h3>
							<span>Go here for a more indepth explanation on all the different functions provided by WormQTL<sup>HD</sup></span>
							</a>
						</td>
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


<#macro homepageStyle>
	<style>
		#browse	a {color:#13507A;}      
		#browse a:visited {color:#13507A;}
		#browse a:hover {color:#4682b4;}
		#browse a:active {color:#0000FF;}
		
		#browse a.tip {
			text-decoration: none
		}
		
		#browse a.tip:hover {
		    position: relative
		}
		
		#browse a.tip span {
		    display: none
		}
		
		#browse a.tip:hover span {
		    border: #c0c0c0 1px dotted;
		    padding: 5px 20px 5px 5px;
		    display: block;
		    z-index: 100;
		    background: url(../images/status-info.png) #f0f0f0 no-repeat 100% 5%;
		    left: 0px;
		    margin: 10px;
		    width: 250px;
		    position: absolute;
		    top: 10px;
		    text-decoration: none
		}
	</style>
</#macro>