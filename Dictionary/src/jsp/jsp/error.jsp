<%@ page  buffer="512kb" autoFlush="false" import="java.io.*, org.thdl.lex.*,org.thdl.lex.component.*" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<style>
#debug table
{
border: black solid 1px;
/* border-collapse:collapse;
border-color: black;
border-style:solid; 
border-weight:1px; */
color:black;
width: 790px;;
margin:5px 5px 5px 5px;
}
#debug th, #debug td
{
border: black solid 1px;
background-color:cyan;
font-size:.9em;
text-align: left;
vertical-align: top;
}
</style>

<title>THDL Dictionary Error Page</title>
</head>
<body> 

<div id="error">
<p>
You have reached the Error page. 
This page indicates that Lex has caught an exception that it does not know how to deal with. 
The message appears below.<br />
<a href="/lex/action">Return to the home page.</a> <br /><br />


<b>Message: </b> <%= exception.getMessage() %> <br /><br />

<% if (request.getAttribute("component") != null) 
	{ LexComponent lab =  (LexComponent) request.getAttribute("component");
%>
<b> Label: </b> <i> <%= lab %> </i><br /><br /> 

<% } %> <br /><br />

<b>Stack Trace: </b>
<% 	StringWriter writer = new StringWriter();
	exception.printStackTrace( new PrintWriter(writer) );
	String stackTrace = writer.getBuffer().toString();
%>
<pre>
<%= stackTrace %>
</pre> 



</p>
</div>

<%-- <jsp:include page="debug.jsf" /> --%>

</body>
</html>
