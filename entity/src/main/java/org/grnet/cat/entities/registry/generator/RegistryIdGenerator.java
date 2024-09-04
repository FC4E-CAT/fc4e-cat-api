package org.grnet.cat.entities.registry.generator;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;

import java.util.EnumSet;

public class RegistryIdGenerator implements BeforeExecutionGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o, Object o1, EventType eventType) {
        return String.format("pid_graph:%s",
                generateSecureRandomHexWithCommonsMathRandomDataGenerator(8));
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EventTypeSets.INSERT_ONLY;
    }

    private String generateSecureRandomHexWithCommonsMathRandomDataGenerator(int len) {
        var randomDataGenerator = new RandomDataGenerator();
        return randomDataGenerator.nextSecureHexString(len);
    }
}
