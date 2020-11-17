package com.buidangkhoa.daffodil.event_type.exceptions

case class EventTypeUpdateException(message: String = "Event type update failed.") extends Exception(message: String)
