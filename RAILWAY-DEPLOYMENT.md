# 🚀 DEPLOYMENT RÁPIDO EN RAILWAY

## Paso 1: Pushear Código a GitHub

```bash
# En tu máquina local
cd c:/Users/Jaedu.HS/Version2-SETI/crezcamos-juntos

# Inicializar repo si no existe
git init
git remote add origin https://github.com/TU-USUARIO/crezcamos-juntos
git add .
git commit -m "Spring Boot API - Ready for Railway Deploy"
git push -u origin main
```

---

## Paso 2: Conectar en Railway

### 2A. Crear Cuenta
1. Ir a: https://railway.app
2. Hacer click: "Continue with GitHub"
3. Autorizar permisos
4. Confirmar email

### 2B. Crear Proyecto
1. Click: "+ New Project"
2. Click: "Deploy from GitHub repo"
3. Seleccionar repositorio: `crezcamos-juntos`
4. Autorizar si es la primera vez

### 2C. Railway Auto-Configura
Railway detectará:
```
✅ Java 17
✅ Maven (pom.xml)
✅ Spring Boot
✅ Procfile
```

---

## Paso 3: Configurar Variables de Entorno

En Railway Dashboard:

```
Project → [Tu Proyecto] → Variables

Agregar:
- SPRING_PROFILES_ACTIVE = prod
- SERVER_PORT = $PORT (ya existe)
- JWT_SECRET = abc123def456ghi789jkl012mno345pqr (generar secuencia larga)
```

---

## Paso 4: Esperar Deploy

```
Railway automáticamente:
1. Detecta cambios en GitHub
2. Compila con Maven
3. Crea JAR
4. Inicia aplicación
5. Asigna URL pública

Tiempo: ~2-3 minutos
```

---

## Paso 5: Acceder a tu API

Railway te dará una URL como:
```
https://crezcamos-juntos-production-xxxx.railway.app
```

### Probar Endpoints:

```bash
# Health Check
curl https://crezcamos-juntos-production-xxxx.railway.app/health

# Login
curl -X POST https://crezcamos-juntos-production-xxxx.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'

# Respuesta esperada:
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
}
```

---

## 🔐 Seguridad para Producción

### 1. Cambiar SecurityConfig.java

**ANTES (Desarrollo):**
```java
.anyRequest().permitAll()
```

**DESPUES (Producción):**
```java
.antMatchers("/api/auth/**").permitAll()
.antMatchers("/health").permitAll()
.antMatchers("/error").permitAll()
.anyRequest().authenticated()
```

### 2. Actualizar application-prod.properties

```properties
# Seguridad
spring.security.user.name=admin
spring.security.user.password=cambiar-a-algo-mas-seguro

app.jwt.secret=GENERAR-UNA-CLAVE-LARGA-AQUI-MINIMO-32-CARACTERES
app.jwt.expiration=86400000

# Logins
logging.level.root=WARN
logging.level.com.cloudnative.academy=INFO
```

### 3. Regenerar JWT Secret

```bash
# Opción 1: Generar online
# https://www.random.org/strings/?num=1&len=64&digits=on&loweralpha=on&res=on

# Opción 2: En Linux/Mac
openssl rand -base64 64

# Opción 3: En Windows PowerShell
[Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes((New-Guid).ToString() + (New-Guid).ToString()))

# Copiar resultado a:
# 1. application-prod.properties (app.jwt.secret=...)
# 2. Variables de Railway (JWT_SECRET=...)
```

---

## ✅ Verificar Deployment

### En Railway Dashboard:

1. Click en tu proyecto
2. Ver tab "Deployment"
3. Ver últimas builds
4. Ver logs en tiempo real

### En Terminal:

```bash
# Ping a endpoint
curl -I https://tu-url.railway.app/health

# Debe retornar: HTTP/1.1 200 OK
```

---

## 🐛 Troubleshooting Común

| Problema | Solución |
|----------|----------|
| **Build falla** | Revisar logs → Maven → Verificar pom.xml |
| **App no inicia** | Revisar variables de entorno → PORT debe existir |
| **404 en endpoints** | Verificar context path en application.properties |
| **JWT inválido** | Regenerar secret → Actualizar en prod.properties y Railway vars |
| **Timeout** | Railway puede tomar 2-3 min → Esperar |

---

## 📈 Próximos Pasos

1. ✅ Probá endpoints en Railway
2. Implementa base de datos (PostgreSQL en Railway = fácil)
3. Agrega más validaciones
4. Implementa rate limiting
5. Agrega logging centralizado

---

**Estado:** 🟢 Listo para Deploy
**Tiempo estimado:** 5-10 minutos
**Costo:** GRATUITO (primeros $5/mes en Railway)
