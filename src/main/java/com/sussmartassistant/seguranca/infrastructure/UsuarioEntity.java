package com.sussmartassistant.seguranca.infrastructure;

import com.sussmartassistant.seguranca.domain.Usuario;
import com.sussmartassistant.shared.domain.Role;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private UUID referenciaId;

    @Column(nullable = false)
    private boolean ativo;

    public UsuarioEntity() {}

    public static UsuarioEntity fromDomain(Usuario u) {
        UsuarioEntity e = new UsuarioEntity();
        e.id = u.getId();
        e.username = u.getUsername();
        e.senhaHash = u.getSenhaHash();
        e.role = u.getRole();
        e.referenciaId = u.getReferenciaId();
        e.ativo = u.isAtivo();
        return e;
    }

    public Usuario toDomain() {
        return new Usuario(id, username, senhaHash, role, referenciaId, ativo);
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getSenhaHash() { return senhaHash; }
    public Role getRole() { return role; }
    public UUID getReferenciaId() { return referenciaId; }
    public boolean isAtivo() { return ativo; }
}
