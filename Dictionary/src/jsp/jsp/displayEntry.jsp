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
<%-- 	<c:set var="jsFunction" value="MM_jumpMenu('parent',this,0)" />
	<c:out value='<select name="id" ${multiple} onchange="${jsFunction}" onclick="${jsFunction}">' escapeXml='false' /> 
		<c:forEach var="resultsMapItem" items="${query.results}">
			<c:set var="sel" value="" />
				<c:out value="<!--resultsMapItem='${resultsMapItem.key}' component='${component.metaId }'-->" escapeXml="false" />
				<c:if test="${ resultsMapItem.key == termEntry.metaId }">
					<c:set var="sel" value="selected='selected'" />
				</c:if>
				<c:out value='<option value="${resultsMapItem.key}" ${sel}>${ resultsMapItem.value}</option>' escapeXml="false" />
		</c:forEach>
	</select>--%>
	
	<c:forEach var="resultsMapItem" items="${query.results}">
				<c:set var="cls" value="" />
				<c:if test="${ resultsMapItem.key == query.entry.metaId }">
					<c:set var="cls" value="class='selected'" />
				</c:if>
		<c:out value='<a ${cls} href="/lex/action?cmd=displayFull&comp=term&metaId=${resultsMapItem.key}">${ resultsMapItem.value}</a>' escapeXml='false' /><br />		
	</c:forEach>

	<%-- Click term to display entry<br /> 
	<c:if test="${ param.mode == 'newTerm'}">
		<c:out value='<input type="hidden" id="term" name="term" value="${ term }" />' escapeXml="false" />
		<input type="submit" value="New Entry" onclick="setCmd('getInsertForm',0)" /><br />
	</c:if>	
	<c:if test="${ editMode }">
		<input type="submit" value="Remove Term" onclick="setCmd('remove',0)" /><br />
	</c:if>--%>
	Query took: <c:out value="${ session.query.duration/1000 }"/> seconds.
</p>
</form>
</div><!--END MENU-->
</c:if>
<%-- <c:if test="${ ! empty results }">  --%>
<c:choose>
	<c:when test="${ param.comp == 'encyclopediaArticle' && param.cmd == 'display' }">
	<jsp:include page="encyclopedia.jsf" flush="false"/>
	</c:when>
	<c:when test="${ param.cmd == 'testing' }">
	<jsp:include page="testing.jsf" flush="false"/>
	</c:when>
	<c:otherwise>
	<jsp:include page="displayTree.jsf" flush="false"/>
	</c:otherwise>
</c:choose>
<%-- </c:if> --%>
</div><!--END columnSingle-->

<jsp:include page="footer.jsf" flush="false" />


