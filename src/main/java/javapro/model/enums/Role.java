package javapro.model.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(javapro.model.enums.Permission.USER)),
    MODDERATOR(Set.of(javapro.model.enums.Permission.USER,
            javapro.model.enums.Permission.MODERATE)),
    ADMINISTRATOR(Set.of(javapro.model.enums.Permission.USER,
            javapro.model.enums.Permission.MODERATE,
            javapro.model.enums.Permission.ADMINISTRATE));

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
