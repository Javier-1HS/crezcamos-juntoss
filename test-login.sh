#!/bin/bash

# Esperar a que el servidor esté listo
echo "Esperando servidor..."
sleep 25

# Test 1: Health check sin /api
echo "=== TEST 1: Health Check ==="
curl -s http://localhost:8080/health

echo -e "\n=== TEST 2: Login endpoint ==="
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'

echo -e "\n=== TEST 3: Users list (without auth) ==="
curl -s http://localhost:8080/api/users

echo -e "\n✅ Tests completados"
