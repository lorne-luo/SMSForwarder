/*
 * TelstraSMSAUSLib
 *
 * This file was automatically generated by APIMATIC BETA v2.0 on 05/16/2016
 */
package api.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class MessageResponse 
        extends java.util.Observable
        implements java.io.Serializable {
    private static final long serialVersionUID = 4839313085596459545L;
    private String acknowledgedTimestamp;
    private String content;
    private String from;
    /** GETTER
     * TODO: Write general description for this method
     */
    @JsonGetter("acknowledgedTimestamp")
    public String getAcknowledgedTimestamp ( ) {
        return this.acknowledgedTimestamp;
    }
    
    /** SETTER
     * TODO: Write general description for this method
     */
    @JsonSetter("acknowledgedTimestamp")
    public void setAcknowledgedTimestamp (String value) {
        this.acknowledgedTimestamp = value;
        notifyObservers(this.acknowledgedTimestamp);
    }
 
    /** GETTER
     * TODO: Write general description for this method
     */
    @JsonGetter("content")
    public String getContent ( ) {
        return this.content;
    }
    
    /** SETTER
     * TODO: Write general description for this method
     */
    @JsonSetter("content")
    public void setContent (String value) {
        this.content = value;
        notifyObservers(this.content);
    }
 
    /** GETTER
     * TODO: Write general description for this method
     */
    @JsonGetter("from")
    public String getFrom ( ) {
        return this.from;
    }
    
    /** SETTER
     * TODO: Write general description for this method
     */
    @JsonSetter("from")
    public void setFrom (String value) {
        this.from = value;
        notifyObservers(this.from);
    }
 
}
 