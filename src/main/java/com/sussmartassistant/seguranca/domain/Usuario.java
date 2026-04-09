package com.sussmartassistant.seguranca.domain;

import com.sussmartassistant.shared.domain.Role;

import java.util.UUID;

/**
 * Entidade de domínio representando um usuário do sistema.
 * POJO puro — sem dependências de framework.
 */
public class Usuario {

    private UUID id;
    private String username;
    private String senhaHash;
    private Role role;
    private UUID referenciaId;
    private boolean ativo;

    public Usuario() {
    }

    public Usuario(UUID id, String username, String senhaHash, Role role, UUID referenciaId, boolean ativo) {
        this.id = id;
        this.username = username;
        this.senhaHash = senhaHash;
        this.role = role;
        this.referenciaId = referenciaId;
        this.ativo = ativo;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public UUID getReferenciaId() { return referenciaId; }
    public void setReferenciaId(UUID referenciaId) { this.referenciaId = referenciaId; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
