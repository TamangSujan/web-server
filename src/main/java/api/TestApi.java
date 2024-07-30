package api;

import annotations.Api;
import annotations.ApiPathVariable;
import annotations.ApiRequestParam;
import annotations.GetApi;
import http.response.ResponseEntity;

@Api(name="Test")
public class TestApi {
    @GetApi(url="/test")
    public ResponseEntity getTest(@ApiRequestParam(key = "name") String name, @ApiRequestParam(key="age") String age) {
        return new ResponseEntity(200, "Success", "Hello, "+name+"!, You're age is "+age);
    }

    @GetApi(url="/test/{id}/{name}")
    public ResponseEntity getTestPathVariable(@ApiPathVariable(name = "name") String name, @ApiPathVariable(name="id") String id) {
        return new ResponseEntity(200, "Success", "Hello, "+name+"!, You're age is "+id);
    }
}
