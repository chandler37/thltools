package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.*;
import org.thdl.lex.*;
import org.thdl.lex.component.*;

public final class error_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(1);
    _jspx_dependants.add("/WEB-INF/tld/c.tld");
  }

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    Throwable exception = org.apache.jasper.runtime.JspRuntimeLibrary.getThrowable(request);
    if (exception != null) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 524288, false);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"DTD/xhtml1-strict.dtd\">\r\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\r\n");
      out.write("<head>\r\n");
      out.write("<style>\r\n");
      out.write("#debug table\r\n");
      out.write("{\r\n");
      out.write("border: black solid 1px;\r\n");
      out.write("/* border-collapse:collapse;\r\n");
      out.write("border-color: black;\r\n");
      out.write("border-style:solid; \r\n");
      out.write("border-weight:1px; */\r\n");
      out.write("color:black;\r\n");
      out.write("width: 790px;;\r\n");
      out.write("margin:5px 5px 5px 5px;\r\n");
      out.write("}\r\n");
      out.write("#debug th, #debug td\r\n");
      out.write("{\r\n");
      out.write("border: black solid 1px;\r\n");
      out.write("background-color:cyan;\r\n");
      out.write("font-size:.9em;\r\n");
      out.write("text-align: left;\r\n");
      out.write("vertical-align: top;\r\n");
      out.write("}\r\n");
      out.write("</style>\r\n");
      out.write("\r\n");
      out.write("<title>THDL Dictionary Error Page</title>\r\n");
      out.write("</head>\r\n");
      out.write("<body> \r\n");
      out.write("\r\n");
      out.write("<div id=\"error\">\r\n");
      out.write("<p>\r\n");
      out.write("You have reached the Error page. \r\n");
      out.write("This page indicates that Lex has caught an exception that it does not know how to deal with. \r\n");
      out.write("The message appears below.<br />\r\n");
      out.write("<a href=\"/lex/action\">Return to the home page.</a> <br /><br />\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<b>Message: </b> ");
      out.print( exception.getMessage() );
      out.write(" <br /><br />\r\n");
      out.write("\r\n");
 if (request.getAttribute("comp") != null) 
	{ LexComponent lab =  (LexComponent) request.getAttribute("comp");

      out.write("\r\n");
      out.write("<b> Label: </b> <i> ");
      out.print( lab );
      out.write(" </i><br /><br /> \r\n");
      out.write("\r\n");
 } 
      out.write(" <br /><br />\r\n");
      out.write("\r\n");
      out.write("<b>Stack Trace: </b>\r\n");
 	StringWriter writer = new StringWriter();
	exception.printStackTrace( new PrintWriter(writer) );
	String stackTrace = writer.getBuffer().toString();

      out.write("\r\n");
      out.write("<pre>\r\n");
      out.print( stackTrace );
      out.write("\r\n");
      out.write("</pre> \r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</p>\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
