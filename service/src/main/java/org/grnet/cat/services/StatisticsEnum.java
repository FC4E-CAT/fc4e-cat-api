package org.grnet.cat.services;

import io.quarkus.hibernate.orm.panache.Panache;
import org.grnet.cat.enums.UserType;
import org.grnet.cat.enums.ValidationStatus;
import org.grnet.cat.repositories.*;

import java.util.stream.Collectors;

public class StatisticsEnum{
    public enum User {
        TOTAL {
            @Override
            public Long getStatistics(RoleRepository roleRepository, UserRepository userRepository) {
                return userRepository.find("from User user").stream().count();
            }
        },
        IDENTIFIED {
            @Override
            public Long getStatistics(RoleRepository roleRepository, UserRepository userRepository) {
                var users = userRepository.find("from User user").stream().collect(Collectors.toList());

                var addedRolesAndUserType = users
                        .stream()
                        .map(user -> {

                            var roles = roleRepository.fetchUserRoles(user.getId());
                            user.setRoles(roles);
                            user.setType(userRepository.findUserType(roles));

                            return user;
                        }).collect(Collectors.toList());

                return addedRolesAndUserType.stream().filter(user -> user.getType().equals(UserType.Identified)).count();

            }
        },
        VALIDATED {
            @Override
            public Long getStatistics(RoleRepository roleRepository, UserRepository userRepository) {
                var users = userRepository.find("from User user").stream().collect(Collectors.toList());

                var addedRolesAndUserType = users
                        .stream()
                        .map(user -> {

                            var roles = roleRepository.fetchUserRoles(user.getId());
                            user.setRoles(roles);
                            user.setType(userRepository.findUserType(roles));

                            return user;
                        }).collect(Collectors.toList());

                return addedRolesAndUserType.stream().filter(user -> user.getType().equals(UserType.Validated)).count();

            }
        },

        BANNED {
            @Override
            public Long getStatistics(RoleRepository roleRepository, UserRepository userRepository) {
                return userRepository.find("from User user where banned =?1", true).stream().count();

            }
        };

        public abstract Long getStatistics(RoleRepository roleRepository, UserRepository userRepository);

    }

    public enum Validation {
        TOTAL {
            @Override
            public Long getStatistics(ValidationRepository validationRepository) {
                return validationRepository.find("from Validation val").stream().count();

            }
        },
        ACCEPTED {
            @Override
            public Long getStatistics(ValidationRepository validationRepository) {
                return validationRepository.find("from Validation val where val.status =?1 or val.status=?2 ", ValidationStatus.PENDING, ValidationStatus.REVIEW).stream().count();

            }
        },
        PENDING {
            @Override
            public Long getStatistics(ValidationRepository validationRepository) {
                return validationRepository.find("from Validation val where val.status =?1 ", ValidationStatus.APPROVED).stream().count();

            }
        };

        public abstract Long getStatistics(ValidationRepository validationRepository);

    }


    public enum Assessment {
        TOTAL {
            @Override
            public Long getStatistics(AssessmentRepository assessmentRepository) {
                return assessmentRepository.find("from Assessment as").stream().count();

            }
        },
        PUBLIC {
            @Override
            public Long getStatistics(AssessmentRepository assessmentRepository) {
                var em = Panache.getEntityManager();
                return em.createNativeQuery("SELECT count(DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject')) FROM Assessment a WHERE JSON_EXTRACT(a.assessment_doc, '$.published') = true").getResultStream().count();
            }
        },
        PRIVATE {
            @Override
            public Long getStatistics(AssessmentRepository assessmentRepository) {
                var em = Panache.getEntityManager();
                return em.createNativeQuery("SELECT count(DISTINCT JSON_EXTRACT(a.assessment_doc, '$.subject')) FROM Assessment a WHERE JSON_EXTRACT(a.assessment_doc, '$.published') = false").getResultStream().count();

            }
        };


        public abstract Long getStatistics(AssessmentRepository assessmentRepository);
    }


    public enum Subject {
        TOTAL {
            @Override
            public Long getStatistics(SubjectRepository subjectRepository) {
                return subjectRepository.find("from Subject s").stream().count();
            }
        };

        public abstract Long getStatistics(SubjectRepository subjectRepository);
    }
}



