<%-- <%@ page buffer="512kb" autoFlush="false" import="org.thdl.lex.*,org.thdl.lex.component.*" errorPage="/jsp/error.jsp" %>--%>
<%@ page  buffer="512kb" autoFlush="false" import="org.thdl.lex.*,org.thdl.lex.component.*" errorPage="/jsp/error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<jsp:include page="header.jsf" flush="false" />

<!--displayEntry.jsp-->
<c:set var="editMode" value="${ false }" />
<c:if test="${ ! sessionScope.user.guest }">
	<c:set var="editMode" value="${ true }" />
</c:if>

<div id="label"><p>Search Results Page</p></div><!--END label-->
<div id="message">
<p>
<c:choose>
	<c:when test="${ ! empty message }">
		<c:out value="${ message }"/>. <br/>
	</c:when>
	<c:when test="${ empty message }">
		<%-- <c:out value="Search returned zero results"/> --%>
	</c:when>
</c:choose>
</p>
</div><!--END message-->

<div id="columnSingle">
<c:if test="${ ! empty query.results }"> 
<div id="menu">
<form method="get" action="/lex/action">
<p>
	Search Results: <br />
	<input type="hidden" name="cmd" value="" />
	<input type="hidden" name="comp" value="term" />
	<c:set var="multiple" value="multiple='multiple'" />

	
	<c:forEach var="resultsMapItem" items="${query.results}">
				<c:set var="cls" value="" />
				<c:if test="${ resultsMapItem.key == query.entry.metaId }">
					<c:set var="cls" value="class='selected'" />
				</c:if>
		<c:out value='<a ${cls} href="/lex/action?cmd=displayFull&comp=term&metaId=${resultsMapItem.key}">${ resultsMapItem.value}</a>' escapeXml='false' /><br />		
	</c:forEach>


	Query took: <c:out value="${ session.query.duration }"/> seconds.
</p>
</form>
</div><!--END MENU-->
</c:if>
<c:choose>
	<c:when test="${ param.comp == 'encyclopediaArticle' && param.cmd == 'display' }">
	<jsp:include page="encyclopedia.jsf" flush="false"/>
	</c:when>
	<c:otherwise>
	<jsp:include page="displayTree.jsf" flush="false"/>
	</c:otherwise>
</c:choose>
</div><!--END columnSingle-->

<jsp:include page="footer.jsf" flush="false" />


