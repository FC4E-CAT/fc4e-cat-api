package org.grnet.cat.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.history.History;

@ApplicationScoped
public class HistoryRepository implements Repository<History, Long> {
}
