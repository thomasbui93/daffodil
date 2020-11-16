val Http4sVersion = "0.21.8"
val CirceVersion = "0.13.0"
val Specs2Version = "4.10.5"
val LogbackVersion = "1.2.3"
val CirceConfigVersion = "0.7.0"
val DoobieVersion = "0.8.8"
val MySQLDriverVersion = "5.1.18"
val ScalaLogVersion = "3.9.2"

lazy val root = (project in file("."))
  .settings(
    organization := "com.buidangkhoa",
    name := "daffodil",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.3",
    libraryDependencies ++= Seq(
      "org.http4s"            %% "http4s-blaze-server"  % Http4sVersion,
      "org.http4s"            %% "http4s-blaze-client"  % Http4sVersion,
      "org.http4s"            %% "http4s-circe"         % Http4sVersion,
      "org.http4s"            %% "http4s-dsl"           % Http4sVersion,
      "io.circe"              %% "circe-generic"        % CirceVersion,
      "io.circe"              %% "circe-config"         % CirceConfigVersion,
      "org.tpolecat"          %% "doobie-core"          % DoobieVersion,
      "org.tpolecat"          %% "doobie-h2"            % DoobieVersion,
      "org.tpolecat"          %% "doobie-hikari"        % DoobieVersion,
      "org.tpolecat"          %% "doobie-specs2"        % DoobieVersion,
      "com.typesafe.scala-logging" %% "scala-logging"   % ScalaLogVersion,
      "org.specs2"            %% "specs2-core"          % Specs2Version % "test",
      "ch.qos.logback"        %  "logback-classic"      % LogbackVersion,
      "mysql"                 % "mysql-connector-java"  % MySQLDriverVersion,
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings",
)
