package com.gestorsolar.backend.security.services;

import com.gestorsolar.backend.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Getter
    private Long id;
    private String correo;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean verificado;

    public UserDetailsImpl(Long id, String correo, String password, Collection<? extends GrantedAuthority> authorities, boolean verificado) {
        this.id = id;
        this.correo = correo;
        this.password = password;
        this.authorities = authorities;
        this.verificado = verificado;
    }

    public static UserDetailsImpl build(Usuario usuario) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(usuario.getRol()));

        return new UserDetailsImpl(
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getContrasena(),
                authorities,
                usuario.isVerificado()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return correo; // Usamos el correo como username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return verificado; // Simplificado por ahora mientras se prueba, pero debería ser el valor real
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
