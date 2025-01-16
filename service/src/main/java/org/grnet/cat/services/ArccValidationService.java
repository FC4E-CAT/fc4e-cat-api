package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.grnet.cat.dtos.ArccValidationRequest;
import org.grnet.cat.dtos.ArccValidationResponse;
import org.grnet.cat.validators.XmlMetadataValidator.GeneralMetadataValidator;
import org.grnet.cat.validators.XmlMetadataValidator.XmlSchemaValidator;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class ArccValidationService {

    @Inject
    XmlSchemaValidator xmlSchemaValidator;

    @Inject
    GeneralMetadataValidator metadataValidator;

    public ArccValidationResponse validateMetadataByTestType(String type, ArccValidationRequest request) {

        var validatedResponse = xmlSchemaValidator.validateSchema(request.metadataUrl);
        var response = new ArccValidationResponse();
        response.lastRun = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(ZonedDateTime.now());

        if (!validatedResponse.isSchemaValid()) {
            response.isValid = false;
            switch (validatedResponse.getCode()) {
                case "VALIDATION_ERROR":
                    response.code = 422;
                    break;
                case "NOT_FOUND_ERROR":
                    response.code = 404;
                    break;
                case "GENERIC_ERROR":
                default:
                    response.code = 500;

            }
            response.message = validatedResponse.getMessage();
            return response;
        }
        var documentResponse = xmlSchemaValidator.parseXmlFromUrl(request.metadataUrl);
        if (documentResponse.getDocument() == null) {
            response.isValid = false;
            response.message = documentResponse.getMessage();
            response.code = documentResponse.getCode();
            return response;
        }
        var testValidationResult = metadataValidator.validate(type, documentResponse.getDocument());

        if (!testValidationResult.isCompliant) {
           response.isValid=false;
           response.code=400;
           response.message=testValidationResult.feedback;
            return response;
        }
        response.isValid = testValidationResult.isCompliant;
        response.code = 200;
        response.message="Successful schema validation";
        return response;
    }


}
