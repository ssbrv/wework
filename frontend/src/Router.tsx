import { BrowserRouter, Route, Routes } from "react-router-dom";
import AuthProvider from "./AuthProvider";
import LoginPage from "./pages/Login/LoginPage";
import RegisterPage from "./pages/Register/RegisterPage";
import WelcomePage from "./pages/WelocomePage/WelcomePage";
import LoginGuard from "./LoginGuard";

export const Router = (): JSX.Element => {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route element={<LoginGuard />}>
            <Route path="/" element={<WelcomePage />}></Route>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
};
