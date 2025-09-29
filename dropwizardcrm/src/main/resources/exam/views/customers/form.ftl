<#import "../layout/base.ftl" as layout>
<@layout.layout title=title activePath=activePath>
    <#assign fieldErrors = fieldErrors!{} />
    <#assign globalErrors = globalErrors![] />
    <div class="bg-white border border-gray-200 rounded-lg p-8 shadow-sm">
        <div class="flex justify-between items-center mb-6">
            <div>
                <h1 class="text-2xl font-bold text-gray-900">
                    <#if edit> Kunde bearbeiten <#else> Neuen Kunden anlegen </#if>
                </h1>
                <p class="text-gray-600 mt-2">
                    Pflichtfelder sind mit * markiert.
                </p>
            </div>
            <a href="/ui/customers" class="text-blue-600 hover:text-blue-800">Zur Liste</a>
        </div>

        <#if globalErrors?size gt 0>
            <div class="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
                <ul class="list-disc list-inside space-y-1">
                    <#list globalErrors as err>
                        <li>${err}</li>
                    </#list>
                </ul>
            </div>
        </#if>

        <form action="<#if edit>/ui/customers/${form.id}<#else>/ui/customers</#if>"
              method="post" class="space-y-6" novalidate>
            <#if edit>
                <input type="hidden" name="id" value="${form.id}">
            </#if>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Firmenname *</label>
                    <input type="text" name="name" value="${form.name!''}"
                           class="w-full border rounded-md px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200">
                    <#assign errs = fieldErrors["name"]![] >
                    <#if errs?size gt 0>
                        <#list errs as err>
                            <p class="text-sm text-red-600 mt-1">${err}</p>
                        </#list>
                    </#if>
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Ansprechpartner *</label>
                    <input type="text" name="contactPerson" value="${form.contactPerson!''}"
                           class="w-full border rounded-md px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200">
                    <#assign errs = fieldErrors["contactPerson"]![] >
                    <#if errs?size gt 0>
                        <#list errs as err>
                            <p class="text-sm text-red-600 mt-1">${err}</p>
                        </#list>
                    </#if>
                </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">E-Mail</label>
                    <input type="email" name="email" value="${form.email!''}"
                           class="w-full border rounded-md px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200">
                    <#assign errs = fieldErrors["email"]![] >
                    <#if errs?size gt 0>
                        <#list errs as err>
                            <p class="text-sm text-red-600 mt-1">${err}</p>
                        </#list>
                    </#if>
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Telefon *</label>
                    <input type="tel" name="phone" value="${form.phone!''}"
                           class="w-full border rounded-md px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200">
                    <#assign errs = fieldErrors["phone"]![] >
                    <#if errs?size gt 0>
                        <#list errs as err>
                            <p class="text-sm text-red-600 mt-1">${err}</p>
                        </#list>
                    </#if>
                </div>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Adresse</label>
                <textarea name="address" rows="3"
                          class="w-full border rounded-md px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200">${form.address!''}</textarea>
                <#assign errs = fieldErrors["address"]![] >
                <#if errs?size gt 0>
                    <#list errs as err>
                        <p class="text-sm text-red-600 mt-1">${err}</p>
                    </#list>
                </#if>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Kundentyp *</label>
                    <select name="customerType"
                            class="w-full border rounded-md px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200">
                        <option value="">Kundentyp auswählen</option>
                        <#list customerTypeOptions?keys as key>
                            <option value="${key}" <#if form.customerType?? && form.customerType == key>selected</#if>>
                                ${customerTypeOptions[key]}
                            </option>
                        </#list>
                    </select>
                    <#assign errs = fieldErrors["customerType"]![] >
                    <#if errs?size gt 0>
                        <#list errs as err>
                            <p class="text-sm text-red-600 mt-1">${err}</p>
                        </#list>
                    </#if>
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Status *</label>
                    <select name="status"
                            class="w-full border rounded-md px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200">
                        <option value="">Status auswählen</option>
                        <#list statusOptions?keys as key>
                            <option value="${key}" <#if form.status?? && form.status == key>selected</#if>>
                                ${statusOptions[key]}
                            </option>
                        </#list>
                    </select>
                    <#assign errs = fieldErrors["status"]![] >
                    <#if errs?size gt 0>
                        <#list errs as err>
                            <p class="text-sm text-red-600 mt-1">${err}</p>
                        </#list>
                    </#if>
                </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Branche</label>
                    <input type="text" name="industry" value="${form.industry!''}"
                           class="w-full border rounded-md px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200">
                    <#assign errs = fieldErrors["industry"]![] >
                    <#if errs?size gt 0>
                        <#list errs as err>
                            <p class="text-sm text-red-600 mt-1">${err}</p>
                        </#list>
                    </#if>
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Letzter Kontakt</label>
                    <input type="date" name="lastContactDate" value="${form.lastContactDate!''}"
                           class="w-full border rounded-md px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200">
                    <#assign errs = fieldErrors["lastContactDate"]![] >
                    <#if errs?size gt 0>
                        <#list errs as err>
                            <p class="text-sm text-red-600 mt-1">${err}</p>
                        </#list>
                    </#if>
                </div>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700 mb-3">Kontaktpräferenzen</label>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                    <#list contactMethodOptions?keys as key>
                        <label class="flex items-center space-x-3 border border-gray-200 rounded-lg px-3 py-2 hover:bg-gray-50">
                            <input type="checkbox" name="wantsToBeContactedBy" value="${key}"
                                   <#if form.wantsToBeContactedBy?seq_contains(key)>checked</#if>>
                            <span>${contactMethodOptions[key]}</span>
                        </label>
                    </#list>
                </div>
                <#assign errs = fieldErrors["wantsToBeContactedBy"]![] >
                <#if errs?size gt 0>
                    <#list errs as err>
                        <p class="text-sm text-red-600 mt-1">${err}</p>
                    </#list>
                </#if>
            </div>

            <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <a href="/ui/customers" class="px-5 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">
                    Abbrechen
                </a>
                <button type="submit" class="px-5 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
                    <#if edit> Kunde aktualisieren <#else> Kunden anlegen </#if>
                </button>
            </div>
        </form>
    </div>
</@layout.layout>