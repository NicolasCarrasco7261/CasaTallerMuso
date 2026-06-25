import { Outlet, useNavigate } from "react-router-dom";
import Navbar from "../componentes/Navbar/Navbar";
import { useAuth } from "../utils/AuthProvider";
import { useEffect } from "react";
import Footer from "../componentes/Footer/Footer";

export function PublicLayout() {
  return (
    <div className="d-flex flex-column min-vh-100 bg-custom-cream">
      <Navbar />
      <main className="container pb-5">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
}

export function AuthLayout() {
  return (
    <div className="min-vh-100 bg-custom-cream d-flex align-items-center pb-5">
      <main className="container py-5">
        <Outlet />
      </main>
    </div>
  );
}

export function DashboardLayout() {
  const { isLoggedIn } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoggedIn()) {
      navigate("/auth/login");
    }
  }, []);

  return (
    <div className="d-flex flex-column min-vh-100 bg-custom-cream">
      <Navbar />
      <main className="container d-flex flex-column flex-grow-1 my-4">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
}
