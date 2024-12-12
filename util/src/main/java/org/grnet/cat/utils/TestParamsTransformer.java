package org.grnet.cat.utils;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class TestParamsTransformer {

    private static final Map<String, String>  SPECIFIC_REPLACEMENTS = new HashMap<>();

    static {
        // Mapping tokens that require specific replacements
         SPECIFIC_REPLACEMENTS.put("admContDetails", "administrative_contact_details");
         SPECIFIC_REPLACEMENTS.put("OpSecDetailsEmail", "operational_security_contact_email");
         SPECIFIC_REPLACEMENTS.put("OpSecDetailsPhone", "operational_security_contact_phone");
         SPECIFIC_REPLACEMENTS.put("CryptKey", "cryptographic_key");
         SPECIFIC_REPLACEMENTS.put("userAuth", "user_authentication");
         SPECIFIC_REPLACEMENTS.put("machineMeta", "machine_metadata");
         SPECIFIC_REPLACEMENTS.put("physEntity", "physical_entity");
         SPECIFIC_REPLACEMENTS.put("numCountries", "number_of_countries");
         SPECIFIC_REPLACEMENTS.put("communityEngage", "community_engagement");
         SPECIFIC_REPLACEMENTS.put("costModel", "cost_model");
         SPECIFIC_REPLACEMENTS.put("ctdResolution", "continued_resolution");
         SPECIFIC_REPLACEMENTS.put("commPol", "community_policy");
         SPECIFIC_REPLACEMENTS.put("opCommDef", "operational_community_definitions");
         SPECIFIC_REPLACEMENTS.put("commDocs", "community_documents");
         SPECIFIC_REPLACEMENTS.put("urlCheck", "url_check");
         SPECIFIC_REPLACEMENTS.put("strucFunds", "structural_funding");
         SPECIFIC_REPLACEMENTS.put("sensitive_meta", "sensitive_metadata");
         SPECIFIC_REPLACEMENTS.put("trLevel", "technology_readiness_level");
         SPECIFIC_REPLACEMENTS.put("relationConforms", "relation_conformance");
         SPECIFIC_REPLACEMENTS.put("httpsAPI", "https_api");
         SPECIFIC_REPLACEMENTS.put("pidOwner", "pid_owner");
         SPECIFIC_REPLACEMENTS.put("mtnProvision", "maintenance_provision");
         SPECIFIC_REPLACEMENTS.put("glblGovernance", "global_governance");
         SPECIFIC_REPLACEMENTS.put("declareCertification", "declare_certification");
         SPECIFIC_REPLACEMENTS.put("apiUpdate", "api_update");
         SPECIFIC_REPLACEMENTS.put("versionSupport", "version_support");
         SPECIFIC_REPLACEMENTS.put("cmtyGovernance", "community_governance");
         SPECIFIC_REPLACEMENTS.put("planContinuity", "continuity_plan");
         SPECIFIC_REPLACEMENTS.put("resolverExists", "resolver_exists");
         SPECIFIC_REPLACEMENTS.put("openData", "open_data");
         SPECIFIC_REPLACEMENTS.put("memberEvents", "member_events");
         SPECIFIC_REPLACEMENTS.put("useGranularity", "use_granularity");
         SPECIFIC_REPLACEMENTS.put("ericUse", "eric_use");
         SPECIFIC_REPLACEMENTS.put("metaEncryption", "meta_encryption");
         SPECIFIC_REPLACEMENTS.put("metaMaintain", "meta_maintain");
         SPECIFIC_REPLACEMENTS.put("percentResolution", "percent_resolution");
         SPECIFIC_REPLACEMENTS.put("operationMonths", "operation_months");
         SPECIFIC_REPLACEMENTS.put("exitStrategy", "exit_strategy");
         SPECIFIC_REPLACEMENTS.put("requestData", "request_data");
         SPECIFIC_REPLACEMENTS.put("responseVariable", "response_variable");
         SPECIFIC_REPLACEMENTS.put("euResearch", "eu_research");
         SPECIFIC_REPLACEMENTS.put("apiExists", "api_exists");
         SPECIFIC_REPLACEMENTS.put("sensitiveMeta", "sensitive_metadata");
         SPECIFIC_REPLACEMENTS.put("ownerTransfer", "owner_transfer");
         SPECIFIC_REPLACEMENTS.put("userForum", "user_forum");
         SPECIFIC_REPLACEMENTS.put("versionPolicy", "version_policy");
         SPECIFIC_REPLACEMENTS.put("typeVerify", "type_verify");
         SPECIFIC_REPLACEMENTS.put("mnmtPolicy", "mnmt_policy");
         SPECIFIC_REPLACEMENTS.put("managerPolicy", "manager_policy");
         SPECIFIC_REPLACEMENTS.put("contractExists", "contract_exists");
    }

    /**
     * Transforms the testParams string to snake_case, applying specific replacements.
     *
     * @param testParams The original testParams string.
     * @return The transformed testParams string in snake_case.
     */
    public static String transformTestParams(String testParams) {
        if (testParams == null || testParams.isEmpty()) {
            return testParams;
        }

        // Split the testParams by '|'
        String[] tokens = testParams.split("\\|");

        // Transform each token
        String transformed = java.util.Arrays.stream(tokens)
                .map(token -> {
                    // Trim whitespace and remove surrounding quotes if any
                    String trimmed = token.trim().replaceAll("^['“”\"]|['“”\"]$", "");

                    // Apply specific replacement if exists
                    return  SPECIFIC_REPLACEMENTS.getOrDefault(trimmed, convertCamelToSnake(trimmed));
                })
                .collect(Collectors.joining("|"));

        return transformed;
    }

    /**
     * Converts a camelCase or PascalCase string to snake_case.
     *
     * @param input The original string in camelCase or PascalCase.
     * @return The converted string in snake_case.
     */
    private static String convertCamelToSnake(String input) {
        return input.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
    }
}
