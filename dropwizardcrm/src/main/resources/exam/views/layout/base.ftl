<#macro layout title activePath>
<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        primary: '#3b82f6',
                        secondary: '#6b7280'
                    }
                }
            }
        };
    </script>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
    <#assign homeClass = (activePath == '/ui')?string(' text-blue-200 font-medium','')>
    <#assign customersClass = (activePath?starts_with('/ui/customers'))?string(' text-blue-200 font-medium','')>
    <#assign validationClass = (activePath?starts_with('/ui/test-validation'))?string(' text-blue-200 font-medium','')>
    <nav class="bg-blue-600 text-white shadow">
        <div class="max-w-7xl mx-auto px-4">
            <div class="flex justify-between items-center py-4">
                <a href="/ui" class="flex items-center space-x-2 text-xl font-bold hover:text-blue-200 transition">
                    <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M13 6a3 3 0 11-6 0 3 3 0 016 0zM18 8a2 2 0 11-4 0 2 2 0 014 0zM14 15a4 4 0 00-8 0v3h8v-3z"></path>
                    </svg>
                    <span>CRM System</span>
                </a>
                <div class="flex space-x-4">
                    <a href="/ui" class="hover:text-blue-200 transition${homeClass}">Startseite</a>
                    <a href="/ui/customers" class="hover:text-blue-200 transition${customersClass}">Kunden</a>
                    <a href="/ui/test-validation" class="hover:text-blue-200 transition${validationClass}">Validation Playground</a>
                    <a href="/customers" class="hover:text-blue-200 transition">REST-API</a>
                </div>
            </div>
        </div>
    </nav>
    <main class="flex-1 max-w-7xl mx-auto px-4 py-8 w-full">
        <#nested>
    </main>
    <footer class="bg-gray-800 text-white py-8 mt-8">
        <div class="max-w-7xl mx-auto px-4 text-center">
            <p class="text-sm text-gray-400">
                © 2024 Dropwizard CRM System. Erstellt mit Dropwizard, FreeMarker & PostgreSQL.
            </p>
            <div class="mt-2 space-x-4 text-xs">
                <a href="/customers" class="text-blue-400 hover:text-blue-300" target="_blank">API-Endpunkt</a>
                <a href="/ui/customers" class="text-blue-400 hover:text-blue-300">Kundenübersicht</a>
                <a href="/ui/test-validation" class="text-blue-400 hover:text-blue-300">Validierung testen</a>
            </div>
        </div>
    </footer>
</body>
</html>
</#macro>