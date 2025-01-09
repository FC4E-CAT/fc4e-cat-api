package org.grnet.cat.validators.XmlMetadataValidator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.grnet.cat.repositories.registry.TestRepository;
import org.w3c.dom.Document;

@ApplicationScoped
public class GeneralMetadataValidator implements MetadataValidator {
    @Inject
    MetadataValidationFactory metadataValidationFactory;

    @Inject
    TestRepository testRepository;

    @Override
    public ValidationResult validate(String testId, Document document) {
        var attributes = metadataValidationFactory.getAttributes(testId);
        var validationRule = metadataValidationFactory.getValidationRule(testId);

        if (validationRule == null || attributes.isEmpty()) {
            return new ValidationResult(false, "No validation rule found for test: " + testRepository.findById(testId).getTES());
        }
        return validationRule.apply(attributes, document);
    }
}
