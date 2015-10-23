package com.musasyihab.timeline.service;
import java.util.HashMap;
import java.util.Map;

public class ResponseData {
    private String MethodName;
    private String Result;
    private String ResultDesc;
    private String SessionID;
    private Map<String,Object> Response = new HashMap<String,Object>();

    public ResponseData(){

    }

    public ResponseData(String methodName, String result, String resultDesc, String sessionID, Map<String, Object> response) {
        MethodName = methodName;
        Result = result;
        ResultDesc = resultDesc;
        SessionID = sessionID;
        Response = response;
    }

    public String getMethodName() { return MethodName; }

    public void setMethodName(String methodName) { MethodName = methodName; }

    public String getResult() { return Result; }

    public void setResult(String result) { Result = result; }

    public String getResultDesc() { return ResultDesc; }

    public void setResultDesc(String resultDesc) { ResultDesc = resultDesc; }

    public String getSessionID() { return SessionID; }

    public void setSessionID(String sessionID) { SessionID = sessionID; }

    public Map<String, Object> getResponse() { return Response; }

    public void setResponse(Map<String, Object> response) { Response = response; }
}
