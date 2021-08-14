package de.hbrs.se1.team28.crispypark.servlets;

import de.hbrs.se1.team28.crispypark.TicketType;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "SettingsServlet", value = "/settings")
public class SettingsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        setRateAttributes(request);
        request.getRequestDispatcher("/settings.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TicketType.NORMAL.setRate( Integer.parseInt(request.getParameter("normal")) );
        TicketType.FAMILY.setRate( Integer.parseInt(request.getParameter("family")) );
        TicketType.MEMBER.setRate( Integer.parseInt(request.getParameter("member")) );
        TicketType.MONTHLY.setRate( Integer.parseInt(request.getParameter("monthly")) );

        setRateAttributes(request);
        request.getRequestDispatcher("/settings.jsp").forward(request, response);

    }

    private void setRateAttributes(HttpServletRequest request) {
        request.setAttribute("rateNormal", TicketType.NORMAL.getRate());
        request.setAttribute("rateFamily", TicketType.FAMILY.getRate());
        request.setAttribute("rateMember", TicketType.MEMBER.getRate());
        request.setAttribute("rateMonthly", TicketType.MONTHLY.getRate());
    }
}
