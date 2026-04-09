package com.sussmartassistant.paciente.infrastructure;

import com.sussmartassistant.paciente.application.*;
import com.sussmartassistant.paciente.domain.Alergia;
import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import com.sussmartassistant.paciente.domain.Paciente;
import com.sussmartassistant.paciente.infrastructure.dto.*;
import com.sussmartassistant.shared.domain.CNS;
import com.sussmartassistant.shared.domain.CPF;
import com.sussmartassistant.shared.infrastructure.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pacientes")
@Validated
@Tag(name = "Pacientes", description = "Cadastro e gestão de pacientes, alergias e medicamentos")
public class PacienteController {

    private final CadastrarPacienteUseCase cadastrarPaciente;
    private final BuscarPacientePorCpfUseCase buscarPacientePorCpf;
    private final RegistrarAlergiaUseCase registrarAlergia;
    private final ListarAlergiasUseCase listarAlergias;
    private final RegistrarMedicamentoUseCase registrarMedicamento;
    private final ListarMedicamentosAtivosUseCase listarMedicamentosAtivos;
    private final DescontinuarMedicamentoUseCase descontinuarMedicamento;

    public PacienteController(CadastrarPacienteUseCase cadastrarPaciente,
                              BuscarPacientePorCpfUseCase buscarPacientePorCpf,
                              RegistrarAlergiaUseCase registrarAlergia,
                              ListarAlergiasUseCase listarAlergias,
                              RegistrarMedicamentoUseCase registrarMedicamento,
                              ListarMedicamentosAtivosUseCase listarMedicamentosAtivos,
                              DescontinuarMedicamentoUseCase descontinuarMedicamento) {
        this.cadastrarPaciente = cadastrarPaciente;
        this.buscarPacientePorCpf = buscarPacientePorCpf;
        this.registrarAlergia = registrarAlergia;
        this.listarAlergias = listarAlergias;
        this.registrarMedicamento = registrarMedicamento;
        this.listarMedicamentosAtivos = listarMedicamentosAtivos;
        this.descontinuarMedicamento = descontinuarMedicamento;
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastrar paciente", description = "Cadastra um novo paciente e gera prontuário associado")
    @ApiResponse(responseCode = "201", description = "Paciente cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou CPF duplicado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<PacienteResponse> cadastrar(@Valid @RequestBody CadastrarPacienteRequest request) {
        CPF cpf = new CPF(request.cpf());
        CNS cns = request.cns() != null ? new CNS(request.cns()) : null;
        Paciente paciente = cadastrarPaciente.executar(
                request.nome(), cpf, cns, request.dataNascimento(), request.sexo(), request.contato());
        return ResponseEntity.status(HttpStatus.CREATED).body(PacienteResponse.from(paciente));
    }

    @GetMapping
    @Operation(summary = "Buscar paciente por CPF", description = "Retorna os dados cadastrais do paciente")
    @ApiResponse(responseCode = "200", description = "Paciente encontrado")
    @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<PacienteResponse> buscarPorCpf(
            @Parameter(description = "CPF do paciente (11 dígitos)", required = true, example = "52998224725")
            @RequestParam String cpf) {
        CPF cpfVo = new CPF(cpf);
        Paciente paciente = buscarPacientePorCpf.executar(cpfVo);
        return ResponseEntity.ok(PacienteResponse.from(paciente));
    }

    @PostMapping("/{id}/alergias")
    @Operation(summary = "Registrar alergia", description = "Registra uma nova alergia para o paciente")
    @ApiResponse(responseCode = "201", description = "Alergia registrada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou alergia duplicada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<AlergiaResponse> registrarAlergia(
            @PathVariable UUID id,
            @Valid @RequestBody RegistrarAlergiaRequest request) {
        Alergia alergia = registrarAlergia.executar(
                id, request.substancia(), request.gravidade(), request.reacaoObservada(), request.registradoPorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(AlergiaResponse.from(alergia));
    }

    @GetMapping("/{id}/alergias")
    @Operation(summary = "Listar alergias", description = "Lista alergias do paciente ordenadas por gravidade")
    @ApiResponse(responseCode = "200", description = "Lista de alergias")
    @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<List<AlergiaResponse>> listarAlergias(@PathVariable UUID id) {
        List<AlergiaResponse> alergias = listarAlergias.executar(id).stream()
                .map(AlergiaResponse::from)
                .toList();
        return ResponseEntity.ok(alergias);
    }

    @PostMapping("/{id}/medicamentos")
    @Operation(summary = "Registrar medicamento", description = "Registra um novo medicamento em uso para o paciente")
    @ApiResponse(responseCode = "201", description = "Medicamento registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou medicamento duplicado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<MedicamentoResponse> registrarMedicamento(
            @PathVariable UUID id,
            @Valid @RequestBody RegistrarMedicamentoRequest request) {
        MedicamentoEmUso medicamento = registrarMedicamento.executar(
                id, request.nome(), request.dosagem(), request.frequencia(),
                request.dataInicio(), request.registradoPorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(MedicamentoResponse.from(medicamento));
    }

    @GetMapping("/{id}/medicamentos")
    @Operation(summary = "Listar medicamentos ativos", description = "Lista medicamentos ativos do paciente")
    @ApiResponse(responseCode = "200", description = "Lista de medicamentos ativos")
    @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<List<MedicamentoResponse>> listarMedicamentos(@PathVariable UUID id) {
        List<MedicamentoResponse> medicamentos = listarMedicamentosAtivos.executar(id).stream()
                .map(MedicamentoResponse::from)
                .toList();
        return ResponseEntity.ok(medicamentos);
    }

    @PatchMapping("/{id}/medicamentos/{medId}/descontinuar")
    @Operation(summary = "Descontinuar medicamento", description = "Marca um medicamento como descontinuado")
    @ApiResponse(responseCode = "200", description = "Medicamento descontinuado com sucesso")
    @ApiResponse(responseCode = "400", description = "Medicamento já descontinuado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Medicamento não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<MedicamentoResponse> descontinuarMedicamento(
            @PathVariable UUID id,
            @PathVariable UUID medId) {
        MedicamentoEmUso medicamento = descontinuarMedicamento.executar(medId);
        return ResponseEntity.ok(MedicamentoResponse.from(medicamento));
    }
}
