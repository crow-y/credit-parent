package tech.fullink.credit.tools.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author perdonare 2017/3/24.
 */
public class SchedulerFilter extends OncePerRequestFilter implements Ordered {
    private int order = 2147413132;
    @Autowired
    private final SchedulerManager schedulerManager;

    public SchedulerFilter(SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getRequestURI();
        if (schedulerManager.include(requestPath)) {
            schedulerManager.schedule(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }


    @Override
    public int getOrder() {
        return this.order;
    }

}
