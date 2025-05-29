package org.grnet.cat.services.arcc;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServerErrorException;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.grnet.cat.dtos.AarcClaimLocation;
import org.grnet.cat.dtos.ArccValidationRequest;
import org.grnet.cat.dtos.ArccValidationResult;
import org.grnet.cat.dtos.AutomatedTestResponse;
import org.grnet.cat.dtos.AutomatedTestStatus;
import org.grnet.cat.services.arcc.g069.NacoClient;
import org.grnet.cat.services.arcc.g069.NacoEntryResponse;
import org.grnet.cat.validators.XmlMetadataValidator.GeneralMetadataValidator;
import org.grnet.cat.validators.XmlMetadataValidator.XmlSchemaValidator;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
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

    private static final String AARC_G056_REGEX = "^([a-zA-Z0-9][a-zA-Z0-9.:-]*):res:([a-zA-Z0-9][a-zA-Z0-9_-]*)(?::[a-zA-Z0-9][a-zA-Z0-9_-]*)*?(?::act:[a-zA-Z0-9][a-zA-Z0-9_-]*(?:,[a-zA-Z0-9][a-zA-Z0-9_-]*)*)?(?:#[a-zA-Z0-9][a-zA-Z0-9.:%_-]*)?$";

    private static final String EMAIL_RFC2821_REGEX = "^(?i)[a-z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?(?:\\.[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?)*$";

    public AutomatedTestResponse validateMetadataByTestType(String type, ArccValidationRequest request) {

        var validatedResponse = xmlSchemaValidator.validateSchema(request.metadataUrl);
        var response = new AutomatedTestResponse();

        var status =  new AutomatedTestStatus();
        response.testStatus = status;
        response.lastRun = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(ZonedDateTime.now());

        if (!validatedResponse.isSchemaValid()) {
            status.isValid = false;
            status.message = validatedResponse.getMessage();
            switch (validatedResponse.getCode()) {

                case "VALIDATION_ERROR":
                    status.code = 422;
                    break;
                case "NOT_FOUND_ERROR":
                    throw new NotFoundException( validatedResponse.getMessage());
                case "GENERIC_ERROR":
                default:
                    throw new ServerErrorException(validatedResponse.getMessage(), 500);
            }
            return response;
        }
        var documentResponse = xmlSchemaValidator.parseXmlFromUrl(request.metadataUrl);

        if (documentResponse.getDocument() == null) {
            status.isValid = false;
            status.message = documentResponse.getMessage();
            status.code = documentResponse.getCode();
            return response;
        }
        var testValidationResult = metadataValidator.validate(type, documentResponse.getDocument());

        if (!testValidationResult.isCompliant) {

            status.isValid = false;
            status.code = 400;
            status.message = testValidationResult.feedback;
            return response;
        }

        status.isValid = true;
        status.code = 200;
        status.message="Successful schema validation";
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

            if(response.getIntrospectionInfo().getEntitlements().stream().anyMatch(value-> isValidValue(value, AARC_G069_REGEX))){

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

            if(response.getUserInfo().getEntitlements().stream().anyMatch(value-> isValidValue(value, AARC_G069_REGEX))){

                entitlementsInUserInfo.isValid = true;
                entitlementsInUserInfo.message = "entitlements found in user_info, and at least one entitlement follows the expected URN format (AARC-G069).";
            } else {

                entitlementsInUserInfo.isValid = false;
                entitlementsInUserInfo.message = "entitlements found in user_info, but none follow the expected URN format (AARC-G069).";
            }
        }

        var arccResponse = new AutomatedTestResponse();

        arccResponse.additionalInfo.put("entitlements_in_user_info", entitlementsInUserInfo);
        arccResponse.additionalInfo.put("entitlements_in_token_introspection", entitlementsInIntrospection);

        var status = new AutomatedTestStatus();
        status.message = "All validations were executed successfully during the test run.";

        status.isValid = entitlementsInUserInfo.isValid;

        arccResponse.testStatus = status;

        return arccResponse;
    }

    public AutomatedTestResponse validateAarcG056(String aaiProviderId){

        var nacoResponse = nacoClient.getEntry(aaiProviderId, SERVICE_KEY);

        var tests = new ArrayList<ArccValidationResult>();

        var apiResponse = new AutomatedTestResponse();

        validateAarcName(nacoResponse, apiResponse, tests);

        validateAarcGivenName(nacoResponse, apiResponse, tests);

        var status = new AutomatedTestStatus();
        status.isValid = tests.stream().allMatch(test->test.isValid);
        status.message = "All validations were executed successfully during the test run.";

        apiResponse.testStatus = status;

        return apiResponse;
    }

    private void validateAarcName(NacoEntryResponse nacoResponse, AutomatedTestResponse apiResponse, List<ArccValidationResult> tests){

       var name = new ArccValidationResult();

        if(Objects.isNull(nacoResponse.getUserInfo())){

            name.isValid = false;
            name.message = "user_info section is missing from the response.";
        } else if (StringUtils.isEmpty(nacoResponse.getUserInfo().getName())){

            name.isValid = false;
            name.message = "Missing or empty name claim in UserInfo response.";
        } else{

            name.isValid = true;
            name.message = "name found in user_info.";
        }

        apiResponse.additionalInfo.put("name_user_info", name);
        tests.add(name);
    }

    private void validateAarcGivenName(NacoEntryResponse nacoResponse, AutomatedTestResponse apiResponse, List<ArccValidationResult> tests){

        var givenName = new ArccValidationResult();

        if(Objects.isNull(nacoResponse.getUserInfo())){

            givenName.isValid = false;
            givenName.message = "user_info section is missing from the response.";
        } else if (StringUtils.isEmpty(nacoResponse.getUserInfo().getGivenName())){

            givenName.isValid = false;
            givenName.message = "Missing or empty given_name claim in UserInfo response.";
        } else{

            givenName.isValid = true;
            givenName.message = "given_name found in user_info.";
        }

        apiResponse.additionalInfo.put("given_name_user_info", givenName);
    }

    private void validateAarcFamilyName(NacoEntryResponse nacoResponse, AutomatedTestResponse apiResponse){

        var familyName = new ArccValidationResult();

        var location =  new AarcClaimLocation();

        if(Objects.isNull(nacoResponse.getUserInfo())){

            familyName.isValid = false;
            familyName.message = "user_info section is missing from the response.";
        } else if (StringUtils.isEmpty(nacoResponse.getUserInfo().getFamilyName())){

            familyName.isValid = false;
            familyName.message = "Missing or empty family_name claim in UserInfo response.";
        } else{

            familyName.isValid = true;
            familyName.message = "family_name found in user_info.";
        }

        location.userInfo =  familyName;

        apiResponse.additionalInfo.put("family_name", location);
    }

    private void validateAarcEmail(NacoEntryResponse nacoResponse, AutomatedTestResponse apiResponse){

        var email = new ArccValidationResult();

        var location =  new AarcClaimLocation();

        if(Objects.isNull(nacoResponse.getUserInfo())){

            email.isValid = false;
            email.message = "user_info section is missing from the response.";
        } else if (StringUtils.isEmpty(nacoResponse.getUserInfo().getEmail())){

            email.isValid = false;
            email.message = "Missing or empty email claim in UserInfo response.";
        } else{

            if(isValidValue(nacoResponse.getUserInfo().getEmail(), EMAIL_RFC2821_REGEX)){

                email.isValid = true;
                email.message = "email found in user_info, and follows the expected email format (RFC 2821).";
            } else {

                email.isValid = false;
                email.message = "email found in user_info, but it doesn't follow the expected email format (RFC 2821).";
            }
        }

        location.userInfo =  email;

        apiResponse.additionalInfo.put("email", location);
    }

    private void validateAarcOrganizationName(NacoEntryResponse nacoResponse, AutomatedTestResponse apiResponse){

        var organizationName = new ArccValidationResult();

        var location =  new AarcClaimLocation();

        if(Objects.isNull(nacoResponse.getUserInfo())){

            organizationName.isValid = false;
            organizationName.message = "user_info section is missing from the response.";
        } else if (StringUtils.isEmpty(nacoResponse.getUserInfo().getOrganizationName())){

            organizationName.isValid = false;
            organizationName.message = "Missing or empty organization_name claim in UserInfo response.";
        } else{

            organizationName.isValid = true;
            organizationName.message = "organization_name found in user_info.";
        }

        location.userInfo = organizationName;

        apiResponse.additionalInfo.put("organization_name", location);
    }

    private void validateAarcOrganizationDomain(NacoEntryResponse nacoResponse, AutomatedTestResponse apiResponse){

        var organizationNameInUserInfo = new ArccValidationResult();

        var organizationNameInTokenIntrospection = new ArccValidationResult();

        var location =  new AarcClaimLocation();

        if(Objects.isNull(nacoResponse.getUserInfo())){

            organizationNameInUserInfo.isValid = false;
            organizationNameInUserInfo.message = "user_info section is missing from the response.";
        } else if (StringUtils.isEmpty(nacoResponse.getUserInfo().getOrganizationDomain())){

            organizationNameInUserInfo.isValid = false;
            organizationNameInUserInfo.message = "Missing or empty schac_home_organization claim in UserInfo response.";
        } else{

            organizationNameInUserInfo.isValid = true;
            organizationNameInUserInfo.message = "schac_home_organization found in user_info.";
        }

        if(Objects.isNull(nacoResponse.getIntrospectionInfo())){

            organizationNameInTokenIntrospection.isValid = false;
            organizationNameInTokenIntrospection.message = "introspection_info section is missing from the response.";
        } else if (StringUtils.isEmpty(nacoResponse.getIntrospectionInfo().getOrganizationDomain())){

            organizationNameInTokenIntrospection.isValid = false;
            organizationNameInTokenIntrospection.message = "Missing or empty schac_home_organization claim in Token Introspection response.";
        } else{

            organizationNameInTokenIntrospection.isValid = true;
            organizationNameInTokenIntrospection.message = "schac_home_organization found in introspection_info.";
        }

        location.userInfo = organizationNameInUserInfo;
        location.introspectionInfo = organizationNameInTokenIntrospection;

        apiResponse.additionalInfo.put("schac_home_organization", location);
    }

    private void validateAarcAffiliationWithinHomeOrganisation(NacoEntryResponse nacoResponse, AutomatedTestResponse apiResponse){

        var affiliationNameInUserInfo = new ArccValidationResult();

        var affiliationInTokenIntrospection = new ArccValidationResult();

        var location =  new AarcClaimLocation();

        if(Objects.isNull(nacoResponse.getUserInfo())){

            affiliationNameInUserInfo.isValid = false;
            affiliationNameInUserInfo.message = "user_info section is missing from the response.";
        } else if (Objects.isNull(nacoResponse.getUserInfo().getAffiliationWithHomeOrganization()) || nacoResponse.getUserInfo().getAffiliationWithHomeOrganization().isEmpty()){

            affiliationNameInUserInfo.isValid = false;
            affiliationNameInUserInfo.message = "Missing or empty voperson_external_affiliation claim in UserInfo response.";
        } else{

            affiliationNameInUserInfo.isValid = true;
            affiliationNameInUserInfo.message = "voperson_external_affiliation found in user_info.";
        }

        if(Objects.isNull(nacoResponse.getIntrospectionInfo())){

            affiliationInTokenIntrospection.isValid = false;
            affiliationInTokenIntrospection.message = "introspection_info section is missing from the response.";
        } else if ((Objects.isNull(nacoResponse.getIntrospectionInfo().getAffiliationWithHomeOrganization()) || nacoResponse.getIntrospectionInfo().getAffiliationWithHomeOrganization().isEmpty())){

            affiliationInTokenIntrospection.isValid = false;
            affiliationInTokenIntrospection.message = "Missing or empty voperson_external_affiliation claim in Token Introspection response.";
        } else{

            affiliationInTokenIntrospection.isValid = true;
            affiliationInTokenIntrospection.message = "voperson_external_affiliation found in introspection_info.";
        }

        location.userInfo = affiliationNameInUserInfo;
        location.introspectionInfo = affiliationInTokenIntrospection;

        apiResponse.additionalInfo.put("voperson_external_affiliation", location);
    }

    private void validateAarcAffiliationAssurance(NacoEntryResponse nacoResponse, AutomatedTestResponse apiResponse){

        var affiliationInTokenIntrospection = new ArccValidationResult();
        var affiliationNameInAccessToken = new ArccValidationResult();

        var location =  new AarcClaimLocation();

        if(Objects.isNull(nacoResponse.getIntrospectionInfo())){

            affiliationInTokenIntrospection.isValid = false;
            affiliationInTokenIntrospection.message = "introspection_info section is missing from the response.";
        } else if ((Objects.isNull(nacoResponse.getIntrospectionInfo().getAssurance()) || nacoResponse.getIntrospectionInfo().getAssurance().isEmpty())){

            affiliationInTokenIntrospection.isValid = false;
            affiliationInTokenIntrospection.message = "Missing or empty eduperson_assurance claim in Token Introspection response.";
        } else{

            affiliationInTokenIntrospection.isValid = true;
            affiliationInTokenIntrospection.message = "eduperson_assurance found in introspection_info.";
        }

        if(Objects.isNull(nacoResponse.getAccessTokenInfo())){

            affiliationNameInAccessToken.isValid = false;
            affiliationNameInAccessToken.message = "access_token_info section is missing from the response.";
        } else if ((Objects.isNull(nacoResponse.getAccessTokenInfo().getAssurance()) || nacoResponse.getAccessTokenInfo().getAssurance().isEmpty())){

            affiliationNameInAccessToken.isValid = false;
            affiliationNameInAccessToken.message = "Missing or empty eduperson_assurance claim in Access Token response.";
        } else{

            affiliationNameInAccessToken.isValid = true;
            affiliationNameInAccessToken.message = "eduperson_assurance found in access_token_info.";
        }

        location.userInfo = affiliationNameInAccessToken;
        location.introspectionInfo = affiliationInTokenIntrospection;

        apiResponse.additionalInfo.put("eduperson_assurance", location);
    }

    private void validateAarcGroupAndRole(NacoEntryResponse nacoResponse, AutomatedTestResponse apiResponse) {

        var entitlementsInUserInfo = new ArccValidationResult();

        var entitlementsInIntrospection = new ArccValidationResult();

        var location =  new AarcClaimLocation();

        if(Objects.isNull(nacoResponse.getIntrospectionInfo())){

            entitlementsInIntrospection.isValid = false;
            entitlementsInIntrospection.message = "introspection_info section is missing from the response.";

        } else if (Objects.isNull(nacoResponse.getIntrospectionInfo().getEntitlements())){

            entitlementsInIntrospection.isValid = false;
            entitlementsInIntrospection.message = "entitlements claim is missing from the Token Introspection response.";
        } else{

            if(nacoResponse.getIntrospectionInfo().getEntitlements().stream().anyMatch(value-> isValidValue(value, AARC_G069_REGEX))){

                entitlementsInIntrospection.isValid = true;
                entitlementsInIntrospection.message = "entitlements found in introspection_info, and at least one entitlement follows the expected URN format (AARC-G069).";
            } else {

                entitlementsInIntrospection.isValid = false;
                entitlementsInIntrospection.message = "entitlements found in introspection_info, but none follow the expected URN format (AARC-G069).";
            }
        }

        if(Objects.isNull(nacoResponse.getUserInfo())){

            entitlementsInUserInfo.isValid = false;
            entitlementsInUserInfo.message = "user_info section is missing from the response.";

        } else if (Objects.isNull(nacoResponse.getUserInfo().getEntitlements())){

            entitlementsInUserInfo.isValid = false;
            entitlementsInUserInfo.message = "entitlements claim is missing from the UserInfo response.";
        } else{

            if(nacoResponse.getUserInfo().getEntitlements().stream().anyMatch(value-> isValidValue(value, AARC_G069_REGEX))){

                entitlementsInUserInfo.isValid = true;
                entitlementsInUserInfo.message = "entitlements found in user_info, and at least one entitlement follows the expected URN format (AARC-G069).";
            } else {

                entitlementsInUserInfo.isValid = false;
                entitlementsInUserInfo.message = "entitlements found in user_info, but none follow the expected URN format (AARC-G069).";
            }
        }

        location.userInfo = entitlementsInUserInfo;
        location.introspectionInfo = entitlementsInIntrospection;

        apiResponse.additionalInfo.put("group_and_role_entitlements", location);
    }

    private void validateAarcSub(NacoEntryResponse nacoResponse, AutomatedTestResponse apiResponse){

        var subInUserInfo = new ArccValidationResult();

        var subInTokenIntrospection = new ArccValidationResult();

        var subInAccessToken = new ArccValidationResult();

        var location =  new AarcClaimLocation();

        if(Objects.isNull(nacoResponse.getUserInfo())){

            subInUserInfo.isValid = false;
            subInUserInfo.message = "user_info section is missing from the response.";
        } else if (StringUtils.isEmpty(nacoResponse.getUserInfo().getSub())){

            subInUserInfo.isValid = false;
            subInUserInfo.message = "Missing or empty sub claim in UserInfo response.";
        } else{

            subInUserInfo.isValid = true;
            subInUserInfo.message = "sub found in user_info.";
        }

        if(Objects.isNull(nacoResponse.getIntrospectionInfo())){

            subInTokenIntrospection.isValid = false;
            subInTokenIntrospection.message = "introspection_info section is missing from the response.";
        } else if (StringUtils.isEmpty(nacoResponse.getIntrospectionInfo().getSub())){

            subInTokenIntrospection.isValid = false;
            subInTokenIntrospection.message = "Missing or empty sub claim in Token Introspection response.";
        } else{

            subInTokenIntrospection.isValid = true;
            subInTokenIntrospection.message = "sub found in introspection_info.";
        }

        if(Objects.isNull(nacoResponse.getAccessTokenInfo())){

            subInAccessToken.isValid = false;
            subInAccessToken.message = "access_token_info section is missing from the response.";
        } else if (StringUtils.isEmpty(nacoResponse.getAccessTokenInfo().getSub())){

            subInAccessToken.isValid = false;
            subInAccessToken.message = "Missing or empty sub claim in Access Token response.";
        } else{

            subInAccessToken.isValid = true;
            subInAccessToken.message = "sub found in access_token_info.";
        }

        location.userInfo = subInUserInfo;
        location.introspectionInfo = subInTokenIntrospection;
        location.accessTokenInfo = subInAccessToken;

        apiResponse.additionalInfo.put("sub", location);
    }

    private void validateAarcVopersonId(NacoEntryResponse nacoResponse, AutomatedTestResponse apiResponse){

        var voPersonIdInUserInfo = new ArccValidationResult();

        var voPersonIdInTokenIntrospection = new ArccValidationResult();

        var voPersonIdInAccessToken = new ArccValidationResult();

        var location =  new AarcClaimLocation();

        if(Objects.isNull(nacoResponse.getUserInfo())){

            voPersonIdInUserInfo.isValid = false;
            voPersonIdInUserInfo.message = "user_info section is missing from the response.";
        } else if ((Objects.isNull(nacoResponse.getUserInfo().getVopersonId()) || nacoResponse.getUserInfo().getVopersonId().isEmpty())){

            voPersonIdInUserInfo.isValid = false;
            voPersonIdInUserInfo.message = "Missing or empty voperson_id claim in UserInfo response.";
        } else{

            if(nacoResponse.getUserInfo().getVopersonId().size() == 1){

                voPersonIdInUserInfo.isValid = true;
                voPersonIdInUserInfo.message = "voperson_id found in user_info.";
            } else {

                voPersonIdInUserInfo.isValid = false;
                voPersonIdInUserInfo.message = "voperson_id in user_info contains multiple values; expected a single value.";

            }
        }

        if(Objects.isNull(nacoResponse.getIntrospectionInfo())){

            voPersonIdInTokenIntrospection.isValid = false;
            voPersonIdInTokenIntrospection.message = "introspection_info section is missing from the response.";
        } else if ((Objects.isNull(nacoResponse.getIntrospectionInfo().getVopersonId()) || nacoResponse.getIntrospectionInfo().getVopersonId().isEmpty())){

            voPersonIdInTokenIntrospection.isValid = false;
            voPersonIdInTokenIntrospection.message = "Missing or empty voperson_id claim in Token Introspection response.";
        } else{

            if(nacoResponse.getIntrospectionInfo().getVopersonId().size() == 1){

                voPersonIdInTokenIntrospection.isValid = true;
                voPersonIdInTokenIntrospection.message = "voperson_id found in introspection_info.";
            } else {

                voPersonIdInTokenIntrospection.isValid = false;
                voPersonIdInTokenIntrospection.message = "voperson_id in introspection_info contains multiple values; expected a single value.";

            }
        }

        if(Objects.isNull(nacoResponse.getAccessTokenInfo())){

            voPersonIdInAccessToken.isValid = false;
            voPersonIdInAccessToken.message = "access_token_info section is missing from the response.";
        } else if ((Objects.isNull(nacoResponse.getAccessTokenInfo().getVopersonId()) || nacoResponse.getAccessTokenInfo().getVopersonId().isEmpty())){

            voPersonIdInAccessToken.isValid = false;
            voPersonIdInAccessToken.message = "Missing or empty voperson_id claim in Access Token response.";
        } else{

            if(nacoResponse.getAccessTokenInfo().getVopersonId().size() == 1){

                voPersonIdInAccessToken.isValid = true;
                voPersonIdInAccessToken.message = "voperson_id found in access_token_info.";
            } else {

                voPersonIdInAccessToken.isValid = false;
                voPersonIdInAccessToken.message = "voperson_id in access_token_info contains multiple values; expected a single value.";
            }
        }

        location.userInfo = voPersonIdInUserInfo;
        location.introspectionInfo = voPersonIdInTokenIntrospection;
        location.accessTokenInfo = voPersonIdInAccessToken;

        apiResponse.additionalInfo.put("voperson_id", location);
    }

    private void validateAarcResourceCapabilities(NacoEntryResponse nacoResponse, AutomatedTestResponse apiResponse) {

        var entitlementsInUserInfo = new ArccValidationResult();

        var entitlementsInIntrospection = new ArccValidationResult();

        var location =  new AarcClaimLocation();

        if(Objects.isNull(nacoResponse.getIntrospectionInfo())){

            entitlementsInIntrospection.isValid = false;
            entitlementsInIntrospection.message = "introspection_info section is missing from the response.";

        } else if (Objects.isNull(nacoResponse.getIntrospectionInfo().getEntitlements())){

            entitlementsInIntrospection.isValid = false;
            entitlementsInIntrospection.message = "entitlements claim is missing from the Token Introspection response.";
        } else{

            if(nacoResponse.getIntrospectionInfo().getEntitlements().stream().anyMatch(value-> isValidValue(value, AARC_G056_REGEX))){

                entitlementsInIntrospection.isValid = true;
                entitlementsInIntrospection.message = "entitlements found in introspection_info, and at least one entitlement follows the expected URN format (AARC-G056).";
            } else {

                entitlementsInIntrospection.isValid = false;
                entitlementsInIntrospection.message = "entitlements found in introspection_info, but none follow the expected URN format (AARC-G056).";
            }
        }

        if(Objects.isNull(nacoResponse.getUserInfo())){

            entitlementsInUserInfo.isValid = false;
            entitlementsInUserInfo.message = "user_info section is missing from the response.";

        } else if (Objects.isNull(nacoResponse.getUserInfo().getEntitlements())){

            entitlementsInUserInfo.isValid = false;
            entitlementsInUserInfo.message = "entitlements claim is missing from the UserInfo response.";
        } else{

            if(nacoResponse.getUserInfo().getEntitlements().stream().anyMatch(value-> isValidValue(value, AARC_G056_REGEX))){

                entitlementsInUserInfo.isValid = true;
                entitlementsInUserInfo.message = "entitlements found in user_info, and at least one entitlement follows the expected URN format (AARC-G056).";
            } else {

                entitlementsInUserInfo.isValid = false;
                entitlementsInUserInfo.message = "entitlements found in user_info, but none follow the expected URN format (AARC-G056).";
            }
        }

        location.userInfo = entitlementsInUserInfo;
        location.introspectionInfo = entitlementsInIntrospection;

        apiResponse.additionalInfo.put("resource_capabilities_entitlements", location);
    }

    private boolean isValidValue(String value, String regex) {

        var PATTERN = Pattern.compile(regex);
        var matcher = PATTERN.matcher(value);
        return matcher.matches();
    }
}
