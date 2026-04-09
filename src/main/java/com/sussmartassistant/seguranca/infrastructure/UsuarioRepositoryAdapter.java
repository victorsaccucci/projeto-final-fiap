package com.sussmartassistant.seguranca.infrastructure;

import com.sussmartassistant.seguranca.application.UsuarioRepository;
import com.sussmartassistant.seguranca.domain.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final JpaUsuarioRepository jpa;

    public UsuarioRepositoryAdapter(JpaUsuarioRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return jpa.findByUsername(username).map(UsuarioEntity::toDomain);
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity entity = UsuarioEntity.fromDomain(usuario);
        return jpa.save(entity).toDomain();
    }
}
