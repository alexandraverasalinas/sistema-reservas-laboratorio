# Sistema de Reservas de Laboratorio

Plataforma web centralizada para la gestión de reservas de laboratorios en la Escuela de Educación Superior Cibertec. Permite a los alumnos reservar cupos individuales, a los profesores reservar el salón completo y a los administradores gestionar todos los recursos del sistema. Integrado con infraestructura de red basada en Windows Server y Active Directory para autenticación centralizada.

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
```
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
└── recursos-red/(Infraestructura de red)
    ├── Active Directory (usuarios, grupos, unidades organizativas)
    └── Recursos compartidos (alumnos, docentes, material)
```

---

<p align="center">Proyecto final - Julio 2026</p>
