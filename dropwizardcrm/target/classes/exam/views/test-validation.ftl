<#import "layout/base.ftl" as layout>
<@layout.layout title=title activePath=activePath>
    <div class="max-w-5xl mx-auto space-y-8">
        <div class="bg-white p-8 rounded-xl shadow-lg">
            <h1 class="text-3xl font-bold text-gray-900 mb-6">Validierung &amp; Fehlerbehandlung testen</h1>
            <p class="text-gray-600">
                Nutzen Sie dieses Dashboard, um die verschiedenen Validierungswege im Dropwizard CRM zu testen.
                Sie können ungültige Formulardaten senden, gezielt Ausnahmen auslösen oder die REST-API mit
                fehlerhaften Nutzlasten ansprechen.
            </p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div class="bg-white p-6 rounded-xl shadow-lg border border-blue-100">
                <h2 class="text-xl font-semibold text-blue-600 mb-4">Webformular-Test</h2>
                <p class="text-gray-600 mb-4">
                    Senden Sie absichtlich ungültige Formulardaten an den <strong>/ui/customers</strong> Endpunkt.
                    Die Validierungsfehler erscheinen direkt im Formular.
                </p>
                <form action="/ui/customers" method="post" class="space-y-3">
                    <input type="hidden" name="name" value="">
                    <input type="hidden" name="contactPerson" value="">
                    <input type="hidden" name="email" value="invalid-email">
                    <input type="hidden" name="phone" value="abc123">
                    <input type="hidden" name="customerType" value="">
                    <input type="hidden" name="status" value="">
                    <input type="hidden" name="industry" value="This is a very long industry description that exceeds the maximum allowed length of 100 characters and should trigger a validation error">
                    <input type="hidden" name="lastContactDate" value="2025-12-31">
                    <button type="submit" class="w-full bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg transition">
                        Ungültige Formulardaten senden
                    </button>
                </form>
            </div>

            <div class="bg-white p-6 rounded-xl shadow-lg border border-red-100">
                <h2 class="text-xl font-semibold text-red-600 mb-4">Exception-Mappings testen</h2>
                <p class="text-gray-600 mb-4">
                    Diese Aktionen lösen gezielt Exceptions aus und demonstrieren die Dropwizard Exception Mapper.
                </p>
                <div class="space-y-3">
                    <form action="/ui/test-force-error" method="post">
                        <input type="hidden" name="errorType" value="illegal-argument">
                        <button type="submit" class="w-full bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg transition">
                            IllegalArgumentException auslösen
                        </button>
                    </form>
                    <form action="/ui/test-force-error" method="post">
                        <input type="hidden" name="errorType" value="null-pointer">
                        <button type="submit" class="w-full bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg transition">
                            NullPointerException auslösen
                        </button>
                    </form>
                    <form action="/ui/test-force-error" method="post">
                        <input type="hidden" name="errorType" value="generic">
                        <button type="submit" class="w-full bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg transition">
                            RuntimeException auslösen
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <div class="bg-white p-6 rounded-xl shadow-lg border border-green-100">
            <h2 class="text-xl font-semibold text-green-600 mb-4">REST-API Validierung</h2>
            <p class="text-gray-600 mb-4">
                Testen Sie die JSON-Validierung der API. Die folgenden Befehle nutzen die ProblemDetail-Antworten
                des REST-Endpunkts unter <strong>/customers</strong>.
            </p>
            <div class="bg-gray-900 text-green-300 font-mono text-xs rounded-lg p-4 space-y-4 overflow-x-auto">
                <div>
                    <p class="text-gray-400 mb-2"># Ungültiges JSON (Parser-Fehler)</p>
                    <code>curl -X POST http://localhost:8080/customers \</code><br>
                    <code>  -H "Content-Type: application/json" \</code><br>
                    <code>  -d '{"invalid": json}'</code>
                </div>
                <div>
                    <p class="text-gray-400 mb-2"># Validierungsfehler (fehlende Pflichtfelder)</p>
                    <code>curl -X POST http://localhost:8080/customers \</code><br>
                    <code>  -H "Content-Type: application/json" \</code><br>
                    <code>  -d '{}'</code>
                </div>
            </div>
            <a href="/customers" target="_blank" class="inline-block mt-4 bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg transition">
                API-Endpunkt öffnen
            </a>
        </div>

        <div class="bg-yellow-50 border border-yellow-200 rounded-xl p-6">
            <h3 class="text-lg font-semibold text-yellow-900 mb-3">Hinweise</h3>
            <ul class="list-disc list-inside text-yellow-800 space-y-2">
                <li><span class="font-medium">Formularvalidierung:</span> Fehlermeldungen erscheinen direkt im Formular und werden über JSR 380 ausgelöst.</li>
                <li><span class="font-medium">Exception Mapper:</span> Die roten Buttons zeigen die HTML-Debug-Seite, die von den Dropwizard Exception Mappern gerendert wird.</li>
                <li><span class="font-medium">REST-API:</span> JSON-Anfragen verwenden ProblemDetail-Antworten und demonstrieren die Fehlerstruktur für Clients.</li>
            </ul>
        </div>
    </div>
</@layout.layout>
