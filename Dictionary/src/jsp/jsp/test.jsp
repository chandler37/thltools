<%@ page buffer="512kb" autoFlush="false" import="org.thdl.lex.*,org.thdl.lex.component.*" errorPage="/jsp/error.jsp" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jstl/xml" %> 
<% request.setCharacterEncoding("UTF-8"); %>

<jsp:include page="header.jsf" flush="false" />
<!--menu.jsp-->
<c:set var="editMode" value="${ false }" />
<c:if test="${ ! sessionScope.visit.user.guest }">
	<c:set var="editMode" value="${ true }" />
</c:if>

<div id="columnLeft">

	<form id="oai" action="/lex/action" method="get" >
	<p>
	<input type="hidden" name="cmd" value="refreshSources" />
	<input type="submit" value="Refresh Sources" /> <br />
	</p>
	</form>

</div><!--END COLUMN LEFT-->
	
<div id="columnCenter">
	<jsp:include page="navLinks.jsf" flush="false"/>
	<h1>THDL Tibetan Collaborative Dictionaries: Test Page</h1>
	<p><c:out value="${ applicationScope.sources }"/></p>
	<ol>
	<c:forEach var="testString" items="${requestScope.testArray}">
		<x:parse var="title" xml="${ testString }"/>
		<li><x:out select="$title/xml-fragment"/></li>
		
	</c:forEach>
	</ol>
	
</div><!--END COLUMN CENTER-->

<jsp:include page="footer.jsf" flush="false" />

