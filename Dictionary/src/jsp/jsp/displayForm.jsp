<%@ page  buffer="512kb" autoFlush="false" import="org.thdl.lex.*,org.thdl.lex.component.*" errorPage="/jsp/error.jsp" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<% request.setCharacterEncoding("UTF-8"); %>

<jsp:include page="header.jsf" flush="false" />

<!--displayEntry.jsp-->

<div id="label">Component Entry Form Page
</div><!--END label-->

<div id="message">
<c:if test="${ ! empty message }">
	<span><c:out value="${ message }"/></span>
</c:if>
</div><!--END message-->

<div id="columnSingle">
<c:if test="${ ! empty termEntry }">
<div id="backButton">
Back to:
<c:out value='<a href="/lex/action?cmd=displayFull&amp;comp=term&id=${ termEntry.term.id }">${ termEntry.term.term }</a>' escapeXml="false" />
</div>
</c:if>

<c:if test="${ param.cmd != 'getInsertForm' 
					&& param.cmd != 'getUpdateForm' 
					&& param.cmd != 'getTranslationForm'
					&& param.cmd != 'getAnnotationForm'}">
	<c:set var="editMode" value="${ true }" />
</c:if>
<c:if test="${ displayMode == 'addEditForm' }">
	<c:set var="updateForm" value="${ true }" />
</c:if>
<c:if test="${ displayMode == 'addNewComponent' && param.cmd != 'getAnnotationForm' }">
	<c:set var="newForm" value="${ true }" />
</c:if>

<c:choose>

<c:when test="${ param.cmd == 'getAnnotationForm' }">
	<jsp:include page="analyticalNoteForm.jsf" />
</c:when>

<c:when test="${  param.comp == 'term' }">
	<p><jsp:include page="termForm.jsf" /></p>
</c:when>

<c:when test="${ param.comp == 'pronunciation'}">
	<jsp:include page="pronunciationForm.jsf" />
</c:when>

<c:when test="${ param.comp == 'etymology'}">
	<jsp:include page="etymologyForm.jsf" />
</c:when>

<c:when test="${ param.comp == 'spelling'}">
	<jsp:include page="spellingForm.jsf" />
</c:when>

 <c:when test="${ param.comp == 'grammaticalFunction'}">
	<jsp:include page="functionForm.jsf" />
</c:when>

<c:when test="${ param.comp == 'encyclopediaArticle'}">
	<jsp:include page="encyclopediaArticleForm.jsf" />
</c:when>

<c:when test="${ param.comp == 'definition'}">
	<jsp:include page="definitionForm.jsf" />
</c:when>

<c:when test="${ param.comp == 'subdefinition'}">
	<jsp:include page="subdefinitionForm.jsf" />
</c:when>

<c:when test="${ param.comp == 'keyword' }">
	<jsp:include page="keywordForm.jsf" />
</c:when>

<c:when test="${ param.comp == 'modelSentence' }">
	<jsp:include page="modelSentenceForm.jsf" />
</c:when>

<c:when test="${ param.comp == 'translationEquivalent' }">
	<jsp:include page="translationEquivalentForm.jsf" />
</c:when>

<c:when test="${ param.comp == 'relatedTerm' }">
	<jsp:include page="relatedTermForm.jsf" />
</c:when>

<c:when test="${ param.comp == 'passage' }">
	<jsp:include page="passageForm.jsf" />
</c:when>

<c:when test="${ param.comp == 'speechRegister' }">
	<jsp:include page="registerForm.jsf" />
</c:when>

<c:when test="${ param.comp == 'transitionalData' }">
	<jsp:include page="transitionalDataForm.jsf" />
</c:when>

	<c:otherwise>
	Error: no form was included
	</c:otherwise>
</c:choose>

</div><!--END columnSingle-->

<jsp:include page="footer.jsf" flush="false" />


