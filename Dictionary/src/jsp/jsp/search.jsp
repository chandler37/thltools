<%@ page buffer="512kb" autoFlush="false" import="org.thdl.lex.*,org.thdl.lex.component.*" errorPage="/jsp/error.jsp" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<jsp:include page="header.jsf" flush="false" />
<!--menu.jsp-->
<c:set var="editMode" value="${ false }" />
<c:if test="${ ! sessionScope.visit.user.guest }">
	<c:set var="editMode" value="${ true }" />
</c:if>

<div id="columnLeft">
	
	<div class="highlightBox">
	<form action="/lex/action" method="get" >
	<h2>Find a term</h2>
	<p>
	<input type="hidden" name="cmd" value="find" />
	<input type="hidden" name="comp" value="term" />
	Term: <input type="text" name="term" id="term" size="20" value=""  /> <br />
	Find:

	<select name="findMode">
		<option value="exact">Exact match</option>
		<option value="startsWith">At beginning of term</option>
		<option value="anywhere">Anywhere in term</option>
	</select>
	</p>
	
	<p>
	<input type="submit" value="Find Term"/>
	</p>
	</form>
	</div>
</div><!--END COLUMN LEFT-->
	
<div id="columnCenter">
	<jsp:include page="navLinks.jsf" flush="false"/>
	<h1>THDL Tibetan Collaborative Dictionaries: Advanced Search</h1>
	<p>
		<span class="label">Last Update</span>: <c:out value="${ applicationScope.global.lastUpdate }"/>
	</p>
	<p>
		This page is under Construction
	</p>
	<c:if test="${ ! empty message }">
	<p id="message">
		<c:out value="${ message }" />
	</p>
	</c:if>

</div><!--END COLUMN CENTER-->

<jsp:include page="footer.jsf" flush="false" />

