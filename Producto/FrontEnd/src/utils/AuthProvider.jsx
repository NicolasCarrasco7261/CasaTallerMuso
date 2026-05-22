import { createContext, useState, useEffect, useContext } from "react";

const AuthContext = createContext();

function parseJwt(token) {
    try {
        const [encodedHeader, encodedPayload, signature] = token.split('.');
        if (!encodedHeader || !encodedPayload || !signature) return null;

        const header = JSON.parse(atob(encodedHeader));
        const payload = JSON.parse(atob(encodedPayload));

        return { header, payload, signature };
    } catch (e) {
        console.error("Invalid JWT", e)
        return null;
    }
}

export function AuthProvider({ children }) {
    const [token, setToken] = useState(() => localStorage.getItem('jwt') || null);
    const [user, setUser] = useState(null);

    const login = (jwt) => {
        setToken(jwt);
    };

    const logout = () => {
        setToken(null);
    };

    const isLoggedIn = () => {
        return (token != null);
    };

    useEffect(() => {
        let logoutTimer;

        if (!token) {
            localStorage.removeItem('jwt');
            setUser(null);
            return;
        }

        const { payload } = parseJwt(token);
        if (!payload) {
            setToken(null);
            return;
        }

        localStorage.setItem('jwt', token);

        const getTimeToJwtExpiration = () => {
            return payload.exp * 1000 - Date.now();
        }

        const fetchUser = async () => {
            const response = await fetch("/api/usuarios/me");
            if (response.ok) {
                const user = await response.json();
                setUser(user);
            }
        };

        const tokenExpiresIn = getTimeToJwtExpiration();

        if (tokenExpiresIn <= 0) {
            setToken(null);
        } else {
            fetchUser();
            logoutTimer = setTimeout(() => {
                setToken(null);
            }, tokenExpiresIn);
        }

        return () => clearTimeout(logoutTimer);
    }, [token]);

    return (
        <AuthContext.Provider value={{ user, login, logout, isLoggedIn }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => useContext(AuthContext);