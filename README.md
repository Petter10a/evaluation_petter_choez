# 🚀 **Evaluación Petter Choez - Microservicio Spring Boot**

### 🎯 **Características Principales**

- ✅ **CRUD completo** de empleados con validaciones
- ✅ **Sistema de evaluaciones** por categorías (Liderazgo, Comunicación, Resolución de Problemas, Trabajo en Equipo)
- ✅ **Cálculo automático** de promedios de evaluación
- ✅ **MapStruct** para mapeo automático de DTOs
- ✅ **HTTP Request Logging** con interceptor personalizado
- ✅ **Carga automática de datos** al iniciar la aplicación
- ✅ **Suite completa de tests** (Unitarios, Controller, Integración)
- ✅ **Documentación API** con Swagger/OpenAPI
- ✅ **Base de datos H2** en memoria para desarrollo

## 🛠️ **Tecnologías Utilizadas**

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Spring Boot** | 3.5.4 | Framework principal |
| **Java** | 17 | Lenguaje de programación |
| **Maven** | 3.8+ | Gestión de dependencias |

## 🚀 **Instrucciones de Ejecución**

### **Prerrequisitos**

- ☕ **Java 17** o superior
- 📦 **Maven 3.8+**
- 🖥️ **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### **1. Clonar y Compilar**

```bash
# Navegar al directorio del proyecto
cd evaluacion_petter_choez

# Compilar el proyecto
mvn clean compile

# Ejecutar tests (opcional pero recomendado)
mvn test
```

### **2. Ejecutar la Aplicación**

```bash
# Opción 1: Con Maven
mvn spring-boot:run

# Opción 2: Ejecutar JAR compilado
mvn clean package
java -jar target/evaluacion_petter_choez-0.0.1-SNAPSHOT.jar
```

### **3. Verificar el Inicio**

La aplicación estará disponible en:
- 🌐 **API Base URL**: `http://localhost:8080/kevaluacion`
- 📚 **Swagger UI**: `http://localhost:8080/kevaluacion/swagger-ui/index.html`

## 📊 **Carga Automática de Datos de Prueba**

### **¿Qué datos se cargan automáticamente?**

Al iniciar la aplicación, el **DataLoader** ejecuta automáticamente la carga de datos de prueba:

#### **👥 Empleados Creados (5 empleados):**
```
ID | Nombre             | Email
---|--------------------|--------------------------
1  | Juan Pérez         | juan.perez@empresa.com
2  | María García       | maria.garcia@empresa.com
3  | Carlos Rodríguez   | carlos.rodriguez@empresa.com
4  | Ana Martínez       | ana.martinez@empresa.com
5  | Pedro Ramírez      | pedro.ramirez@empresa.com
```

#### **📋 Evaluaciones Creadas (6 evaluaciones):**

1. **Carlos Rodríguez** evaluó a **Juan Pérez** (Q1)
2. **Ana Martínez** evaluó a **Juan Pérez** (Anual 2024)
3. **Carlos Rodríguez** evaluó a **María García** (Desempeño)
4. **Juan Pérez** evaluó a **Pedro Ramírez** (Mentor-Aprendiz)
5. **Ana Martínez** evaluó a **Carlos Rodríguez** (Liderazgo)
6. **Ana Martínez** evaluó a **Pedro Ramírez** (Adicional)

Cada evaluación incluye puntuaciones (1-5) en las 4 categorías:
- 🎯 **LIDERAZGO**
- 💬 **COMUNICACION**
- 🧩 **PROBLEMA** (Resolución de Problemas)
- 🤝 **TRABAJO_EN_EQUIPO**

### **¿Cuándo se ejecuta la carga?**

- ✅ **Automáticamente** al iniciar la aplicación
- ✅ **Solo si no existen datos** previos (evita duplicados)
- ✅ **Se muestran logs** informativos del proceso

```bash
# Logs que verás al iniciar:
INFO c.k.e.Config.DataLoader : Cargando datos de prueba...
INFO c.k.e.Config.DataLoader : Empleados creados: [Juan Pérez, María García, ...]
INFO c.k.e.Config.DataLoader : Evaluación creada: Carlos Rodríguez evaluó a Juan Pérez
INFO c.k.e.Config.DataLoader : Datos de prueba cargados exitosamente!
```

## 📡 **API Endpoints Principales**

### **👥 Empleados**

| Método | Endpoint | Descripción | Ejemplo |
|--------|----------|-------------|---------|
| `GET` | `/employees` | Listar empleados (paginado) | `GET /employees?page=0&size=10` |
| `GET` | `/employees/{id}` | Obtener empleado por ID | `GET /employees/1` |
| `POST` | `/employees` | Crear nuevo empleado | Ver ejemplo abajo |
| `GET` | `/employees/{id}/summary` | Promedios de evaluación | `GET /employees/1/summary` |

### **📋 Evaluaciones**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/evaluations` | Listar evaluaciones |
| `POST` | `/evaluations` | Crear evaluación |

## 🧪 **Ejecución de Tests**

El proyecto incluye una suite completa de 19 tests:

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar solo tests unitarios
mvn test -Dtest="*ServiceTest"

# Ejecutar solo tests de controlador
mvn test -Dtest="*ControllerTest"

# Ejecutar solo tests de integración
mvn test -Dtest="*IntegrationTest"
```

## 📝 **Logging y Monitoreo**

### **HTTP Request Logging**
La aplicación incluye un interceptor que registra todas las peticiones HTTP:

```bash
# Logs que verás durante las peticiones:
INFO c.k.e.C.I.RequestLoggingInterceptor : INICIA - Método: GET | URI: /employees/1 | IP: 127.0.0.1
INFO c.k.e.C.I.RequestLoggingInterceptor : FINALIZA - Método: GET | URI: /employees/1 | Status: 200 | Duración: 95ms | IP: 127.0.0.1
```

### **Configurar Nivel de Logging**
```properties
# En application.properties
logging.level.com.kevaluacion.evaluacion_petter_choez=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

## 🚨 **Manejo de Errores**

La API maneja errores de forma consistente:

### **Ejemplos de Respuestas de Error:**

**404 - Empleado no encontrado:**
```json
{
  "success": false,
  "message": "Employee not found with id: 999",
  "data": null
}
```

**409 - Email duplicado:**
```json
{
  "success": false,
  "message": "An employee with the email already exists: juan.perez@empresa.com",
  "data": null
}
```

**400 - Datos inválidos:**
```json
{
  "success": false,
  "message": "Full name is required",
  "data": null
}
```

## 🔧 **Configuración Personalizada**

### **Variables de Entorno Soportadas:**

```bash
# Puerto del servidor
SERVER_PORT=8080

# Context path
SERVER_SERVLET_CONTEXT_PATH=/kevaluacion

# Base de datos H2
SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=password

# Habilitar SQL logging
SPRING_JPA_SHOW_SQL=true
```

## 📚 **Documentación Adicional**

### **Swagger/OpenAPI**
- **URL**: http://localhost:8080/kevaluacion/swagger-ui/index.html
- **Incluye**: Todos los endpoints con ejemplos y esquemas
- **Interactivo**: Puedes probar la API directamente desde la interfaz

### **Arquitectura del Proyecto**
```
src/
├── main/java/com/kevaluacion/evaluacion_petter_choez/
│   ├── Config/          # Configuraciones (DataLoader, WebConfig, etc.)
│   ├── Controller/      # Controladores REST
│   ├── DTO/            # Data Transfer Objects
│   ├── Entity/         # Entidades JPA
│   ├── Exception/      # Excepciones personalizadas
│   ├── Mapper/         # MapStruct mappers
│   ├── Repository/     # Repositorios JPA
│   ├── Service/        # Lógica de negocio
│   ├── Util/          # Utilidades y respuestas genéricas
│   └── Validation/    # Validaciones personalizadas
└── test/              # Tests unitarios, controlador e integración
```