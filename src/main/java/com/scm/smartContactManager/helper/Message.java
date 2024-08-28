package com.scm.smartContactManager.helper;

import com.scm.smartContactManager.constants.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private String message;
    @Builder.Default
    private MessageType messageType = MessageType.blue;

}
