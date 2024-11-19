package com.kosa.mini.api.dto.admin;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestionDetailDTO {
    private String title;
    private String storeName;
    private String content;
    private Integer memberId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isModified;
    private Integer views;
    private String memberName;
    private Integer contactId;
}
