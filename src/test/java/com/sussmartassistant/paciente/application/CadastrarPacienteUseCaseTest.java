package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.Paciente;
import com.sussmartassistant.shared.domain.CNS;
import com.sussmartassistant.shared.domain.CPF;
import com.sussmartassistant.shared.domain.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class CadastrarPacienteUseCaseTest {

    private PacienteRepository pacienteRepository;
    private AtomicReference<UUID> callbackPacienteId;
    private CadastrarPacienteUseCase useCase;

    private static final CPF CPF_VALIDO = new CPF("52998224725");
    private static final CNS CNS_VALIDO = new CNS("198765432100001");

    @BeforeEach
    void setUp() {
        callbackPacienteId = new AtomicReference<>();

        pacienteRepository = new PacienteRepository() {
            private Paciente salvo;

            @Override
            public Paciente salvar(Paciente paciente) {
                this.salvo = paciente;
                return paciente;
            }

            @Override
            public Optional<Paciente> buscarPorCpf(CPF cpf) {
                return salvo != null && salvo.getCpf().equals(cpf) ? Optional.of(salvo) : Optional.empty();
            }

            @Override
            public Optional<Paciente> buscarPorId(UUID id) {
                return salvo != null && salvo.getId().equals(id) ? Optional.of(salvo) : Optional.empty();
            }

            @Override
            public boolean existePorCpf(CPF cpf) {
                return salvo != null && salvo.getCpf().valor().equals(cpf.valor());
            }
        };

        PacienteCadastradoCallback callback = callbackPacienteId::set;
        useCase = new CadastrarPacienteUseCase(pacienteRepository, callback);
    }

    @Test
    void deveCadastrarPacienteComSucesso() {
        Paciente resultado = useCase.executar("Maria Silva", CPF_VALIDO, CNS_VALIDO,
                LocalDate.of(1985, 3, 15), "F", "(11) 98765-4321");

        assertNotNull(resultado.getId());
        assertEquals("Maria Silva", resultado.getNome());
        assertEquals("52998224725", resultado.getCpf().valor());
    }

    @Test
    void deveChamarCallbackAposCadastro() {
        Paciente resultado = useCase.executar("Maria Silva", CPF_VALIDO, CNS_VALIDO,
                LocalDate.of(1985, 3, 15), "F", "(11) 98765-4321");

        assertEquals(resultado.getId(), callbackPacienteId.get());
    }

    @Test
    void deveRejeitarCpfDuplicado() {
        useCase.executar("Maria Silva", CPF_VALIDO, CNS_VALIDO,
                LocalDate.of(1985, 3, 15), "F", "(11) 98765-4321");

        assertThrows(DomainException.class, () ->
                useCase.executar("Outra Pessoa", CPF_VALIDO, new CNS("198765432100002"),
                        LocalDate.of(1990, 1, 1), "M", "(11) 91234-5678"));
    }
}
