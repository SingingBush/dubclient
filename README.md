DUB Client
==========

Stand-alone library to provide convenient access to the REST API of a dub repository. Deserializes the json from the API for convenient use within a JVM language such as Java or Kotlin.

`https://code.dlang.org/api/packages/{package}/info`

`https://code.dlang.org/api/packages/{package}/stats`

`https://code.dlang.org/api/packages/{package}/latest`

This package requires _Apache HTTP Client v4.5.*_, _Gson v2.8_, and uses _slf4j-api_ for logging. It's intended for use in the [Intellij-DUB](https://github.com/intellij-dlanguage/intellij-dub) plugin but may also be helpful to other projects.
