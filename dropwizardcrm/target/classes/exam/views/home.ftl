<#import "layout/base.ftl" as layout>
<@layout.layout title=title activePath=activePath>
    <div class="space-y-8">
        <div>
            <h1 class="text-3xl font-semibold text-primary mb-3 flex items-center">
                <svg class="w-8 h-8 text-secondary mr-3" fill="currentColor" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
                    <path d="M496 448H16c-8.84 0-16 7.16-16 16v32c0 8.84 7.16 16 16 16h480c8.84 0 16-7.16 16-16v-32c0-8.84-7.16-16-16-16zm-304-64l-64-32 64-32 32-64 32 64 64 32-64 32-16 32h208l-86.41-201.63a63.955 63.955 0 0 1-1.89-45.45L416 0 228.42 107.19a127.989 127.989 0 0 0-53.46 59.15L64 416h144l-16-32zm64-224l16-32 16 32 32 16-32 16-16 32-16-32-32-16 32-16z"/>
                </svg>
                Willkommen im Dropwizard CRM
            </h1>
            <p class="text-muted/80 max-w-2xl">
                Verwalten Sie Kunden, Kontakte und Statusänderungen über eine schlanke Dropwizard-Oberfläche.
            </p>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">
            <div class="rounded-2xl border border-border bg-white p-6">
                <p class="text-xs uppercase tracking-[0.25em] text-muted/70">Gesamtanzahl Kunden</p>
                <p class="text-4xl font-semibold text-primary mt-4">${customerCount}</p>
            </div>
            <a href="/ui/customers" class="rounded-2xl border border-border bg-white p-6 hover:border-secondary transition flex flex-col justify-between">
                <div>
                    <p class="text-xs font-semibold text-muted uppercase tracking-[0.25em]">Verwaltung</p>
                    <p class="text-xl font-semibold text-primary mt-3">Kundenliste öffnen</p>
                </div>
                <span class="text-secondary mt-4 font-medium">Zur Übersicht →</span>
            </a>
            <a href="/ui/test-validation" class="rounded-2xl border border-border bg-white p-6 hover:border-secondary transition flex flex-col justify-between">
                <div>
                    <p class="text-xs font-semibold text-muted uppercase tracking-[0.25em]">Qualitätssicherung</p>
                    <p class="text-xl font-semibold text-primary mt-3">Validierung testen</p>
                </div>
                <span class="text-secondary mt-4 font-medium">Playground öffnen →</span>
            </a>
            <a href="/customers" class="rounded-2xl border border-border bg-white p-6 hover:border-secondary transition flex flex-col justify-between">
                <div>
                    <p class="text-xs font-semibold text-muted uppercase tracking-[0.25em]">REST-API</p>
                    <p class="text-xl font-semibold text-primary mt-3">JSON-Endpunkte testen</p>
                </div>
                <span class="text-accent mt-4 font-medium">API öffnen →</span>
            </a>
        </div>
    </div>
</@layout.layout>