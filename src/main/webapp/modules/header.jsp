<%--
  header of webpage with navigation
--%>
</head>
<body>

<div class="bg-light">
    <div class="container px-2 py-4">
        <%-- Header --%>
        <div class="row align-items-center px-4 pt-3 pb-4 bg-dark rounded-3 text-light">
            <div class="col">
                <h1>
                    <i class="fas fa-cookie-bite text-warning h-auto me-1" aria-hidden="true"></i>
                    <span class="fw-bold">CrispyPark</span>
                    Parkhaus
                </h1>
            </div>
            <div class="col-4 text-end">
                <p>
                    SE1 Team 28<br>
                    <em>running on <%= application.getServerInfo() %></em>
                </p>
            </div>
        </div>

        <%-- Navigation --%>
        <div class="d-inline-flex shadow bg-white px-3 pt-2 pb-3 mb-4 rounded" style="transform: translateY(-1.4rem)">
            <div>
                <a class="btn btn-success mt-1" href="./">
                    <i class="fas fa-parking me-2" aria-hidden="true"></i>Home</a>
                <a class="btn btn-outline-success mt-1" href="pay">
                    <i class="fas fa-money-bill-wave me-2" aria-hidden="true"></i>Kasse</a>
            </div>
            <div class="ms-4">
                <a class="btn btn-outline-primary mt-1" href="stats">
                    <i class="fas fa-chart-pie me-2" aria-hidden="true"></i>Statistiken</a>
                <a class="btn btn-outline-primary mt-1" href="settings">
                    <i class="fas fa-tools me-2" aria-hidden="true"></i>Einstellungen</a>
            </div>
        </div>

        <%-- Main Body --%>
        <div class="">
            <h2>${title}</h2>
            <div class="shadow-lg px-3 py-4 bg-body rounded-1">