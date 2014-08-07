package nz.kiwi.johnson.virtual_dom

import scala.scalajs.js
import org.scalajs.dom.Element
import scalatags.generic.Bundle
import scalatags.generic.Aliases
import scalatags.generic
import scala.annotation.unchecked.uncheckedVariance
import scalatags.DataConverters
import scalatags.Escaping
import scalatags.Companion
import scala.scalajs.js.PropertyDescriptor

object VirtualDom
    extends Bundle[VirtualNode, VirtualNode, VirtualNode]
    with Aliases[VirtualNode, VirtualNode, VirtualNode]{
  
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
  
  trait Cap extends Util { self =>
    type ConcreteHtmlTag[T <: VirtualNode] = TypedTag[T]

    protected[this] implicit def stringAttrX = new GenericAttr[String]
    protected[this] implicit def stringStyleX = new GenericStyle[String]

    def makeAbstractTypedTag[T](tag: String, void: Boolean) = {
      TypedTag(tag, Nil, void)
    }
  }
    
  trait Aggregate extends generic.Aggregate[VirtualNode, VirtualNode, VirtualNode]{
    def genericAttr[T] = new VirtualDom.GenericAttr[T]
    def genericStyle[T] = new VirtualDom.GenericStyle[T]

    implicit def stringFrag(v: String) = new VirtualDom.StringFrag(v)

    val RawFrag = VirtualDom.RawFrag
    val StringFrag = VirtualDom.StringFrag
    type StringFrag = VirtualDom.StringFrag
    type RawFrag = VirtualDom.RawFrag
    def raw(s: String) = RawFrag(s)
  }

  type Tag = VirtualDom.TypedTag[VirtualNode]
  val Tag = VirtualDom.TypedTag

  object StringFrag extends Companion[StringFrag]
  case class StringFrag(v: String) extends vdom.Frag{
    def render = libraryInterface.h("textHolder", js.Object(), v)
  }
  
  object RawFrag extends Companion[RawFrag]
  case class RawFrag(v: String) extends vdom.Frag {
    def render = libraryInterface.h("textHolder", js.Object(), v)
  }
  
  class GenericAttr[T] extends AttrValue[T]{
    def apply(t: VirtualNode, a: Attr, v: T): Unit = {
      t.properties.updateDynamic(a.name)(v.toString)
    }
  }

  class GenericStyle[T] extends StyleValue[T]{
    def apply(t: VirtualNode, s: Style, v: T): Unit = {
      val attr = t.properties.selectDynamic("style")
      
      attr.updateDynamic(s.cssName)(v.toString)
    }
  }
  
  case class TypedTag[+Output <: VirtualNode](tag: String = "",
                                         modifiers: List[Seq[Modifier]],
                                         void: Boolean = false)
                                         extends generic.TypedTag[VirtualNode, Output, VirtualNode]
                                         with vdom.Frag {
    // unchecked because Scala 2.10.4 seems to not like this, even though
    // 2.11.1 works just fine. I trust that 2.11.1 is more correct than 2.10.4
    // and so just force this.
    protected[this] type Self = TypedTag[Output @uncheckedVariance]
    
    def apply(xs: Modifier*): TypedTag[Output] = {
      this.copy(tag=tag, void = void, modifiers = xs :: modifiers)
    }
      
    def render() = {
      val builder = libraryInterface.h(tag, js.Object(), "")
      build(builder)
      
      builder.asInstanceOf[Output]
    }
  }
}