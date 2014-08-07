package nz.kiwi.johnson.virtual_dom.vdom

import scalatags.generic
import nz.kiwi.johnson.virtual_dom.VirtualNode
import nz.kiwi.johnson.virtual_dom.VirtualNode
import nz.kiwi.johnson.virtual_dom.libraryInterface

trait Frag extends generic.Frag[VirtualNode, VirtualNode, VirtualNode]{
  def render: VirtualNode
  
  def applyTo(b: VirtualNode) = {
    val node = this.render
    
    if (node.tagName == "textHolder") {
      val text = node.children.pop()
      
      b.children.push(text)
    } else {
      b.children.push(node) 
    }
  }
}