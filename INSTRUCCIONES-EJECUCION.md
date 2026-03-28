# 🚀 INSTRUCCIONES PARA EJECUTAR - SEGURIDAD SIMPLIFICADA

## Estado Actual

✅ SecurityConfig.java actualizado  
✅ Compilación lista  
⏳ Inicio de Spring Boot en progreso  

---

## 📋 Instrucciones Paso a Paso

### PASO 1: Abrir una terminal NUEVA

```bash
# Ir al directorio del proyecto
cd c:/Users/Jaedu.HS/Version2-SETI/crezcamos-juntos
```

### PASO 2: Iniciar Spring Boot

```bash
# Compilar e iniciar en modo desarrollo
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

**Esperar a ver este mensaje:**
```
Tomcat started on port(s): 8080 (http) with context path '/api'
Started MiAplicacionJavaApplication
```

### PASO 3: Abrir OTRA terminal y probar

```bash
# Test de Health Check (debe retornar 200 OK)
curl http://localhost:8080/api/actuator/health

# Respuesta esperada:
# {"status":"UP"}
```

### PASO 4: Probar Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}' \
  -w "\nHTTP: %{http_code}\n"
```

**Respuesta esperada (HTTP 200):**
```json
{"token":"eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."}
HTTP: 200
```

### PASO 5: Usar el token para obtener usuarios

```bash
# Guardar el token (reemplazar <TOKEN> con el valor real)
TOKEN="eyJhbGciOiJIUzUxMi..."

# Acceder a endpoint protegido
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"

# Respuesta esperada (HTTP 200):
# []
```

---

## 🔑 Credenciales Demo

```
Username: admin
Password: password123
```

---

## 🖥️ Cambios en SecurityConfig.java

**Antes (Restrictivo):**
```
.antMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
.anyRequest().authenticated()
```

**Ahora (Desarrollo abierto):**
```
.anyRequest().permitAll()
```

⚠️ Esto permite TODO SIN autenticación en desarrollo.  
Para producción, cambiar a configuración restrictiva.

---

## ✅ Checklist

- [ ] Abierto terminal 1
- [ ] Ejecutado: `mvn spring-boot:run ...`
- [ ] Ves mensaje: "Tomcat started on port(s): 8080"
- [ ] Abierto terminal 2
- [ ] Probado: `curl http://localhost:8080/api/actuator/health`
- [ ] Recibido: `{"status":"UP"}`
- [ ] Probado: `curl ... /api/auth/login`
- [ ] Recibido: Token JWT
- [ ] Probado: `curl ... /api/users -H "Authorization: Bearer <TOKEN>"`
- [ ] Recibido: `[]`

---

## 🚨 Si hay problemas

### "Connection refused"
→ Spring Boot no está iniciado  
→ Verificar outputs en terminal 1  
→ Compilación puede tomar 30-60 segundos

### "HTTP 403 Forbidden"
→ Problema de CORS aún presente  
→ Reiniciar Maven con `mvn clean compile`  
→ Luego `mvn spring-boot:run ...`

### "HTTP 401 Unauthorized" (en /api/users)
→ Token JWT es inválido o expiró  
→ Obtener nuevo token con login

### "Command not found"
→ Maven no está en PATH  
→ Usar ruta completa a maven  
→ O instalar Maven: https://maven.apache.org

---

## 📊 Endpoints Ahora Disponibles

```
✅ GET  /api/actuator/health          → {"status":"UP"}
✅ POST /api/auth/login                → {"token":"..."}
✅ GET  /api/users                     → []
✅ POST /api/users                     → Create user
```

**Modo:** Ahora TODO es accesible sin restricciones (desarrollo)

---

## 🎯 Próximo Paso

Una vez que todo funcione correctamente:

1. Probamos endpoints locales
2. Deployamos a servidor público (Railway, Heroku, etc)
3. Testamos desde URL pública
4. Volvemos a implementar seguridad restrictiva

---

## 📌 Importante

Este cambio de seguridad (.anyRequest().permitAll()) es SOLO para desarrollo/testing.

Para PRODUCCIÓN, volver a:
```java
.antMatchers("/api/auth/**").permitAll()
.antMatchers("/api/actuator/health").permitAll()
.anyRequest().authenticated()
```

---

**Próxima acción:** Ejecutar Spring Boot y probar endpoints
