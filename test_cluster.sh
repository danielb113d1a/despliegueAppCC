#!/bin/bash

# 1. Arrancar el clúster en segundo plano
echo "--- Asegurando que el clúster está arriba... ---"
docker compose up -d

# 2. Esperar un poco por si acaso
echo "--- Esperando 30 segundos para que todo inicialice bien ---"
sleep 30

# 3. Probar el Frontend (Puerto 3000)
echo "--- Probando conexión al Frontend... ---"
STATUS_FRONT=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:3000)

if [ "$STATUS_FRONT" == "200" ]; then
    echo "Frontend responde correctamente (HTTP 200)"
else
    echo "Frontend falló con estado $STATUS_FRONT"
    exit 1
fi

# 4. Probar el Backend (Puerto 8080) /api/books
echo "--- Probando conexión al Backend... ---"

STATUS_BACK=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/books)

if [[ "$STATUS_BACK" =~ ^(200|401)$ ]]; then
    echo "API Backend responde correctamente (HTTP $STATUS_BACK)"
else
    echo "Fallo en el Backend (Estado $STATUS_BACK)"
    docker logs backend-app
    exit 1
fi

echo "--- Apagando clúster... ---"
docker compose down

echo "--- TEST SUPERADO CON ÉXITO ---"