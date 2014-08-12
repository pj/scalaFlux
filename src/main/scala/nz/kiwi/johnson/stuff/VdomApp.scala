package nz.kiwi.johnson.stuff

import scala.scalajs.js
import org.scalajs.dom.Document
import org.scalajs.dom.Element
import org.scalajs.dom.document
import org.scalajs.dom.window
import scala.scalajs.js.Math
import scala.concurrent.Future

import nz.kiwi.johnson.virtual_dom.libraryInterface

import nz.kiwi.johnson.virtual_dom.VirtualDom.all._
import nz.kiwi.johnson.virtual_dom.VirtualDom.tags2.section
import nz.kiwi.johnson.virtual_dom.VirtualNode

import org.scalajs.jquery.jQuery
import org.scalajs.jquery.JQueryEventObject

import rx.Rx
import rx.Var
import rx.Obs
import rx.core.Emitter

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