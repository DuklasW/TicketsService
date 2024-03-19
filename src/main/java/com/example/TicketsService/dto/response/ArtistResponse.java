package com.example.TicketsService.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistResponse {

    @Id
    private String id;
    @NotBlank
    private String managerId;
    @NotBlank
    private String name;
    private String surname;
    private String nickname;
    @NotBlank
    private String description;
    private Boolean isActive;
}
