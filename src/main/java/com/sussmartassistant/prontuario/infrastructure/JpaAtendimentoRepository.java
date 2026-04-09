package com.sussmartassistant.prontuario.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface JpaAtendimentoRepository extends JpaRepository<RegistroAtendimentoEntity, UUID> {

    @Query("SELECT a FROM RegistroAtendimentoEntity a WHERE a.prontuarioId IN " +
           "(SELECT p.id FROM ProntuarioEntity p WHERE p.pacienteId = :pacienteId)")
    Page<RegistroAtendimentoEntity> findByPacienteId(@Param("pacienteId") UUID pacienteId, Pageable pageable);

    @Query("SELECT a FROM RegistroAtendimentoEntity a WHERE a.prontuarioId IN " +
           "(SELECT p.id FROM ProntuarioEntity p WHERE p.pacienteId = :pacienteId) " +
           "AND a.data BETWEEN :inicio AND :fim")
    Page<RegistroAtendimentoEntity> findByPacienteIdAndDataBetween(
            @Param("pacienteId") UUID pacienteId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim,
            Pageable pageable);

    @Query("SELECT a FROM RegistroAtendimentoEntity a WHERE a.prontuarioId IN " +
           "(SELECT p.id FROM ProntuarioEntity p WHERE p.pacienteId = :pacienteId) " +
           "AND a.diagnosticoCid = :cid")
    Page<RegistroAtendimentoEntity> findByPacienteIdAndDiagnosticoCid(
            @Param("pacienteId") UUID pacienteId,
            @Param("cid") String cid,
            Pageable pageable);
}
