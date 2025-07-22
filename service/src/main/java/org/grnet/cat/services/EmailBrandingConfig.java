package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@ApplicationScoped
public class EmailBrandingConfig {

    @Getter
    @ConfigProperty(name = "app.logo.url", defaultValue = "")
    Optional<String> logoUrl;

    @Getter
    @ConfigProperty(name = "app.title", defaultValue = "FAIRCORE4EOSC Compliance Assessment Toolkit")
    String title;

    @ConfigProperty(name = "app.partners.hide", defaultValue = "false")
    boolean hidePartners;

    public boolean shouldHidePartners() {
        return hidePartners;
    }
}
