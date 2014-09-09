package nz.kiwi.johnson.stuff

import nz.kiwi.johnson.framework.OnClickEvent
import scalatags.generic
import scalatags.VirtualNode
import nz.kiwi.johnson.framework.EventAttr
import nz.kiwi.johnson.framework.DetailsEvent

case class Filter(override val identifier: FilterState) 
    extends DetailsEvent(identifier)
    
case class Delete(override val identifier: String) 
	extends DetailsEvent(identifier)

case class ClearCompleted() extends DetailsEvent(false)
