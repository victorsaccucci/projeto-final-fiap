package com.sussmartassistant.seguranca.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaEventoAuditoriaRepository extends JpaRepository<EventoAuditoriaEntity, UUID> {
    List<EventoAuditoriaEntity> findByProntuarioIdOrderByDataHoraDesc(UUID prontuarioId);
}
