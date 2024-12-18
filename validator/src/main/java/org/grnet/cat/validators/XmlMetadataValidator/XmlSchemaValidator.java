package org.grnet.cat.validators.XmlMetadataValidator;

import jakarta.enterprise.context.ApplicationScoped;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;
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
    public Document parseXmlFromUrl(String metadataUrl) throws Exception {
        var url = new URL(metadataUrl);
        var factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        var builder = factory.newDocumentBuilder();

        try (InputStream inputStream = url.openStream()) {
            Document document = builder.parse(inputStream);

            if (document.getDocumentElement() == null) {
                throw new IllegalArgumentException("The response from the URL is not a valid XML document.");
            }
            return document;
        }
    }

    /**
     * Validates the XML at the provided URL against the SAML schema.
     *
     * @param metadataUrl The URL of the XML file.
     * @return true if the XML is schema-compliant.
     */
    public boolean validateSchema(String metadataUrl) {
        try {
            var schemaUrl = new URL(SAML_SCHEMA_URL);
            var metadataXmlUrl = new URL(metadataUrl);

            var schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            var schema = schemaFactory.newSchema(schemaUrl);

            var validator = schema.newValidator();

            try (InputStream metadataStream = metadataXmlUrl.openStream()) {
                validator.validate(new javax.xml.transform.stream.StreamSource(metadataStream));
            }

            return true;
        } catch (SAXException e) {
            throw new IllegalArgumentException("Schema validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error validating XML against schema: " + e.getMessage(), e);
        }
    }
}
