/*
 * TelstraSMSAUSLib
 *
 * This file was automatically generated by APIMATIC BETA v2.0 on 05/16/2016
 */
package api.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SendMessageRequest 
        extends java.util.Observable
        implements java.io.Serializable {
    private static final long serialVersionUID = 5744885408507550075L;
    private String body;
    private String to;
    /** GETTER
     * TODO: Write general description for this method
     */
    @JsonGetter("body")
    public String getBody ( ) {
        return this.body;
    }
    
    /** SETTER
     * TODO: Write general description for this method
     */
    @JsonSetter("body")
    public void setBody (String value) {
        this.body = value;
        notifyObservers(this.body);
    }
 
    /** GETTER
     * TODO: Write general description for this method
     */
    @JsonGetter("to")
    public String getTo ( ) {
        return this.to;
    }
    
    /** SETTER
     * TODO: Write general description for this method
     */
    @JsonSetter("to")
    public void setTo (String value) {
        this.to = value;
        notifyObservers(this.to);
    }
 
}
 