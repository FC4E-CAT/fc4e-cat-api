package org.grnet.cat.dtos.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.constraints.NotFoundEntity;
import org.grnet.cat.repositories.UserRepository;

@Schema(name="PermitAccess", description="This object allows access to previously banned User.")
public class PermitAccess {

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The unique identifier of the user to whom the access will be allowed.",
            example = "7827fbb605a23b0cbd5cb4db292fe6dd6c7734a27057eb163d616a6ecd02d2ec@einfra.grnet.gr"
    )
    @JsonProperty("user_id")
    @NotEmpty(message = "user_id may not be empty.")
    @NotFoundEntity(repository = UserRepository.class, message = "There is no User with the following id:")
    public String userId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            required = true,
            description = "The reason for allowing access to the user.",
            example = "We want to allow access to the user."
    )
    @JsonProperty("reason")
    @NotEmpty(message = "reason may not be empty.")
    public String reason;
}
