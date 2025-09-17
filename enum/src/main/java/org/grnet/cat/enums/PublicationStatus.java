package org.grnet.cat.enums;

public enum PublicationStatus {
    PUBLISHED("1", "published", true),
    UNPUBLISHED("2", "unpublished", false);

    private final String id;
    private final String label;
    private final boolean flag;

    PublicationStatus(String id, String label, boolean flag) {
        this.id = id;
        this.label = label;
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public boolean asBoolean() {
        return flag;
    }

    public static PublicationStatus fromId(String id) {
        for (PublicationStatus ps : values()) {
            if (ps.id.equals(id)) {
                return ps;
            }
        }
        throw new IllegalArgumentException("Invalid publication_status: " + id);
    }
}
