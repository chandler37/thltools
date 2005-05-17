<%@ page buffer="512kb" autoFlush="false" import="org.thdl.lex.*,org.thdl.lex.component.*" errorPage="/jsp/error.jsp" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<% request.setCharacterEncoding("UTF-8"); %>

<jsp:include page="header.jsf" flush="false" />
<!--menu.jsp-->
<c:set var="editMode" value="${ false }" />
<c:if test="${ ! sessionScope.visit.user.guest }">
	<c:set var="editMode" value="${ true }" />
</c:if>

<div id="columnLeft">
	
	<div class="highlightBox">
	<form id="quickSearch" action="/lex/action" method="get" >
	<h2>Dictionary Quick Search</h2>
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
	
	<c:if test="${ sessionScope.visit.user.canAddTerms }">

	<div class="highlightBox">
	<form id="newTerm" action="/lex/action" method="get" >
	<h2>Add a new term</h2>	
	<p>
	<input type="hidden" name="cmd" value="getInsertTermForm" />
	<input type="hidden" name="comp" value="term" />
	Term: <input type="text" name="term" id="term" size="20" value=""  />
	</p>
	<p class="inlineDocumentation">
	Note: if an exact match of this term already exists, this action will direct to the edit page for that term.
	</p>
	<p>
	<input type="submit" value="New Entry" /> <br />
	</p>
	</form>
	</div>
	
	</c:if>

	<c:if test="${ editMode }">
				
	<div class="highlightBox">
	<h2>Defaults &amp; Preferences</h2>
	<form id="defaults" action="/lex/action" method="get" >
	<p>
	<input type="hidden" name="cmd" value="getMetaDefaultsForm" />
	<input type="submit" value="Defaults"/> 
	</p>
	</form>
	
	<form id="preferences" action="/lex/action" method="get" >
	<p>
	<input type="hidden" name="cmd" value="getMetaPrefsForm" />
	<input type="submit" value="Preferences" /> <br />
	</p>
	</form>

	<form id="oai" action="/lex/action" method="get" >
	<p>
	<input type="hidden" name="cmd" value="refreshSources" />
	<input type="submit" value="Refresh Sources" /> <br />
	</p>
	</form>
<p><span class="warning">This dictionary requires the <a href="http://iris.lib.virginia.edu/tibet/tools/tmw.html">TibetanMachineWeb font</a>  to display Tibetan script.</span></p>

	</div>
	
	</c:if>
</div><!--END COLUMN LEFT-->
	
<div id="columnCenter">
	<jsp:include page="navLinks.jsf" flush="false"/>
	<h1>THDL Tibetan Collaborative Dictionaries: Main Menu</h1>
	<p>
		<c:set target="${ sessionScope.visit.helper }" property="date" value="${ applicationScope.global.lastUpdate }"/>
		<span class="label">Last Update</span>: <c:out value="${ sessionScope.visit.helper.formattedDate }"/>
	</p>
	<c:if test="${ ! empty message }">
	<p id="message">
		<c:out value="${ message }" />
	</p>
	</c:if>

	<h2>Recently Modified Terms</h2>

	<div id="recentTerms">

	
	<c:forEach var="term" items="${applicationScope.global.recentTerms }">
		<p class="tmw-block">
		<c:set target="${ sessionScope.visit.helper}" property="wylie" value="${ term.term }"/>
		<c:set var="tib" value="${ sessionScope.visit.helper.tibetan } " />
		<c:out value="<a class='tmw-link' href='/lex/action?cmd=displayFull&comp=term&metaId=${term.metaId}'>${ tib } </a>" escapeXml='false' /> 
		&nbsp;&nbsp;&nbsp;
		<c:out value="<a href='/lex/action?cmd=displayFull&comp=term&metaId=${term.metaId}'><span class='tmw-roman'>${ term.term }</span></a>" escapeXml='false' /> 
		&nbsp;&nbsp;
		<span class="tmw-roman">
		Created by <c:out value="${ applicationScope.flatData.users[ term.meta.createdBy ] }" /> 
		<c:set target="${ sessionScope.visit.helper }" property="date" value="${ term.meta.modifiedOn }"/>
		<c:out value="${ sessionScope.visit.helper.formattedDate }"  />
		</span>
		</p>
	</c:forEach>
	</div><!--END RECENT TERMS-->
</div><!--END COLUMN CENTER-->

<jsp:include page="footer.jsf" flush="false" />

