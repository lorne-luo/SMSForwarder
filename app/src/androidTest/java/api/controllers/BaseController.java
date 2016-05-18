/*
 * TelstraSMSAUSLib
 *
 * This file was automatically generated by APIMATIC BETA v2.0 on 05/16/2016
 */
package api.controllers;

import com.telstra.api.http.client.HttpClient;
import com.telstra.api.http.client.VolleyClient;

public abstract class BaseController {
    /**
     * Protected variable to keep reference of client instance
     */
    protected HttpClient clientInstance = null;

    /**
     * Initialize with the default http client
     */
    public BaseController() {
        clientInstance = VolleyClient.getSharedInstance();
    }

    /**
     * Initialize the base controller using the given http client
     *
     * @param _client The given http client
     */
    public BaseController(HttpClient _client) {
        clientInstance = _client;
    }
}
