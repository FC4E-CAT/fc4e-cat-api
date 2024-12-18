package org.grnet.cat.validators.XmlMetadataValidator;

import org.w3c.dom.Document;

public interface MetadataValidator {
    ValidationResult validate(String metadataUrl, Document document);

    class ValidationResult {
        public final boolean isCompliant;
        public final String feedback;

        public ValidationResult(boolean isCompliant, String feedback) {
            this.isCompliant = isCompliant;
            this.feedback = feedback;
        }
    }
}
