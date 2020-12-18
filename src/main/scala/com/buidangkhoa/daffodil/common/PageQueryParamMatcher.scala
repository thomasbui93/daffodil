package com.buidangkhoa.daffodil.common

import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher

object PageQueryParamMatcher extends OptionalQueryParamDecoderMatcher[Int]("page")
