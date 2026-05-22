const originalFetch = window.fetch;
const backendUrl = import.meta.env.VITE_REACT_APP_BACKEND_URL || "http://localhost:8080"

export function apiUrlFromPath(requestPath) {
    const apiUrl = `${backendUrl}/${
        requestPath.split("/").filter(Boolean).join("/")
    }`;
    return apiUrl;
}

window.fetch = async (requestPath, init) => {
    const token = localStorage.getItem('jwt') || null;
    const requestUrl = requestPath.startsWith("http") ? requestPath : apiUrlFromPath(requestPath);

    const logLines = [
        'Request outbound to API URL',
        token && '(w/ token)',
        requestUrl
    ].filter(Boolean);
    console.log(logLines.join(" "));

    if (requestPath.startsWith("http")) {
        console.warn("Using absolute URL");
    }

    if (!init) init = {};
    init.headers = {
        ...init.headers,
        'Content-Type': 'application/json',
        ...(token && { 'Authorization': `Bearer ${token}` })
    };
    
    const response = await originalFetch(requestUrl, init);
    return response;
}