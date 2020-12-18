package com.buidangkhoa.daffodil.common

import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher

object SizeQueryParamMatcher extends OptionalQueryParamDecoderMatcher[Int]("size")
