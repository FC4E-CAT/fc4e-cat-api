package org.grnet.cat.api.schedulers;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.grnet.cat.entities.registry.Imperative;
import org.grnet.cat.entities.registry.MotivationType;
import org.grnet.cat.entities.registry.RegistryActor;
import org.grnet.cat.entities.registry.metric.TypeAlgorithm;
import org.grnet.cat.entities.registry.metric.TypeMetric;
import org.grnet.cat.repositories.registry.ImperativeRepository;
import org.grnet.cat.repositories.registry.MotivationTypeRepository;
import org.grnet.cat.repositories.registry.RegistryActorRepository;
import org.grnet.cat.repositories.registry.metric.TypeAlgorithmRepository;
import org.grnet.cat.repositories.registry.metric.TypeMetricRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VocabularyScheduler {
    //
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

    @ConfigProperty(name = "terminology.algorithm")
    String terminologyAlgorithm;

    @Inject
    RegistryActorRepository registryActorRepository;

    @Inject
    MotivationTypeRepository motivationTypeRepository;

    @Inject
    TypeMetricRepository typeMetricRepository;

    @Inject
    TypeAlgorithmRepository typeAlgorithmRepository;

    @Inject
    ImperativeRepository imperativeRepository;


//    void onStart(@Observes StartupEvent ev) {
//        execute();
//    }

    @Scheduled(cron = "{vocabulary.cron.expr}")
    void cronJobWithExpressionInConfig() {
        execute();
    }


    /**
     * This method fetches the available Actors from Vocabulary
     * and saves them in the CAT database.
     */
    public void execute() {
        System.out.println("am i executing? ");
        retrieveConcepts(terminologyActor);
        retrieveConcepts(terminologyMetric);
        retrieveConcepts(terminologyMotivation);
        retrieveConcepts(terminologyImperative);
        retrieveConcepts(terminologyAlgorithm);
    }

    private void retrieveConcepts(String terminologyId) {
        TerminologyRequest request = new TerminologyRequest();
        request.setTerminologyId(List.of(terminologyId));
        var response = vocabularyClient.getAll(request);

        if (response.getStatus() == 200) { // Check if the response is OK
            TerminologyResponse terminologyResponse = response.readEntity(TerminologyResponse.class);
            List<Concept> concepts = terminologyResponse.getConcepts();
            for (Concept concept : concepts) {
                if (isDraftOrPublished(concept.status)) {
                    updateConcept(terminologyId, concept);
                }
            }


        }
    }

    private boolean isDraftOrPublished(String status) {
        return status.equals(TermonologyStatus.DRAFT) || status.equals(TermonologyStatus.PUBLISHED);
    }
    private void insertConcept(String terminologyId, Concept concept) {
        if (terminologyId.equals(terminologyActor)) {

            if (registryActorRepository.find("labelActor", concept.getLabel().getEn()).firstResultOptional().isEmpty()) {
                RegistryActor conceptEntry = new RegistryActor();
                conceptEntry.setDescActor(concept.getDefinition().getEn());
                conceptEntry.setLabelActor(concept.getLabel().getEn());
                conceptEntry.setUriActor(vocabularyClientUrl + "/vocabularies/terminology/" + concept.getTerminology().id + "/concepts/" + concept.id);
                conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                registryActorRepository.persist(conceptEntry);

            }
        } else if (terminologyId.equals(terminologyMotivation)) {
            if (motivationTypeRepository.find("labelMotivationType", concept.getLabel().getEn()).firstResultOptional().isEmpty()) {

                MotivationType conceptEntry = new MotivationType();
                conceptEntry.setDescMotivationType(concept.getDefinition().getEn());
                conceptEntry.setUrlMotivationType(vocabularyClientUrl + "/vocabularies/terminology/" + concept.getTerminology().id + "/concepts/" + concept.id);
                conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                motivationTypeRepository.persist(conceptEntry);
            }
        } else if (terminologyId.equals(terminologyImperative)) {
            if (imperativeRepository.find("labelImperative", concept.getLabel().getEn()).firstResultOptional().isEmpty()) {

                Imperative conceptEntry = new Imperative();
                conceptEntry.setDescImperative(concept.getDefinition().getEn());
                conceptEntry.setLabelImperative(concept.getLabel().getEn());
                //  imperative.setUriActor(vocabularyClientUrl + "/vocabularies/terminology/" + concept.getTerminology().id + "/concepts/" + concept.id);
                conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                imperativeRepository.persist(conceptEntry);

            }
        } else if (terminologyId.equals(terminologyAlgorithm)) {
            if (typeAlgorithmRepository.find("labelAlgorithmType", concept.getLabel().getEn()).firstResultOptional().isEmpty()) {

                TypeAlgorithm conceptEntry = new TypeAlgorithm();
                conceptEntry.setDescAlgorithmType(concept.getDefinition().getEn());
                conceptEntry.setLabelAlgorithmType(concept.getLabel().getEn());
                conceptEntry.setUriAlgorithmType(vocabularyClientUrl + "/vocabularies/terminology/" + concept.getTerminology().id + "/concepts/" + concept.id);
                conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                typeAlgorithmRepository.persist(conceptEntry);

            }
        } else if (terminologyId.equals(terminologyMetric)) {
            if (typeMetricRepository.find("labelTypeMetric", concept.getLabel().getEn()).firstResultOptional().isEmpty()) {

                TypeMetric conceptEntry = new TypeMetric();
                conceptEntry.setDescTypeMetric(concept.getDefinition().getEn());
                conceptEntry.setLabelTypeMetric(concept.getLabel().getEn());
                conceptEntry.setUriTypeMetric(vocabularyClientUrl + "/vocabularies/terminology/" + concept.getTerminology().id + "/concepts/" + concept.id);
                conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                typeMetricRepository.persist(conceptEntry);
            }
        }
    }

    private void updateConcept(String terminologyId, Concept concept) {
        boolean isUpdated = false;
        if (terminologyId.equals(terminologyActor)) {
            Optional<RegistryActor> registryActorOpt = registryActorRepository.find("vocabulary_id", concept.getId()).firstResultOptional();
            if (registryActorOpt.isEmpty()) {
                registryActorOpt = registryActorRepository.find("labelActor", concept.getLabel().getEn()).firstResultOptional();
            }
            if (registryActorOpt.isPresent()) {
                RegistryActor conceptEntry = registryActorOpt.get();

                if (!conceptEntry.getLabelActor().equals(concept.getLabel().getEn())){

                    conceptEntry.setLabelActor(concept.getLabel().getEn());
                    isUpdated = true;
                }

                if (!conceptEntry.getDescActor().equals(concept.getLabel().getEn())){
                    conceptEntry.setDescActor(concept.getDefinition().getEn());
                    isUpdated = true;
                }

                String uri = vocabularyClientUrl + "/vocabularies/terminology/" + concept.getTerminology().id + "/concepts/" + concept.id;
                if (!conceptEntry.getUriActor().equals(uri)) {
                    conceptEntry.setUriActor(uri);
                    isUpdated = true;
                }
                if (isUpdated) {
                    conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                    registryActorRepository.persist(conceptEntry);
                }
            }
        } else if (terminologyId.equals(terminologyMotivation)) {
            Optional<MotivationType> motivationTypeOpt = motivationTypeRepository.find("vocabulary_id", concept.getId()).firstResultOptional();
            if (motivationTypeOpt.isEmpty()) {
                motivationTypeOpt = motivationTypeRepository.find("labelMotivationType", concept.getLabel().getEn()).firstResultOptional();
            }

            if (motivationTypeOpt.isPresent()) {
                MotivationType conceptEntry = motivationTypeOpt.get();

                if (!conceptEntry.getLabelMotivationType().equals(concept.getLabel().getEn())) {
                    conceptEntry.setLabelMotivationType(concept.getLabel().getEn());
                    isUpdated = true;
                }

                if (!conceptEntry.getDescMotivationType().equals(concept.getLabel().getEn())){
                    conceptEntry.setDescMotivationType(concept.getDefinition().getEn());
                    isUpdated = true;
                }

                String uri = vocabularyClientUrl + "/vocabularies/terminology/" + concept.getTerminology().id + "/concepts/" + concept.id;
                if (!conceptEntry.getUrlMotivationType().equals(uri)) {
                    conceptEntry.setUrlMotivationType(uri);
                    isUpdated = true;
                }
                if (isUpdated) {
                    conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                    motivationTypeRepository.persist(conceptEntry);
                }
            }
        } else if (terminologyId.equals(terminologyImperative)) {
            Optional<Imperative> imperativeOpt = imperativeRepository.find("vocabulary_id", concept.getId()).firstResultOptional();
            if (imperativeOpt.isEmpty()) {
                imperativeOpt =  imperativeRepository.find("labelImperative", concept.getLabel().getEn()).firstResultOptional();
            }

            if (imperativeOpt.isPresent()) {
                Imperative conceptEntry = imperativeOpt.get();

                if (!conceptEntry.getLabelImperative().equals(concept.getLabel().getEn())) {
                    conceptEntry.setLabelImperative(concept.getLabel().getEn());
                    isUpdated = true;
                }

                if (!conceptEntry.getDescImperative().equals(concept.getLabel().getEn())){
                    conceptEntry.setDescImperative(concept.getDefinition().getEn());
                    isUpdated = true;
                }

                if (isUpdated) {
                    conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                    imperativeRepository.persist(conceptEntry);
                }

            }
        } else if (terminologyId.equals(terminologyAlgorithm)) {
            Optional<TypeAlgorithm> typeAlgorithmOpt = typeAlgorithmRepository.find("labelAlgorithmType", concept.getLabel().getEn()).firstResultOptional();
            if (typeAlgorithmOpt.isEmpty()) {
                typeAlgorithmOpt =   typeAlgorithmRepository.find("labelAlgorithmType", concept.getLabel().getEn()).firstResultOptional();
            }

            if (typeAlgorithmOpt.isPresent()) {
                TypeAlgorithm conceptEntry = typeAlgorithmOpt.get();

                if (!conceptEntry.getLabelAlgorithmType().equals(concept.getLabel().getEn())) {
                    conceptEntry.setLabelAlgorithmType(concept.getLabel().getEn());
                    isUpdated = true;
                }

                if (!conceptEntry.getDescAlgorithmType().equals(concept.getLabel().getEn())){
                    conceptEntry.setDescAlgorithmType(concept.getDefinition().getEn());
                    isUpdated = true;
                }
                String uri = vocabularyClientUrl + "/vocabularies/terminology/" + concept.getTerminology().id + "/concepts/" + concept.id;
                ;
                if (!conceptEntry.getUriAlgorithmType().equals(uri)) {
                    conceptEntry.setUriAlgorithmType(uri);
                    isUpdated = true;
                }


                if (isUpdated) {
                    conceptEntry.setLastTouch(Timestamp.from(Instant.now()));
                    typeAlgorithmRepository.persist(conceptEntry);
                }

            }
        } else if (terminologyId.equals(terminologyMetric)) {
            Optional<TypeMetric> typeMetricOpt = typeMetricRepository.find("vocabulary_id", concept.getId()).firstResultOptional();
            if (typeMetricOpt.isEmpty()) {
                typeMetricOpt =    typeMetricRepository.find("labelTypeMetric", concept.getLabel().getEn()).firstResultOptional();
            }

            if (typeMetricOpt.isPresent()) {
                TypeMetric conceptEntry = typeMetricOpt.get();

                if (!conceptEntry.getLabelTypeMetric().equals(concept.getLabel().getEn())) {
                    conceptEntry.setLabelTypeMetric(concept.getLabel().getEn());
                    isUpdated = true;
                }

                if (!conceptEntry.getDescTypeMetric().equals(concept.getLabel().getEn())){
                    conceptEntry.setDescTypeMetric(concept.getDefinition().getEn());
                    isUpdated = true;
                }
                String uri = vocabularyClientUrl + "/vocabularies/terminology/" + concept.getTerminology().id + "/concepts/" + concept.id;
                if (!conceptEntry.getUriTypeMetric().equals(uri)) {
                    conceptEntry.setUriTypeMetric(uri);
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
