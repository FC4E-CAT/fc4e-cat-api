package org.grnet.cat.repositories.registry;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.registry.PrincipleCriterionJunction;
import org.grnet.cat.repositories.Repository;

import java.util.Optional;

@ApplicationScoped
public class PrincipleCriterionRepository  implements Repository<PrincipleCriterionJunction, String> {

    public Optional<PrincipleCriterionJunction> findCriterion(String criterionId, String motivationId) {

       return find("FROM PrincipleCriterionJunction pri WHERE pri.criterion.id = ?1 and pri.motivation.id =?2 ",criterionId,motivationId).firstResultOptional();
   }
}
