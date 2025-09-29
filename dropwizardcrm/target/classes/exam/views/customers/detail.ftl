<#import "../layout/base.ftl" as layout>
<@layout.layout title=title activePath=activePath>
    <div class="bg-white border border-gray-200 rounded-lg p-8 shadow-sm">
        <div class="flex justify-between items-center mb-6">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Kundendetails</h1>
                <p class="text-gray-600 mt-2">${customer.name}</p>
            </div>
            <div class="flex space-x-3">
                <a href="/ui/customers/${customer.id}/edit" class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg">
                    Bearbeiten
                </a>
                <form action="/ui/customers/${customer.id}/delete" method="post"
                      onsubmit="return confirm('Soll „${customer.name}“ wirklich gelöscht werden?');">
                    <button type="submit" class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg">
                        Löschen
                    </button>
                </form>
                <a href="/ui/customers" class="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50">
                    Zurück
                </a>
            </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
                <h2 class="text-sm font-semibold text-gray-500 uppercase tracking-wide mb-2">Kontakt</h2>
                <p><span class="font-medium">Ansprechpartner:</span> ${customer.contactPerson}</p>
                <p><span class="font-medium">E-Mail:</span>
                    <#if customer.email?? && customer.email?has_content>
                        ${customer.email}
                    <#else>
                        <span class="text-gray-400">-</span>
                    </#if>
                </p>
                <p><span class="font-medium">Telefon:</span> ${customer.phone}</p>
                <p><span class="font-medium">Adresse:</span>
                    <#if customer.address?? && customer.address?has_content>
                        ${customer.address}
                    <#else>
                        <span class="text-gray-400">-</span>
                    </#if>
                </p>
            </div>

            <div>
                <h2 class="text-sm font-semibold text-gray-500 uppercase tracking-wide mb-2">Status</h2>
                <p><span class="font-medium">Kundentyp:</span> ${customer.customerType?replace("_", " ")}</p>
                <p><span class="font-medium">Status:</span> ${customer.status}</p>
                <p><span class="font-medium">Branche:</span>
                    <#if customer.industry?? && customer.industry?has_content>
                        ${customer.industry}
                    <#else>
                        <span class="text-gray-400">-</span>
                    </#if>
                </p>
                <p><span class="font-medium">Letzter Kontakt:</span>
                    <#if customer.lastContactDate??>
                        ${customer.lastContactDate}
                    <#else>
                        <span class="text-gray-400">-</span>
                    </#if>
                </p>
            </div>
        </div>

        <div class="mt-6">
            <h2 class="text-sm font-semibold text-gray-500 uppercase tracking-wide mb-2">Kontaktpräferenzen</h2>
            <#if customer.wantsToBeContactedBy?? && customer.wantsToBeContactedBy?size gt 0>
                <ul class="list-disc list-inside space-y-1 text-gray-700">
                    <#list customer.wantsToBeContactedBy as method>
                        <li>${contactMethodOptions[method]!method}</li>
                    </#list>
                </ul>
            <#else>
                <p class="text-gray-400">Keine speziellen Präferenzen hinterlegt.</p>
            </#if>
        </div>
    </div>
</@layout.layout>