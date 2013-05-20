RxJava extension for VertX
=============================
This is an extension for [VertX](http://vertx.io) that provides support for [Reactive Extensions](https://rx.codeplex.com/) (RX) using the [RxJava](https://github.com/Netflix/RxJava/wiki) library

This allows VertX developers to use the RxJava type-safe composable API to build VertX verticles

## Dependencies

- The module wraps the VertX core objects to add Observable support so it is tightly bound to the VertX release. 
- This module also contains the Netflix RxJava library.
- The module can be installed as a non-running module or built as a standalone jar and included in other modules.

## Name
The module name is `com.meez.mod-rxjava`.

## Status
The module is for use with VertX 1.3.1.final only.

For VertX 2.0.0 use the [mod-rxvertx](https://github.com/vert-x/mod-rxvertx) module instead.

Currently Observable wrappers are provided for

- EventBus
- FileSystem
- HttpServer
- HttpClient
- NetServer
- NetClient

There are also base Observable adapters that map Handler<T> and AsyncResultHandler<T> to Observable<T> that can be used to call other Handler based APIs.

In future, additional wrappers will be provided for

- Timer 
- SockJSServer

## Usage

### Observable wrappers

To access the Rx methods you just wrap the existing Vertx instance with an instance of `meez.rxvertx.java.RxVertx`. The methods of `RxVertx` will return the appropriately wrapper subsystem.

e.g. to send a message to the eventbus

```java
RxVertx rxVertx=new RxVertx(vertx);

Observable<String> req=rxVertx.eventBus().sendRx("foo");

req
  .subscribe(new Action1<String>(){
    public void call(String resp) {
      System.out.println("got response");
    }
  })
```

All standard API methods of the form 

```java
void method(args...,Handler<T> handler)
```

are available in the form

```java
Observable<T> method(args...)
```

### Helper ###
The support class `RxSupport` provides several helper methods for some standard tasks

#### Codec ####
There are several Func1 codec methods to go from `Buffer` to `JsonObject` and `Buffer` to a Java Pojo (using `ObjectMapper`)

#### Streams ####
There are two primary wrappers

##### Observable<Buffer> RxSupport.toObservable(ReadStream) ####
Convert a `ReadStream` into an `Observable<Buffer>`

##### RxSupport.stream(Observable<Buffer>,WriteStream) ####
Stream the output of an `Observable` to a `WriteStream`.

_please note that this method does not handle `writeQueueFull` so cannot be used as a pump_

### Pipelines ###
The real power of RxJava comes from composing asynchronous flows as part of a workflow. `mod-rxjava` provides several pipeline helpers to enable building handler pipelines.

eg. building a Json HttpServer

```java

RxVertx rx=new RxVertx(vertx);

// Create a new HttpServerPipeline that takes Json requests
server=rx.createHttpServer().requestHandler(new HttpServerPipeline<JsonObject>() {
  // Request pipeline for JsonObject request
  public Observable<JsonObject> process(Observable<HttpServerRequest> request) {
    
    return request
      // Fetch the request body into a Json Object
      .flatMap(RxHttpSupport.downloadJson())
      // Simple pong responder
      .map(new Func1<JsonObject,JsonObject>() {
        public JsonObject call(JsonObject in) {
          // Handle the request
          // Return the response as a JsonObject (pipeline will encode as Json)
          return new JsonObject()...;
        }
      });
  }
}).listen(8080);

```