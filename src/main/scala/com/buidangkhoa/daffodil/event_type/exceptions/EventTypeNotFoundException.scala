package com.buidangkhoa.daffodil.event_type.exceptions

final case class EventTypeNotFoundException(message: String = "Event type not found.") extends Exception(message: String)
