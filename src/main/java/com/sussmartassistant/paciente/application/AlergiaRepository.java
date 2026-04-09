package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.Alergia;

import java.util.List;
import java.util.UUID;

public interface AlergiaRepository {
    Alergia salvar(Alergia alergia);
    List<Alergia> buscarPorPacienteId(UUID pacienteId);
    boolean existePorPacienteESubstancia(UUID pacienteId, String substancia);
}
