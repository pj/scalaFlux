package nz.kiwi.johnson.old

import scala.scalajs.js
import org.scalajs.dom.Element
import scalatags.VirtualNode
import scalatags.VirtualDom.all._
import scalatags.VirtualDom.tags2.section
import scalatags.libraryInterface
import org.scalajs.dom.document

object VdomApp extends js.JSApp {
  
  def otree: VirtualNode = section(id:="hello", div(id:="asdf", "Hello World"), "werwertert").render
  
  def ntree: VirtualNode = div(id:="hello", div(id:="asdf", "Goodbye World"), "dfgdfgdfg").render
  
  def main(): Unit = {
    val rootNode = document.getElementsByTagName("body")
    
    println("asdfasdfasdf")

    // create initial node
    val tree = otree
    
    val newTree = ntree
    
    val element = libraryInterface.createElement(tree, null)
    
    rootNode(0).appendChild(element)
    
    val patch = libraryInterface.diff(tree, newTree)
        
    libraryInterface.patch(rootNode(0).asInstanceOf[Element], patch)
  }
}