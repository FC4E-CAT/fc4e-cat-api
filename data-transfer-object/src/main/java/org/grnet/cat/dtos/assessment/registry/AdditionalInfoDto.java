package org.grnet.cat.dtos.assessment.registry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInfoDto {
    private String timestamp;
    private String message;
}
