/* 
====================
Scripts for Lex - general 
====================
*/
function assert( fact, details )
{
	if ( ! fact ) alert( details );
}
function element(id)
{
	if (document.getElementById != null)
	{
		return getElementById(id);
	}
	if (document.all != null)
	{
		return document.all[id]
	}
	if (document.layers != null)
	{
		return document.layers[id]
	}
}

/* 
====================
Scripts for menu.jsp 
====================
*/
function setCmd(cmd)
{
document.forms[1].cmd.value = cmd;
// element( elementId ).cmd.value = cmd; 
}
function setTerm()
{
document.forms[1].term.value = element( termSource ).value;
}
/* 
=============================
Scripts for displayEntry.jsp 
=============================
*/
function MM_jumpMenu(targ,selObj,restore) //v3.0 modified by TWM 
{ 
eval(targ + ".location='/lex/action?cmd=displayFull&componentLabel=term&id=" + selObj.options[selObj.selectedIndex].value + "'");
if (restore) selObj.selectedIndex=0;
}

function jumpToAnchor(sAnchor) {
ns = (document.layers)? true:false;
ie = (document.all)? true:false;
if (ns) 
	{
		window.scrollTo(0, document.anchors[sAnchor].y);
	}
if (ie) 
	{
		eval('document.all.' + sAnchor + '.scrollIntoView()');
	}
}

/* 
=============================
Scripts for displayTree.jsf
=============================
*/
function submitForm( cmd, component, id, parentId, componentHex, parentHex )
{
	document.forms[2].cmd.value = cmd;
	document.forms[2].componentLabel.value = component;
	document.forms[2].id.value = id;
	document.forms[2].parentId.value = parentId;
	assert( componentHex != null , "no componentHex");
		document.forms[2].componentHex.value = componentHex;
	assert( parentHex != null , "no parentHex") ;
		document.forms[2].parentHex.value = parentHex;
	document.forms[2].submit();
}

function smile()
{}


