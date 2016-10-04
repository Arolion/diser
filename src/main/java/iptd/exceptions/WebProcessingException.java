package iptd.exceptions;

import iptd.web.WebProcessing;

/**
 * Created by Arol on 18.09.2016.
 */
public class WebProcessingException extends Exception {

    private String errorMsg;

    private Throwable exception = null;

    public WebProcessingException(String msg){
        this.errorMsg = msg;
    }

    public WebProcessingException(String msg, Throwable ex){
        this.errorMsg = msg;
        this.exception = ex;
        //1
    }

    public Throwable getException(){
        return  this.exception;
    }

    @Override
    public String toString(){
        return errorMsg;
    }
}
