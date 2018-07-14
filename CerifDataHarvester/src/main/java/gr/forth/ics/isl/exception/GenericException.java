package gr.forth.ics.isl.exception;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class GenericException extends Exception{
    public GenericException(String msg){
        super(msg);
    }
    
    public GenericException(String msg, Throwable thr){
        super(msg,thr);
    }
}