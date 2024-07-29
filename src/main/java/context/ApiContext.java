package context;

import annotations.Api;
import annotations.ApiRequestParam;
import annotations.GetApi;
import annotations.PostApi;
import http.request.WebHttpRequest;
import http.response.ResponseEntity;
import utils.clazz.ClassHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiContext {
    private final Map<String, Map<String, List<Object>>> apiContextMap = new HashMap<>();
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
                apiContextMap.get("GET").put(method.getAnnotation(GetApi.class).url(), List.of(clazz.getDeclaredConstructor().newInstance(), method));
            }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored){

            }
        }
    }

    private void loadPostApi(Method method, Class<?> clazz) {
        if(method.getAnnotation(PostApi.class)!=null){
            if(!apiContextMap.containsKey("POST"))
                apiContextMap.put("POST", new HashMap<>());
            try {
                apiContextMap.get("POST").put(method.getAnnotation(PostApi.class).url(), List.of(clazz.getDeclaredConstructor().newInstance(), method));
            }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored){

            }
        }
    }

    public ResponseEntity getResponse(WebHttpRequest request){
        if(!apiContextMap.containsKey(request.getMethod())){
            System.err.println("No such method: "+request.getMethod());
            return new ResponseEntity(404, "Error", "No such method: "+request.getMethod());
        }
        if(!apiContextMap.get(request.getMethod()).containsKey(request.getServerPath())){
            System.err.println("No such url: "+request.getServerPath());
            return new ResponseEntity(404, "Error", "No such url: "+request.getServerPath());
        }
        List<Object> classMethod  = apiContextMap.get(request.getMethod()).get(request.getServerPath());
        Method method = (Method) classMethod.get(1);
        Object[] attributeValues = getAttributeValuesFromWebRequest(method.getParameters(), request);
        try {
            return (ResponseEntity) method.invoke(classMethod.getFirst(), attributeValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] getAttributeValuesFromWebRequest(Parameter[] parameters, WebHttpRequest request){
        Object[] attributeValues = new Object[parameters.length];
        for(int index=0; index< parameters.length; index++){
            if(parameters[index].getAnnotation(ApiRequestParam.class)!=null){
                attributeValues[index] = request.getParameters().get(parameters[index].getAnnotation(ApiRequestParam.class).key());
            }else{
                attributeValues[index] = null;
            }
        }
        return attributeValues;
    }
}
