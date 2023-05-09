DUB Client
==========

[![Java CI](https://github.com/SingingBush/dubclient/actions/workflows/maven.yml/badge.svg)](https://github.com/SingingBush/dubclient/actions/workflows/maven.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.singingbush/dub-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.singingbush/dub-client)
[![Javadocs](https://www.javadoc.io/badge/com.singingbush/dub-client.svg)](https://www.javadoc.io/doc/com.singingbush/dub-client)
[![Coverage Status](https://coveralls.io/repos/github/SingingBush/dubclient/badge.svg?branch=master)](https://coveralls.io/github/SingingBush/dubclient?branch=master)

Stand-alone library to provide convenient access to the REST API of a dub repository. Deserializes the json from the API for convenient use within a JVM language such as Java or Kotlin.

`https://code.dlang.org/api/packages/search?q=`

`https://code.dlang.org/api/packages/{package}/info`

`https://code.dlang.org/api/packages/{package}/{version}/info`

`https://code.dlang.org/api/packages/{package}/stats`

`https://code.dlang.org/api/packages/{package}/{version}/stats`

`https://code.dlang.org/api/packages/{package}/latest`

## Adding the dependency to your project

```xml
<dependency>
    <groupId>com.singingbush</groupId>
    <artifactId>dub-client</artifactId>
    <version>0.2.3</version>
</dependency>
```

The API for the dub registry can be found [here](https://github.com/dlang/dub-registry/blob/master/source/dubregistry/api.d):

```D
interface IPackages {
@safe:

	@method(HTTPMethod.GET)
	SearchResult[] search(string q = "");

	@path(":name/latest")
	string getLatestVersion(string _name);

	@path(":name/stats")
	DbPackageStats getStats(string _name);

	@path(":name/:version/stats")
	DownloadStats getStats(string _name, string _version);

	@path(":name/info")
	Json getInfo(string _name);

	@path(":name/:version/info")
	Json getInfo(string _name, string _version);
}
```

This package requires _Apache HTTP Client v4.5.*_, _Gson v2.8_, and uses _slf4j-api_ for logging. It's intended for use in the [Intellij-DUB](https://github.com/intellij-dlanguage/intellij-dub) plugin but may also be helpful to other projects.
