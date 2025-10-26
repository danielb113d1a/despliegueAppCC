# Makefile Raíz (Versión compatible con Windows y Linux)

.PHONY: backend-test frontend-test test

# --- Detección de Sistema Operativo ---
ifeq ($(OS),Windows_NT)
	MVNW_CMD = mvnw
else
	MVNW_CMD = ./mvnw
endif

# Opciones de Maven para CI (Renombrada de _OPTS a _ARGS)
BACKEND_CI_ARGS = -B -Dspring.profiles.active=test

# --- OBJETIVO: Backend ---
# Llama al 'make test' anidado, pasándole dos variables:
# 1. MVNW_CMD: El comando mvnw correcto para el OS
# 2. MVN_ARGS: Las opciones de CI (RENOMBRADA)
backend-test:
	@echo "--- Ejecutando Tests del Backend (via nested Make) ---"
	cd backend && make test MVNW_CMD="$(MVNW_CMD)" MVN_ARGS="$(BACKEND_CI_ARGS)"

# --- OBJETIVO: Frontend ---
frontend-test:
	@echo "--- Ejecutando Tests del Frontend (Jest) ---"
	cd frontend && npm ci && npm run ci:test

# --- OBJETIVO: Principal ---
test: backend-test frontend-test
	@echo "--- Pipeline de CI completado ---"