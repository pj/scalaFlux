package nz.kiwi.johnson.stuff

import scala.scalajs.js
import org.scalajs.dom.Element
import scalatags.generic.Bundle
import scalatags.generic.Aliases
import scalatags.generic
import scala.annotation.unchecked.uncheckedVariance
import scalatags.DataConverters
import scalatags.Escaping
import scalatags.Companion
import nz.kiwi.johnson.stuff.vdom.VBuilder

trait VirtualNode extends js.Object
trait PatchObject extends js.Object

object virtualDom extends js.Object {
  
  // function h(tagName, properties, children) {
  def h(tagName: String, properties: js.Object, children: Array[VirtualNode]): VirtualNode = ???

  // function h(tagName, properties, children) {
  def h(tagName: String, properties: js.Object, children: Array[String]): VirtualNode = ???
  
  // function h(tagName, properties, children) {
  def h(tagName: String, properties: js.Object, children: String): VirtualNode = ???
  
  // function diff(a, b) {
  def diff(a: VirtualNode, b: VirtualNode): PatchObject = ???
  
  // function patch(rootNode, patches) {
  def patch(rootNode: Element, patches: js.Any): Element = ???
  
  // function createElement(vnode, opts) {
  def createElement(vnode: VirtualNode, opts: PatchObject): Element = ???
}

trait Frag extends generic.Frag[VBuilder, String, String]{
  def writeTo(strb: StringBuilder): Unit
  def render: String
  def applyTo(b: VBuilder) = Unit//b.addChild(this)
}

object VirtualDom
  extends Bundle[VBuilder, String, String]
  with Aliases[VBuilder, String, String]{
    object attrs extends VirtualDom.Cap with Attrs
  object tags extends VirtualDom.Cap with vdom.Tags
  object tags2 extends VirtualDom.Cap with vdom.Tags2
  object styles extends VirtualDom.Cap with Styles
  object styles2 extends VirtualDom.Cap with Styles2
  object svgTags extends VirtualDom.Cap with vdom.SvgTags
  object svgStyles extends VirtualDom.Cap with SvgStyles

  object implicits extends Aggregate
  
  object all
    extends Cap
    with Attrs
    with Styles
    with vdom.Tags
    with DataConverters
    with Aggregate

  object short
    extends Cap
    with Util
    with DataConverters
    with Aggregate
    with AbstractShort{

    object * extends Cap with Attrs with Styles
  }
  
  trait Cap extends Util{ self =>
    type ConcreteHtmlTag[T <: String] = TypedTag[T]

    protected[this] implicit def stringAttrX = new GenericAttr[String]
    protected[this] implicit def stringStyleX = new GenericStyle[String]

    def makeAbstractTypedTag[T](tag: String, void: Boolean) = {
      TypedTag(tag, Nil, void)
    }
  }
    
  trait Aggregate extends generic.Aggregate[VBuilder, String, String]{
    def genericAttr[T] = new VirtualDom.GenericAttr[T]
    def genericStyle[T] = new VirtualDom.GenericStyle[T]

    implicit def stringFrag(v: String) = new VirtualDom.StringFrag(v)

    val RawFrag = VirtualDom.RawFrag
    val StringFrag = VirtualDom.StringFrag
    type StringFrag = VirtualDom.StringFrag
    type RawFrag = VirtualDom.RawFrag
    def raw(s: String) = RawFrag(s)
  }

  type Tag = VirtualDom.TypedTag[String]
  val Tag = VirtualDom.TypedTag

  case class StringFrag(v: String) extends Frag{
    def render = {
      val strb = new StringBuilder()
      writeTo(strb)
      strb.toString()
    }
    def writeTo(strb: StringBuilder) = Escaping.escape(v, strb)
  }
  object StringFrag extends Companion[StringFrag]
  object RawFrag extends Companion[RawFrag]
  case class RawFrag(v: String) extends Frag {
    def render = v
    def writeTo(strb: StringBuilder) = strb ++= v
  }
  
  class GenericAttr[T] extends AttrValue[T]{
    def apply(t: VBuilder, a: Attr, v: T): Unit = {
//      t.addAttr(a.name, v.toString)
    }
  }

  class GenericStyle[T] extends StyleValue[T]{
    def apply(t: VBuilder, s: Style, v: T): Unit = {
//      val strb = new StringBuilder()
//
//      Escaping.escape(s.cssName, strb)
//      strb ++=  ": "
//      Escaping.escape(v.toString, strb)
//      strb ++= ";"
//
//      t.addAttr("style", strb.toString)
    }
  }
  


  case class TypedTag[+Output <: String](tag: String = "",
                                         modifiers: List[Seq[Modifier]],
                                         void: Boolean = false)
                                         extends generic.TypedTag[VBuilder, Output, String]
                                         with Frag{
    // unchecked because Scala 2.10.4 seems to not like this, even though
    // 2.11.1 works just fine. I trust that 2.11.1 is more correct than 2.10.4
    // and so just force this.
    protected[this] type Self = TypedTag[Output @uncheckedVariance]

    /**
     * Serialize this [[TypedTag]] and all its children out to the given StringBuilder.
     *
     * Although the external interface is pretty simple, the internals are a huge mess,
     * because I've inlined a whole lot of things to improve the performance of this code
     * ~4x from what it originally was, which is a pretty nice speedup
     */
    def writeTo(strb: StringBuilder): Unit = {
//      val builder = new text.Builder()
//      build(builder)
//
//      // tag
//      strb += '<' ++= tag
//
//      // attributes
//      var i = 0
//      while (i < builder.attrIndex){
//        val pair = builder.attrs(i)
//        strb += ' ' ++= pair._1 ++= "=\""
//        Escaping.escape(pair._2, strb)
//        strb += '\"'
//        i += 1
//      }
//
//      if (builder.childIndex == 0 && void) {
//        // No children - close tag
//        strb ++= " />"
//      } else {
//        strb += '>'
//        // Childrens
//        var i = 0
//        while(i < builder.childIndex){
//          builder.children(i).writeTo(strb)
//          i += 1
//        }
//
//        // Closing tag
//        strb ++= "</" ++= tag += '>'
      }
    }
}