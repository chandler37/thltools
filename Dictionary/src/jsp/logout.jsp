<jsp:include page="jsp/header.jsf" flush="false" />

<div id="columns">
<div id="columnSingle">
<div class="label">
Log out of Lex the Dictionary Server</div>

<div class="content">

<p>
Log out account <b><jsp:getProperty name="user" property="username" /></b>
</p>

<form action='<%= response.encodeURL( request.getContextPath() + "/logout" )%>' method="post">
<input type="submit" value="Log out">
</form>

</div>
</div>
</div>
<jsp:include page="jsp/footer.jsf" flush="false" />
