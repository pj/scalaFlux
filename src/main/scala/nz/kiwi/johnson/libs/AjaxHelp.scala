package nz.kiwi.johnson.libs

import org.scalajs.jquery.{JQueryAjaxSettings, JQueryStatic}
import scala.scalajs.js
import scala.concurrent.Future
import scala.scalajs.js.Any.fromFunction1

//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:15 PM
//////////////////////////////////////////////////////////////


object AjaxHelp {
  val jQuery: JQueryStatic = js.Dynamic.global.jQuery.asInstanceOf[JQueryStatic]

  object HttpRequestTypes extends Enumeration {
    type HttpRequestType = Value
    val Get = Value("Get")
    val Post = Value("Post")
    val Put = Value("Put")
    val Delete = Value("Delete")
  }

  def aplusToScala[A](that: js.Dynamic): Future[A] = {
    val p = JsPromise[A]()
    that.`then`((data: js.Any) => p.success(data.asInstanceOf[A]))
    p.future
  }


  def apply[A](url: String, requestType: HttpRequestTypes.HttpRequestType, data: Option[String]): Future[A] = {
    aplusToScala[A] {
      val req = js.Dictionary(
        "url" -> url,
        "type" -> requestType.toString
      )
      data.foreach(data => req("data") = data)
      jQuery.ajax(req.asInstanceOf[JQueryAjaxSettings])
    }
  }
}