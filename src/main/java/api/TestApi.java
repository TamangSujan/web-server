package api;

import annotations.Api;
import annotations.ApiRequestParam;
import annotations.GetApi;
import http.response.ResponseEntity;

@Api(name="Test")
public class TestApi {
    @GetApi(url="/test")
    public ResponseEntity getTest(@ApiRequestParam(key = "name") String name, @ApiRequestParam(key="age") String age) {
        return new ResponseEntity(200, "Success", "Hello, "+name+"!, You're age is "+age);
    }
}
