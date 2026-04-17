# Kittigram — Claude Code Rules

## Stack
- Java 21, Quarkus 3.34.3, Hibernate Reactive + Panache, SmallRye Mutiny
- PostgreSQL 16 (esquemas separados por servicio), MinIO (S3-compatible)
- Kafka (SmallRye Reactive Messaging), gRPC (auth↔user), SmallRye JWT (15 min)

## Servicios y puertos
| Servicio            | HTTP | gRPC        |
|---------------------|------|-------------|
| gateway-service     | 8080 | —           |
| user-service        | 8081 | 9090 server |
| auth-service        | 8082 | —           |
| storage-service     | 8083 | —           |
| cat-service         | 8084 | —           |
| notification-service| 8085 | —           |
| adoption-service    | 8086 | —           |
| form-analysis-service| 8087| —           |

## Arquitectura — reglas duras
- Sin dependencias Maven entre módulos. Comunicación solo vía gRPC o Kafka.
- Sin relaciones JPA cross-service. Cada servicio tiene su propio esquema PostgreSQL.
- DTOs siempre (Records Java). Nunca exponer entidades Panache directamente.
- Patrón Repository, no Active Record.

## Reactividad (Mutiny)
- `@WithSession` para lecturas en Service; `@WithTransaction` para escrituras.
- `@WithSession` NO funciona en métodos que devuelven `Multi<T>` → poner `@WithSession` en el Repository y transformar en Service:
  ```java
  public Multi<T> search() {
      return repo.findAll()
          .onItem().transformToMulti(list -> Multi.createFrom().iterable(list));
  }
  ```
- `@Incoming` (Kafka) + `@WithTransaction` combinados directamente → fallo. Delegar la persistencia a un bean separado.

## Gotchas conocidos
- Records Java: añadir `@JsonProperty` en cada campo, o registrar `ParameterNamesModule` en JacksonConfig.
- `"order"` es palabra reservada HQL → usar `imageOrder` con `@Column(name = "image_order")`.
- Copiar el `.proto` en `src/main/proto/` de cada servicio que lo necesite. No compartir módulo Maven.
- Borrado de usuarios: lógico (status → Inactive), nunca físico.

## Comportamiento esperado
- **Compilar solo el módulo afectado**: `mvn compile -pl <módulo>` o `mvn test -pl <módulo>`.
- **Tests e2e**: `mvn test -Pe2e -pl e2e-tests` (requiere stack completo corriendo).
- **Si un fix falla dos veces seguidas por el mismo motivo → parar y preguntar al usuario.**
- Cambios atómicos: modificar solo lo necesario. No reescribir ficheros completos salvo que sea inevitable.
- No añadir comentarios salvo que el WHY sea no obvio.
- No añadir atribución `Co-Authored-By: Anthropic` en commits.