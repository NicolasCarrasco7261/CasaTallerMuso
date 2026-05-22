import { Outlet } from "react-router-dom";
import Navbar from "../componentes/Navbar/Navbar";

export function PublicLayout() {
    return <div className="min-vh-100 bg-custom-cream pb-5">
        <Navbar />
        <main className="container">
            <Outlet />
        </main>
    </div>
}

export function AuthLayout() {
    return <div className="min-vh-100 bg-custom-cream d-flex align-items-center pb-5">
        <main className="container py-5">
            <Outlet />
        </main>
    </div>
}