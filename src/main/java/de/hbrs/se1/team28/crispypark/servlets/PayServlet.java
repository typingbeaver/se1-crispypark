package de.hbrs.se1.team28.crispypark.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "PayServlet", value = "/pay")
public class PayServlet extends HttpServlet {

    /**
     * HTTP GET
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (request.getParameterMap().containsKey("license")) {
            try {
                // fetch car data from ParkhausServlet
                request.getRequestDispatcher("/").include(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Technische Probleme - Datenabruf nicht möglich.");
            }
        }

        request.getRequestDispatcher("/pay.jsp").forward(request, response);
    }

    /**
     * HTTP POST
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

    }



}
