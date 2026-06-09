#!/bin/bash
# AV-CAR — Inicia API + Swing com um único comando

echo "=== AV-CAR Auto Center ==="
echo ""

# 1. Compilar (se necessário)
if [ ! -d target/classes ]; then
  echo "[1/3] Compilando projeto..."
  mvn clean package -DskipTests -q
else
  echo "[1/3] Projeto já compilado."
fi

# 2. Iniciar API em background
echo "[2/3] Iniciando API (porta 8080)..."
mvn spring-boot:run -q > /tmp/avcar-api.log 2>&1 &
API_PID=$!

# Aguardar API ficar pronta
echo "      Aguardando API..."
for i in $(seq 1 60); do
  sleep 1
  if curl -s http://localhost:8080/api/clientes > /dev/null 2>&1; then
    echo "      API pronta!"
    break
  fi
  if [ $i -eq 60 ]; then
    echo "      ERRO: API não subiu. Verifique /tmp/avcar-api.log"
    exit 1
  fi
done

# 3. Iniciar Swing
echo "[3/3] Iniciando interface gráfica..."
mvn compile exec:java -Pswing

# Quando o Swing fechar, encerrar a API
echo ""
echo "Encerrando API..."
kill $API_PID 2>/dev/null
echo "=== Finalizado ==="
