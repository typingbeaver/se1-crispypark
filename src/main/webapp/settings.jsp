<%@ include file="modules/head.jsp" %>
<title> Einstellungen | CrispyPark Parkhaus </title>

<c:set var="title" value="Einstellungen"/>
<%@include file="modules/header.jsp"%>

    <div class="card" style="max-width: 320px">
        <div class="card-header bg-dark text-light">
            <span class="fs-5">
                <span class="fw-bold">Park</span>preise
            </span>
        </div>
        <div class="card-body bg-light">
            <form action="settings" method="post">

                <label for="normal">Normal</label>
                <div class="input-group mb-3">
                    <input type="number" id="normal" name="normal" value="${rateNormal}" min="0" step="5"
                           class="form-control text-end" aria-label="Preis (in Cent)">
                    <div class="input-group-append">
                        <span class="input-group-text"> Cent</span>
                    </div>
                </div>

                <label for="family">Familie</label>
                <div class="input-group mb-3">
                    <input type="number" id="family" name="family" value="${rateFamily}" min="0" step="5"
                           class="form-control text-end" aria-label="Preis (in Cent)">
                    <div class="input-group-append">
                        <span class="input-group-text"> Cent</span>
                    </div>
                </div>

                <label for="member">Kundenkarte</label>
                <div class="input-group mb-3">
                    <input type="number" id="member" name="member" value="${rateMember}" min="0" step="5"
                           class="form-control text-end" aria-label="Preis (in Cent)">
                    <div class="input-group-append">
                        <span class="input-group-text"> Cent</span>
                    </div>
                </div>

                <label for="monthly">Monatlich (á 30 Tage)</label>
                <div class="input-group mb-3">
                    <input type="number" id="monthly" name="monthly" value="${rateMonthly}" min="0" step="5"
                           class="form-control text-end" aria-label="Preis (in Cent)">
                    <div class="input-group-append">
                        <span class="input-group-text"> Cent</span>
                    </div>
                </div>

                <div class="text-end">
                    <input type="submit" value="Preise speichern" class="btn btn-warning">
                </div>

            </form>
        </div>
    </div>

<%@ include file="modules/footer.jsp" %>