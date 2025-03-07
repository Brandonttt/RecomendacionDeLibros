## Requisitos

- Java 11 o superior
- Maven 3.6.0 o superior
- Docker (opcional, para ejecutar la base de datos en un contenedor)

## Configuración de la Base de Datos

### Opción 1: Usar Docker

1. Asegúrate de tener Docker instalado y en funcionamiento.
2. Ejecuta el siguiente comando para iniciar la base de datos con Docker:

    ```sh
    docker-compose up -d
    ```

### Opción 2: Configuración Manual

1. Crea una base de datos en tu sistema de gestión de bases de datos preferido (por ejemplo, MySQL, PostgreSQL).
2. Actualiza el archivo `src/main/resources/application.properties` con la configuración de tu base de datos:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/tu_base_de_datos
    spring.datasource.username=admin
    spring.datasource.password=admin
    spring.jpa.hibernate.ddl-auto=update
    ```

## Instalación de Dependencias

1. Clona el repositorio:

    ```sh
    git clone https://github.com/Brandonttt/HolaSpring6CV3.git
    cd HolaSpring6CV3
    ```

2. Usa Maven para instalar las dependencias:

    ```sh
    ./mvnw clean install
    ```

## Ejecución de la Aplicación

1. Ejecuta la aplicación con Maven:

    ```sh
    ./mvnw spring-boot:run
    ```

2. La aplicación estará disponible en `http://localhost:8085`.

## Ejecución de Pruebas

1. Para ejecutar las pruebas, usa el siguiente comando:

    ```sh
    ./mvnw test
    ```


---  CONFIGURACIÓN DOCKER  ---

1.- Tener instaldo el Docker Desktop

2.- Generar el archivo "Dockerfile" en la raiz del proyecto

3.- Generar el Docker-compose.yml

4.- escribir en la terminal el siguiente comando "docker-compose up --build"
