<#import "../layout/base.ftl" as layout>
<@layout.layout title=title activePath=activePath>
    <div class="flex justify-between items-center mb-6">
        <div>
            <h1 class="text-3xl font-bold text-gray-900">Kundenverwaltung</h1>
            <p class="text-gray-600 mt-2">Verwalten Sie alle Kunden, Kontakte und Stati.</p>
        </div>
        <a href="/ui/customers/new" class="bg-green-600 hover:bg-green-700 text-white px-5 py-2 rounded-lg transition">
            Neuen Kunden anlegen
        </a>
    </div>

    <#if message?? && message?has_content>
        <div class="mb-6 bg-green-50 border border-green-200 text-green-800 px-4 py-3 rounded-lg">
            ${message}
        </div>
    </#if>

    <#if customers?has_content>
        <#assign statusClasses = {
            "CUSTOMER":"bg-green-100 text-green-800",
            "LEAD":"bg-yellow-100 text-yellow-800",
            "WARM":"bg-blue-100 text-blue-800",
            "COLD":"bg-indigo-100 text-indigo-800",
            "CLOSED":"bg-gray-200 text-gray-700"
        }>
        <div class="bg-white border border-gray-200 rounded-lg overflow-hidden">
            <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                    <tr>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">ID</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Name</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Ansprechpartner</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">E-Mail</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Telefon</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Typ</th>
                        <th class="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Status</th>
                        <th class="px-4 py-3 text-center text-xs font-semibold text-gray-500 uppercase tracking-wider">Aktionen</th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-100">
                    <#list customers as customer>
                        <#assign statusClass = statusClasses[customer.status]?if_exists!"bg-gray-200 text-gray-700">
                        <tr class="hover:bg-gray-50">
                            <td class="px-4 py-3 text-sm text-gray-600">${customer.id}</td>
                            <td class="px-4 py-3 text-sm font-medium text-gray-900">${customer.name}</td>
                            <td class="px-4 py-3 text-sm text-gray-700">${customer.contactPerson}</td>
                            <td class="px-4 py-3 text-sm text-gray-600">
                                <#if customer.email?? && customer.email?has_content>
                                    ${customer.email}
                                <#else>
                                    <span class="text-gray-400">-</span>
                                </#if>
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-700">
                                <#if customer.phone?? && customer.phone?has_content>
                                    ${customer.phone}
                                <#else>
                                    <span class="text-gray-400">-</span>
                                </#if>
                            </td>
                            <td class="px-4 py-3 text-sm text-gray-700">
                                ${customer.customerType?replace("_", " ")}
                            </td>
                            <td class="px-4 py-3 text-sm">
                                <span class="px-2 py-1 text-xs font-medium rounded-full ${statusClass}">
                                    ${customer.status}
                                </span>
                            </td>
                            <td class="px-4 py-3 text-sm text-center space-x-2">
                                <a href="/ui/customers/${customer.id}" class="text-blue-600 hover:text-blue-800">Anzeigen</a>
                                <a href="/ui/customers/${customer.id}/edit" class="text-indigo-600 hover:text-indigo-800">Bearbeiten</a>
                                <form action="/ui/customers/${customer.id}/delete" method="post" class="inline" onsubmit="return confirm('Soll „${customer.name}“ wirklich gelöscht werden?');">
                                    <button type="submit" class="text-red-600 hover:text-red-800">Löschen</button>
                                </form>
                            </td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    <#else>
        <div class="bg-white border border-gray-200 rounded-lg p-10 text-center text-gray-500">
            Keine Kunden gefunden. Legen Sie den ersten Kunden an.
        </div>
    </#if>
</@layout.layout>