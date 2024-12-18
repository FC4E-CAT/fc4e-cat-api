package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.grnet.cat.dtos.ArccValidationRequest;
import org.grnet.cat.dtos.ArccValidationResponse;
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

    public ArccValidationResponse validateMetadataByTestId(ArccValidationRequest request) {

        var test = testRepository.findById(request.testId);
        var isSchemaCompliant = xmlSchemaValidator.validateSchema(request.metadataUrl);
        var document = parseXmlSafely(request.metadataUrl);
        var testValidationResult = metadataValidator.validate(test.getId(), document);

        return constructResponse(test, isSchemaCompliant, testValidationResult);
    }

    private Document parseXmlSafely(String metadataUrl) {
        try {
            return xmlSchemaValidator.parseXmlFromUrl(metadataUrl);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing XML: " + e.getMessage(), e);
        }
    }

    private ArccValidationResponse constructResponse(Test test, boolean isSchemaCompliant, MetadataValidator.ValidationResult testResult) {
        ArccValidationResponse response = new ArccValidationResponse();
        response.isSchemaCompliant = isSchemaCompliant;
        response.isTestCompliant = testResult.isCompliant;
        response.feedback = testResult.feedback;
        response.label = test.getLabelTest();
        response.name = test.getTES();
        return response;
    }
}
