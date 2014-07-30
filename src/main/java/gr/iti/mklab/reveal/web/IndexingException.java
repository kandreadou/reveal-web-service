package gr.iti.mklab.reveal.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by kandreadou on 7/30/14.
 */
@ResponseStatus(value= HttpStatus.OK, reason="Indexing Service Exception") //404
public class IndexingException extends Exception{

    public IndexingException(String message, Throwable throwable){
        super(message, throwable);
    }
}
