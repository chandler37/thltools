<%@ page buffer="512kb" autoFlush="false" import="org.thdl.lex.*,org.thdl.lex.component.*" errorPage="/jsp/error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<jsp:include page="header.jsf" flush="false" />
<!--metaPrefsForm.jsp-->

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
<form action="/lex/action" method="post">
<p>
In the metadata categories below, please choose sets of options that you use frequently. 
Use the <em>ctrl-key</em> (<em>command-key</em> on Mac) to select multiple options. 
Your choices from this page will appear at the top of every dropdown menu 
in the metadata section of every form in this application. 
</p>

<p>
Languages: <br />
<select name="languages" multiple="multiple" size="10">
<optgroup label="Preferred Languages">
<c:forEach var="prefLangs" items="${ sessionScope.visit.preferences.languageSet }">
<c:out value='<option value="${ prefLangs }" selected="selected">${ applicationScope.flatData.languages[ prefLangs ] }</option>' escapeXml="false" />
</c:forEach>
</optgroup>
<optgroup label="All Languages">
<c:forEach var="langs" items="${ applicationScope.flatData.languages }">
	<c:set var="disabled" value="" />
	<c:forEach var="prefLangs" items="${ sessionScope.visit.preferences.languageSet }">
		<c:if test="${ prefLangs == langs.key }">
			<c:set var="disabled" value='disabled="disabled"' />
		</c:if>
	</c:forEach>
	<c:out value='<option value="${ langs.key }" ${disabled}>${ langs.value }</option>' escapeXml="false" />
</c:forEach>
</optgroup>
</select>
</p>
<p>
Dialects: <br />
<select name="dialects" multiple="multiple" size="10">
<optgroup label="Preferred Dialects">
<c:forEach var="prefDials" items="${ sessionScope.visit.preferences.dialectSet }">
<c:out value='<option value="${ prefDials }" selected="selected">${ applicationScope.flatData.majorDialectFamilies[ prefDials ] }</option>' escapeXml="false" />
</c:forEach>
</optgroup>
<optgroup label="All Dialects">
<c:forEach var="dials" items="${ applicationScope.flatData.majorDialectFamilies }">
	<c:set var="disabled" value="" />
	<c:forEach var="prefDials" items="${ sessionScope.visit.preferences.dialectSet }">
		<c:if test="${ prefDials == dials.key }">
			<c:set var="disabled" value='disabled="disabled"' />
		</c:if>
	</c:forEach>
<c:out value='<option value="${ dials.key }" ${disabled}>${ dials.value }</option>' escapeXml="false" />
</c:forEach>
</optgroup>
</select>
</p>
<p>
Sources: <br />
<select name="sources" multiple="multiple" size="10">
<optgroup label="Preferred Sources">
<c:forEach var="prefSources" items="${ sessionScope.visit.preferences.sourceSet }">
<c:out value='<option value="${ prefSources }" selected="selected">${ applicationScope.flatData.sources[ prefSources ] }</option>' escapeXml="false" />
</c:forEach>
</optgroup>
<optgroup label="All Sources">
<c:forEach var="srcs" items="${ applicationScope.flatData.sources }">
	<c:set var="disabled" value="" />
	<c:forEach var="prefSources" items="${ sessionScope.visit.preferences.sourceSet }">
		<c:if test="${ prefSources == srcs.key }">
			<c:set var="disabled" value='disabled="disabled"' />
		</c:if>
	</c:forEach>
<c:out value='<option value="${ srcs.key }" ${disabled}>${ srcs.value }</option>' escapeXml="false" />
</c:forEach>
</optgroup>
</select>
</p>
<p>

Project/Subject: <br />
<select name="projectSubjects" multiple="multiple" size="10">
<optgroup label="Preferred Project/Subjects">
<c:forEach var="prefProjSub" items="${ sessionScope.visit.preferences.projectSubjectSet }">
<c:out value='<option value="${ prefProjSub }" selected="selected">${ applicationScope.flatData.projectSubjects[ prefProjSub ] }</option>' escapeXml="false" />
</c:forEach>
</optgroup>
<optgroup label="All Project/Subjects">
<c:forEach var="projSubs" items="${ applicationScope.flatData.projectSubjects }">
	<c:set var="disabled" value="" />
	<c:forEach var="prefProjSub" items="${ sessionScope.visit.preferences.projectSubjectSet }">
		<c:if test="${ prefProjSub == projSubs.key }">
			<c:set var="disabled" value='disabled="disabled"' />
		</c:if>
	</c:forEach>
<c:out value='<option value="${ projSubs.key }" ${disabled}>${ projSubs.value }</option>' escapeXml="false" />
</c:forEach>
</optgroup>
</select>
</p>
<p>
Scripts: <br />
<select name="scripts" multiple="multiple" size="10">
<optgroup label="Preferred Scripts">
<c:forEach var="prefScripts" items="${ sessionScope.visit.preferences.scriptSet }">
<c:out value='<option value="${ prefScripts }" selected="selected">${ applicationScope.flatData.scripts[ prefScripts ] }</option>' escapeXml="false" />
</c:forEach>
</optgroup>
<optgroup label="All Scripts">
<c:forEach var="scripts" items="${ applicationScope.flatData.scripts }">
	<c:set var="disabled" value="" />
	<c:forEach var="prefScripts" items="${ sessionScope.visit.preferences.scriptSet }">
		<c:if test="${ prefScripts == scripts.key }">
			<c:set var="disabled" value='disabled="disabled"' />
		</c:if>
	</c:forEach>
<c:out value='<option value="${ scripts.key }" ${disabled}>${ scripts.value }</option>' escapeXml="false" />
</c:forEach>
</optgroup>
</select>

</p>
<p>
<input type="hidden" name="cmd" value="setMetaPrefs" />
<input type="submit" value="Submit Changes to the input session" />
</p>
</form>

</div><!--END columnSingle-->

<jsp:include page="footer.jsf" flush="false" />

