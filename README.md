# UFW Web Based Firewall

ArcFirewall es una interfaz web para administrar reglas de firewall UFW en servidores Linux de manera centralizada. Permite:

- Ver el estado actual de UFW (Activo/Inactivo).  
- Habilitar o deshabilitar UFW desde el panel.  
- Visualizar reglas activas con detalle de puerto, acción y origen.  
- Agregar nuevas reglas (`ALLOW` o `DENY`).  
- Eliminar reglas no protegidas (SSH, HTTP, HTTPS).  

Todo esto con una interfaz basada en **Thymeleaf**. Próximamente se migrará la parte frontend a **Angular**.
La idea de este proyecto es que a futuro cuente con endpoints protegidos por tokens y log-in.

---

## Tecnologías

- **Backend:** Java Spring Boot 3  
- **Frontend:** Thymeleaf + HTML/CSS  
- **Firewall:** UFW (Uncomplicated Firewall)  
- **Sistema operativo:** Linux (Ubuntu Server)  

---

## Instalación y Configuración

### Requisitos

- Java 17+  
- Maven  
- Linux con UFW instalado y permisos sudo  
- Firewall UFW habilitado (opcional, puede habilitarse desde la app web)  

### Clonar y Compilar

```bash
git clone https://github.com/tuusuario/ArcticFirewall.git
cd ArcticFirewall
mvn clean install
```
La aplicación se ejecuta por defecto en el puerto 8378, accesible en:
```bash
http://serverip:8378/
