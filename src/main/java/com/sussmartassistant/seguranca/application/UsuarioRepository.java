package com.sussmartassistant.seguranca.application;

import com.sussmartassistant.seguranca.domain.Usuario;

import java.util.Optional;

/**
 * Port de repositório para a entidade Usuario.
 */
public interface UsuarioRepository {
    Optional<Usuario> buscarPorUsername(String username);
    Usuario salvar(Usuario usuario);
}
