package nz.kiwi.johnson.virtual_dom.vdom

import scalatags.generic
import nz.kiwi.johnson.virtual_dom.VirtualNode
import nz.kiwi.johnson.virtual_dom.libraryInterface

class VBuilder(var children: Array[Frag] = new Array(4),
              var attrs: Array[(String, String)] = new Array(4)){
  final var childIndex = 0
  final var attrIndex = 0
  private[this] var styleIndex = -1

//  private[this] def increment[T: ClassTag](arr: Array[T], index: Int) = {
//    if (index >= arr.length){
//      val newArr = new Array[T](arr.length * 2)
//      var i = 0
//      while(i < arr.length){
//        newArr(i) = arr(i)
//        i += 1
//      }
//      newArr
//    }else{
//      null
//    }
//  }

  def addChild(c: Frag) = {
    println(c)
//    val newChildren = increment(children, childIndex)
//    if (newChildren != null) children = newChildren
//    children(childIndex) = c
//    childIndex += 1
  }
  
  def addAttr(k: String, v: String) = {
    println(k)
    println(v)
//    (k, styleIndex) match{
//      case ("style", -1) =>
//        val newAttrs = increment(attrs, attrIndex)
//        if (newAttrs!= null) attrs = newAttrs
//        styleIndex = attrIndex
//        attrs(attrIndex) = (k -> v)
//        attrIndex += 1
//      case ("style", n) =>
//        val (oldK, oldV) = attrs(styleIndex)
//        attrs(styleIndex) = (oldK, oldV + v)
//      case _ =>
//        val newAttrs = increment(attrs, attrIndex)
//        if (newAttrs!= null) attrs = newAttrs
//        attrs(attrIndex) = (k -> v)
//        attrIndex += 1
//    }
  }
  
  def render(): VirtualNode = {
    libraryInterface.h("div", null, "asdf")
  }
}  
  
trait Frag extends generic.Frag[VBuilder, VirtualNode, VirtualNode]{
  def render: VirtualNode
  
  def applyTo(b: VBuilder) = b.addChild(this)
}