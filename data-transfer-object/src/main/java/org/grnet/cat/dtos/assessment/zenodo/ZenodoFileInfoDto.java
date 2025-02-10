package org.grnet.cat.dtos.assessment.zenodo;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ZenodoFileInfoDto", description = "Response DTO for files uploaded to deposit,existing in Zenodo")
@Getter
@Setter
public class ZenodoFileInfoDto {
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "File name  of the file uploaded to deposit ",
            example = "test file"
    )
    private String fileName;
    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "File size in bytes  of the file uploaded to deposit ",
            example = "20394"
    )
    private long fileSize;
    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "File url  of the file uploaded to deposit ",
            example = "this is a test"
    )
    private String fileUrl;
}