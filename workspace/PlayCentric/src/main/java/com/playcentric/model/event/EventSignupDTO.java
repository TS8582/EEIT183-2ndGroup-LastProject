package com.playcentric.model.event;

import lombok.Data;

@Data
public class EventSignupDTO {
    private Integer signupId;
    private String workTitle;
    private String workDescription;
    private Integer voteCount;
    private String workImageBase64;
}