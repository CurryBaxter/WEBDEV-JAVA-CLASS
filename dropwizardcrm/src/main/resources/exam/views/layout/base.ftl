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
                        primary: '#0b1f3a',
                        secondary: '#1d4ed8',
                        accent: '#facc15',
                        neutral: '#f1f5f9',
                        muted: '#475569',
                        border: '#d0d7e3',
                        destroy: '#dc2626'
                    }
                }
            }
        };
    </script>
</head>
<body class="min-h-screen flex flex-col bg-neutral text-muted">
    <#assign homeClass = (activePath == '/ui')?string(' text-accent',' text-white/70')>
    <#assign customersClass = (activePath?starts_with('/ui/customers'))?string(' text-accent',' text-white/70')>
    <#assign validationClass = (activePath?starts_with('/ui/test-validation'))?string(' text-accent',' text-white/70')>
    <nav class="bg-primary text-white shadow-md">
        <div class="max-w-7xl mx-auto px-4">
            <div class="flex justify-between items-center py-4">
                <a href="/ui" class="flex items-center space-x-2 text-xl font-bold hover:text-accent transition">
                    <svg class="w-6 h-6 text-accent" fill="currentColor" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
                        <path d="M670.5 471.7c-7.1-3.1-14.2-5.9-21.4-8.5 49.8-40.3 81.6-101.8 81.6-170.6 0-121-98.4-219.4-219.4-219.4s-219.4 98.4-219.4 219.4c0 68.9 31.9 130.5 81.7 170.7C219.4 519.6 109 667.8 109 841.3h73.1c0-181.5 147.7-329.1 329.1-329.1 45.3 0 89.1 9 130.2 26.7l29.1-67.2zM511.3 146.3c80.7 0 146.3 65.6 146.3 146.3S592 438.9 511.3 438.9 365 373.2 365 292.6s65.6-146.3 146.3-146.3zM612.5 636.5c0 10.2 5.6 19.5 14.6 24.2l128 67.6c4 2.1 8.4 3.2 12.8 3.2s8.8-1.1 12.8-3.2l128-67.6c9-4.8 14.6-14.1 14.6-24.2s-5.6-19.5-14.6-24.2l-128-67.7c-8-4.2-17.6-4.2-25.6 0l-128 67.7c-9 4.7-14.6 14-14.6 24.2z m155.4-36.6l69.3 36.6-69.3 36.6-69.3-36.6 69.3-36.6z" />
                        <path d="M767.9 763.4l-147-77.7-25.6 48.5 172.6 91.2 171.9-90.8-25.6-48.5z" />
                        <path d="M767.9 851.4l-147-77.6-25.6 48.4 172.6 91.3 171.3-90.6-25.6-48.5z" />
                    </svg>
                    <span>CRM System</span>
                </a>
                <div class="flex space-x-6 text-sm font-medium uppercase tracking-wide">
                    <a href="/ui" class="transition hover:text-accent${homeClass}">Startseite</a>
                    <a href="/ui/customers" class="transition hover:text-accent${customersClass}">Kunden</a>
                    <a href="/ui/test-validation" class="transition hover:text-accent${validationClass}">Validation Playground</a>
                    <a href="/customers" class="transition hover:text-accent text-white/70">REST-API</a>
                </div>
            </div>
        </div>
    </nav>
    <main class="flex-1 max-w-7xl mx-auto px-4 py-10 w-full">
        <div class="bg-white border border-border rounded-2xl shadow-sm">
            <div class="p-8">
                <#nested>
            </div>
        </div>
    </main>
    <footer class="bg-primary text-white py-8 mt-12">
        <div class="max-w-7xl mx-auto px-4 text-center space-y-2">
            <p class="text-sm text-white/80">
                © 2025 Dropwizard CRM System. Gebaut mit Dropwizard, FreeMarker & PostgreSQL von Felix Schreiber.
            </p>
            <div class="flex justify-center gap-4 text-xs uppercase tracking-wide">
                <a href="/customers" class="text-accent hover:text-secondary transition" target="_blank">API-Endpunkt</a>
                <a href="/ui/customers" class="text-accent hover:text-secondary transition">Kundenübersicht</a>
                <a href="/ui/test-validation" class="text-accent hover:text-secondary transition">Validierung testen</a>
            </div>
        </div>
    </footer>
</body>
</html>
</#macro>