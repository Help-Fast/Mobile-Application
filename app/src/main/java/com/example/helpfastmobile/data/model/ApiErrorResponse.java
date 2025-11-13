package com.example.helpfastmobile.data.model;

import com.google.gson.annotations.SerializedName;

// Esta classe é o JSON de erro retornado pela API.
public class ApiErrorResponse {

    @SerializedName("error")
    private String message;

    public String getMessage() {
        return message;
    }
}
