package org.grnet.cat.services.env;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class EnvironmentDetector {

    @ConfigProperty(name = "api.server.url")
    String apiServerUrl;

    @ConfigProperty(name = "zenodo.sandbox.url")
    String zenodoSandboxUrl;

    @ConfigProperty(name = "zenodo.prod.url")
    String zenodoProdUrl;

    public boolean isDevel() {
        if (apiServerUrl == null) return true;
        String url = apiServerUrl.toLowerCase();
        return url.contains("localhost") || url.contains("127.0.0.1") || url.contains("devel");
    }

    public String getZenodoBaseUrl() {
        return isDevel() ? zenodoSandboxUrl : zenodoProdUrl;
    }
}