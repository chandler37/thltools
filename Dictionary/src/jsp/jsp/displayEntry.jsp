<%@ page  buffer="512kb" autoFlush="false" import="org.thdl.lex.*,org.thdl.lex.component.*" errorPage="/jsp/error.jsp" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<% request.setCharacterEncoding("UTF-8"); %>

<!--Begin Render-->
<jsp:include page="header.jsf" flush="false"/>


<!--displayEntry.jsp-->
<c:set var="editMode" value="${ false }" scope="request"/>
<c:if test="${ ! sessionScope.visit.user.guest && sessionScope.visit.helper.showEditOptions }">
	<c:set var="editMode" value="${ true }" scope="request"/>
</c:if>

<div id="columnRight">
	<div class="highlightBox">
		<form action="/lex/action" method="get">
			<h2>Quick Search</h2>
			<p>
				<input type="hidden" name="cmd" value="find"/>
				<input type="hidden" name="comp" value="term"/>
				Term: 
				<input type="text" name="term" id="term" size="20" value=""/>
				<br/>
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
	
	<div id="results" class="highlightBox">
		<h2>	Search Results </h2>
		<ol>
			<c:forEach var="resultsMapItem" items="${sessionScope.visit.query.results}">
				<c:set var="cls" value=""/>
				<c:if test="${ resultsMapItem.key == sessionScope.visit.query.entry.metaId }">
					<c:set var="cls" value="class='selected'"/>
				</c:if>
				<li>
					<c:out value="<a ${cls} href='/lex/action?cmd=displayFull&amp;comp=term&amp;metaId=${resultsMapItem.key}'>${ resultsMapItem.value}</a>" escapeXml="false"/>
					<br/>
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
	
	<jsp:include page="navLinks.jsf"/>
	
	<p id="message">
		<c:choose>
			<c:when test="${ ! empty message }">
				<c:out value="${ message }"/>. <br/>
			</c:when>
			<c:when test="${ empty message }"></c:when>
		</c:choose>
	</p>
	
	<form action="/lex/action">
		<p id="helper">
			
			<span class="label">Display</span>:

			<c:set var="ckd" value=""/>
			<c:if test="${ sessionScope.visit.helper.showMeta }">
				<c:set var="ckd" value="checked='checked'"/>
			</c:if>
			<c:out value="<input name='showMeta' type='checkbox' value='true' ${ckd}/> credits |" escapeXml="false"/>
			
			
			<c:set var="ckd" value=""/>
			<c:if test="${ sessionScope.visit.helper.showNotes }">
				<c:set var="ckd" value="checked='checked'"/>
			</c:if>
			<c:out value="<input name='showNotes' type='checkbox' value='true' ${ckd}/> analysis |" escapeXml="false"/>
			
			
			<c:set var="ckd" value=""/>
			<c:if test="${ sessionScope.visit.helper.showTranslations }">
				<c:set var="ckd" value="checked='checked'"/>
			</c:if>
			<c:out value="<input name='showTranslations' type='checkbox' value='true' ${ckd}/> translations" escapeXml="false"/>
			
			
			<c:if test="${ ! sessionScope.visit.user.guest }">
				<c:set var="ckd" value=""/>
				<c:if test="${ sessionScope.visit.helper.showEditOptions }">
					<c:set var="ckd" value="checked='checked'"/>
				</c:if>
				<c:out value="| <input name='showEditOptions' type='checkbox' value='true' ${ckd}/> edit options" escapeXml="false"/>
			
			</c:if>
			
			<c:out value="<input type='hidden' name='metaId' value='${ sessionScope.visit.query.entry.metaId }'/>" escapeXml="false"/>
			<input type="hidden" name="cmd" value="displayFull"/>
			<input type="hidden" name="comp" value="term"/>
			<input type="submit" value="Redisplay"/>
		</p>
	</form>
	
	<p><span class="warning">The requires the <a href="http://iris.lib.virginia.edu/tibet/tools/tmw.html">TibetanMachineWeb font</a>  to display Tibetan script.</span></p>

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

<jsp:include page="footer.jsf" flush="false"/>


