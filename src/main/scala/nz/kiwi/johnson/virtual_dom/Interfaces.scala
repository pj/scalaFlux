package nz.kiwi.johnson.virtual_dom

import scala.scalajs.js
import org.scalajs.dom.Element
import scala.scalajs.js.annotation.JSName

trait VirtualNode extends js.Object {
  var tagName: String
  var properties: js.Dynamic
  var children: js.Dynamic
}

trait PatchObject extends js.Object

@JSName("virtualDom")
object libraryInterface extends js.Object {
  
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
