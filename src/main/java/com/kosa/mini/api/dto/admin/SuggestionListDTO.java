// com.kosa.mini.api.dto.admin.SuggestionListDTO.java
package com.kosa.mini.api.dto.admin;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SuggestionListDTO {

    private Integer contactId;
    private String title;
    private String storeName;
    private String content;
    private LocalDateTime createdAt;
    private Integer views;
    private Integer memberId;
    private String memberName;
}
