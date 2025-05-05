package org.grnet.cat.services.arcc;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.grnet.cat.dtos.ArccValidationRequest;
import org.grnet.cat.dtos.ArccValidationResponse;
import org.grnet.cat.dtos.ArccValidationResult;
import org.grnet.cat.dtos.AutomatedTestResponse;
import org.grnet.cat.dtos.AutomatedTestStatus;
import org.grnet.cat.services.arcc.g069.NacoClient;
import org.grnet.cat.validators.XmlMetadataValidator.GeneralMetadataValidator;
import org.grnet.cat.validators.XmlMetadataValidator.XmlSchemaValidator;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

@ApplicationScoped
public class ArccValidationService {

    @Inject
    XmlSchemaValidator xmlSchemaValidator;

    @Inject
    GeneralMetadataValidator metadataValidator;

    @RestClient
    NacoClient nacoClient;

    @ConfigProperty(name = "naco.service.key")
    String SERVICE_KEY;

    // Define the regex pattern for AARC-G069 entitlements
    private static final String AARC_G069_REGEX = "^([a-zA-Z0-9][a-zA-Z0-9:._-]*):group:([a-zA-Z0-9._-]+(?:[:][a-zA-Z0-9._-]+)*)(:role=[a-zA-Z0-9._-]+)?(#.+)?$";
    private static final Pattern PATTERN = Pattern.compile(AARC_G069_REGEX);


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

    public Set<String> getAarcG069Entries(){

        var entries = nacoClient.getEntries(SERVICE_KEY);

        return entries.keySet();

    }

    public AutomatedTestResponse validateAarcG069(String aaiProviderId){

        var entitlementsInUserInfo = new ArccValidationResult();

        var entitlementsInIntrospection = new ArccValidationResult();

        var response = nacoClient.getEntry(aaiProviderId, SERVICE_KEY);

        if(Objects.isNull(response.getIntrospectionInfo())){

            entitlementsInIntrospection.isValid = false;
            entitlementsInIntrospection.message = "introspection_info section is missing from the response.";

        } else if (Objects.isNull(response.getIntrospectionInfo().getEntitlements())){

            entitlementsInIntrospection.isValid = false;
            entitlementsInIntrospection.message = "entitlements claim is missing from the Token Introspection response.";
        } else{

            if(response.getIntrospectionInfo().getEntitlements().stream().anyMatch(this::isValidEntitlement)){

                entitlementsInIntrospection.isValid = true;
                entitlementsInIntrospection.message = "entitlements found in introspection_info, and at least one entitlement follows the expected URN format (AARC-G069).";
            } else {

                entitlementsInIntrospection.isValid = false;
                entitlementsInIntrospection.message = "entitlements found in introspection_info, but none follow the expected URN format (AARC-G069).";
            }
        }

        if(Objects.isNull(response.getUserInfo())){

            entitlementsInUserInfo.isValid = false;
            entitlementsInUserInfo.message = "user_info section is missing from the response.";

        } else if (Objects.isNull(response.getUserInfo().getEntitlements())){

            entitlementsInUserInfo.isValid = false;
            entitlementsInUserInfo.message = "entitlements claim is missing from the UserInfo response.";
        } else{

            if(response.getUserInfo().getEntitlements().stream().anyMatch(this::isValidEntitlement)){

                entitlementsInUserInfo.isValid = true;
                entitlementsInUserInfo.message = "entitlements found in user_info, and at least one entitlement follows the expected URN format (AARC-G069).";
            } else {

                entitlementsInUserInfo.isValid = false;
                entitlementsInUserInfo.message = "entitlements found in user_info, but none follow the expected URN format (AARC-G069).";
            }
        }

        var arccResponse = new AutomatedTestResponse();

        arccResponse.additionalInfo.put("entitlements_in_user_info", entitlementsInUserInfo);
        arccResponse.additionalInfo.put("entitlements_in_introspection", entitlementsInIntrospection);

        var status = new AutomatedTestStatus();
        status.message = "All validations were executed successfully during the test run.";

        if(entitlementsInUserInfo.isValid && entitlementsInIntrospection.isValid){

            status.isValid = true;

        } else{

            status.isValid = false;
        }

        arccResponse.testStatus = status;

        return arccResponse;
    }

    private boolean isValidEntitlement(String value) {

        var matcher = PATTERN.matcher(value);
        return matcher.matches();
    }
}
