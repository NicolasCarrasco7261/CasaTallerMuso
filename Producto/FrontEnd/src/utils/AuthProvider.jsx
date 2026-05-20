import { createContext, useState, useEffect, useContext } from "react";

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [token, setToken] = useState(() => localStorage.getItem('jwt') || null);
    const [user, setUser] = useState(null);

    useEffect(() => {
        if (token) {
            localStorage.setItem('jwt', token);
            const fetchUser = async () => {
                const response = await fetch("/api/usuarios/me");
                if (response.ok) {
                    const user = await response.json();
                    setUser(user);
                }
            };
            fetchUser();
        } else {
            localStorage.removeItem('jwt');
            setUser(null);
        }
    }, [token]);

    const login = (jwt) => {
        setToken(jwt);
    };

    const logout = () => {
        setToken(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => useContext(AuthContext);