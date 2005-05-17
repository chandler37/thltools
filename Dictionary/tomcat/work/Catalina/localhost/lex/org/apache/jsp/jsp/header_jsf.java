package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.thdl.lex.*;

public final class header_jsf extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(1);
    _jspx_dependants.add("/WEB-INF/tld/c.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_c_if_test;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_c_if_test = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_c_if_test.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
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
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n");
      out.write("<head>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8;\" />\n");
      out.write("\n");
      out.write("<script type='text/javascript' src='http://iris.lib.virginia.edu/tibet/scripts/thdl_scripts.js'></script>\n");
      out.write("<script type='text/javascript' src='/lex/js/lex.js'></script>\n");
      out.write("\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"/lex/css/lex.css\" />\n");
      out.write("\n");
      out.write("<title>THDL Tibetan Collaborative Dictionaries</title>\n");
      out.write("<style type=\"text/css\">\n");
      if (_jspx_meth_c_if_0(_jspx_page_context))
        return;
      out.write("\n");
      out.write("/* ");
      out.print( org.thdl.tib.text.TibetanHTML.getStyles( "20" ) );
      out.write(" */\n");
      out.write("</style>\n");
      out.write("</head>\n");
      out.write("<body> \n");
      out.write("\n");
      out.write("<div id=\"banner\">\n");
      out.write("\t<a id=\"logo\" href=\"http://iris.lib.virginia.edu/tibet/index.html\"><img id=\"test\" alt=\"THDL Logo\" src=\"http://iris.lib.virginia.edu/tibet/images/logo.png\"/></a>\n");
      out.write("\t<h1>The Tibetan &amp; Himalayan Digital Library</h1>\n");
      out.write("\n");
      out.write("\t<div id=\"menubar\">\n");
      out.write("\t\t");
      out.write("\n");
      out.write("\t</div><!--END menubar-->\n");
      out.write("</div><!--END banner-->\n");
      out.write("\n");
      out.write("<div id=\"sub_banner\">\n");
      out.write("\t<div id=\"search\">\n");
      out.write("\t\t<form  method=\"get\" action=\"http://www.google.com/u/thdl\">\n");
      out.write("\t\t\t<p>\n");
      out.write("\t\t\t\t<input type=\"text\" name=\"q\" id=\"q\" size=\"15\" maxlength=\"255\" value=\"\" />\n");
      out.write("\t\t\t\t<input type=\"submit\" name=\"sa\" id=\"sa\" value=\"Search\"/>\n");
      out.write("\t\t\t\t<input type=\"hidden\" name=\"hq\" id=\"hq\"  value=\"inurl:iris.lib.virginia.edu\"/>\n");
      out.write("\t\t\t</p>\n");
      out.write("\t\t</form>\n");
      out.write("\t</div>\n");
      out.write("\n");
      out.write("\t <div id=\"breadcrumbs\">\n");
      out.write("\t\t<a href=\"http://iris.lib.virginia.edu/tibet/index.html\">THDL</a> &gt;\n");
      out.write("\t\t<a href=\"http://iris.lib.virginia.edu/tibet/reference/index.html\">Reference</a> &gt;\n");
      out.write("\t\tTHDL Tibetan Collaborative Dictionaries\n");
      out.write("\t</div> \n");
      out.write("</div><!--END sub_banner-->\n");
      out.write("\n");
      out.write("<div id=\"main\">\n");
      out.write("\n");
      out.write('\n');
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

  private boolean _jspx_meth_c_if_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.el.core.IfTag _jspx_th_c_if_0 = (org.apache.taglibs.standard.tag.el.core.IfTag) _jspx_tagPool_c_if_test.get(org.apache.taglibs.standard.tag.el.core.IfTag.class);
    _jspx_th_c_if_0.setPageContext(_jspx_page_context);
    _jspx_th_c_if_0.setParent(null);
    _jspx_th_c_if_0.setTest("${ ! sessionScope.visit.helper.showEditOptions }");
    int _jspx_eval_c_if_0 = _jspx_th_c_if_0.doStartTag();
    if (_jspx_eval_c_if_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("p.data\n");
        out.write("{\n");
        out.write("border: none;\n");
        out.write("}\n");
        int evalDoAfterBody = _jspx_th_c_if_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_if_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_c_if_test.reuse(_jspx_th_c_if_0);
    return false;
  }
}
