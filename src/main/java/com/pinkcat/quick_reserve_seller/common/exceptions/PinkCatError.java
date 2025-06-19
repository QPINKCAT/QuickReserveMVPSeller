package com.pinkcat.quick_reserve_seller.common.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PinkCatError implements Serializable {

    private final String path;
    private final int code;
    private final String message;

    @JsonCreator
    public PinkCatError(
            @JsonProperty("path") String path,
            @JsonProperty("code") int code,
            @JsonProperty("message") String message
    ) {
        this.path = path;
        this.code = code;
        this.message = message;
    }
}