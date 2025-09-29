<#import "layout/base.ftl" as layout>
<@layout.layout title=title activePath=activePath>
    <div class="bg-white rounded-xl shadow-lg p-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-4 flex items-center">
            <svg class="w-8 h-8 text-blue-600 mr-3" fill="currentColor" viewBox="0 0 20 20">
                <path d="M13 6a3 3 0 11-6 0 3 3 0 016 0zM18 8a2 2 0 11-4 0 2 2 0 014 0zM14 15a4 4 0 00-8 0v3h8v-3z"></path>
            </svg>
            Willkommen im Dropwizard CRM
        </h1>
        <p class="text-gray-600 mb-8">
            Verwalten Sie Kunden, Kontakte und Statusänderungen über eine moderne Dropwizard-Oberfläche.
        </p>
        <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">
            <div class="bg-blue-50 border border-blue-100 rounded-lg p-6">
                <p class="text-sm uppercase tracking-wide text-blue-600 font-semibold">Gesamtanzahl Kunden</p>
                <p class="text-4xl font-bold text-blue-700 mt-3">${customerCount}</p>
            </div>
            <a href="/ui/customers" class="bg-white border border-gray-200 rounded-lg p-6 hover:border-blue-300 transition flex flex-col justify-between">
                <div>
                    <p class="text-sm font-semibold text-gray-500 uppercase tracking-wide">Verwaltung</p>
                    <p class="text-xl font-semibold text-gray-900 mt-3">Kundenliste öffnen</p>
                </div>
                <span class="text-blue-600 mt-4">Zur Übersicht →</span>
            </a>
            <a href="/ui/test-validation" class="bg-white border border-gray-200 rounded-lg p-6 hover:border-purple-300 transition flex flex-col justify-between">
                <div>
                    <p class="text-sm font-semibold text-gray-500 uppercase tracking-wide">Qualitätssicherung</p>
                    <p class="text-xl font-semibold text-gray-900 mt-3">Validierung testen</p>
                </div>
                <span class="text-purple-600 mt-4">Playground öffnen →</span>
            </a>
            <a href="/customers" class="bg-white border border-gray-200 rounded-lg p-6 hover:border-green-300 transition flex flex-col justify-between">
                <div>
                    <p class="text-sm font-semibold text-gray-500 uppercase tracking-wide">REST-API</p>
                    <p class="text-xl font-semibold text-gray-900 mt-3">JSON-Endpunkte testen</p>
                </div>
                <span class="text-green-600 mt-4">API öffnen →</span>
            </a>
        </div>
    </div>
</@layout.layout>