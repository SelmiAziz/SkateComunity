package model;

import utils.RequestRelevance;

public class CustomizedRequest {
    private String customizedDescription;
    private RequestRelevance requestRelevance;

    public CustomizedRequest(){};

    public CustomizedRequest(String customizedDescription, RequestRelevance requestRelevance){
        this.customizedDescription = customizedDescription;
        this.requestRelevance = requestRelevance;
    }

    public String getCustomizedDescription(){
        return this.customizedDescription;
    }

    public RequestRelevance getRequestRelevance(){
        return this.requestRelevance;
    }

    public void setCustomizedDescription(String customizedDescription){
        this.customizedDescription = customizedDescription;
    }

    public void setRequestRelevance(RequestRelevance requestRelevance){
        this.requestRelevance = requestRelevance;
    }
}
