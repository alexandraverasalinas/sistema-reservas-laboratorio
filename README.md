# Sistema de Reservas de Laboratorio

Plataforma web para gestionar la reserva de horas de laboratorio, con roles diferenciados para alumnos, profesores y administradores.

---

## Stack

| Frontend | Backend | DB |
|:---:|:---:|:---:|
| Angular 17+ | Spring Boot | MySQL |

## Roles

- **Alumno** — Consulta disponibilidad en tiempo real, reserva cupos individuales, comparte horario con otros alumnos, visualiza y cancela sus reservas
- **Profesor** — Consulta disponibilidad, reserva salón completo, visualiza y cancela sus reservas
- **Administrador** — Dashboard con estadísticas, gestión de laboratorios, horarios, profesores, alumnos y reservas. Control total del sistema

  
## Estructura

├── sistema-reservas-lab-backend/     (Spring Boot + Maven)
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   ├── dto/
│   ├── enums/
│   └── config/
│
├── sistema-reservas-lab-frontend/    (Angular 17+)
│   ├── admin/    (dashboard, laboratorios, horarios, profesores, alumnos, reservas)
│   ├── alumno/   (calendario, mis-reservas, perfil)
│   ├── profesor/ (calendario, mis-reservas, perfil)
│   ├── auth/     (login, registro)
│   ├── services/
│   ├── models/
│   └── guards/
│
└── recursos-red/                     (Infraestructura de red)
    ├── Active Directory (usuarios, grupos, unidades organizativas)
    └── Recursos compartidos (alumnos, docentes, material)


---

<p align="center">Proyecto final - Julio 2026</p>
