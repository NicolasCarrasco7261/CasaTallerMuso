import { createHashRouter } from "react-router-dom";
import { AuthLayout, DashboardLayout, PublicLayout } from "../paginas/Layouts";
import Home from "../paginas/Home/Home";
import Login from "../paginas/Login/Login";
import Register from "../paginas/Register/Register";
import Cursos from "../paginas/Cursos/Cursos";
import CursoPage from "../paginas/Cursos/CursoPage";
import TuActividad from "../paginas/TuActividad/TuActividad";
import Eventos from "../paginas/Eventos/Eventos";
import EventoPage from "../paginas/Eventos/EventoPage";

const router = createHashRouter([
  {
    path: "/",
    element: <PublicLayout />,
    children: [
      {
        index: true,
        element: <Home />,
      },
      {
        path: "/cursos",
        element: <Cursos />,
      },
      {
        path: "/cursos/:id",
        element: <CursoPage />,
      },
      {
        path: "/eventos",
        element: <Eventos />,
      },
      {
        path: "/eventos/:id",
        element: <EventoPage />,
      },
    ],
  },
  {
    path: "/auth",
    element: <AuthLayout />,
    children: [
      {
        path: "/auth/login",
        element: <Login />,
      },
      {
        path: "/auth/signup",
        element: <Register />,
      },
    ],
  },
  {
    path: "/me",
    element: <DashboardLayout />,
    children: [
      {
        index: true,
        element: <TuActividad />,
      },
      {
        path: "/me/dashboard",
      },
    ],
  },
]);

export default router;
