package com.sussmartassistant.shared.infrastructure;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CorrelationIdFilterTest {

    private CorrelationIdFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new CorrelationIdFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
    }

    @Test
    void deveGerarCorrelationIdQuandoHeaderAusente() throws ServletException, IOException {
        filter.doFilterInternal(request, response, filterChain);

        String correlationId = response.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER);
        assertNotNull(correlationId);
        assertFalse(correlationId.isBlank());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void deveUsarCorrelationIdDoHeaderQuandoPresente() throws ServletException, IOException {
        String expectedId = "meu-correlation-id-123";
        request.addHeader(CorrelationIdFilter.CORRELATION_ID_HEADER, expectedId);

        filter.doFilterInternal(request, response, filterChain);

        assertEquals(expectedId, response.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void deveLimparMdcAposRequisicao() throws ServletException, IOException {
        filter.doFilterInternal(request, response, filterChain);

        assertNull(MDC.get(CorrelationIdFilter.CORRELATION_ID_KEY));
    }

    @Test
    void deveLimparMdcMesmoQuandoExcecaoOcorre() throws ServletException, IOException {
        doThrow(new ServletException("erro")).when(filterChain).doFilter(request, response);

        assertThrows(ServletException.class,
                () -> filter.doFilterInternal(request, response, filterChain));

        assertNull(MDC.get(CorrelationIdFilter.CORRELATION_ID_KEY));
    }
}
