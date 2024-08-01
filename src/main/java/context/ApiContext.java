package context;

import annotations.*;
import http.constant.HttpContentType;
import http.request.WebHttpRequest;
import http.request.body.WebHttpRequestBodyHandler;
import http.request.body.WebHttpRequestFormBodyHandler;
import http.response.ResponseEntity;
import utils.clazz.ClassHandler;
import utils.string.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiContext {
    private final Map<String, Map<String, List<Object>>> apiContextMap = new HashMap<>();
    private static ApiContext apiContext;
    private Map<String, WebHttpRequestBodyHandler> requestBodyHandlerType;
    private ApiContext(){
        List<Class<?>> classes = null;
        try {
            classes = ClassHandler.getAllClassFromProject("api");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadApis(classes);
        loadRequestBodyHandlers();
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

    private void loadRequestBodyHandlers(){
        requestBodyHandlerType = new HashMap<>();
        requestBodyHandlerType.put(HttpContentType.HTML, new WebHttpRequestFormBodyHandler());
        requestBodyHandlerType.put(HttpContentType.JSON, new WebHttpRequestFormBodyHandler());
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
        Map<String, List<Object>> apiAnnotationUrlsKeyMap = apiContextMap.get(request.getMethod());
        String matchedKey = getMatchedAnnotationUrl(apiAnnotationUrlsKeyMap, request.getServerPath());
        if(matchedKey == null){
            System.err.println("No such url: "+request.getServerPath());
            return new ResponseEntity(404, "Error", "No such url: "+request.getServerPath());
        }
        List<Object> classMethod  = apiAnnotationUrlsKeyMap.get(matchedKey);
        Method method = (Method) classMethod.get(1);
        Object[] attributeValues = getAttributeValuesFromWebRequest(method.getParameters(), request, matchedKey);
        try {
            return (ResponseEntity) method.invoke(classMethod.getFirst(), attributeValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMatchedAnnotationUrl(Map<String, List<Object>> apiAnnotationUrlMap, String requestUrl){
        for(String apiUrl: apiAnnotationUrlMap.keySet()){
            if(!isUrlSlashesEqual(apiUrl, requestUrl))
                continue;
            String originalApiUrl = apiUrl;
            String tempRequestUrl = requestUrl;
            while(apiUrl.contains("/{")){
                int startIndex = apiUrl.indexOf("/{");
                int endIndex = apiUrl.indexOf("}");
                apiUrl = apiUrl.substring(0, startIndex) + "/v" + apiUrl.substring(endIndex+1);
                String lastPart = tempRequestUrl.substring(startIndex + 1);
                if(lastPart.contains("/"))
                    tempRequestUrl = tempRequestUrl.substring(0, startIndex) + "/v" + lastPart.substring(lastPart.indexOf("/"));
                else
                    tempRequestUrl = tempRequestUrl.substring(0, startIndex) + "/v";
            }
            if(apiUrl.equals(tempRequestUrl))
                return originalApiUrl;
        }
        return null;
    }

    private boolean isUrlSlashesEqual(String url1, String url2){
        return StringUtils.getCharCount(url1, '/') == StringUtils.getCharCount(url2, '/');
    }

    private Object[] getAttributeValuesFromWebRequest(Parameter[] parameters, WebHttpRequest request, String apiAnnotationUrl){
        Object[] attributeValues = new Object[parameters.length];
        Map<String, String> pathVariables = getPathVariables(apiAnnotationUrl, request.getServerPath());
        for(int index=0; index< parameters.length; index++){
            if(parameters[index].getAnnotation(ApiRequestParam.class)!=null) {
                attributeValues[index] = request.getParameters().get(parameters[index].getAnnotation(ApiRequestParam.class).key());
                continue;
            }
            if(parameters[index].getAnnotation(ApiPathVariable.class)!=null){
                String pathVariableName = parameters[index].getAnnotation(ApiPathVariable.class).name();
                if(pathVariables.containsKey(pathVariableName)){
                    attributeValues[index] = pathVariables.get(pathVariableName);
                    continue;
                }
            }
            if(parameters[index].getAnnotation(ApiResponseBody.class)!=null && request.getParameters().get("Body").isEmpty()){
                Map<String, String> body = requestBodyHandlerType.get(request.getContentType()).getBody(request);
                try {
                    Class<?> clazz = (Class<?>) parameters[index].getType().getDeclaredConstructor().newInstance();
                    for(Field field: clazz.getFields()){
                        field.set(field.getName(), body.get(field.getName()));
                    }
                    attributeValues[index] = clazz;
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
            attributeValues[index] = null;
        }
        return attributeValues;
    }

    private Map<String, String> getPathVariables(String apiAnnotationUrl, String webRequestUrl){
        Map<String, String> pathVariables = new HashMap<>();
        while(apiAnnotationUrl.contains("/{")){
                int startIndex = apiAnnotationUrl.indexOf("/{");
                int endIndex = apiAnnotationUrl.indexOf("}");
                String variableName = apiAnnotationUrl.substring(startIndex+2, endIndex);
                String lastRequestUrl = webRequestUrl.substring(startIndex + 1);
                String variableValue = null;
                if(lastRequestUrl.contains("/")) {
                    variableValue = lastRequestUrl.substring(0, lastRequestUrl.indexOf("/"));
                    webRequestUrl = lastRequestUrl.substring(lastRequestUrl.indexOf("/"));
                }
                else {
                    variableValue = lastRequestUrl;
                    webRequestUrl = lastRequestUrl;
                }
                pathVariables.put(variableName, variableValue);
                apiAnnotationUrl = apiAnnotationUrl.substring(endIndex + 1);

        }
        return pathVariables;
    }
}
