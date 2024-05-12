import { BrowserRouter, Route, Routes } from "react-router-dom";
import AuthProvider from "./AuthProvider";
import LoginPage from "./pages/Login/LoginPage";
import RegisterPage from "./pages/Register/RegisterPage";
import WelcomePage from "./pages/Welocome/WelcomePage";
import LoginGuard from "./LoginGuard";
import AuthGuard from "./AuthGuard";
import NotFoundPage from "./pages/NotFound/NotFoundPage";
import ProfilePage from "./pages/Profile/Profile";

export const Router = (): JSX.Element => {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route element={<LoginGuard />}>
            <Route path="/" element={<WelcomePage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
          </Route>
          <Route element={<AuthGuard />}>
            <Route path="/profile" element={<ProfilePage />} />
          </Route>
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
};
