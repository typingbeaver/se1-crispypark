<%@ include file="modules/head.jsp" %>
<title> Kasse | CrispyPark Parkhaus </title>

<c:set var="title" value="Kasse"/>
<%@include file="modules/header.jsp"%>

    <c:if test="${error != null}">
        <div class="alert alert-danger" role="alert" style="max-width: 600px">
            <i class="fas fa-exclamation-circle me-2" aria-hidden="true"></i>
            ${error}
        </div>
    </c:if>

    <div class="card text-center" style="max-width: 600px">
        <div class="card-header bg-dark text-light">
            <span class="fs-3">
                <span class="fw-bold">Park</span>automat
            </span>
        </div>

        <%-- Kennzeicheneingabe --%>
        <div class="card-body">
            <form action="pay" method="get" autocomplete="off">
                <div class="input-group mx-auto" style="max-width: 460px">
                    <span class="input-group-text"> Kennzeichen </span>
                    <input type="text" name="license" placeholder="AB-C 123" value="${license}"
                           pattern="^[A-Z]{1,3}-[A-Z]{1,2} [1-9]{1}\d{0,3}$" title="Kennzeichen"
                           aria-label="Kennzeichen" autofocus required class="form-control text-center">
                    <input type="submit" value="Gebühren abrufen" class="btn btn-warning">
                </div>
            </form>
        </div>

        <%-- Parkinformationen --%>
        <c:if test="${error == null && license != null}">
            <div class="card-body">
                <h5 class=""> Stellplatz Nr. <span class="font-monospace">${carSpace}</span> </h5>
                <p>
                    Sie parken zum aktuellen Zeitpunkt seit <br/>
                    <span class="font-monospace">${ticketDuration}</span> <br/>
                    als <span class="font-monospace">${ticketType}</span>.
                </p>
                <div class="pb-3">
                    <h5 class="pb-1">Ihr voraussichtlicher Zahlbetrag beträgt aktuell:</h5>
                    <div class="input-group input-group-lg mx-auto" style="max-width: 180px">
                        <input name="carPrice" value="${ticketPrice}"
                               class="pe-2 text-end text-muted fw-bold fs-5 font-monospace" style="width: 100px" readonly/>
                        <span class="input-group-text" style="width: 80px"> Euro </span>
                    </div>
                </div>
            </div>
            <div class="card-footer">
                <button onclick="post_leave(${carNo})" class="btn btn-dark">Bezahlen und ausfahren</button>
            </div>
        </c:if>
    </div>

    <script>
        function post_leave( no ) {
            const xhr = new XMLHttpRequest();
            xhr.open('POST', './');
            xhr.setRequestHeader('Content-Type', 'text/plain');
            xhr.send('leave,' + no);
            xhr.onload = () => {
                window.location.replace("./");
            }
        }
    </script>

<%@ include file="modules/footer.jsp" %>