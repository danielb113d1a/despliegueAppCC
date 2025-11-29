#!/bin/bash

# 1. Arrancar el clúster en segundo plano (si no está arrancado)
echo "🚀 Asegurando que el clúster está arriba..."
docker compose up -d

# 2. Esperar un poco por si acaso
echo "⏳ Esperando 10 segundos..."
sleep 10

# 3. Probar el Frontend (Puerto 3000)
echo "🔍 Probando conexión al Frontend..."
STATUS_FRONT=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:3000)

if [ "$STATUS_FRONT" == "200" ]; then
    echo "✅ Frontend responde correctamente (HTTP 200)"
else
    echo "❌ Frontend falló con estado $STATUS_FRONT"
    exit 1
fi

# 4. Probar el Backend (Puerto 8080)
echo "🔍 Probando conexión al Backend..."
# Probamos la raíz. Si devuelve 200, 401, 404 o 500, significa que Tomcat está escuchando.
STATUS_BACK=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080)

if [[ "$STATUS_BACK" =~ ^(200|401|403|404|500)$ ]]; then
    echo "✅ Backend ESTÁ VIVO (Respondió HTTP $STATUS_BACK)"
else
    echo "❌ Backend MUERTO o inalcanzable (Estado $STATUS_BACK)"
    docker logs backend-app
    exit 1
fi

# 5. Apagar todo (Opcional, coméntalo si quieres dejarlo encendido)
echo "🧹 Apagando clúster..."
docker compose down

echo "🎉 TEST SUPERADO CON ÉXITO"