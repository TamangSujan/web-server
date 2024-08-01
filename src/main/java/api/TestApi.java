package api;

import annotations.*;
import http.response.ResponseEntity;
import model.TestFormBody;
import page.WebPageHandler;

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

    @GetApi(url = "/test/form")
    public ResponseEntity getTestForm(){
        return new ResponseEntity(200, "Success", WebPageHandler.getWebPage("none"));
    }

    @PostApi(url = "/test/form")
    public ResponseEntity processTestForm(@ApiResponseBody() TestFormBody body){
        return new ResponseEntity(200, "Success", "Your username is "+body.getUsername()+" and password is "+body.getPassword());
    }
}
