<#macro helpScreen>
<div id="help" align="left" style="padding-left:10px;">	
	<nobr><a href="molgenis.do?__target=QtlFinderHD&select=QtlFinderHD&screen=humanDisease" style="float:right;padding-right:10px;">Back to Disease2QTL</a></nobr>
	<h2>Help</h2>

	<h3>For biologists:</h3>
	
	<ul>
		<li>
			Download the supplementary materials for WormQTL-HD.org <a target="_blank" href="SupplementaryTutorial_S1_S2_S3.pdf">here</a>. The supplementaries
			cover the use cases described in the manuscript. 
		</li>
		<li>
			For a description on pvalues and other statistical information, see Gene Networks <a target="_blank" href="http://www.genenetwork.org/glossary.html#Permutation">glossary of terms and features<a>.
		</li>
		<li>
			Download a pdf with a step by step explanation on all tools <a target="_blank" href="WormQTLHD_toolTutorial.pdf">here</a>. This tutorial explains every tools functionality.
		</li>
		<li>
			For information on the the QTL finder tool, please refer to <a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/23180786">Snoek & Van der velde et al. (2012)</a>, or its original site: <a target="_blank" href="http://www.wormqtl.org">www.wormqtl.org</a>
		</li>
	</ul>

	<h3>For bioinformaticians:</h3>

	<h4>API's:</h4>
	<ul>
		<li>
			<a target="_blank" href="api/find/">Scriptable interface</a> which you can use to download annotations programmatically via the web, including the use of filters.
		</li>
		<li>
			<a target="_blank" href="api/R/">R API</a> to query via R. Copy-paste this into an R terminal to connect. The first time you may need to install Bitops and RCurl. (see instructions)
		</li>
	</ul>

	<h4>Downloaders:</h4>
	
	Click links to quick view the data in a new tab. Right click and "save as" to download the data to your desktop.
	
	<ul>
		<li>
			<a target="_blank" href="downloadmatrixascsv">Matrix CSV downloader</a> to download all experimental data programmatically. Includes options for subsets and streaming.
			<br>
			Some concrete examples:
			<ul>
				<li>
					Genotypes: <a target="_blank" href="downloadmatrixascsv?id=24&download=all&stream=false">CBxN2_WUR_RILs</a> (Panel x Marker) / <a href="downloadmatrixascsv?id=22&download=all&stream=false">CBxN2_USA_RILs</a> (Marker x Panel)
				</li>
				<li>
					Classical phenotypes: <a target="_blank" href="downloadmatrixascsv?id=31&download=all&stream=false">nil_pump_phe</a> (Measurement x Panel) / <a href="downloadmatrixascsv?id=27&download=all&stream=false">gut_phe</a> (Panel x Measurement)
				</li>
				<li>
					Expression phenotypes: <a target="_blank" href="downloadmatrixascsv?id=20&download=all&stream=false">age3_exp_norm_ril</a> (Sample x Probe) / <a href="downloadmatrixascsv?id=35&download=some&stream=false&coff=0&roff=0&clim=100&rlim=1000">rock_exp_norm</a> (Sample x Probe)
				</li>
			</ul>
		</li>
		<li>
			<a target="_blank" href="downloadmatrixasexcel">Matrix Excel downloader</a> to download all experimental data as Excel files. Similar to the CSV downloader, except no streaming options. Example: <a href="downloadmatrixasexcel?id=24&download=all">CBxN2_WUR_RILs</a>
		</li>
		<li>
			<a target="_blank" href="downloadfile">File downloader</a> to get the matrices as <a href="https://raw.github.com/joerivandervelde/molgenis_apps/master/modules/xgap/matrix/implementations/binary/etc/design.txt">xQTL binaries</a>. This goes by name, e.g. <a href="downloadfile?name=CBxN2_USA_RILs">CBxN2_USA_RILs</a>. You can get a list of all names of matrices by <a href="api/find/Data">via the scriptable interface</a>. In addition, you can also download all <a href="api/find/InvestigationFile">GFF files</a> with QTL results via this downloader.
		</li>
	</ul>

	<h4>Documentation:</h4>
	<ul>
		<li>
			 <a target="_blank" href="generated-doc/fileformat.html">Exchange format</a>: a full description of the datamodel.
		</li>
		
		<li>
			<a target="_blank" href="http://www.molgenis.org/wiki/xQTL">xQTL wiki</a> with all general documentation regarding installing and using <a target="_blank" href="http://xqtl.org/">xQTL workbench</a>.
		</li>
		
		<#-- DOWNLOAD NOT FOUND 
		<li>
			<a href="http://molgenis02.target.rug.nl/download/wormqtl/wormqtl_set1_annotations_minusUSAprobes.xls">WormQTL annotation data</a> (the largest part) in a single Excel file, as a resource and to help people understand the format.
		</li>
		-->
		
		
		
	</ul>
	
	<br>

</div>
</#macro>