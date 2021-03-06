package gr.iti.mklab.reveal.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.socialsensor.framework.common.domain.JSONable;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kandreadou on 7/30/14.
 */
public class IndexingRequest implements JSONable, Serializable {

    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("collection")
    protected String collection;

    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("urls")
    protected List<String> urls = new ArrayList<String>();

    public IndexingRequest(){

    }

    public IndexingRequest(String collection){
        this.collection = collection;
    }

    public void addUrl(String url){
        urls.add(url);
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public String toJSONString() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }

    public static void main(String[] args) throws Exception{
        IndexingRequest request = new IndexingRequest("MyNewCollection");
        request.addUrl("http://static4.businessinsider.com/image/5326130f69bedd780c549606-1200-924/putin-68.jpg");
        request.addUrl("http://www.trbimg.com/img-531a4ce6/turbine/topic-peplt007593");
        System.out.println(request.toJSONString());
    }
}
