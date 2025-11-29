# --- Etapa 1: Construcción (Builder) ---
# Usamos la imagen de Node que acabas de cargar
FROM node:20-alpine AS builder

WORKDIR /app

COPY package.json package-lock.json ./

# ¡OJO! Aquí intentará descargar librerías de internet.
# Si tu Docker no tiene red, esto podría fallar. Cruzaremos los dedos.
RUN npm ci

COPY . .

# Esto crea la carpeta 'dist' con tu web optimizada
RUN npm run build

# --- Etapa 2: Servidor Web (Nginx) ---
# Usamos la imagen de Nginx que acabas de cargar
FROM nginx:alpine

# Copiamos la web compilada al servidor
COPY --from=builder /app/dist /usr/share/nginx/html

# Puerto 80 para la web
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]