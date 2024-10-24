package org.grnet.cat.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.quarkus.oidc.TokenIntrospection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import lombok.SneakyThrows;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.grnet.cat.dtos.template.TemplateDto;
import org.grnet.cat.entities.registry.RegistryActor;
import org.grnet.cat.repositories.registry.RegistryActorRepository;

import java.util.stream.Collectors;

@ApplicationScoped
public class Utility {

    /**
     * Injection point for the Token Introspection
     */
    @Inject
    TokenIntrospection tokenIntrospection;

    @ConfigProperty(name = "api.oidc.user.unique.id")
    String key;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    RegistryActorRepository registryActorRepository;

    public String getUserUniqueIdentifier(){

        String id;

        try{

            id = tokenIntrospection.getJsonObject().getString(key);
        } catch (Exception e){

            String message = String.format("The User's unique identifier {%s} is missing from the access token.", key);
            throw new BadRequestException(message);
        }

        return id;
    }

    /**
     * Validates a JSON string against a JSON schema.
     * @param jsonSchema A string containing the JSON schema against which the JSON will be validated.
     * @param template A string containing the JSON template to be validated.
     * @param version A version represents the set of keywords and semantics that can be used to evaluate a schema
     * @throws  org.grnet.cat.exceptions.BadRequestException If there is a validation error.
     */
    @SneakyThrows
    public void validateTemplateJson(String jsonSchema, TemplateDto template, SpecVersion.VersionFlag version){

        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(version);
        var schema = schemaFactory.getSchema(jsonSchema);
        var json = objectMapper.readTree(objectMapper.writeValueAsString(template));
        var messages = schema.validate(json);

        if(!messages.isEmpty()){
            throw new org.grnet.cat.exceptions.BadRequestException("Json validation error.", messages.stream().map(ValidationMessage::getMessage).collect(Collectors.toSet()));
        }
    }

    public RegistryActor transformActorToRegistryActor(String name){


        if(name.equals("PID Scheme")){

            return registryActorRepository.findById("pid_graph:0E00C332");
        } else if (name.equals("PID Authority")) {

            return registryActorRepository.findById("pid_graph:1A718108");
        } else if (name.equals("End User")) {

            return registryActorRepository.findById("pid_graph:20A7A125");
        } else if (name.equals("Compliance Monitoring")) {

            return registryActorRepository.findById("pid_graph:234B60D8");
        } else if (name.equals("PID Owner")) {

            return registryActorRepository.findById("pid_graph:B5CC396B");
        } else if (name.equals("PID Manager")) {

            return registryActorRepository.findById("pid_graph:D42428D7");
        } else if (name.equals("PID Service Provider")) {

            return registryActorRepository.findById("pid_graph:E92B9B49");
        } else if (name.equals("Multi-Primary Administrator")) {

            return registryActorRepository.findById("pid_graph:7835EF43");
        } else {

            return registryActorRepository.findById("pid_graph:566C01F6");
        }
    }
}
