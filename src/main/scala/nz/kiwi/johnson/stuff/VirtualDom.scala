package nz.kiwi.johnson.stuff

import scala.scalajs.js
import org.scalajs.dom.Document
import org.scalajs.dom.Element
import org.scalajs.dom.document
import scala.concurrent.Future
import org.scalajs.dom.Event
import scalatags.generic._

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

object virtualDomXmlHelper extends js.Object {
  def div(properties: js.Object = js.Object(), children: Array[VirtualNode] = null): VirtualNode = {
    virtualDom.h("div", properties, children)
  }
}

// TODO: virtual dom helper for scalatags
//class VBuilder {
//  
//}
//
//object VirtualDom
//  extends Bundle[VBuilder, String, String]
//  with Aliases[VBuilder, String, String]{
//  
//}