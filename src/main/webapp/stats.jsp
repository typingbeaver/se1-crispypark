<%@ include file="modules/head.jsp" %>
    <title> Statistiken | CrispyPark Parkhaus </title>

    <script src="assets/js/ccm.parkhaus-charts-1.0.0.js"></script>
    <script src="assets/js/ccm.parkhaus-table-1.0.0.js"></script>

<c:set var="title" value="Statistiken"/>
<%@include file="modules/header.jsp"%>

    <c:set var="server_url" value="./"/>
    <c:set var="css" value="assets/css/parkhaus.css"/>

    <%-- DATEN --%>
    <div class="row gx-2">
        <%-- TAG --%>
        <div class="col-4">
            <div class="card">
                <div class="card-header bg-dark text-light">
                    <span class="fw-bold">Park</span>Daten &ensp;<span class="fw-bold text-warning">Heute</span>
                </div>
                <div class="card-body">
                    <ccm-parkhaus-charts-1-0-0
                            server_url = "${server_url}"
                            css.1 = "${css}"
                            extra_buttons = '[
                                {   "extra_class": "priceSumDay",
                                    "extra_inner":"Einnahmen",
                                    "extra_popup_title":"Summe aller Parkgebühren"
                                },
                                {   "extra_class": "priceAvgDay",
                                    "extra_inner":"durchschnittlicher Parkpreis",
                                    "extra_popup_title":"Durchschnitt aller Parkgebühren"
                                },
                                {   "extra_class": "durationAvgDay",
                                    "extra_inner":"durchschnittliche Parkdauer",
                                    "extra_popup_title":"Durchschnitt aller Parkdauern"
                                },
                                {   "extra_class": "transactionsDay",
                                    "extra_inner":"Transaktionen",
                                    "extra_popup_title":"Zahl der abgeschlossenen Parkvorgänge"
                                }
                            ]'
                    ></ccm-parkhaus-charts-1-0-0>
                </div>
            </div>
        </div>
        <%-- MONAT --%>
        <div class="col-4">
            <div class="card">
                <div class="card-header bg-dark text-light">
                    <span class="fw-bold">Park</span>Daten &ensp;<span class="fw-bold text-warning">Monat</span>
                </div>
                <div class="card-body">
                    <ccm-parkhaus-charts-1-0-0
                            server_url = "${server_url}"
                            css.1 = "${css}"
                            extra_buttons = '[
                                {   "extra_class": "priceSumMonth",
                                    "extra_inner":"Einnahmen",
                                    "extra_popup_title":"Summe aller Parkgebühren"
                                },
                                {   "extra_class": "priceAvgMonth",
                                    "extra_inner":"durchschnittlicher Parkpreis",
                                    "extra_popup_title":"Durchschnitt aller Parkgebühren"
                                },
                                {   "extra_class": "durationAvgMonth",
                                    "extra_inner":"durchschnittliche Parkdauer",
                                    "extra_popup_title":"Durchschnitt aller Parkdauern"
                                },
                                {   "extra_class": "transactionsMonth",
                                    "extra_inner":"Transaktionen",
                                    "extra_popup_title":"Zahl der abgeschlossenen Parkvorgänge"
                                }
                            ]'
                    ></ccm-parkhaus-charts-1-0-0>
                </div>
            </div>
        </div>
        <%-- GESAMT --%>
        <div class="col-4">
            <div class="card">
                <div class="card-header bg-dark text-light">
                    <span class="fw-bold">Park</span>Daten &ensp;<span class="fw-bold text-warning">Gesamt</span>
                </div>
                <div class="card-body">
                    <ccm-parkhaus-charts-1-0-0
                            server_url = "${server_url}"
                            css.1 = "${css}"
                            extra_buttons = '[
                                {   "extra_class": "priceSumAll",
                                    "extra_inner":"Einnahmen",
                                    "extra_popup_title":"Summe aller Parkgebühren"
                                },
                                {   "extra_class": "priceAvgAll",
                                    "extra_inner":"durchschnittlicher Parkpreis",
                                    "extra_popup_title":"Durchschnitt aller Parkgebühren"
                                },
                                {   "extra_class": "durationAvgAll",
                                    "extra_inner":"durchschnittliche Parkdauer",
                                    "extra_popup_title":"Durchschnitt aller Parkdauern"
                                },
                                {   "extra_class": "transactionsAll",
                                    "extra_inner":"Transaktionen",
                                    "extra_popup_title":"Zahl der abgeschlossenen Parkvorgänge"
                                }
                            ]'
                    ></ccm-parkhaus-charts-1-0-0>
                </div>
            </div>
        </div>
    </div>

    <%-- GRAPHEN --%>
    <div class="row card mt-4 mx-1">
        <div class="card-header bg-dark text-light">
            <span class="fw-bold">Park</span>Graphen
        </div>
        <div class="card-body">
            <ccm-parkhaus-charts-1-0-0
                    server_url = "${server_url}"
                    css.1 = "${css}"
                    extra_charts = '[
                                {   "extra_class": "driverDurationChart",
                                    "extra_inner":"Parkdauer nach Kunden",
                                    "extra_popup_title":""
                                },
                                {   "extra_class": "ticketChart",
                                    "extra_inner":"Verteilung Ticket-Typen",
                                    "extra_popup_title":""
                                },
                                {   "extra_class": "driverChart",
                                    "extra_inner":"Verteilung Kunden-Typen",
                                    "extra_popup_title":""
                                }
                            ]'
            ></ccm-parkhaus-charts-1-0-0>
        </div>
    </div>

    <%-- TABELLE --%>
    <div class="row card mt-4 mx-1">
        <div class="card-header bg-dark text-light">
            <span class="fw-bold">Park</span>Liste
        </div>
        <div class="card-body">
            <ccm-parkhaus-table-1-0-0
                    server_url = "${server_url}"
                    name = "CrispyPark"
                    css.1 = "${css}"
            ></ccm-parkhaus-table-1-0-0>
        </div>
    </div>

<%@ include file="modules/footer.jsp" %>