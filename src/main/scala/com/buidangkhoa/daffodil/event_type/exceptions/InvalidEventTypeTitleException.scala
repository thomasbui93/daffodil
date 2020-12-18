package com.buidangkhoa.daffodil.event_type.exceptions

case class InvalidEventTypeTitleException(message: String = "Event type title is invalid") extends Exception(message: String)
