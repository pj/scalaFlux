package nz.kiwi.johnson.stuff

import nz.kiwi.johnson.framework.OnClickEvent
import scalatags.generic
import scalatags.VirtualNode
import nz.kiwi.johnson.framework.EventAttr
import nz.kiwi.johnson.framework.ApplicationEvent
import nz.kiwi.johnson.framework.DetailsEvent
import nz.kiwi.johnson.framework.KeyUpEvent

case class Filter(val filterKey: FilterState) 
    extends OnClickEvent[FilterState](filterKey, null)
    
case class Delete(override val details: String) 
	extends DetailsEvent[String](details)

    
case class ClearCompleted() extends ApplicationEvent
