<!--Date:        May 15, 2009
 * Template:	PluginScreenFTLTemplateGen.ftl.ftl
 * generator:   org.molgenis.generators.screen.PluginScreenFTLTemplateGen 3.3.0-testing
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
-->
<#macro plugins_help_duplicates_DuplicatesPlugin screen>
<!-- normally you make one big form for the whole plugin-->
<form method="post" enctype="multipart/form-data" name="${screen.name}" action="">
	<!--needed in every form: to redirect the request to the right screen-->
	<input type="hidden" name="__target" value="${screen.name}">
	<!--needed in every form: to define the action. This can be set by the submit button-->
	<input type="hidden" name="__action">
	
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
		
	<h3>Help getting rid of duplicates</h3>
	[explanation]
	
	Here, you can paste lists of strings and [explanation]<br><br>
	<h2><font color="red">Please disable linkout_overlay, or the example and possibly your inputs (if ID's) will get mangled</font></h2>
	<br><br>
	
	<table>
		<tr>
			<td>
				<i>Input your list of 'name' fields here:</i><br>
				<textarea name="input" ROWS="40" COLS="40"><#if screen.input?exists>${screen.input}</#if></textarea>
				<br>
				<input type="submit" value="Load example" onclick="document.forms.${screen.name}.__action.value = 'loadExample'; document.forms.${screen.name}.submit();"/>
				<input type="submit" value="Clear" onclick="document.forms.${screen.name}.__action.value = 'clear'; document.forms.${screen.name}.submit();"/>
				<input type="submit" value="Convert" onclick="document.forms.${screen.name}.__action.value = 'convertNames'; document.forms.${screen.name}.submit();"/>
				
			</td>
			<td>
				<i>Output with renamed duplicates:</i><br>
				<textarea name="output" ROWS="40" COLS="40"><#if screen.output?exists>${screen.output}</#if></textarea>
				<#-- possible TODO ><br>Are the items in this list unique? <#if screen.unique?exists><b>${screen.unique}</b><#else>bla</#if>-->
			</td>
		</tr>
	</table>

	
<#--end of your plugin-->	
			</div>
		</div>
	</div>
</form>
</#macro>
