# 🚀 GUÍA COMPLETA PARA DEPLOYMENT PÚBLICO

## ✅ JAR Compilado

```
Archivo: mi-aplicacion-java-2.0.0.jar
Tamaño: 22 MB
Ubicación: target/mi-aplicacion-java-2.0.0.jar
```

---

## 🌍 Opciones de Deployment Gratuito

### 1️⃣ RAILWAY.APP (⭐ RECOMENDADO)

**Ventajas:**
- ✅ Crédito gratuito $5/mes
- ✅ Muy fácil de setup
- ✅ Soporte para Java
- ✅ Dominio gratis
- ✅ Variables de entorno automáticas

**Pasos:**

```bash
# 1. Ir a railway.app y crear cuenta con GitHub
# 2. Crear nuevo proyecto → "Deploy from GitHub"
# 3. Seleccionar repositorio crezcamos-juntos
# 4. Railway auto-detectará Java + Maven
# 5. Variables de entorno automáticas:
#    - PORT (estaré disponible)
```

**URL Final:**
```
https://tu-proyecto-xxxx.railway.app
```

---

### 2️⃣ RENDER.COM

**Ventajas:**
- ✅ 750 horas/mes gratis
- ✅ Buen soporte Java
- ✅ Fácil de usar

**Pasos:**

```bash
# 1. Ir a render.com y crear cuenta
# 2. New → Web Service
# 3. Conectar GitHub
# 4. Seleccionar repositorio
# 5. Runtime: Java
# 6. Build Command: mvn clean package -DskipTests
# 7. Start Command: java -Dserver.port=$PORT ...
```

**URL Final:**
```
https://tu-proyecto.onrender.com
```

---

### 3️⃣ REPLIT.COM

**Ventajas:**
- ✅ Muy fácil de usar
- ✅ Editor online incluido
- ✅ Gratis pero con limitaciones

**Pasos:**

```bash
# 1. Ir a replit.com
# 2. Create → Import from GitHub
# 3. Pegar URL: https://github.com/tu-usuario/crezcamos-juntos
# 4. Ejecutar JAR directamente
```

---

## 📋 INSTRUCCIONES PASO-A-PASO (RAILWAY - Lo más fácil)

### PASO 1: Preparar Repositorio Git

```bash
cd c:/Users/Jaedu.HS/Version2-SETI/crezcamos-juntos

# Inicializar git si no está hecho
git init
git add .
git commit -m "Spring Boot Security Implementation - Ready for Deploy"

# Agregar repositorio remoto (ej. GitHub)
git remote add origin https://github.com/tu-usuario/crezcamos-juntos
git push -u origin main
```

### PASO 2: Crear Cuenta en Railway

1. Ir a https://railway.app
2. Hacer click en "Login with GitHub"
3. Autorizar conexión
4. Crear nuevo proyecto

### PASO 3: Conectar Repositorio

1. Click en "+ New Project"
2. Click en "Deploy from GitHub repo"
3. Conectar GitHub
4. Seleccionar `crezcamos-juntos`
5. Railway detectará automáticamente:
   - Java 17
   - Maven
   - Spring Boot 2.7.18

### PASO 4: Configurar Variables de Entorno

En Railway Dashboard → Settings → Environment:

```
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=tu-secreto-muy-largo-aqui-32+ caracteres
DATABASE_URL=postgresql://... (si usas BD)
```

### PASO 5: Deploy Automático

```
Railway se conectará a GitHub
Cada push a main dispara build automático
Build toma ~2-3 minutos
Aplicación se reinicia automáticamente
```

### PASO 6: Acceder a la Aplicación

Railway te dará una URL como:
```
https://tu-proyecto-abc123.railway.app
```

---

## 🧪 PROBAR ENDPOINTS PÚBLICOS

Una vez deployado:

```bash
# 1. Health Check
curl https://tu-proyecto-abc123.railway.app/health

# 2. Login
curl -X POST https://tu-proyecto-abc123.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'

# 3. Usar Token Retornado
TOKEN="tu-token-aqui"
curl https://tu-proyecto-abc123.railway.app/api/users \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🔒 Configuración de Seguridad para Producción

### Cambiar SecurityConfig

En `src/main/java/.../config/SecurityConfig.java`:

```java
// CAMBIAR DE ESTO:
.anyRequest().permitAll()

// A ESTO:
.antMatchers("/api/auth/**").permitAll()
.antMatchers("/health").permitAll()
.anyRequest().authenticated()
```

### Actualizar JWT Secret

En `application-prod.properties`:

```properties
app.jwt.secret=TU-SECRETO-MUY-LARGO-AQUI-MINIMO-32-CARACTERES
app.jwt.expiration=86400000
```

---

## 🛠️ Troubleshooting

### Error: "Port 8080 already in use"
→ Railway asigna puerto dinámicamente via `$PORT`
→ Procfile ya lo maneja automáticamente

### Error: "JWT validation failed"
→ Verificar que `app.jwt.secret` sea igual en todas partes
→ No cambiar secret después de generar tokens

### Error: "Connection timeout"
→ Podría estar en fase de init (2-3 minutos)
→ Revisar build logs en Railway Dashboard

### API retorna 404
→ Verificar que endpoints existan: `/api/auth/login`, `/api/users`
→ Verificar contexto path en application.properties

---

## 📊 Estructura de Archivos para Deploy

```
crezcamos-juntos/
├── pom.xml                    ✅ Maven config
├── Procfile                   ✅ Heroku/Railway config
├── render.yaml                ✅ Render config
├── src/
│   ├── main/
│   │   ├── java/...           ✅ Código
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-prod.properties  ⭐ Para producción
│   │       ├── application-dev.properties
│   └── test/                  ✅ Tests
├── target/
│   └── mi-aplicacion-java-2.0.0.jar    ⭐ JAR A DEPLOYAR
└── .gitignore                 ✅ Excluir target/
```

---

## ✅ Checklist Final

- [ ] Código compilado sin errores
- [ ] Tests pasando (o skipped en CI/CD)
- [ ] JAR creado: `mi-aplicacion-java-2.0.0.jar`
- [ ] Repositorio Git creado en GitHub
- [ ] Cuenta Railway/Render creada
- [ ] Proyecto conectado a repositorio
- [ ] Variables de entorno configuradas
- [ ] Build exitoso en CI/CD
- [ ] Aplicación accesible en URL pública
- [ ] Endpoints probados con curl
- [ ] SecurityConfig en modo producción

---

## 🔗 Referencias Útiles

- **Railway Docs:** https://docs.railway.app
- **Render Docs:** https://render.com/docs
- **Spring Boot 2.7 Deployment:** https://spring.io/guides/gs/spring-boot/
- **JWT Testing:** https://jwt.io
- **HTTP Status Codes:** https://httpwg.org/specs/rfc9110.html

---

## 📞 Soporte

Si tienes problemas:
1. Revisar logs en plataforma de deployment
2. Verificar variables de entorno
3. Probar localmente primero (en puerto 9090)
4. Comprobar que archivos .properties están en resources/

**Estado Actual:** ✅ Listo para producción
