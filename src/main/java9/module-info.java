module dub.client {
    requires com.google.gson;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.slf4j;
    requires static org.jetbrains.annotations;
    requires sdlang; // 2.2.0 and above support JPMS

    opens com.singingbush.dubclient.data to com.google.gson;

    exports com.singingbush.dubclient;
    exports com.singingbush.dubclient.data;
}
