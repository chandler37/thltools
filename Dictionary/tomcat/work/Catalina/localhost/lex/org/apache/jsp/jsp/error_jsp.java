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

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"DTD/xhtml1-strict.dtd\">\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n");
      out.write("<head>\n");
      out.write("<style>\n");
      out.write("#debug table\n");
      out.write("{\n");
      out.write("border: black solid 1px;\n");
      out.write("/* border-collapse:collapse;\n");
      out.write("border-color: black;\n");
      out.write("border-style:solid; \n");
      out.write("border-weight:1px; */\n");
      out.write("color:black;\n");
      out.write("width: 790px;;\n");
      out.write("margin:5px 5px 5px 5px;\n");
      out.write("}\n");
      out.write("#debug th, #debug td\n");
      out.write("{\n");
      out.write("border: black solid 1px;\n");
      out.write("background-color:cyan;\n");
      out.write("font-size:.9em;\n");
      out.write("text-align: left;\n");
      out.write("vertical-align: top;\n");
      out.write("}\n");
      out.write("</style>\n");
      out.write("\n");
      out.write("<title>THDL Dictionary Error Page</title>\n");
      out.write("</head>\n");
      out.write("<body> \n");
      out.write("\n");
      out.write("<div id=\"error\">\n");
      out.write("<p>\n");
      out.write("You have reached the Error page. \n");
      out.write("This page indicates that Lex has caught an exception that it does not know how to deal with. \n");
      out.write("The message appears below.<br />\n");
      out.write("<a href=\"/lex/action\">Return to the home page.</a> <br /><br />\n");
      out.write("\n");
      out.write("\n");
      out.write("<b>Message: </b> ");
      out.print( exception.getMessage() );
      out.write(" <br /><br />\n");
      out.write("\n");
 if (request.getAttribute("comp") != null) 
	{ LexComponent lab =  (LexComponent) request.getAttribute("comp");

      out.write("\n");
      out.write("<b> Label: </b> <i> ");
      out.print( lab );
      out.write(" </i><br /><br /> \n");
      out.write("\n");
 } 
      out.write(" <br /><br />\n");
      out.write("\n");
      out.write("<b>Stack Trace: </b>\n");
 	StringWriter writer = new StringWriter();
	exception.printStackTrace( new PrintWriter(writer) );
	String stackTrace = writer.getBuffer().toString();

      out.write("\n");
      out.write("<pre>\n");
      out.print( stackTrace );
      out.write("\n");
      out.write("</pre> \n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("</p>\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>\n");
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
