<%@ page buffer="512kb" autoFlush="false" import="org.thdl.lex.*" errorPage="/jsp/error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<jsp:include page="jsp/header.jsf" flush="false" />

<div id="columns">
<div id="columnSingle">
<div class="label">
Log out of THDL Tibetan Collaborative Dictionaries</div>

<div class="content">

<p>
Log out account <b><c:out value="${ sessionScope.visit.user.username }" /></b>
</p>

<form action='<%= response.encodeURL( request.getContextPath() + "/logout" )%>' method="post">
<input type="submit" value="Log out">
</form>

</div>
</div>
</div>
<jsp:include page="jsp/footer.jsf" flush="false" />
