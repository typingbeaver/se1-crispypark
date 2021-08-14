package de.hbrs.se1.team28.crispypark.servlets;
/**
 * Created by mkaul2m on 16.04.19.
 */

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter( urlPatterns = "/*" )
public class SimpleCORSFilter implements Filter {

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) res;
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "origin, x-requested-with, content-type");
    chain.doFilter(req, res);
  }

  @Override
  public void destroy() {
    // I just exist.
  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {
    // I just exist.
  }

}

