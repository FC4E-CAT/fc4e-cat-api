package org.grnet.cat.validators.XmlMetadataValidator;

import jakarta.enterprise.context.ApplicationScoped;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.SAXException;

@ApplicationScoped
public class XmlSchemaValidator {

    private static final String SAML_SCHEMA_URL = "https://docs.oasis-open.org/security/saml/v2.0/saml-schema-metadata-2.0.xsd";

    /**
     * Parses XML from the provided URL.
     *
     * @param metadataUrl The URL pointing to the XML file.
     * @return Parsed Document.
     * @throws Exception if the URL is invalid or the content is not XML.
     */
    public ParseResult parseXmlFromUrl(String metadataUrl) {
        try {
            // Initialize URL and DocumentBuilder
            var url = new URL(metadataUrl);
            var factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            var builder = factory.newDocumentBuilder();

            // Open stream and parse document
            try (InputStream inputStream = url.openStream()) {
                Document document = builder.parse(inputStream);

                // Check if the document's root element is valid
                if (document.getDocumentElement() == null) {
                    return new ParseResult("The response from the URL is not a valid XML document.", 422); // Unprocessable Entity
                }

                // Return a successful result with the parsed document
                return new ParseResult(document);
            }
        } catch (MalformedURLException e) {
            return new ParseResult("Invalid URL: " + e.getMessage(), 400); // Bad Request
        } catch (IOException e) {
            return new ParseResult("Error reading from URL: " + e.getMessage(), 404); // Not Fou
        } catch (SAXException e) {
            return new ParseResult("The XML document is not well-formed or does not conform to the required schema. Please check the structure and try again." + e.getMessage(), 422);
        } catch (ParserConfigurationException e) {
            return new ParseResult("An error occurred while configuring the XML parser. Please contact support or try again later. " + e.getMessage(), 500); // Not Fou

        }
    }

    /**
     * Validates the XML at the provided URL against the SAML schema.
     *
     * @param metadataUrl The URL of the XML file.
     * @return true if the XML is schema-compliant.
     */
    public ValidatedResponse validateSchema(String metadataUrl) {
        var validatedRespnse = new ValidatedResponse();

        try {
            var schemaUrl = new URL(SAML_SCHEMA_URL);
            var metadataXmlUrl = new URL(metadataUrl);

            var schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            var schema = schemaFactory.newSchema(schemaUrl);

            var validator = schema.newValidator();

            try (InputStream metadataStream = metadataXmlUrl.openStream()) {
                validator.validate(new javax.xml.transform.stream.StreamSource(metadataStream));
            }
            validatedRespnse.isSchemaValid = true;
            validatedRespnse.code = "SUCCESS";
        } catch (SAXException e) {
            validatedRespnse.isSchemaValid = false;
            validatedRespnse.message = "Schema validation failed: " + e.getMessage();
            validatedRespnse.code = "VALIDATION_ERROR";
        } catch (FileNotFoundException e) {
            validatedRespnse.isSchemaValid = true;
            validatedRespnse.message = "Url not found: " + e.getMessage();
            validatedRespnse.code = "NOT_FOUND_ERROR";
        } catch (Exception e) {
            validatedRespnse.isSchemaValid = true;
            validatedRespnse.message = "Error validating XML against schema: " + e.getMessage();
            validatedRespnse.code = "GENERIC_ERROR";
        }
        return validatedRespnse;

    }

    public class ValidatedResponse {
        boolean isSchemaValid;
        String message;
        String code;

        public boolean isSchemaValid() {
            return isSchemaValid;
        }

        public void setSchemaValid(boolean schemaValid) {
            isSchemaValid = schemaValid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }


    public class ParseResult {
        private Document document;
        private String message;
        private Integer code;

        // Constructors
        public ParseResult(Document document) {
            this.document = document;
            this.message = "Parsing successful";
            this.code = 200; // HTTP OK
        }

        public ParseResult(String message, Integer code) {
            this.message = message;
            this.code = code;
        }

        // Getters and setters
        public Document getDocument() {
            return document;
        }

        public void setDocument(Document document) {
            this.document = document;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "ParseResult{document=" + (document != null ? "exists" : "null") +
                    ", message='" + message + '\'' +
                    ", code=" + code + '}';
        }
    }
}
