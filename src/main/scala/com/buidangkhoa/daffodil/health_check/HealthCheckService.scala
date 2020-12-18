package com.buidangkhoa.daffodil.health_check

trait HealthCheckService[F[_]] {
  def healthCheck(): F[HeathCheckResponse]
}
