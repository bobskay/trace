package com.xxx.dto;

import com.xxx.response.ApiResponse;
import lombok.Data;

@Data
public class UserDataStream extends ApiResponse {
    private String listenKey;
}
