DUB Client
==========

Stand-alone library to provide convenient access to the REST API of a dub repository. Deserializes the json from the API for convenient use within a JVM language such as Java or Kotlin.

`https://code.dlang.org/api/packages/search?q=`

`https://code.dlang.org/api/packages/{package}/info`

`https://code.dlang.org/api/packages/{package}/{version}/info`

`https://code.dlang.org/api/packages/{package}/stats`

`https://code.dlang.org/api/packages/{package}/{version}/stats`

`https://code.dlang.org/api/packages/{package}/latest`


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
