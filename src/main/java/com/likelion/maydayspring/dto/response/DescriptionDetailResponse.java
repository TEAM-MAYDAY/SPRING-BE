package com.likelion.maydayspring.dto.response;

import jakarta.persistence.Entity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DescriptionDetailResponse {
    private String name;
    private String phoneNumber;
    private String address;
    private String operatingTime;
    private String locationIntroduction;
    private List<String> providedDetails;
}
