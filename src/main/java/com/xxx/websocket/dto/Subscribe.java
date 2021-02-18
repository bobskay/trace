package com.xxx.websocket.dto;

import lombok.Data;

import java.util.List;

@Data
public class Subscribe {

    private String method="SUBSCRIBE";
    private Integer id;
    private List<String> params;
}
