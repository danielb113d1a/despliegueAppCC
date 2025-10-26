# Makefile - Backend (Spring Boot)
# MODIFICADO para aceptar un comando 'mvnw' dinámico

.PHONY: test unit integration clean

# 'MVNW' usará la variable $(MVNW_CMD) pasada desde el Makefile raíz.
# Si no se pasa, usará 'mvnw' por defecto
MVNW ?= $(or $(MVNW_CMD),mvnw)

# Todos los comandos ahora usan la variable $(MVNW)
clean:
	$(MVNW) clean

unit:
	@echo "=== Ejecutando tests unitarios (Surefire) ==="
	$(MVNW) test $(MVN_ARGS)

integration:
	@echo "=== Ejecutando tests de integración (Failsafe) ==="
	$(MVNW) verify -DskipUnitTests=true $(MVN_ARGS)

test:
	@echo "=== Ejecutando todos los tests (unit + integration) ==="
	$(MVNW) clean verify $(MVN_ARGS)