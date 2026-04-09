package com.sussmartassistant.prontuario.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface JpaExameRepository extends JpaRepository<ExameEntity, UUID> {

    @Query("SELECT e FROM ExameEntity e WHERE e.prontuarioId IN " +
           "(SELECT p.id FROM ProntuarioEntity p WHERE p.pacienteId = :pacienteId)")
    Page<ExameEntity> findByPacienteId(@Param("pacienteId") UUID pacienteId, Pageable pageable);

    @Query("SELECT e FROM ExameEntity e WHERE e.prontuarioId IN " +
           "(SELECT p.id FROM ProntuarioEntity p WHERE p.pacienteId = :pacienteId) " +
           "AND LOWER(e.tipo) = LOWER(:tipo)")
    Page<ExameEntity> findByPacienteIdAndTipo(
            @Param("pacienteId") UUID pacienteId,
            @Param("tipo") String tipo,
            Pageable pageable);

    @Query("SELECT e FROM ExameEntity e WHERE e.prontuarioId IN " +
           "(SELECT p.id FROM ProntuarioEntity p WHERE p.pacienteId = :pacienteId) " +
           "AND e.dataRealizacao BETWEEN :inicio AND :fim")
    Page<ExameEntity> findByPacienteIdAndDataRealizacaoBetween(
            @Param("pacienteId") UUID pacienteId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim,
            Pageable pageable);
}
