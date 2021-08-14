# Projektbeschreibung "CrispyPark"
Software Engineering 1 (Prof. Manfred Kaul) - SS 2021  
Team 28 - Niclas Bartsch

> Kurzbeschreibung des erstellten Team-Produkts (max. 5 Seiten mit Markierung der Eigenleistungen): Was kann der Prototyp? Welche Features hat er? Welche Funktionalität? Welche Charts? Welchen Nutzwert für welche Zielgruppe? (Parkkunde, Manager, Finanzamt, etc?)
---

## Features & Funktionalität

### Parkhauskunden
- einfache ticketlose Benutzung des Parkhauses
- aktuelle Informationen über Parkauslastung, Öffnungszeiten & Ticketpreise
- automatische Platzeinweisung
- benutzerfreundlicher Kassenautomat
    - Abrufen des Parkvorgangs ausschließlich über die Eingabe des Kennzeichens
    - Parkplatz vergessen? Kein Problem, Ihre Stellplatznummer ist uns bekannt!

### Parkhausbetreiber
- einfacher, farbig kategorisierter Überblick über die aktuelle Parkauslastung Ihres Parkdecks
  - **_Stellplatzfarben:_**  
    `blau` Behindertenstellplätze  
    `rot` Frauenstellplätze  
    `grau` Standardstellplätze
  - **_Fahrzeugfarben:_**  
    `grau` Standardparker  
    `grün` Familien  
    `rot` Kundenkarteninhaber  
    `gelb` Monatsticketinhaber

#### Statistiken
- aktuelle Daten über das Parkgeschehen (aktueller Tag, Monat, Gesamt)
    - Einnahmen
    - durchschnittlicher Zahlbetrag & Parkdauer
    - Anzahl der Parkvorgänge
- Graphen
    - Parkdauer: Welche Kunden haben wie lange bei Ihnen geparkt?
    - Verteilung der Tickettarife & Fahrertypen
- technische Grundlagen zur Erstellung weiterer Statistiken
    
zudem:
- Datenausgabe über jeden abgeschlossenen Parkvorgang für erweiterte Auswertung und einfache Übermittlung von Informationen an das Finanzamt

#### Einstellungsmöglichkeiten
- Öffnungszeit
- Anzahl der verfügbaren Parkplätze
- Parkpreise
- (eine Änderung der Zahl und Verteilung von Sonderparkplätzen während der Laufzeit in dieser Version leider noch nicht möglich)

---
## verwendete Technologien

### Frontend
- JSP
- JSTL Core (Tag Library)
- ccm.parkhaus mit Plotly.js<br/>
  sowie angepasste Versionen ccm.parkhaus-charts sowie ccm.parkhaus-table 
- Bootstrap 5
- FontAwesome 5

### Backend
- Java
- Jakarta Servlet
- javax.json
- JUnit 5
- Maven (Build-Management-Tool)

### Deployment
- SEPP GitLab (Versionierung)
- SEPP Jenkins & Tomcat 8 (Deployment)
- SEPP SonarQube (Tests & Code Quality) in Verbindung mit Surfire & JaCoCo