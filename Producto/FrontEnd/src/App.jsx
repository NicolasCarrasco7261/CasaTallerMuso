import { RouterProvider } from "react-router-dom";

import "./App.css";
import { AuthProvider } from "./utils/AuthProvider";
import router from "./utils/router";

import "./utils/api";

function App() {
  return (
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  );
}

export default App;
