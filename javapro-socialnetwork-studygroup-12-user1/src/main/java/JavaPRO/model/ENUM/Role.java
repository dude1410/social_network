package JavaPRO.model.ENUM;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(JavaPRO.model.ENUM.Permission.USER)),
    MODDERATOR(Set.of(JavaPRO.model.ENUM.Permission.USER,
            JavaPRO.model.ENUM.Permission.MODERATE)),
    ADMINISTRATOR(Set.of(JavaPRO.model.ENUM.Permission.USER,
            JavaPRO.model.ENUM.Permission.MODERATE,
            JavaPRO.model.ENUM.Permission.ADMINISTRATE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorites() {
        return permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getAccessPermission()))
                .collect(Collectors.toSet());
    }
}
