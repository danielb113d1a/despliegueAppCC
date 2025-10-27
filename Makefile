.PHONY: backend-test frontend-test test

# Opciones de Maven para CI (Batch mode y perfil de test)
BACKEND_CI_ARGS = -B -Dspring.profiles.active=test

backend-test:
	@echo "--- Ejecutando Tests del Backend (via nested Make) ---"
	cd backend && make test MVN_ARGS="$(BACKEND_CI_ARGS)"

frontend-test:
	@echo "--- Ejecutando Tests del Frontend (Jest) ---"
	cd frontend && npm ci && npm run ci:test

test: backend-test frontend-test
	@echo "--- Pipeline de CI completado ---"