package org.grnet.cat.api.schedulers;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.grnet.cat.entities.registry.Imperative;
import org.grnet.cat.entities.registry.MotivationType;
import org.grnet.cat.entities.registry.RegistryActor;
import org.grnet.cat.entities.registry.metric.TypeAlgorithm;
import org.grnet.cat.entities.registry.metric.TypeMetric;
import org.grnet.cat.entities.registry.metric.TypeReproducibility;
import org.grnet.cat.repositories.registry.ImperativeRepository;
import org.grnet.cat.repositories.registry.MotivationTypeRepository;
import org.grnet.cat.repositories.registry.RegistryActorRepository;
import org.grnet.cat.repositories.registry.metric.TypeAlgorithmRepository;
import org.grnet.cat.repositories.registry.metric.TypeMetricRepository;
import org.grnet.cat.repositories.registry.metric.TypeReproducibilityRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VocabularyScheduler {

    @Inject
    @RestClient
    VocabularyClient vocabularyClient;

    @ConfigProperty(name = "quarkus.rest-client.\"org.grnet.cat.api.schedulers.VocabularyClient\".url")
    String vocabularyClientUrl;
    @ConfigProperty(name = "terminology.actor")
    String terminologyActor;

    @ConfigProperty(name = "terminology.metric")
    String terminologyMetric;

    @ConfigProperty(name = "terminology.motivation")
    String terminologyMotivation;

    @ConfigProperty(name = "terminology.imperative")
    String terminologyImperative;

    @Inject
    RegistryActorRepository registryActorRepository;

    @Inject
    MotivationTypeRepository motivationTypeRepository;

    @Inject
    TypeMetricRepository typeMetricRepository;

    @Inject
    ImperativeRepository imperativeRepository;

    @Inject
    TypeReproducibilityRepository typeReproducibilityRepository;


    @Scheduled(cron = "{vocabulary.cron.expr}")
    void cronJobWithExpressionInConfig() {
        execute();
    }


    /**
     * This method fetches the available Actors from Vocabulary
     * and saves them in the CAT database.
     */
    @Transactional
    public void execute() {
        Log.info("Connecting to vocabulary service");
        retrieveConcepts(terminologyActor);
        retrieveConcepts(terminologyMetric);
        retrieveConcepts(terminologyMotivation);
        retrieveConcepts(terminologyImperative);
    }

    @Transactional
    public void retrieveConcepts(String terminologyId) {
        TerminologyRequest request = new TerminologyRequest();
        request.setTerminologyId(List.of(terminologyId));
        try {
            var response = vocabularyClient.getAll(request);
            if (response.getStatus() == 200) { // Check if the response is OK
                TerminologyResponse terminologyResponse = response.readEntity(TerminologyResponse.class);
                List<Concept> concepts = terminologyResponse.getConcepts();
                for (Concept concept : concepts) {
                    if (isDraftOrPublished(concept.status)) {
                        System.out.println("scheduler running");
                        // updateConcept(terminologyId, concept);
                        insertConcept(terminologyId, concept);
                    }
                }
            }
        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() == Response.Status.SERVICE_UNAVAILABLE.getStatusCode()) {
                // Log or handle the specific 503 status
                Log.error("Vocabulary service returned 503 Service Unavailable: " + e.getMessage());
            } else {
                // Handle other status codes or rethrow if needed
                Log.error("Failed to call Vocabulary service: " + e.getMessage());

            }
        }
    }

    private boolean isDraftOrPublished(String status) {
        return status.equals(TermonologyStatus.DRAFT.name()) || status.equals(TermonologyStatus.PUBLISHED.name());
    }

    @Transactional
    public void insertConcept(String terminologyId, Concept concept) {

        String url = vocabularyClientUrl + "vocabularies/terminology/" + concept.getTerminology().id + "/concept/" + concept.id;

        Optional<RegistryActor> registryActorOptional = registryActorRepository.find("uriActor", url).firstResultOptional();
        if (terminologyId.equals(terminologyActor)) {
            if (registryActorOptional.isEmpty()) {
                System.out.println("new actor");
                RegistryActor conceptEntry = new RegistryActor();
                conceptEntry.setDescActor(concept.getDefinition().getEn());
                conceptEntry.setLabelActor(concept.getLabel().getEn());
                conceptEntry.setUriActor(url);
                conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                conceptEntry.setAct(concept.getLabel().getEn() + "(N/A)");
                conceptEntry.setVerified(false);
                registryActorRepository.persist(conceptEntry);

            } else {
                boolean isUpdated = false;
                RegistryActor conceptEntry = registryActorOptional.get();
                if (!conceptEntry.getLabelActor().equals(concept.getLabel().getEn())) {

                    conceptEntry.setLabelActor(concept.getLabel().getEn());
                    isUpdated = true;
                }

                if (!conceptEntry.getDescActor().equals(concept.getLabel().getEn())) {
                    conceptEntry.setDescActor(concept.getDefinition().getEn());
                    isUpdated = true;
                }
                if (isUpdated) {
                    conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                    registryActorRepository.persist(conceptEntry);
                }

            }
        } else if (terminologyId.equals(terminologyMotivation)) {
            Optional<MotivationType> motivationTypeOpt = motivationTypeRepository.find("urlMotivationType", url).firstResultOptional();
            if (motivationTypeOpt.isEmpty()) {
                MotivationType conceptEntry = new MotivationType();
                conceptEntry.setLabelMotivationType(concept.getLabel().getEn());
                conceptEntry.setDescMotivationType(concept.getDefinition().getEn());
                conceptEntry.setUrlMotivationType(url);
                conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                conceptEntry.setTmt(concept.getLabel().getEn() + "(N/A)");
                conceptEntry.setVerified(false);
                motivationTypeRepository.persist(conceptEntry);
            } else {
                boolean isUpdated = false;
                MotivationType conceptEntry = motivationTypeOpt.get();
                if (!conceptEntry.getLabelMotivationType().equals(concept.getLabel().getEn())) {
                    conceptEntry.setLabelMotivationType(concept.getLabel().getEn());
                    isUpdated = true;
                }

                if (!conceptEntry.getDescMotivationType().equals(concept.getLabel().getEn())) {
                    conceptEntry.setDescMotivationType(concept.getDefinition().getEn());
                    isUpdated = true;
                }
                if (isUpdated) {
                    conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                    motivationTypeRepository.persist(conceptEntry);
                }

            }
        } else if (terminologyId.equals(terminologyImperative)) {
            Optional<Imperative> imperativeOpt = imperativeRepository.find("labelImperative", concept.getLabel().getEn()).firstResultOptional();
            if (imperativeOpt.isEmpty()) {

                Imperative conceptEntry = new Imperative();
                conceptEntry.setDescImperative(concept.getDefinition().getEn());
                conceptEntry.setLabelImperative(concept.getLabel().getEn());
                conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                conceptEntry.setImp(concept.getLabel().getEn() + "(N/A)");
                conceptEntry.setVerified(false);
                imperativeRepository.persist(conceptEntry);

            } else {
                boolean isUpdated = false;
                Imperative conceptEntry = imperativeOpt.get();

                if (!conceptEntry.getLabelImperative().equals(concept.getLabel().getEn())) {
                    conceptEntry.setLabelImperative(concept.getLabel().getEn());
                    isUpdated = true;
                }

                if (!conceptEntry.getDescImperative().equals(concept.getLabel().getEn())) {
                    conceptEntry.setDescImperative(concept.getDefinition().getEn());
                    isUpdated = true;
                }

                if (isUpdated) {
                    conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                    imperativeRepository.persist(conceptEntry);
                }

            }
        } else if (terminologyId.equals(terminologyMetric)) {
            Optional<TypeMetric> typeMetricOpt = typeMetricRepository.find("uriTypeMetric", url).firstResultOptional();
            if (typeMetricOpt.isEmpty()) {

                TypeMetric conceptEntry = new TypeMetric();
                conceptEntry.setDescTypeMetric(concept.getDefinition().getEn());
                conceptEntry.setLabelTypeMetric(concept.getLabel().getEn());
                conceptEntry.setUriTypeMetric(url);
                conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                conceptEntry.setTMT(concept.getLabel().getEn() + "(N/A)");
                conceptEntry.setDescBenchmarkApproach("N/A");
                conceptEntry.setDescMetricApproach("N/A");
                var typeReproducability = typeReproducibilityRepository.findById("pid_graph:1BA2356B");
                conceptEntry.setTypeReproducibility(typeReproducability);
                conceptEntry.setVerified(false);
                typeMetricRepository.persist(conceptEntry);
            } else {

                TypeMetric conceptEntry = typeMetricOpt.get();
                boolean isUpdated = false;
                if (!conceptEntry.getLabelTypeMetric().equals(concept.getLabel().getEn())) {
                    conceptEntry.setLabelTypeMetric(concept.getLabel().getEn());
                    isUpdated = true;
                }

                if (!conceptEntry.getDescTypeMetric().equals(concept.getLabel().getEn())) {
                    conceptEntry.setDescTypeMetric(concept.getDefinition().getEn());
                    isUpdated = true;
                }

                if (isUpdated) {
                    conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                    typeMetricRepository.persist(conceptEntry);
                }

            }
        }
    }


    public class TerminologyRequest {
        private List<String> terminologyId;

        // Constructor
        public TerminologyRequest() {
        }

        public List<String> getTerminologyId() {
            return terminologyId;
        }

        public void setTerminologyId(List<String> terminologyId) {
            this.terminologyId = terminologyId;
        }
    }

    public static class TerminologyResponse {
        private int totalHitCount;
        private int resultStart;
        private List<Concept> concepts;

        // Getters and Setters
        public int getTotalHitCount() {
            return totalHitCount;
        }

        public void setTotalHitCount(int totalHitCount) {
            this.totalHitCount = totalHitCount;
        }

        public int getResultStart() {
            return resultStart;
        }

        public void setResultStart(int resultStart) {
            this.resultStart = resultStart;
        }

        public List<Concept> getConcepts() {
            return concepts;
        }

        public void setConcepts(List<Concept> concepts) {
            this.concepts = concepts;
        }
    }

    static class Concept {
        private String id;
        private String uri;
        private String status;
        private Label label;
        private Definition definition;
        private String modified;
        private Terminology terminology;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Label getLabel() {
            return label;
        }

        public void setLabel(Label label) {
            this.label = label;
        }

        public Definition getDefinition() {
            return definition;
        }

        public void setDefinition(Definition definition) {
            this.definition = definition;
        }

        public String getModified() {
            return modified;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }

        public Terminology getTerminology() {
            return terminology;
        }

        public void setTerminology(Terminology terminology) {
            this.terminology = terminology;
        }
    }

    static class Label {
        @JsonProperty("en")
        private String en;

        // Getter and Setter
        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }
    }

    static class Definition {
        @JsonProperty("en")
        private String en;

        // Getter and Setter
        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }
    }

    static class Terminology {
        private String id;
        private String uri;
        private String status;
        private Label label;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Label getLabel() {
            return label;
        }

        public void setLabel(Label label) {
            this.label = label;
        }
    }

    public enum TermonologyStatus {

        DRAFT,
        PUBLISHED;

    }

}
