package org.grnet.cat.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import org.grnet.cat.entities.Setting;
import org.grnet.cat.entities.Subject;

import java.util.Optional;

@ApplicationScoped
public class SettingRepository implements Repository<Setting, String>{

}
