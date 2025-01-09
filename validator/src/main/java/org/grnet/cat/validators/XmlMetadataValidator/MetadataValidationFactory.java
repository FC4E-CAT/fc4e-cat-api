package org.grnet.cat.validators.XmlMetadataValidator;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.enums.ArccTestType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@ApplicationScoped
public class MetadataValidationFactory {

    private final Map<String, Map<String, String>> validatorAttributes = new HashMap<>();
    private final Map<String, BiFunction<Map<String, String>, Document, MetadataValidator.ValidationResult>> validationRules = new HashMap<>();

    public MetadataValidationFactory() {
        //MD-1a | Administrative Contact Details
        validatorAttributes.put(ArccTestType.MD1a.getType(), Map.of(
                "namespace", "urn:oasis:names:tc:SAML:2.0:metadata",
                "contactType", "administrative",
                "emailAddressTag", "EmailAddress"
        ));
        // MD-1b1 | Operational Security Contact Email
        validatorAttributes.put(ArccTestType.MD1b1.getType(), Map.of(
                "namespace", "urn:oasis:names:tc:SAML:2.0:metadata",
                "contactType", "other",
                "remdContactType", "http://refeds.org/metadata/contactType/security",
                "emailAddressTag", "EmailAddress"
        ));
        // MD-1b2 | Operational Security Contact Phone Number
        validatorAttributes.put(ArccTestType.MD1b2.getType(), Map.of(
                "namespace", "urn:oasis:names:tc:SAML:2.0:metadata",
                "contactType", "other",
                "telephoneNumberTag", "TelephoneNumber"
        ));
        validationRules.put(ArccTestType.MD1a.getType(), this::validateMd1a);
        validationRules.put(ArccTestType.MD1b1.getType(), this::validateMd1b1);
        validationRules.put(ArccTestType.MD1b2.getType(), this::validateMd1b2);
    }

    public Map<String, String> getAttributes(String testId) {
        return validatorAttributes.getOrDefault(testId, Map.of());
    }

    public BiFunction<Map<String, String>, Document, MetadataValidator.ValidationResult> getValidationRule(String testId) {
        return validationRules.getOrDefault(testId, (attributes, document) ->
                new MetadataValidator.ValidationResult(false, "Unsupported test: " + testId));
    }
    private MetadataValidator.ValidationResult validateMd1a(Map<String, String> attributes, Document document) {
        return validateContactPerson(document, attributes, "MD-1a validation passed.",
                "MD-1a validation failed: No administrative EmailAddress found.");
    }

    private MetadataValidator.ValidationResult validateMd1b1(Map<String, String> attributes, Document document) {
        return validateContactPerson(document, attributes, "MD-1b1 validation passed.",
                "MD-1b1 validation failed: No operational security EmailAddress found.");
    }

    private MetadataValidator.ValidationResult validateMd1b2(Map<String, String> attributes, Document document) {
        return validateContactPerson(document, attributes, "MD-1b2 validation passed.",
                "MD-1b2 validation failed: No operational security TelephoneNumber found.");
    }

    private MetadataValidator.ValidationResult validateContactPerson(Document document, Map<String, String> attributes, String successMessage, String failureMessage) {
        var contactPersons = document.getElementsByTagNameNS(attributes.get("namespace"), "ContactPerson");
        for (int i = 0; i < contactPersons.getLength(); i++) {
            var contactPerson = (Element) contactPersons.item(i);

            if (attributes.get("contactType").equals(contactPerson.getAttribute("contactType"))) {
                if (attributes.containsKey("remdContactType") &&
                        !attributes.get("remdContactType").equals(contactPerson.getAttribute("remd:contactType"))) {
                    continue;
                }
                var tagName = attributes.getOrDefault("emailAddressTag", attributes.getOrDefault("telephoneNumberTag", ""));
                var elements = contactPerson.getElementsByTagNameNS(attributes.get("namespace"), tagName);
                if (elements.getLength() > 0) {
                    return new MetadataValidator.ValidationResult(true, successMessage);
                }
            }
        }
        return new MetadataValidator.ValidationResult(false, failureMessage);
    }
}
