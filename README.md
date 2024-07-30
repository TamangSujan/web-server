## JWeb-Server

JWeb-Server is a fun project created in order to enhances my knowledge on how web works with http.

### Features

- **@Api**(name='Test'): This annotation is used to mark the class in order to scan api method.
- **@GetApi**(url='/test'): This annotation is used to mark the method to be used as api.
- **@ApiRequestParam**(key='param'): This annotation is used along with method parameter in order to map query key-value pair.
      Example: /test?name=sujan will be mapped with any api function(@ApiRequestParam(key="name") String someName)
- **@ApiPathVariable**(name='urlPath'): This annotation is used along with method paramater in order to map url value.
      Example: /test/12 will be mapped to id of @GetApi("/test/{id}") and value can be retrieve on function(@ApiPathVariiable(name="id") String someId)