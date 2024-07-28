package api;

import annotations.Api;
import annotations.GetApi;
import http.response.ResponseEntity;

@Api(name="Test")
public class TestApi {
    @GetApi(url="/test")
    public ResponseEntity getTest() {
        return new ResponseEntity(200, "Success", "Hello, Sujan!");
    }
}
