<%@ page  buffer="512kb" autoFlush="false" import="org.thdl.lex.*,org.thdl.lex.component.*" errorPage="/jsp/error.jsp" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<jsp:include page="header.jsf" flush="false" />

<!--displayEntry.jsp-->
<c:set var="editMode" value="${ false }" scope="request" />
<c:if test="${ ! sessionScope.user.guest && sessionScope.helper.showEditOptions }">
	<c:set var="editMode" value="${ true }" scope="request"/>
</c:if>
<%-- 
<c:set var="showNotes" value="${ false }" scope="request" />
<c:if test="${ sessionScope.helper.showNotes }">
	<c:set var="showNotes" value="${ true }" scope="request"/>
</c:if>

<c:set var="showMeta" value="${ false }" scope="request" />
<c:if test="${ sessionScope.helper.showMeta }">
	<c:set var="showMeta" value="${ true }" scope="request"/>
</c:if>

<c:set var="showTranslations" value="${ false }" scope="request" />
<c:if test="${ sessionScope.helper.showTranslations }">
	<c:set var="showTranslations" value="${ true }" scope="request"/>
</c:if> --%>

<div id="columnRight">
	<div id="results" class="highlightBox">
	<h2>	Search Results </h2>	
		<ol>
		<c:forEach var="resultsMapItem" items="${query.results}">
					<c:set var="cls" value="" />
					<c:if test="${ resultsMapItem.key == query.entry.metaId }">
						<c:set var="cls" value="class='selected'" />
					</c:if>
			<li>
			<c:out value='<a ${cls} href="/lex/action?cmd=displayFull&comp=term&metaId=${resultsMapItem.key}">${ resultsMapItem.value}</a>' escapeXml='false' /><br />
			</li>		
		</c:forEach>
		</ol>
	</div><!--END MENU-->

</div>

<div id="columnLeft">

	<div id="toc" class="highlightBox">
	<jsp:include page="displayTreeToc.jsf"/>
	</div><!--END TOC-->

</div><!--END COLUMN LEFT-->


<div id="columnCenter">
<p id="navLinks"><jsp:include page="navLinks.jsf" /></p>

<p id="message">
<c:choose>
	<c:when test="${ ! empty message }">
		<c:out value="${ message }"/>. <br/>
	</c:when>
	<c:when test="${ empty message }">
	</c:when>
</c:choose>
</p>

<form action="/lex/action">
<p id="helper">

<span class="label">Display</span>:

<c:set var="ckd" value=""/>
<c:if test="${ sessionScope.helper.showMeta }">
<c:set var="ckd" value='checked="checked"'/>
</c:if>
<c:out value='<input name="showMeta" type="checkbox" value="true" ${ckd}/>' escapeXml='false'/>
credits | 

<c:set var="ckd" value=""/>
<c:if test="${ sessionScope.helper.showNotes }">
<c:set var="ckd" value='checked="checked"'/>
</c:if>
<c:out value='<input name="showNotes" type="checkbox" value="true" ${ckd}/>' escapeXml='false'/>
analysis | 

<c:set var="ckd" value=""/>
<c:if test="${ sessionScope.helper.showTranslations }">
<c:set var="ckd" value='checked="checked"'/>
</c:if>
<c:out value='<input name="showTranslations" type="checkbox" value="true" ${ckd}/>' escapeXml='false'/>
translations

<c:if test="${ ! sessionScope.user.guest }">
<c:set var="ckd" value=""/>
<c:if test="${ sessionScope.helper.showEditOptions }">
<c:set var="ckd" value='checked="checked"'/>
</c:if>
<c:out value='<input name="showEditOptions" type="checkbox" value="true" ${ckd}/>' escapeXml='false'/>
edit options
</c:if>

<input type="hidden" name="cmd" value="displayFull"/>
<input type="submit" value="Redisplay"/>
</p>
</form>

<div id="entry">
<c:choose>
	<c:when test="${ param.comp == 'encyclopediaArticle' && param.cmd == 'display' }">
	<jsp:include page="encyclopedia.jsf" flush="false"/>
	</c:when>
	<c:otherwise>
	<jsp:include page="displayTree.jsf" flush="false"/>
	</c:otherwise>
</c:choose>
</div><!--END ENTRY-->
</div><!--END columnMain-->

<jsp:include page="footer.jsf" flush="false" />


