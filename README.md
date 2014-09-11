# scalaFlux

scalaFlux is a mini-framework for creating single page applications based on Facebook's flux architecture using 
[ScalaJS](http://www.scala-js.org/), an implementation of the [Virtual Dom](https://github.com/Matt-Esch/virtual-dom) 
and some additions to the [scalatags](https://github.com/pj/scalatags) library to tie them together.

There is a (slightly broken) TodoMVC example in examples.todomvc.

## App

Apps are created by subclassing scala_flux.App with an appropriate class for the application's state and implementing 
the rootId method to get the id of the element to put the view into, the defaultView method to return the applications 
default view and the update method which dispatches based on the application event.

```scala

   // Type parameter is the applications state class
   object TodoApp extends App[TodoState] {
        // id of an element to put the view into.
        def rootId = {
           "main"
        }
        
        // default view for the project.
        def defaultView(state: TodoState) = {
            TodoHTML.todoView(state)
        }

        // dispatch method, takes the current state and event
        def update(currentState: TodoState, event: DetailsEvent[_]) = {
             event match {
                case InitEvent() => {
                    println("Hello World")
                    
                    // Return initial state
                    ImmediateResponse(TodoState())
                }
             }
        }
    }
                
```

The application state class holds all of the state of the application and GUI.

e.g.:

```scala
    case class TodoState(
        val todos: List[Todo], 
        val filter: FilterState = All(), 
        val currentNumber: Int = 0,
        val toggle: Boolean = false,
        val editing: Option[String] = None)
```

When handling an event a new state is created and returned using a type called Response. See below for more information
about the different response types.

```scala
      case Delete(id) =>
        val todos = currentState.todos.filter(_.id != id)
        val nextState = currentState.copy(todos = todos)

        ImmediateResponse(nextState)
```

## View

Views are created using the scalatags library, which generates a virtual dom tree that is diffed by the current tree
by the framework.

```scala
    header(
      id := "header",
      h1("todos"),
      input(
        id := "new-todo",
        placeholder := "What needs to be done?",
        autofocus := "true",
        `type` := "text",
        onkeypress := EnteredEvent("main")
      )
    )
```

Events are classes that wrap DOM events and get passed to the dispatcher. Usually they take a value of some kind to 
identify the source of the event such as an id.  It's also possible to create subclasses of events:

```scala
    case class Filter(override val identifier: FilterState)
         extends DetailsEvent(identifier)
```

The todomvc example uses a mixture of these.

## Custom Events

In addition to being matched on by the dispatcher event classes do some additional things - they act as hooks to modify 
the underlying DOM node to do things like add event listeners and contain the actual event listener function.

By default the DetailsEvent base class simply dispatches itself in response to some event:

```scala

    abstract class DetailsEvent[E](val identifier: E) {
      
      var eventName: String = ""
      var underlyingEvent: Event = null
    
      val listener = { jsEvent: Event =>
        underlyingEvent = jsEvent
        App.currentApp.runUpdate(this)
      }
    
      // Hook into the DOM
      @JSExport
      def hook(element: Element, name: String, previous: js.Any): Unit = {
        jQuery(element).off(this.eventName)
        jQuery(element).on(this.eventName, listener)
      }
    }
```

They can also be used to call some function on a DOM node but not use an event listener:

```scala

    // Not an event exactly, just sets focus on an element in response to focus being set
    // to true.
    case class FocusHook(val focus: Boolean)
      extends DetailsEvent(Unit) {
    
      @JSExport
      override def hook(element: Element, name: String, previous: js.Any): Unit = {
        if (focus) {
          jQuery(element).focus()
        }
      }
    }
```

Events are interfaced to the library using a subclass of VirtualDom.AttrValue.

## Responses and Response Streams

**NB: This stuff is currently not working and needs to be a bit better thought out. **

In the example above we return a result from the dispatch as an ImmediateResponse object.  This is a result that 
immediately returns a new state based on the existing state.  In many cases events don't return immediately or are a 
series of asynchronous events that update the state as they complete. There are a number of different response types to
 handle these cases:

- FutureResponse[E, T](future: Future[E], updater: (T, E) => T) - A future response takes a future that produces a value 
and an updater that takes that value and returns the update state. The Stream class has an example of a future response 
based on a timeout.
- SequenceResponse(responses: List[Response]) - A series of other responses that are applied in sequence. As an example 
this could be something like show a spinner, start a web service request then return the updated state. This would also 
cover animations.
- AsSoonAsResponse(responses: List[Response]) - In other cases you want to start a series of events and update the state as soon as they arrive
 e.g. a series of web requests to load a different part of a page.

## Todo

- The integration with the underlying virtual dom implementation is still pretty flaky, setting values on text boxes 
doesn't seem to work properly.
- Streams are ugly, use a higher level abstraction such as iteratees, frp or actors instead.
- Proper management of concurrency i.e. cancelling in progress streams of events.
- Setting values from the underlying event on events isn't ideal, maybe a better approach to this would be to wrap the 
js event somehow and provide unapply methods for matching.
- Undo handling.
- Events for routing.
- More streams.
- Page exit event?
- Depend on JQuery or a polyfill for all low level interactions with the DOM.
- Scaladocs.