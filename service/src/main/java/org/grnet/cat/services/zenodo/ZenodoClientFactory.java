package org.grnet.cat.services.zenodo;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import java.net.URI;

@ApplicationScoped
public class ZenodoClientFactory {

    public ZenodoClient create(String baseUrl) {
        return RestClientBuilder.newBuilder()
                .baseUri(URI.create(baseUrl))
                .build(ZenodoClient.class);
    }
}
