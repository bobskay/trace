package com.xxx.dto;

import com.xxx.response.ApiResponse;
import lombok.Data;

@Data
public class CancelAll extends ApiResponse {
    /**
     * code : 200
     * msg : The operation of cancel all open order is done.
     */
    private Integer code;
    private String msg;
}

