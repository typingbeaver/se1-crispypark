<%@ include file="modules/head.jsp" %>
    <title> CrispyPark Parkhaus </title>

    <%-- Parkhaus-Komponente --%>
    <script src="assets/js/ccm.parkhaus-10.6.3.js"></script>

<c:set var="title" value="Parkhaus"/>
<%@include file="modules/header.jsp"%>

    <div class="row">
        <div class="col-lg-10">
            <ccm-parkhaus-10-6-3 server_url = "./"
                                 name = "CrispyPark"
                                 license_max = "128"
                                 client_categories = '["NORMAL", "FAMILY", "MEMBER", "MONTHLY"]'
                                 vehicle_types = '["NORMAL", "NORMAL", "NORMAL", "DISABLED", "WOMEN"]'
                                 images.car = "assets/img/car.png"
                                 images.parking_garage = "assets/img/parking.png"
                                 <%-- I don't work and I dont know why. | https://fontawesome.com/v5.15/icons/car --%>
                                 car = '<%@include file="assets/img/car-svg.min.json"%>'
                                 css.1 = "assets/css/parkhaus.css"
                                 space_color = '${spaceColoring}'
                                 no_keyboard="true"
                                 hide_table="true"
            ></ccm-parkhaus-10-6-3>
        </div>

        <%-- PARKPREISE --%>
        <div class="col-12 col-lg-2 pt-4 pt-lg-0">
            <div class="row">
                <div class="col-6 col-lg-12">
                    <div class="card">
                        <div class="card-header bg-dark text-light text-center">
                            <h5 class="">Unsere Parkpreise</h5>
                            <h6 class="fw-light fst-italic">je angefangene Stunde</h6>
                        </div>
                        <ul class="list-group list-group-flush">
                            <li class="list-group-item">
                                <h6 class="mt-2">Standardtarif</h6>
                                <p class="text-muted text-end">${rateNormal} &euro;</p>
                            </li>
                            <li class="list-group-item">
                                <h6 class="mt-2">Familientarif</h6>
                                <p class="text-muted text-end">${rateFamily} &euro;</p>
                            </li>
                            <li class="list-group-item">
                                <h6 class="mt-2">Kundenkartentarif</h6>
                                <p class="text-muted text-end">${rateMember} &euro;</p>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="col-6 col-lg-12">
                    <div class="card mt-lg-4">
                        <div class="card-header bg-dark text-light text-center">
                            <h5 class="">Unsere Zeittickets</h5>
                        </div>
                        <ul class="list-group list-group-flush">
                            <li class="list-group-item">
                                <h6 class="mt-2">Monatsticket <br> (30 Tage)</h6>
                                <p class="text-muted text-end">${rateMonthly} &euro;</p>
                                <h6 class="mt-4">dann pro Parkvorgang</h6>
                                <p class="text-muted text-end">0,00 &euro;</p>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

<%@ include file="modules/footer.jsp" %>