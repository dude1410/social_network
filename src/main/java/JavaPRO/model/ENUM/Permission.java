package JavaPRO.model.ENUM;

public enum Permission {
    USER("user:write"),
    MODERATE("user:moderate"),
    ADMINISTRATE("user:administrate");

    private final String accessPermission;

    Permission(String accessPermission) {
        this.accessPermission = accessPermission;
    }

    public String getAccessPermission() {
        return accessPermission;
    }
}
