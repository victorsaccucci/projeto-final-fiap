package com.sussmartassistant.seguranca.infrastructure;

import java.util.UUID;

/**
 * Objeto principal armazenado no SecurityContext após autenticação JWT.
 */
public record UsuarioAutenticado(
        UUID userId,
        String username,
        String role,
        UUID referenciaId
) {}
