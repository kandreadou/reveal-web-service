package gr.iti.mklab.reveal.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.socialsensor.framework.common.domain.JSONable;

import java.io.Serializable;

/**
 * Created by kandreadou on 7/30/14.
 */
public class IndexingResult implements JSONable, Serializable {

    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("success")
    protected boolean success = false;

    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("message")
    protected String message;

    public IndexingResult(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public IndexingResult(){
        this.success = true;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }

    @Override
    public String toJSONString() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
