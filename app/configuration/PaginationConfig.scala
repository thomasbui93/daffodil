package configuration

import play.api.ConfigLoader
import com.typesafe.config.Config

case class PaginationConfig (page: Int, size: Int)

object PaginationConfig {
  implicit val configLoader: ConfigLoader[PaginationConfig] = new ConfigLoader[PaginationConfig] {
    def load(rootConfig: Config, path: String): PaginationConfig = {
      val config = rootConfig.getConfig(path)
      PaginationConfig(
        page = config.getInt("page"),
        size = config.getInt("size")
      )
    }
  }
}
