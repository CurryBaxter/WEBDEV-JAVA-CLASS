# Spring Boot CRM System

Ein modernes CRM-System basierend auf Spring Boot Framework.

## Funktionen

- Kundenverwaltung mit vollständigem CRUD
- Weboberfläche mit Thymeleaf Templates
- REST API mit JSON-Responses
- PostgreSQL Datenbankanbindung mit JPA
- Bean Validation und Fehlerbehandlung
- Suche und Filterung

## Endpoints

### Web-Interface (HTML)
- `GET /` - Startseite
- `GET /customers` - Kundenliste (mit Suche `?q=term`)
- `GET /customers/new` - Neuen Kunden anlegen
- `GET /customers/{id}` - Kundendetails
- `GET /customers/{id}/edit` - Kunde bearbeiten
- `POST /customers` - Kunde erstellen
- `POST /customers/{id}` - Kunde aktualisieren
- `POST /customers/{id}/delete` - Kunde löschen
- `GET /test-validation` - Validierung testen

### REST API (JSON)
- `GET /api/customers` - Alle Kunden (mit Suche `?q=term`)
- `GET /api/customers/{id}` - Kunde per ID
- `POST /api/customers` - Neuen Kunden erstellen
- `PUT /api/customers/{id}` - Kunde aktualisieren
- `DELETE /api/customers/{id}` - Kunde löschen

### Monitoring
- `GET /actuator/health` - Systemstatus

## Build & Start

```bash
# Build
mvn clean package

# Start (benötigt PostgreSQL auf localhost:5432)
java -jar target/springcrm-0.0.1-SNAPSHOT.jar
```

**Zugriff:** http://localhost:8082  
**API:** http://localhost:8082/api/customers