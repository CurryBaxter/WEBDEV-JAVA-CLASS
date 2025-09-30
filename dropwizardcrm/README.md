# Dropwizard CRM System

Ein schlankes CRM-System basierend auf Dropwizard Framework.

## Funktionen

- Kundenverwaltung mit vollständigem CRUD
- Weboberfläche mit FreeMarker Templates
- REST API mit JSON-Responses
- PostgreSQL Datenbankanbindung
- Validierung und Fehlerbehandlung
- Suche und Filterung

## Endpoints

### Web-Interface (HTML)
- `GET /ui` - Startseite
- `GET /ui/customers` - Kundenliste
- `GET /ui/customers/new` - Neuen Kunden anlegen
- `GET /ui/customers/{id}` - Kundendetails
- `GET /ui/customers/{id}/edit` - Kunde bearbeiten
- `POST /ui/customers` - Kunde erstellen
- `POST /ui/customers/{id}` - Kunde aktualisieren
- `POST /ui/customers/{id}/delete` - Kunde löschen
- `GET /ui/test-validation` - Validierung testen

### REST API (JSON)
- `GET /customers` - Alle Kunden (mit Suche `?q=term`)
- `GET /customers/{id}` - Kunde per ID
- `POST /customers` - Neuen Kunden erstellen
- `PUT /customers/{id}` - Kunde aktualisieren
- `DELETE /customers/{id}` - Kunde löschen

## Build & Start

```bash
# Build
mvn clean package

# Start (benötigt PostgreSQL auf localhost:5432)
java -jar target/customer-1.0-SNAPSHOT.jar server config.yml
```

**Zugriff:** http://localhost:8080/ui  
**API:** http://localhost:8080/customers