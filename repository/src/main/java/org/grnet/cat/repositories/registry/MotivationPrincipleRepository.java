package org.grnet.cat.repositories.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.grnet.cat.entities.registry.MotivationPrincipleId;
import org.grnet.cat.entities.registry.MotivationPrincipleJunction;
import org.grnet.cat.repositories.Repository;

@ApplicationScoped
public class MotivationPrincipleRepository implements Repository<MotivationPrincipleJunction, MotivationPrincipleId> {

    public boolean existsByMotivationAndPrincipleAndVersion(String motivationId, String principleId, Integer lodMpV) {
        return find("SELECT 1 FROM MotivationPrincipleJunction m WHERE m.id.motivationId = ?1 AND m.id.principleId = ?2 AND m.id.lodMpV = ?3", motivationId, principleId, lodMpV)
                .firstResultOptional()
                .isPresent();
    }


    public boolean existsByPrinciple(String principleId) {
        return find("SELECT 1 FROM MotivationPrincipleJunction pc WHERE pc.id.principleId = ?1", principleId)
                .firstResultOptional()
                .isPresent();
    }

    public boolean existPrincipleInStatus(String principleId,boolean status) {
        return find("SELECT 1 FROM MotivationPrincipleJunction pc INNER JOIN MotivationActorJunction ma ON pc.motivation.id=ma.motivation.id   WHERE pc.id.principleId = ?1 AND ma.published= ?2", principleId,status)
                .firstResultOptional()
                .isPresent();
    }



    @Transactional
    public long removeAll(){
        return deleteAll();
    }
}
