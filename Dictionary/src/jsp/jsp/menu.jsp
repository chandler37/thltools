<%@ page buffer="512kb" autoFlush="false" import="org.thdl.lex.*,org.thdl.lex.component.*" errorPage="/jsp/error.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<jsp:include page="header.jsf" flush="false" />
<!--menu.jsp-->
<c:set var="editMode" value="${ false }" />
<c:if test="${ ! sessionScope.user.guest }">
	<c:set var="editMode" value="${ true }" />
</c:if>
<div id="label"><p>THDL Tibetan Collaborative Dictionaries: Main Menu</p></div><!--END label-->

<div id="message">
<p>
Message: 
<c:if test="${ ! empty message }">
<c:out value="${ message }" />
</c:if>
</p>
</div><!--END message-->

<div id="columnSingle">
	<div id="menu">
	<form id="cmd-menu" action="/lex/action" method="get" >
	<p>
	<input type="hidden" name="cmd" value="" />
	<input type="hidden" name="comp" value="term" />
	Step 1: Enter a term <br />
	<input type="text" name="term" id="term" size="20" value="" onfocus="setCmd('find','menu')" /> <br />
	Step 2: Choose an action <br />
	<input type="submit" value="Find Term" onclick="setCmd('find','menu')" /> <br />
	<c:if test="${ editMode }">
	<input type="submit" value="New Entry" onclick="setCmd('getInsertTermForm', 'menu')" /> <br />
	Metadata Preferences <br />
	<input type="submit" value="Defaults" onclick="setCmd('getMetaDefaultsForm','menu')"/> <br />
	<input type="submit" value="Preferences" onclick="setCmd('getMetaPrefsForm','menu')"/> <br />
	</c:if>
	</p>
	</form>
	</div>

<%-- <p>This THDL Dictionary Server is a new online rich dictionary. </p>
<p>During development, please follow this advice:
<ul>
	<li><em>bookmark this page</em> and use the bookmark to get back after any unexpected errors.</li>
	<li><em>Don't refresh pages</em> pages with your browser's refresh commands. After database inserts, a browser refresh will cause a duplicate insert.</li>
	<li>After you request to add/edit a component look for the 'GO TO FORM' link in the navigation bar to quickly get to the form.</li>
</ul>
</p> --%>
</div><!--END columnSingle-->

<jsp:include page="footer.jsf" flush="false" />

