<%@ page buffer="512kb" autoFlush="false" import="org.thdl.lex.*" errorPage="/jsp/error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<jsp:include page="jsp/header.jsf" flush="false" />
<div id="label">
Please login to use Lex the Dictionary Server
</div><!--END label-->
<div id="message">Message</div><!--END Message-->
<div id="columnSingle">
<div class="content">

<c:if test="${ ! empty param.retry }">
	<p style="color:red"> Invalid username/password combination.</p>
	<c:set var="usernameValue" value="${ param.username }" />
</c:if>


<form action='/lex/login' method="post">
<p>
Username: <c:out value="<input type='text' name='username' size='16' value='${ usernameValue }'>" escapeXml="false" />
<br />
Password: <input type='password' name='password' size='20' value=''>
<br />
<input type='submit' value='Log in'>
</p>
</form>
<form action='/lex/public' method="post">
<p>
If you are not a dictionary contributor please proceed here.
<input type="submit" value="Proceed" />
</p>
</form>
</div><!--END content-->
</div><!--END columnSingle-->

<jsp:include page="jsp/footer.jsf" flush="false" />

