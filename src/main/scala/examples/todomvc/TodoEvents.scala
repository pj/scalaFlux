package examples.todomvc

import scala_flux.DetailsEvent

case class Filter(override val identifier: FilterState) 
    extends DetailsEvent(identifier)
    
case class Delete(override val identifier: String) 
	extends DetailsEvent(identifier)

case class ClearCompleted() extends DetailsEvent(Unit)

case class StartEdit(override val identifier: String) extends DetailsEvent(identifier)

case class StopEdit(override val identifier: String) extends DetailsEvent(identifier) 
