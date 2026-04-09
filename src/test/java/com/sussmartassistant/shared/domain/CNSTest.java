package com.sussmartassistant.shared.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CNSTest {

    @Test
    void deveCriarCnsValido() {
        CNS cns = new CNS("123456789012345");
        assertEquals("123456789012345", cns.valor());
    }

    @Test
    void deveRejeitarCnsNulo() {
        assertThrows(DomainException.class, () -> new CNS(null));
    }

    @Test
    void deveRejeitarCnsCurto() {
        assertThrows(DomainException.class, () -> new CNS("12345678901234"));
    }

    @Test
    void deveRejeitarCnsLongo() {
        assertThrows(DomainException.class, () -> new CNS("1234567890123456"));
    }

    @Test
    void deveRejeitarCnsComLetras() {
        assertThrows(DomainException.class, () -> new CNS("12345678901234A"));
    }
}
