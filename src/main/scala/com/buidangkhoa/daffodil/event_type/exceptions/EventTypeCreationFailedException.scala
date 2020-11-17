package com.buidangkhoa.daffodil.event_type.exceptions

case class EventTypeCreationFailedException(message: String = "Event type creation failed.") extends Exception(message: String)
