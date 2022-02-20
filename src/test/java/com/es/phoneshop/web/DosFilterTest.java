package com.es.phoneshop.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;
    @Mock
    FilterConfig filterConfig;
    @Spy
    private DosFilter filter = new DosFilter();

    @Before
    public void setup() throws ServletException {
        filter.init(filterConfig);
        when(request.getRemoteAddr()).thenReturn("localhost");
    }

    @Test
    public void testDoFilter() throws ServletException, IOException {
        filter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDosAttack() throws ServletException, IOException {
        for (int i = 0; i < 21; i++) {
            filter.doFilter(request, response, filterChain);
        }
        verify(response).setStatus(429);

    }
}