<#macro ExpTable screen>
<!-- normally you make one big form for the whole plugin-->
<form method="post" enctype="multipart/form-data" name="${screen.name}" action="">
	<!--needed in every form: to redirect the request to the right screen-->
	<input type="hidden" name="__target" value="${screen.name}">
	<!--needed in every form: to define the action. This can be set by the submit button-->
	<input type="hidden" name="__action">
	<!--need to be set to "true" in order to force a download-->
	<input type="hidden" name="__show">
	
	<input type="hidden" name="__shopMeName">
	<input type="hidden" name="__shopMeId">
	
<!-- this shows a title and border -->
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
		
<#if screen.myModel?exists>
	<#assign modelExists = true>
	<#assign model = screen.myModel>
<#else>
	No model. An error has occurred.
	<#assign modelExists = false>
</#if>

<br>
<table border="1" bgcolor="#EAEAEA">
	<tr>
		<td>
			<i>Phenotypes</i>
		</td>
		<td>
			<i>Type of array</i>
		</td>
		<td>
			<i>Sample size</i>
		</td>
		<td>
			<i>Parental strains</i>
		</td>
		<td>
			<i>Reference</i>
		</td>
		<td>
			<i>Pubmed link</i>
		</td>
		<td>
			<i>Growing temperature</i>
		</td>
		<td>
			<i>Stage</i>
		</td>
		<td>
			<i>Food</i>
		</td>
		<td>
			<i>Medium</i>
		</td>
		<td>
			<i>Dataset IDs</i>
		</td>
	</tr>
	<tr>
		<td>
			Gene expression
		</td>
		<td>
			Washington State University
		</td>
		<td>
			2x40 RILs
		</td>
		<td>
			CB4856; N2
		</td>
		<td>
			<b>Li et al. 2006;</b> Mapping determinants of gene expression plasticity by genetical genomics in C. elegans.
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/17196041">17196041</a>
		</td>
		<td>
			16oC and 24oC
		</td>
		<td>
			(72h at 16 and 40h at 24); L4
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=37">37</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=38">38</a>
		</td>
	</tr>
	<tr>
		<td>
			Gene expression
		</td>
		<td>
			Affymatrix tiling array
		</td>
		<td>
			60 RILs
		</td>
		<td>
			CB4856; N2
		</td>
		<td>
			<b>Li et al. 2010;</b> Global genetic robustness of the alternative splicing machinery in Caenorhabditis elegans.
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/20610403">20610403</a>
		</td>
		<td>
			24oC
		</td>
		<td>
			(40h) L4
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			n/a
		</td>
	</tr>
	<tr>
		<td>
			Gene expression
		</td>
		<td>
			Washington State University
		</td>
		<td>
			36x3 RILs
		</td>
		<td>
			CB4856; N2
		</td>
		<td>
			<b>Vinuela & Snoek et al. 2010;</b> Genome-wide gene expression regulation as a function of genotype and age in C. elegans.
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/20488933">20488933</a>
		</td>
		<td>
			24oC
		</td>
		<td>
			(40h, 96h and 214h) L4, Adult, Old
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=3">3</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=5">5</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=6">6</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=7">7</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=8">8</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=9">9</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=10">10</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=11">11</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=12">12</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=13">13</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=14">14</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=15">15</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=16">16</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=17">17</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=18">18</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=19">19</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=20">20</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=21">21</a>
		</td>
	</tr>
	<tr>
		<td>
			Gene expression
		</td>
		<td>
			Agilent 4x44k microarrays
		</td>
		<td>
			208 RIAILs
		</td>
		<td>
			CB4856; N2
		</td>
		<td>
			<b>Rockman et al. 2010;</b> Selection at linked sites shapes heritable phenotypic variation in C. elegans. 
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/20947766">20947766</a>
		</td>
		<td>
			20oC
		</td>
		<td>
			YA
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=22">22</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=34">34</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=35">35</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=36">36</a>
		</td>
	</tr>
	<tr>
		<td>
			Feeding curves RNAi exposure
		</td>
		<td>
			n/a
		</td>
		<td>
			56 RILs * 12 RNAi
		</td>
		<td>
			CB4856; N2
		</td>
		<td>
			<b>Elvin & Snoek et al. 2011;</b> A fitness assay for comparing RNAi effects across multiple C. elegans genotypes.
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/22004469">22004469</a>
		</td>
		<td>
			20oC
		</td>
		<td>
			Multi-generational
		</td>
		<td>
			n/a
		</td>
		<td>
			Liquid S-medium
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=24">24</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=32">32</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=33">33</a>
		</td>
	</tr>
	<tr>
		<td>
			Life-history traits
		</td>
		<td>
			n/a
		</td>
		<td>
			80 RILs
		</td>
		<td>
			CB4856; N2
		</td>
		<td>
			<b>Gutteling et al. 2007;</b> Mapping phenotypic plasticity and genotype-environment interactions affecting life-history traits in Caenorhabditis elegans.
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/16955112">16955112</a>
		</td>
		<td>
			12oC and 24oC
		</td>
		<td>
			Egg, L4, YA
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=25">25</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=26">26</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=27">27</a>
		</td>
	</tr>
	<tr>
		<td>
			Lifespan and pharyngeal-pumping
		</td>
		<td>
			n/a
		</td>
		<td>
			 90 NILs
		</td>
		<td>
			CB4856; N2
		</td>
		<td>
			<b>Doroszuk et al. 2009;</b> A genome-wide library of CB4856/N2 introgression lines of Caenorhabditis elegans.
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/19542186">19542186</a>
		</td>
		<td>
			20oC
		</td>
		<td>
			All; synchronised
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=4">4</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=23">23</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=28">28</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=29">29</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=30">30</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=31">31</a>
		</td>
	</tr>
	<tr>
		<td>
			Lifespan, Recovery and reproduction after heat-shock
		</td>
		<td>
			n/a
		</td>
		<td>
			58 RILs
		</td>
		<td>
			CB4856; N2
		</td>
		<td>
			<b>Rodriguez et al. 2012;</b> Genetic variation for stress-response hormesis in C. elegans lifespan.
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/22613270">22613270</a>
		</td>
		<td>
			20oC and 35oC heat-shock
		</td>
		<td>
			L4 and Adult
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=39">39</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=40">40</a>
		</td>
	</tr>
	<tr>
		<td>
			Gene expression
		</td>
		<td>
			Washington State University
		</td>
		<td>
			CB4856 and N2
		</td>
		<td>
			CB4856; N2
		</td>
		<td>
			<b>Vinuela & Snoek et al. 2012;</b> Aging Uncouples Heritability and Expression-QTL in Caenorhabditis elegans.
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/22670229">22670229</a>
		</td>
		<td>
			24oC
		</td>
		<td>
			(40h, 96h and 214h) L4, Adult, Old
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=41">41</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=42">42</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=43">43</a>
		</td>
	</tr>
	<tr>
 		<td>
            Dauer formation
        </td>
        <td>
            n/a
        </td>
        <td>
            90 NILs and 20 Wild-Isolates
        </td>
        <td>
            CB4856; N2
        </td>
        <td>
            <b>Green et al. 2013;</b> Genetic mapping of variation in dauer larvae development in growing populations of </i> Caenorhabditis elegans</i>.
        </td>
        <td>
            <a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/23715016">23715016</a>
        </td>
        <td>
            20oC
        </td>
        <td>
            Multi-generational
        </td>
        <td>
            OP50
        </td>
        <td>
            Dauer agar Plate
        </td>
        <td>
            <a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=44">44</a>,
            <a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=45">45</a>,
            <a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=46">46</a>,
 			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=47">47</a>,
            <a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=48">48</a>,
            <a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=49">49</a>
        </td>
    </tr>
    <tr>
	    <td>
	       NEMADAPT
	    </td>
	    <td>
	        4 x 44K Agilent AGIWUR
	    </td>
	    <td>
	        48 Wild-Isolates
	    </td>
	    <td>
	       JU1511; JU1516; JU1522; JU1523; JU1545; JU1581; JU1582; JU1807; JU1937; JU1938; JU1939; JU1940; JU1941; JU1942; JU1943; JU1944; JU1945; JU1946; JU1947; JU1948; JU1949; JU1918; JU1919; JU1793; JU1920; JU1921; JU1922; JU1923; JU1924; JU1925; JU1926; JU1927; JU1928; JU1929; JU1930; JU1931; JU1932; JU1933; JU1934; JU1935; JU1936; JU314; JU396; WN2001; WN2002; WN2003
	    </td>
	    <td>
	        <b>Volkers & Snoek et al. 2013;</b> Gene-environment and protein degradation signatures characterize genomic and phenotypic diversity in wild Caenorhabditis elegans populations.
	    </td>
	    <td>
	        <a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/23957880">23957880</a>
	    </td>
	    <td>
	        20oC
	    </td>
	    <td>
	        L4
	    </td>
	    <td>
	        OP50
	    </td>
	    <td>
	        NGM Plate
	    </td>
	    <td>
	        <a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=50">50</a>,
	        <a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=51">51</a>,
	        <a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=52">52</a>,
	        <a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=53">53</a>
	    </td>
	</tr>
	<tr>
		<td>
			Gene expression
		</td>
		<td>
			4 x 44K Agilent AGIWUR
		</td>
		<td>
			N2, CB4856 and 6 Wild-isolates
		</td>
		<td>
			N2; CB4856; JU1581; JU1921; JU1930; JU1932; JU1944; and JU1949
		</td>
		<td>
			<b> Snoek et al. 2014;</b> A rapid and massive gene expression shift marking adolescent transition in C. elegans.
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/24468752">24468752</a>
		</td>
		<td>
			20oC
		</td>
		<td>
			(48h) L4
		</td>
		<td>
			OP50 and E. rhapontici
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=54">54</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=55">55</a>
		</td>
	</tr>
	<tr>
		<td>
			Gene expression, Speed of development.
		</td>
		<td>
			4 x 44K Agilent AGIWUR
		</td>
		<td>
			N2, Bar-1(ga80) Mutant
		</td>
		<td>
			N2; EW15
		</td>
		<td>
			<b>vd Bent & Sterken & Volkers et al. 2014;</b> Loss-of-function of	beta-catenin bar-1 slows development and activates the	Wnt pathway in Caenorhabditis elegans.
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/24819947">24819947</a>
		</td>
		<td>
			20oC
		</td>
		<td>
			L4
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=56">56</a>
		</td>
	</tr>
	<tr>
		<td>
			Egg/embryo stage, lifespan, fecundity, size and bagging
		</td>
		<td>
			n/a
		</td>
		<td>
			101 RILs, 87 Nils and 12 Wild-Isolates
		</td>
		<td>
			CB4856; N2; JU393; JU1401; JU1411; MY2; JU345; PX174; MY1; JU1494; CB4853; JU262
		</td>
		<td>
			<b>Snoek & Orbidans <i>et al</i>. 2014; </b>Widespread genomic incompatibilities in <i>Caenorhobditis elegans</i>.
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/25128438">25128438</a>
		</td>
		<td>
			20oC
		</td>
		<td>
			L4
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=57">57</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=58">58</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=59">59</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=60">60</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=61">61</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=62">62</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=63">63</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=64">64</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=65">65</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=66">66</a>
		</td>
	</tr>
	
	
	<tr>
		<td>
			Gene expression
		</td>
		<td>
			Agilent 4x44k microarrays
		</td>
		<td>
			CB4856 and N2
		</td>
		<td>
			CB4856; N2
		</td>
		<td>
			<b>Kamkina & Snoek et al. 2016;</b> NATURAL GENETIC VARIATION ON PROTEOME AND TRANSCRIPTOME LEVEL IN C. ELEGANS 
		</td>
		<td>
			Submitted
		</td>
		<td>
			20oC
		</td>
		<td>
			L4
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=67">67</a>
		</td>
	</tr> 
	
	<tr>
		<td>
			Vulval Induction
		</td>
		<td>
			n/a
		</td>
		<td>
			178 Let-60 miRILs and N2
 		</td>
		<td>
			CB4856; MT2124(let-60(n1046)) 
		</td>
		<td>
			<b>Schmid et al. 2015;</b> Systemic regulation of RAS/MAPK signaling by the Serotonin metabolite 5-HIAA 
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/25978500">25978500</a>
		</td>
		<td>
			20oC
		</td>
		<td>
			L4
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=68">68</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=69">69</a>
		</td>
	</tr> 
	<tr>
		<td>
			Protein abundance & apoptosis
		</td>
		<td>
			NA
		</td>
		<td>
			48 RILs
		</td>
		<td>
			CB4856; N2
		</td>
		<td>
			<b>Singh et al.</b> Natural genetic variation influences protein abundances in C. elegans developmental signalling pathways
		</td>
		<td>
			<a> Submitted</a>
		</td>
		<td>
			20oC
		</td>
		<td>
			(20oC); L4
		</td>
		<td>
			OP50
		</td>
		<td>
			NGM Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=70">70</a>, 
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=71">71</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=72">72</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=73">73</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=74">74</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=75">75</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=80">80</a>
		</td>
	</tr> 
	<tr>
		<td>
			Lifespan & bagging
		</td>
		<td>
			NA
		</td>
		<td>
			78 RILs, 5 ILs, 24 Wild isolates
		</td>
		<td>
			CB4856; N2; CB4853; JU1410; JU1494; JU1511; JU1516; JU1581; JU1926; JU1927; JU1931; JU1932; JU1933; JU1936; JU1937; JU1938; JU1941; JU1946; JU1949; JU345; JU362; JU393; PX179; WN2002; WN2003
		</td>
		<td>
			<b>Stastna et al. 2015</b> Genotype-dependent effects of dietary restriction on lifespan in Caenorhabditis elegans
		</td>
		<td>
			<a target="_blank" href="http://www.ncbi.nlm.nih.gov/pubmed/26539794">26539794</a>
		</td>
		<td>
			20oC
		</td>
		<td>
			(20oC); L4
		</td>
		<td>
			OP50
		</td>	
		<td>	
			NGM Plate, DR Plate
		</td>
		<td>
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=76">76</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=77">77</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=78">78</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=79">79</a>
		</td>
	</tr>
	<tr>
		<td>
			Lawn Leaving (OP50, 3 BT strains)
		</td>
		<td>
			NA
		</td>
		<td>
			4x200 RILs + 4x90 ILs
		</td>
		<td>
			CB4856; N2
		</td>
		<td>
			<b>Nakad et al. 2016</b> Contrasting invertebrate immune defence behaviours caused by a single gene, the Caenorhabditis elegans neuropeptide receptor gene npr-1
		</td>
		<td>
			Submitted
		</td>
		<td>
			20oC
		</td>
		<td>
			(14h and 24h treatment); started at L4
		</td>
		<td>
			OP50 and 3 B. thuringiensis strains
		</td>
		<td>
			peptone free NGM Plate
		</td>
		<td>
		<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=81">81</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=82">82</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=83">83</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=84">84</a>,
			<a href="?select=Datas&__target=Datas&__action=filter_set&__filter_attribute=Data_id&__filter_operator=EQUALS&__filter_value=85">85</a>
		</td>
	
	</tr>
	
	
	
</table>
<br>

	</div>
</form>
</#macro>
