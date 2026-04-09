package com.sussmartassistant.seguranca.infrastructure;

import com.sussmartassistant.seguranca.application.UsuarioRepository;
import com.sussmartassistant.seguranca.domain.Usuario;
import com.sussmartassistant.seguranca.infrastructure.dto.LoginRequest;
import com.sussmartassistant.seguranca.infrastructure.dto.LoginResponse;
import com.sussmartassistant.shared.infrastructure.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação", description = "Login e geração de token JWT")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository,
                          JwtTokenProvider jwtTokenProvider,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentica usuário e retorna token JWT")
    @ApiResponse(responseCode = "200", description = "Login bem-sucedido")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.buscarPorUsername(request.username())
                .filter(u -> u.isAtivo() && passwordEncoder.matches(request.senha(), u.getSenhaHash()))
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException(
                        "Credenciais inválidas"));

        String token = jwtTokenProvider.generateToken(
                usuario.getUsername(),
                usuario.getRole().name(),
                usuario.getId(),
                usuario.getReferenciaId());

        return ResponseEntity.ok(new LoginResponse(token, usuario.getRole().name(), usuario.getUsername()));
    }
}
