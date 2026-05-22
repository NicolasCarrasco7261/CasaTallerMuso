import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
    const user = localStorage.getItem("usuario");

    if (!user) {
        // Si no hay sesión, al login directo
        return <Navigate to="/login" replace />;
    }

    return children;
};

export default ProtectedRoute;