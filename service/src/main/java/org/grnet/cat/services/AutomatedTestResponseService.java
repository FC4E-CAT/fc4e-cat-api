package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import org.grnet.cat.dtos.*;
import org.grnet.cat.dtos.assessment.registry.AdditionalInfoDto;
import org.grnet.cat.dtos.assessment.registry.TestNodeDto;
import org.grnet.cat.enums.ArccTestType;
import org.grnet.cat.services.arcc.ArccValidationService;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class AutomatedTestResponseService {

    @Inject
    AutomatedCheckService automatedCheckService;

    @Inject
    ArccValidationService arccValidationService;

    private void runHttpsCheck(TestNodeDto test) {
        if (test.getValue() instanceof String) {
            var url = (String) test.getValue();
            var request = new AutomatedCheckRequest();
            request.url = url;

            var response = automatedCheckService.isValidHttpsUrl(request);
            if (response == null || response.testStatus == null) return;

            boolean valid = Boolean.TRUE.equals(response.testStatus.isValid);
            test.setValue(valid ? 1 : 0);
            test.setResult(valid ? 1 : 0);

            var info = new AdditionalInfoDto();
            info.setMessage(response.testStatus.message);
            info.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            test.setAdditionalInfo(info);
        }
    }

    private void runAarcG069Check(TestNodeDto test) {
        try {
            if (test.getValue() instanceof String) {
                String providerId = (String) test.getValue();
                var response = arccValidationService.validateAarcG069(providerId);
                if (response == null || response.testStatus == null) return;

                boolean valid = Boolean.TRUE.equals(response.testStatus.isValid);
                test.setValue(valid ? 1 : 0);
                test.setResult(valid ? 1 : 0);

                var info = new AdditionalInfoDto();
                info.setMessage(response.testStatus.message);
                info.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                test.setAdditionalInfo(info);
            }
        } catch (ForbiddenException e) {

            test.setResult(-1);
            test.setValue(-1);

            var info = new AdditionalInfoDto();
            info.setMessage("You do not have permission to run AARC-G069 validation.");
            info.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            test.setAdditionalInfo(info);
        }
    }

    private void runArccMetadataValidation(TestNodeDto test, String type) {
        if (test.getValue() instanceof String) {
            String metadataUrl = (String) test.getValue();
            var request = new ArccValidationRequest();
            request.metadataUrl = metadataUrl;

            var response = arccValidationService.validateMetadataByTestType(type, request);
            if (response == null || response.testStatus == null) return;

            boolean valid = Boolean.TRUE.equals(response.testStatus.isValid);
            test.setValue(valid ? 1 : 0);
            test.setResult(valid ? 1 : 0);

            var info = new AdditionalInfoDto();
            info.setMessage(response.testStatus.message);
            info.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            test.setAdditionalInfo(info);
        }
    }

    public void evaluate(TestNodeDto test) {
        if (test == null || test.getType() == null) return;

        String type = test.getType();

        if ("Auto-Check-Url-Binary".equals(type)) {
            runHttpsCheck(test);
        } else if ("Auto-Check-String-Binary".equals(type) && "Auto-AAI-Check-Entitlements".equals(test.getId())) {
            runAarcG069Check(test);
        } else if (type.startsWith("Auto-Check-Xml-")) {
            String arccType = type.replace("Auto-Check-Xml-", "");
            runArccMetadataValidation(test, arccType);
        }
    }
}
