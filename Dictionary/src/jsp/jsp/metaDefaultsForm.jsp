<%@ page  buffer="512kb" autoFlush="false" import="org.thdl.lex.*,org.thdl.lex.component.*" errorPage="/jsp/error.jsp" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<jsp:include page="header.jsf" flush="false" />
<!--metaDefaultsForm.jsp-->

<div id="label"><p>Lex the Dictionary Server: Manage Input Session Form</p></div><!--END label-->

<div id="message">
<p>
Message: 
<c:if test="${ ! empty message }">
<c:out value="${ message }" />
</c:if>
</p>
</div><!--END message-->

<div id="columnSingle">
<form id="preferencesForm" action="/lex/action" method="post">

<c:set var="language" value="${ sessionScope.visit.preferences.language }" />
<c:set var="dialect" value="${ sessionScope.visit.preferences.dialect }" />
<c:set var="source" value="${ sessionScope.visit.preferences.source }" />
<c:set var="projectSubject" value="${ sessionScope.visit.preferences.projectSubject }" />
<c:set var="script" value="${ sessionScope.visit.preferences.script }" />
<c:set var="note" value="${ sessionScope.visit.preferences.note }" />

<%-- <c:out value="${ sessionScope.visit.preferences.language }" /> <br />
<c:out value="${ sessionScope.visit.preferences.dialect }" /> <br />
<c:out value="${ sessionScope.visit.preferences.source }" /> <br />
<c:out value="${ sessionScope.visit.preferences.projectSubject }" /> <br />
<c:out value="${ sessionScope.visit.preferences.script }" /> --%>

<p>
In the metadata categories below, indicate the option you would like to appear by default.
Then check the "Use Default" box next to metadata fields you want to be entered automatically. 
When this box is checked, the dropdown for that field will not appear 
(this only applies to forms for new components, not edit forms).
</p>

<p>
<c:set var="checked" value="" />
<c:if test="${ sessionScope.visit.preferences.useDefaultLanguage }">
<c:set var="checked" value="checked='checked'" />
</c:if>
<c:out value='<input type="checkbox" name="useDefaultLanguage" value="true" ${checked}/>' escapeXml="false" />
Use Default Language: <br />
<select name="language">
	<c:set var="sel" value="" />
	<c:if test="${ 0 == language }">
		<c:set var="sel" value='selected="selected"' />
	</c:if>
	<c:out value="<option value='0' ${sel}>none</option>" escapeXml="false" /> 
		<c:forEach var="prefLangs" items="${ sessionScope.visit.preferences.languageSet }">
		<c:set var="sel" value="" />		
		<c:if test="${ language == prefLangs }">
			<c:set var="sel" value='selected="selected"' />
		</c:if>
		<c:out value='<option value="${ prefLangs }" ${ sel }>${ applicationScope.flatData.languages[ prefLangs ] }</option>' escapeXml="false" />
	</c:forEach>
	<option disabled="disabled" value="">----------------</option>
	<c:forEach var="langs" items="${ applicationScope.flatData.languages }">
		<c:set var="printOption" value="${ true }" />
		<c:forEach var="prefLangs" items="${ sessionScope.visit.preferences.languageSet }">
			<c:if test="${ prefLangs == langs.key }">
				<c:set var="printOption" value="${ false }" />
			</c:if>
		</c:forEach>
		<c:if test="${ printOption }">
			<c:set var="sel" value="" />		
			<c:if test="${ langs.key == language }">
				<c:set var="sel" value='selected="selected"' />
			</c:if>
			<c:out value='<option value="${ langs.key }" ${sel}>${ langs.value }</option>' escapeXml="false" />
		</c:if>
	</c:forEach>
</select>
</p>

<p>
<c:set var="checked" value="" />
<c:if test="${ sessionScope.visit.preferences.useDefaultScript }">
<c:set var="checked" value="checked='checked'" />
</c:if>
<c:out value='<input type="checkbox" name="useDefaultScript" value="true" ${checked}/>' escapeXml="false" />
Use Default Script: <br />
<select name="script">
	<c:set var="sel" value="" />
	<c:if test="${ 0 == script }">
		<c:set var="sel" value='selected="selected"' />
	</c:if>
	<c:out value="<option value='0' ${sel}>none</option>" escapeXml="false" /> 
<c:forEach var="prefScripts" items="${ sessionScope.visit.preferences.scriptSet }">
		<c:set var="sel" value="" />		
		<c:if test="${ script == prefScripts }">
			<c:set var="sel" value='selected="selected"' />
		</c:if>
<c:out value='<option value="${ prefScripts }" ${ sel }>${ applicationScope.flatData.scripts[ prefScripts ] }</option>' escapeXml="false" />
</c:forEach>
<option disabled="disabled" value="">----------------</option>
<c:forEach var="scripts" items="${ applicationScope.flatData.scripts }">
	<c:set var="printOption" value="${ true }" />
	<c:forEach var="prefScripts" items="${ sessionScope.visit.preferences.scriptSet }">
		<c:if test="${ prefScripts == scripts.key }">
			<c:set var="printOption" value="${ false }" />
		</c:if>
	</c:forEach>
<c:if test="${ printOption }">
			<c:set var="sel" value="" />		
			<c:if test="${ scripts.key == script }">
				<c:set var="sel" value='selected="selected"' />
			</c:if>
<c:out value='<option value="${ scripts.key }"  ${ sel }>${ scripts.value }</option>' escapeXml="false" />
</c:if>
</c:forEach>
</select>

</p>

<p>
<c:set var="checked" value="" />
<c:if test="${ sessionScope.visit.preferences.useDefaultDialect }">
<c:set var="checked" value="checked='checked'" />
</c:if>
<c:out value='<input type="checkbox" name="useDefaultDialect" value="true" ${checked}/>' escapeXml="false" />
Use Default Dialect: <br />
<select name="dialect">
	<c:set var="sel" value="" />
	<c:if test="${ 0 == dialect }">
		<c:set var="sel" value='selected="selected"' />
	</c:if>
	<c:out value="<option value='0' ${sel}>none</option>" escapeXml="false" /> 
<c:forEach var="prefDials" items="${ sessionScope.visit.preferences.dialectSet }">
		<c:set var="sel" value="" />		
		<c:if test="${ dialect == prefDials }">
			<c:set var="sel" value='selected="selected"' />
		</c:if>
<c:out value='<option value="${ prefDials }" ${ sel }>${ applicationScope.flatData.majorDialectFamilies[ prefDials ] }</option>' escapeXml="false" />
</c:forEach>
	<option disabled="disabled" value="">----------------</option>
<c:forEach var="dials" items="${ applicationScope.flatData.majorDialectFamilies }">
	<c:set var="printOption" value="${ true }" />
	<c:forEach var="prefDials" items="${ sessionScope.visit.preferences.dialectSet }">
		<c:if test="${ prefDials == dials.key }">
			<c:set var="printOption" value="${ false }" />
		</c:if>
	</c:forEach>
<c:if test="${ printOption }">
			<c:set var="sel" value="" />		
			<c:if test="${ dials.key == dialect }">
				<c:set var="sel" value='selected="selected"' />
			</c:if>
<c:out value='<option value="${ dials.key }"  ${ sel }>${ dials.value }</option>' escapeXml="false" />
</c:if>
</c:forEach>
</select>
</p>

<p>
<c:set var="checked" value="" />
<c:if test="${ sessionScope.visit.preferences.useDefaultSource }">
<c:set var="checked" value="checked='checked'" />
</c:if>
<c:out value='<input type="checkbox" name="useDefaultSource" value="true" ${checked}/>' escapeXml="false" />
Use Default Source: <br />
<select name="source">
	<c:set var="sel" value="" />
	<c:if test="${ 0 == source }">
		<c:set var="sel" value='selected="selected"' />
	</c:if>
	<c:out value="<option value='0' ${sel}>none</option>" escapeXml="false" /> 
<c:forEach var="prefSources" items="${ sessionScope.visit.preferences.sourceSet }">
		<c:set var="sel" value="" />		
		<c:if test="${ source == prefSources }">
			<c:set var="sel" value='selected="selected"' />
		</c:if>
<c:out value='<option value="${ prefSources }" ${ sel }>${ applicationScope.flatData.sources[ prefSources ] }</option>' escapeXml="false" />
</c:forEach>
<option disabled="disabled" value="">----------------</option>
<c:forEach var="srcs" items="${ applicationScope.flatData.sources }">
	<c:set var="printOption" value="${ true }" />
	<c:forEach var="prefSources" items="${ sessionScope.visit.preferences.sourceSet }">
		<c:if test="${ prefSources == srcs.key }">
			<c:set var="printOption" value="${ false }" />
		</c:if>
	</c:forEach>
<c:if test="${ printOption }">
			<c:set var="sel" value="" />		
			<c:if test="${ srcs.key == source }">
				<c:set var="sel" value='selected="selected"' />
			</c:if>
<c:out value='<option value="${ srcs.key }"  ${ sel }>${ srcs.value }</option>' escapeXml="false" />
</c:if>
</c:forEach>
</select>
</p>

<p>
<c:set var="checked" value="" />
<c:if test="${ sessionScope.visit.preferences.useDefaultProjSub }">
<c:set var="checked" value="checked='checked'" />
</c:if>
<c:out value='<input type="checkbox" name="useDefaultProjSub" value="true" ${checked}/>' escapeXml="false" />
Use Default Project/Subject: <br />
<select name="projectSubject">
	<c:set var="sel" value="" />
	<c:if test="${ 0 == projectSubject }">
		<c:set var="sel" value='selected="selected"' />
	</c:if>
	<c:out value="<option value='0' ${sel}>none</option>" escapeXml="false" /> 
<c:forEach var="prefProjSub" items="${ sessionScope.visit.preferences.projectSubjectSet }">
		<c:set var="sel" value="" />		
		<c:if test="${ projectSubject == prefProjSub }">
			<c:set var="sel" value='selected="selected"' />
		</c:if>
<c:out value='<option value="${ prefProjSub }" ${ sel }>${ applicationScope.flatData.projectSubjects[ prefProjSub ] }</option>' escapeXml="false" />
</c:forEach>
<option disabled="disabled" value="">----------------</option>
<c:forEach var="projSubs" items="${ applicationScope.flatData.projectSubjects }">
	<c:set var="printOption" value="${ true }" />
	<c:forEach var="prefProjSub" items="${ sessionScope.visit.preferences.projectSubjectSet }">
		<c:if test="${ prefProjSub == projSubs.key }">
			<c:set var="printOption" value="${ false }" />
		</c:if>
	</c:forEach>
<c:if test="${ printOption }">
	<c:set var="sel" value="" />		
	<c:out value="<!--${ projSubs.key } ${ projectSubject }-->" escapeXml="false" />
	<c:if test="${ projSubs.key == projectSubject }">
		<c:set var="sel" value='selected="selected"' />
	</c:if>
<c:out value='<option value="${ projSubs.key }"  ${ sel }>${ projSubs.value }</option>' escapeXml="false" />
</c:if>
</c:forEach>
</select>
</p>

<p>

<c:set var="checked" value="" />
<c:if test="${ sessionScope.visit.preferences.useDefaultNote }">
<c:set var="checked" value="checked='checked'" />
</c:if>
<c:out value='<input type="checkbox" name="useDefaultNote" value="true" ${checked}/>' escapeXml="false" />
Use Default Metadata Note: <br />
<textarea name="note" rows="8" cols="90"><c:out value='${ note }' escapeXml='false' /></textarea> <br />


<input type="hidden" name="cmd" value="setMetaDefaults" />
<input type="submit" value="Submit Changes to the input session" />
</p>
</form>

</div><!--END columnSingle-->

<jsp:include page="footer.jsf" flush="false" />

