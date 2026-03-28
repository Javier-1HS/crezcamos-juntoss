#!/bin/bash

# =====================================
# SCRIPT: Preparar para deploy a Railway
# =====================================

set -e

echo "🚀 Preparando aplicación para deployment en Railway..."
echo ""

# 1. Verificar Java
echo "✅ Verificando Java..."
java -version
echo ""

# 2. Limpiar build anterior
echo "✅ Limpiando compilaciones anteriores..."
mvn clean -q
echo ""

# 3. Compilar sin tests
echo "✅ Compilando aplicación..."
mvn compile -q
echo "   Compilación exitosa!"
echo ""

# 4. Construir JAR
echo "✅ Construyendo JAR ejecutable..."
mvn package -DskipTests -q
JAR_FILE=$(find target -name "*.jar" -type f)
JAR_SIZE=$(ls -lh "$JAR_FILE" | awk '{print $5}')
echo "   JAR creado: $JAR_FILE ($JAR_SIZE)"
echo ""

# 5. Verificar archivos de deploy
echo "✅ Verificando archivos de configuración..."
files=(
    "Procfile"
    "render.yaml"
    "DEPLOYMENT-GUIDE.md"
    "RAILWAY-DEPLOYMENT.md"
    "src/main/resources/application-prod.properties"
)

for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo "   ✓ $file"
    else
        echo "   ✗ FALTA: $file"
    fi
done
echo ""

# 6. Verificar Git
echo "✅ Verificando Git..."
if git rev-parse --git-dir > /dev/null 2>&1; then
    echo "   Repositorio Git: ✓"
    BRANCH=$(git rev-parse --abbrev-ref HEAD)
    echo "   Branch: $BRANCH"
    CHANGES=$(git status --porcelain | wc -l)
    echo "   Cambios pendientes: $CHANGES"
else
    echo "   ⚠  Git no inicializado"
    echo "   Ejecutar: git init"
fi
echo ""

# 7. Instrucciones finales
echo "================================================"
echo "✅ PREPARACIÓN COMPLETADA"
echo "================================================"
echo ""
echo "Próximo paso: Pushear cambios a GitHub"
echo ""
echo "  git add ."
echo "  git commit -m 'Ready for Railway deployment'"
echo "  git push -u origin main"
echo ""
echo "Luego:"
echo "  1. Ir a https://railway.app"
echo "  2. Conectar repositorio GitHub"
echo "  3. Railway compilará y deployará automáticamente"
echo "  4. Tu app estará en: https://tu-app-xxxxx.railway.app"
echo ""
echo "Para más detalles, leer: RAILWAY-DEPLOYMENT.md"
echo "================================================"
