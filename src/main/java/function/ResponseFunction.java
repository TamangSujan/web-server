package function;

import http.response.ResponseEntity;

@FunctionalInterface
public interface ResponseFunction {
    ResponseEntity getResponse();
}
