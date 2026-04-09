package com.sussmartassistant.shared.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPFTest {

    @Test
    void deveCriarCpfValido() {
        // CPF válido: 529.982.247-25
        CPF cpf = new CPF("52998224725");
        assertEquals("52998224725", cpf.valor());
    }

    @Test
    void deveCriarCpfValidoComFormatacao() {
        CPF cpf = new CPF("529.982.247-25");
        assertEquals("529.982.247-25", cpf.valor());
    }

    @Test
    void deveRejeitarCpfNulo() {
        assertThrows(DomainException.class, () -> new CPF(null));
    }

    @Test
    void deveRejeitarCpfComTamanhoErrado() {
        assertThrows(DomainException.class, () -> new CPF("1234567890"));
    }

    @Test
    void deveRejeitarCpfComDigitosIguais() {
        assertThrows(DomainException.class, () -> new CPF("11111111111"));
        assertThrows(DomainException.class, () -> new CPF("00000000000"));
    }

    @Test
    void deveRejeitarCpfComDigitoVerificadorInvalido() {
        // 529.982.247-25 é válido; 529.982.247-26 é inválido
        assertThrows(DomainException.class, () -> new CPF("52998224726"));
    }

    @Test
    void deveRejeitarCpfComLetras() {
        assertThrows(DomainException.class, () -> new CPF("5299822472A"));
    }

    @Test
    void deveValidarOutroCpfConhecido() {
        // CPF válido: 111.444.777-35
        CPF cpf = new CPF("11144477735");
        assertEquals("11144477735", cpf.valor());
    }
}
