package com.example.animalcrash.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Communicating with the API through HttpRequests
 *              GET, POST, PUT methods
 *
 */

public class HttpRequest {

    // API URL
    private final String URL = "http://:5000/api/v1/";
    private final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    // API Authentication credentials
    private final String AUTH_USERNAME = "";
    private final String AUTH_PASSWORD = "";
    public final static String AUTH_CREDENTIALS = "";

    private String routeURL;
    private boolean authenticationRequired;
    private OkHttpClient httpClient;
    private String authToken;
    private Request request;

    /**
     * Initialize HTTP request
     *
     * @param routeURL String           API URL
     */
    public HttpRequest(String routeURL) {
        this.httpClient = new OkHttpClient();
        this.routeURL = routeURL;
        this.authenticationRequired = false;
    }

    /**
     * Initialize HTTP request
     *
     * @param routeURL String                   API URL
     * @param authenticationRequired boolean    Authentication required to access URL
     */
    public HttpRequest(String routeURL, boolean authenticationRequired) {
        this.httpClient = new OkHttpClient();
        this.routeURL = routeURL;
        this.authenticationRequired = authenticationRequired;

        if (this.authenticationRequired) {
            try {
                generateToken();
            } catch (ResponseCodeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get auth token
     *
     * @return authToken String
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Set auth token
     *
     * @param token String      Token
     */
    public void setAuthToken(String token) {
        this.authToken = token;
    }

    /**
     * Generate auth token and assign it
     *
     * @throws IOException
     */
    private void generateToken() throws ResponseCodeException {
        Headers headers = new Headers.Builder()
                .add("AUTH_USER", AUTH_USERNAME)
                .add("AUTH_PASS", AUTH_PASSWORD)
                .build();

        Request request = new Request.Builder()
                .url(URL + "auth")
                .headers(headers)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new ResponseCodeException(
                        "API Request Exception: Error occured while trying to retrieve an authentication - [" + response + "]");

            String dataResponse = response.body().string();

            try {
                JSONObject jsonObject = new JSONObject(dataResponse);

                setAuthToken(jsonObject.getString("token"));
            } catch (JSONException e) {
                throw new ResponseCodeException(
                        "JSON Exception: Error occured while generating an authentication token - [" + e.getMessage() + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * HTTP Get request
     *
     * @param multipleDataResponse boolean      Retrieve multiple data
     * @return dataResult JSONObject
     */
    public JSONObject getRequest(boolean multipleDataResponse) {
        JSONObject dataResult = new JSONObject();

        if (this.authenticationRequired) {
            request = new Request.Builder()
                    .url(URL + this.routeURL + "?token=" + getAuthToken())
                    .build();
        } else {
            request = new Request.Builder()
                    .url(URL  + this.routeURL)
                    .build();
        }

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                httpResponse(dataResult, response);

                return dataResult;
            }

            try {
                JSONArray jsonArray = new JSONArray(response.body().string());

                if (multipleDataResponse) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        dataResult.put(i + "", jsonArray.getJSONObject(i));
                    }
                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        dataResult = jsonArray.getJSONObject(i);
                    }
                }

                httpResponse(dataResult, response);
            } catch (JSONException e) {
                throw new ResponseCodeException("JSON Exception: Error occured while fetching data - [" + e.getMessage() + "]");
            }
        } catch (Exception ex) {
            System.out.println("API Request Exception: Error occured while trying to fetch data - [" + ex.getMessage() + "]");
        }

        return dataResult;
    }

    /**
     * HTTP Get (GET) request with header data
     *
     * @param headerData HashMap<S, S>      Header data
     * @param multipleDataResponse boolean  Retrieve multiple data
     * @return dataResult JSONObject
     */
    public JSONObject getRequestWithHeaders(HashMap<String, String> headerData, boolean multipleDataResponse) {
        JSONObject dataResult = new JSONObject();
        Headers headers = addHeaders(headerData);

        if (this.authenticationRequired) {
            request = new Request.Builder()
                    .url(URL + this.routeURL + "?token=" + getAuthToken())
                    .headers(headers)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(URL  + this.routeURL)
                    .headers(headers)
                    .build();
        }

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                httpResponse(dataResult, response);

                return dataResult;
            }

            try {
                JSONArray jsonArray = new JSONArray(response.body().string());

                if (multipleDataResponse) {
                    // Save every value into JSON object
                    for (int i = 0; i < jsonArray.length(); i++) {
                        dataResult.put(i + "", jsonArray.getJSONObject(i));
                    }
                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        dataResult = jsonArray.getJSONObject(i);
                    }
                }

                httpResponse(dataResult, response);
            } catch (JSONException e) {
                throw new ResponseCodeException("JSON Exception: Error occured while fetching data - [" + e.getMessage() + "]");
            }
        } catch (Exception ex) {
            System.out.println("API Request Exception: Error occured while trying to fetch data - [" + ex.getMessage() + "]");
        }

        return dataResult;
    }

    /**
     * HTTP Post (INSERT) request with data body
     *
     * @param keyValue HashMap<S, S>    Key & value data
     * @return dataResult JSONObject
     */
    public JSONObject postRequest(HashMap<String, String> keyValue) {
        JSONObject dataResult = null;

        if (this.authenticationRequired) {
            request = new Request.Builder()
                    .url(URL + this.routeURL + "?token=" + getAuthToken())
                    .post(bodyData(keyValue))
                    .build();
        } else {
            request = new Request.Builder()
                    .url(URL  + this.routeURL)
                    .post(bodyData(keyValue))
                    .build();
        }

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                dataResult = new JSONObject();

                httpResponse(dataResult, response);

                return dataResult;
            }

            try {
                String jsonResponse = response.body().string();
                JSONArray jsonArray = new JSONArray(jsonResponse);

                // Foreach element in json array (with response), retrieve the json object
                for (int i = 0; i < jsonArray.length(); i++) {
                    dataResult = jsonArray.getJSONObject(i);
                }
            } catch (JSONException e) {
                throw new ResponseCodeException("JSON Exception: Error occured while posting data - [" + e.getMessage() + "]");
            }
        } catch (Exception ex) {
            System.out.println("API Request Exception: Error occured while trying to post data - [" + ex.getMessage() + "]");
        }

        return dataResult;
    }

    /**
     * HTTP Put (UPDATE) request with data body
     *
     * @param keyValue HashMap<S, S>    Key & value data
     * @return dataResult JSONObject
     */
    public JSONObject putRequest(HashMap<String, String> keyValue) {
        JSONObject dataResult = null;

        if (this.authenticationRequired) {
            request = new Request.Builder()
                    .url(URL + this.routeURL + "?token=" + getAuthToken())
                    .put(bodyData(keyValue))
                    .build();
        } else {
            request = new Request.Builder()
                    .url(URL  + this.routeURL)
                    .put(bodyData(keyValue))
                    .build();
        }

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                dataResult = new JSONObject();

                httpResponse(dataResult, response);

                return dataResult;
            }

            try {
                String jsonResponse = response.body().string();
                JSONArray jsonArray = new JSONArray(jsonResponse);

                // Foreach element in json array (with response), retrieve the json object
                for (int i = 0; i < jsonArray.length(); i++) {
                    dataResult = jsonArray.getJSONObject(i);
                }
            } catch (JSONException e) {
                throw new ResponseCodeException("JSON Exception: Error occured while updating data - [" + e.getMessage() + "]");
            }
        } catch (Exception ex) {
            System.out.println("API Request Exception: Error occured while trying to update data - [" + ex.getMessage() + "]");
        }

        return dataResult;
    }

    /**
     * Set HTTP response
     *
     * @param object JSONObject     JSON object to be set
     * @param response Response     HTTP Response
     * @throws ResponseCodeException
     */
    private void httpResponse(JSONObject object, Response response) throws ResponseCodeException {
        try {
            object.put("code", response.code());
            object.put("message", response.message());
            object.put("body", response.body().string());

            JSONObject bodyResponse = new JSONObject(object.getString("body"));

            object.put("response", bodyResponse.getString("message"));
        } catch (JSONException e) {
            throw new ResponseCodeException("JSON Exception: Error occured while trying to setup body response - [" + e.getMessage() + "]");
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
    }

    /**
     * Create header data
     *
     * @param keyValue HashMap<S, S>    Key & value of header
     * @return headers Headers
     */
    private Headers addHeaders(HashMap<String, String> keyValue) {
        Headers.Builder builder = new Headers.Builder();

        for (String header : keyValue.keySet()) {
            builder.add(header, keyValue.get(header));
        }

        Headers headers = builder.build();

        return headers;
    }

    /**
     * Create request body
     *
     * @param keyValue HashMap<S, S>    Key & value
     * @return requestBody RequestBody
     */
    private RequestBody bodyData(HashMap<String, String> keyValue) {
        JSONObject jsonData = new JSONObject();

        for (String data : keyValue.keySet()) {
            try {
                jsonData.put(data, keyValue.get(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return RequestBody.create(JSON, jsonData.toString());
    }
}
