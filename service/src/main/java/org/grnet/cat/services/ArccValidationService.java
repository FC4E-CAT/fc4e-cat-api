package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.grnet.cat.dtos.ArccValidationRequest;
import org.grnet.cat.dtos.ArccValidationResponse;
import org.grnet.cat.dtos.AutomatedCheckResponse;
import org.grnet.cat.entities.registry.Test;
import org.grnet.cat.repositories.registry.TestRepository;
import org.grnet.cat.validators.XmlMetadataValidator.GeneralMetadataValidator;
import org.grnet.cat.validators.XmlMetadataValidator.MetadataValidator;
import org.grnet.cat.validators.XmlMetadataValidator.XmlSchemaValidator;
import org.w3c.dom.Document;

@ApplicationScoped
public class ArccValidationService {

    @Inject
    TestRepository testRepository;

    @Inject
    XmlSchemaValidator xmlSchemaValidator;

    @Inject
    GeneralMetadataValidator metadataValidator;

    public ArccValidationResponse validateMetadataByTestId(String type, ArccValidationRequest request) {

        var isSchemaCompliant = xmlSchemaValidator.validateSchema(request.metadataUrl);
        var document = parseXmlSafely(request.metadataUrl);
        var testValidationResult = metadataValidator.validate(type, document);

        if (!testValidationResult.isCompliant) {
            throw new IllegalArgumentException(testValidationResult.feedback);
        }
        var response = new ArccValidationResponse();
        response.isValid= testValidationResult.isCompliant;
        response.code = 200;
        return response;
    }

    private Document parseXmlSafely(String metadataUrl) {
        try {
            return xmlSchemaValidator.parseXmlFromUrl(metadataUrl);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing XML: " + e.getMessage(), e);
        }
    }

}
