package com.buidangkhoa.daffodil.common.exceptions

case class InvalidQueryParamException(message: String = "Invalid query parameter.") extends Exception(message: String)
