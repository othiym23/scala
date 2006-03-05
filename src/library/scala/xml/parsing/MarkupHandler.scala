/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003-2006, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |                                         **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

// $Id$


package scala.xml.parsing;


import scala.io.Source;
import scala.collection.mutable.{ HashMap, Map }
import scala.xml.dtd._ ;

import scala.util.logging._;

/** class that handles markup - provides callback methods to MarkupParser.
 *  the default is nonvalidating behaviour
 *
 *  @todo can we ignore more entity declarations (i.e. those with extIDs)?
 *  @todo expanding entity references
 */
abstract class MarkupHandler extends AnyRef with Logged {

  // impl. of Logged
  //def log(msg:String) = {}

  /** returns true is this markup handler is validing */
  val isValidating: Boolean = false;

  var decls: List[Decl] = Nil;

  var ent:  Map[String, EntityDecl]  = new HashMap[String, EntityDecl]();

  def lookupElemDecl(Label: String): ElemDecl =  {
    def lookup(xs:List[Decl]): ElemDecl = xs match {
      case (z @ ElemDecl(Label, _)) :: zs => return z; 
      case _::zs                        => lookup(zs);
      case _                            => return null
    }
    lookup(decls)
  }

  def replacementText( entityName: String ): Source = ent.get(entityName) match {
      case Some(ParsedEntityDecl(_, IntDef(value))) =>
        Source.fromString(value);
      case Some(ParameterEntityDecl(_, IntDef(value))) =>
        Source.fromString(" "+value+" ");
      case Some(_) =>
        Source.fromString("<!-- "+entityName+"; -->");
      case None => 
        Source.fromString("<!-- unknown entity "+entityName+"; -->")
    }
  

 //def checkChildren(pos:int, pre: String, label:String,ns:NodeSeq): Unit = {}

  def endDTD(n:String): Unit = {}

  /** callback method invoked by MarkupParser after start-tag of element.
   *
   *  @param pos      the position in the sourcefile
   *  @param pre      the prefix
   *  @param label    the local name
   *  @param attrs    the attributes (metadata)
   */
  def elemStart(pos: int, pre: String, label: String, attrs: MetaData, scope:NamespaceBinding): Unit = {}

  /** callback method invoked by MarkupParser after end-tag of element.
   *
   *  @param pos      the position in the sourcefile
   *  @param pre      the prefix
   *  @param label    the local name
   *  @param attrs    the attributes (metadata)
   */  def elemEnd(pos: int, pre: String, label: String): Unit = {}

  /** callback method invoked by MarkupParser after parsing an elementm,
   *  between the elemStart and elemEnd callbacks
   *  
   *
   *  @param pos      the position in the sourcefile
   *  @param pre      the prefix
   *  @param label    the local name
   *  @param attrs    the attributes (metadata)
   *  @param args     the children of this element
   */
  def elem(pos: int, pre: String, label: String, attrs: MetaData, scope:NamespaceBinding, args: NodeSeq): NodeSeq;

  /** callback method invoked by MarkupParser after parsing PI.
   */
  def procInstr(pos: Int, target: String, txt: String): NodeSeq;

  /** callback method invoked by MarkupParser after parsing comment.
   */
  def comment(pos: Int, comment: String ): NodeSeq;

  /** callback method invoked by MarkupParser after parsing entity ref.
   *  @todo expanding entity references
   */
  def entityRef(pos: Int, n: String): NodeSeq;

  /** callback method invoked by MarkupParser after parsing text.
   */
  def text(pos: Int, txt:String): NodeSeq;

  // DTD handler methods

  def elemDecl(n: String, cmstr: String): Unit = {}
    
  def attListDecl(name: String, attList: List[AttrDecl]): Unit = {}

  def parameterEntityDecl(name: String, edef: EntityDef): Unit = {
    //log("parameterEntityDecl("+name+","+edef+")");
    edef match {
      case _:ExtDef if !isValidating => 
        ; // ignore (cf REC-xml 4.4.1)
      case _ =>
        val y =  ParameterEntityDecl(name, edef);
        decls = y :: decls;
        ent.update(name, y);
        //log("ent.get(..) = "+ent.get(name));
    }
  }

  def parsedEntityDecl(name: String, edef: EntityDef): Unit = edef match {
    case _:ExtDef if !isValidating => 
      ; // ignore (cf REC-xml 4.8 and 4.4.1)
    case _ => 
      val y = ParsedEntityDecl(name, edef);
      decls = y :: decls;
      ent.update(name, y)
  }

  def unparsedEntityDecl(name: String, extID: ExternalID, notat: String): Unit =
    {}

  def notationDecl(notat: String, extID: ExternalID): Unit =
    {}

  def peReference(name: String): Unit =
    decls = PEReference( name ) :: decls;

  /** report a syntax error */
  def reportSyntaxError(pos: Int, str: String): Unit;

}

