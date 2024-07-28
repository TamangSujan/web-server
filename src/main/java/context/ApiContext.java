package context;

import annotations.Api;
import annotations.GetApi;
import annotations.PostApi;
import function.ResponseFunction;
import http.request.WebHttpRequest;
import http.response.ResponseEntity;
import utils.clazz.ClassHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiContext {
    private final Map<String, Map<String, ResponseEntity>> apiContextMap = new HashMap<>();
    private static ApiContext apiContext;
    private ApiContext(){
        List<Class<?>> classes = null;
        try {
            classes = ClassHandler.getAllClassFromProject("api");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadApis(classes);
    }

    public static ApiContext context() {
        if(apiContext == null)
            apiContext = new ApiContext();
        return apiContext;
    }

    private void loadApis(List<Class<?>> classes){
        for(Class<?> clazz: classes){
            if(clazz.getAnnotation(Api.class)!=null){
                Method[] methods = clazz.getMethods();
                for(Method method: methods){
                    loadGetApi(method, clazz);
                    loadPostApi(method, clazz);
                }
            }
        }
    }

    private void loadGetApi(Method method, Class<?> clazz){
        if(method.getAnnotation(GetApi.class)!=null){
            if(!apiContextMap.containsKey("GET"))
                apiContextMap.put("GET", new HashMap<>());
            try{
                apiContextMap.get("GET").put(method.getAnnotation(GetApi.class).url(), getFunction(clazz.getDeclaredConstructor().newInstance(), method));
            }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored){

            }
        }
    }

    private void loadPostApi(Method method, Class<?> clazz) {
        if(method.getAnnotation(PostApi.class)!=null){
            if(!apiContextMap.containsKey("POST"))
                apiContextMap.put("POST", new HashMap<>());
            try {
                apiContextMap.get("POST").put(method.getAnnotation(PostApi.class).url(), getFunction(clazz.getDeclaredConstructor().newInstance(), method));
            }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored){

            }
        }
    }

    private ResponseEntity getFunction(Object clazz, Method method) {
        try {
            return (ResponseEntity) method.invoke(clazz);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity getResponse(WebHttpRequest request) {
        if(!apiContextMap.containsKey(request.getMethod())){
            System.err.println("No such method: "+request.getMethod());
            return new ResponseEntity(404, "Error", "No such method: "+request.getMethod());
        }
        if(!apiContextMap.get(request.getMethod()).containsKey(request.getServerPath())){
            System.err.println("No such url: "+request.getServerPath());
            return new ResponseEntity(404, "Error", "No such url: "+request.getServerPath());
        }
        return apiContextMap.get(request.getMethod()).get(request.getServerPath());
    }
}
