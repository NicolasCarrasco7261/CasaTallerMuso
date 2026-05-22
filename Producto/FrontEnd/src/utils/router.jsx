import { createHashRouter } from "react-router-dom";
import { AuthLayout, PublicLayout } from "../paginas/Layouts";
import Home from "../paginas/Home/Home";
import Login from "../paginas/Login/Login";
import Register from "../paginas/Register/Register";
import Cursos from "../paginas/Cursos/Cursos";
import CursoPage from "../paginas/Cursos/CursoPage";

const router = createHashRouter([
  {
    path: "/",
    element: <PublicLayout />,
    children: [
      {
        index: true,
        element: <Home />
      },
      {
        path: "/cursos",
        element: <Cursos />
      },
      {
        path: "/cursos/:id",
        element: <CursoPage />
      }
    ]
  },
  {
    path: "/auth",
    element: <AuthLayout />,
    children: [
      {
        path: "/auth/login",
        element: <Login />
      },
      {
        path: "/auth/signup",
        element: <Register />
      }
    ]
  }
]);

export default router;