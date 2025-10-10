package org.grnet.cat.utils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ZenodoConfig {
//configuration values based on Zenodo official documentation
    @ConfigProperty(name = "zenodo.prod.doi.prefix")
    String prodPrefix;

    @ConfigProperty(name = "zenodo.prod.target.url.resolver")
    String prodResolver;

    @ConfigProperty(name = "zenodo.sandbox.doi.prefix")
    String sandboxPrefix;

    @ConfigProperty(name = "zenodo.sandbox.target.url.resolver")
    String sandboxResolver;

    public String buildDoiUrl(String doi) {
        if (doi == null || doi.isBlank()) {
            return "";
        }

        if (doi.startsWith(prodPrefix)) {
            return prodResolver + doi;
        } else if (doi.startsWith(sandboxPrefix)) {
            return sandboxResolver + doi;
        } else {
            // fallback to production style or log a warning
            return prodResolver + doi;
        }
    }
}
