<#import "../layout/base.ftl" as layout>
<@layout.layout title=title activePath=activePath>
    <div class="flex flex-col gap-6 mb-10 md:flex-row md:items-center md:justify-between">
        <div>
            <h1 class="text-3xl font-semibold text-primary">Kunden</h1>
            <p class="text-muted/80 mt-2 max-w-xl">Verwalten Sie alle Kunden und filtern Sie nach Namen, Städten oder Ansprechpartnern.</p>
        </div>
        <a href="/ui/customers/new" class="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-secondary text-white text-sm font-medium uppercase tracking-wide shadow-sm hover:bg-secondary/90 transition">
            <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd"></path>
            </svg>
            Neuen Kunden anlegen
        </a>
    </div>

    <div class="bg-white border border-border rounded-2xl px-6 py-6 mb-8">
        <form action="/ui/customers" method="get" class="grid gap-4 md:grid-cols-[minmax(0,1fr)_auto] md:items-end">
            <div>
                <label for="customer-search" class="block text-sm font-medium text-muted uppercase tracking-wide mb-2">Suche</label>
                <div class="relative">
                    <input id="customer-search"
                           type="search"
                           name="q"
                           value="${searchTerm!}"
                           placeholder="Nach Name, Ansprechpartner, Branche, Status, Kontakt ..."
                           class="w-full rounded-xl border border-border bg-neutral text-primary placeholder:text-muted/60 py-3 pl-12 pr-4 focus:outline-none focus:ring-2 focus:ring-secondary/30 focus:border-secondary">
                    <svg class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-muted/60" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd"></path>
                    </svg>
                </div>
                <p class="mt-2 text-xs text-muted/70">${resultCount} von ${totalCount} Kunden ${searchTerm?has_content?then('für "' + searchTerm + '"','angezeigt')}</p>
            </div>
            <div class="flex gap-3">
                <button type="submit" class="px-6 py-3 rounded-xl bg-secondary text-white font-semibold uppercase tracking-wide text-sm shadow-sm hover:bg-secondary/90 transition">Suchen</button>
                <#if searching>
                    <a href="/ui/customers" class="px-6 py-3 rounded-xl border border-border text-muted font-semibold uppercase tracking-wide text-sm hover:border-secondary hover:text-secondary transition">Zurücksetzen</a>
                </#if>
            </div>
        </form>
    </div>

    <#if message?? && message?has_content>
        <div class="mb-8 rounded-xl border border-secondary/20 bg-neutral px-4 py-3 text-sm text-primary">
            ${message}
        </div>
    </#if>

    <#if customers?has_content>
        <#assign statusClasses = {
            "CUSTOMER":"bg-emerald-100 text-emerald-700",
            "LEAD":"bg-amber-100 text-amber-700",
            "WARM":"bg-blue-100 text-blue-800",
            "COLD":"bg-indigo-100 text-indigo-800",
            "CLOSED":"bg-gray-200 text-gray-700"
        }>
        <div class="bg-white border border-border rounded-2xl overflow-hidden">
            <table class="min-w-full divide-y divide-border/70">
                <thead class="bg-neutral text-xs font-semibold uppercase tracking-wide text-muted/80">
                    <tr>
                        <th class="px-4 py-3 text-left">ID</th>
                        <th class="px-4 py-3 text-left">Name</th>
                        <th class="px-4 py-3 text-left">Ansprechpartner</th>
                        <th class="px-4 py-3 text-left">E-Mail</th>
                        <th class="px-4 py-3 text-left">Telefon</th>
                        <th class="px-4 py-3 text-left">Typ</th>
                        <th class="px-4 py-3 text-left">Status</th>
                        <th class="px-4 py-3 text-center">Aktionen</th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-border/70 text-sm">
                    <#list customers as customer>
                        <#assign statusClass = statusClasses[customer.status]?if_exists!"bg-gray-200 text-gray-700">
                        <tr class="hover:bg-neutral transition">
                            <td class="px-4 py-3 text-muted/80">${customer.id}</td>
                            <td class="px-4 py-3 font-semibold text-primary">${customer.name}</td>
                            <td class="px-4 py-3 text-muted/80">${customer.contactPerson!'-'}</td>
                            <td class="px-4 py-3 text-muted/80">${customer.email!'-'}</td>
                            <td class="px-4 py-3 text-muted/80">${customer.phone!'-'}</td>
                            <td class="px-4 py-3 text-muted/80">${customer.customerType?replace('_', ' ')}</td>
                            <td class="px-4 py-3">
                                <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-semibold ${statusClass}">
                                    ${customer.status}
                                </span>
                            </td>
                            <td class="px-4 py-3 text-center">
                                <div class="inline-flex items-center gap-4 text-sm font-medium">
                                    <a href="/ui/customers/${customer.id}" class="text-secondary hover:text-secondary/80 transition" title="Anzeigen">
                                        <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M10 12a2 2 0 100-4 2 2 0 000 4z"></path><path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"></path></svg>
                                    </a>
                                    <a href="/ui/customers/${customer.id}/edit" class="text-primary hover:text-primary/80 transition" title="Bearbeiten">
                                        <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"></path></svg>
                                    </a>
                                    <form action="/ui/customers/${customer.id}/delete" method="post" class="inline" onsubmit="return confirm('Soll „${customer.name}“ wirklich gelöscht werden?');">
                                        <button type="submit" class="text-destroy hover:text-muted transition" title="Löschen">
                                            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd"></path></svg>
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    <#else>
        <div class="bg-white border border-border rounded-2xl p-12 text-center text-muted">
            <#if searching>
                <p class="text-primary text-lg font-medium">Keine Kunden entsprechen der Suche nach „${searchTerm!}“.</p>
                <div class="mt-6">
                    <a href="/ui/customers" class="inline-flex items-center justify-center px-5 py-2 rounded-full bg-secondary text-white text-xs font-semibold uppercase tracking-wide hover:bg-secondary/90 transition">
                        Suche zurücksetzen
                    </a>
                </div>
            <#else>
                <p class="text-primary text-lg font-medium">Es wurden noch keine Kunden angelegt.</p>
                <p class="mt-2 text-muted/70">Legen Sie Ihren ersten Eintrag an, um hier eine Übersicht zu erhalten.</p>
            </#if>
        </div>
    </#if>
</@layout.layout>