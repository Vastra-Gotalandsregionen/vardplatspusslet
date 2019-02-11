package se.vgregion.vardplatspusslet.intsvc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class ErrorHandlerFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlerFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void destroy() {

    }
}
