import { BrowserRouter, Route, Routes } from "react-router-dom";
import AuthProvider from "./hooks/AuthProvider";
import LoginPage from "./pages/Login/LoginPage";
import RegisterPage from "./pages/Register/RegisterPage";
import WelcomePage from "./pages/Welocome/WelcomePage";
import LoginGuard from "./LoginGuard";
import AuthGuard from "./AuthGuard";
import NotFoundPage from "./pages/NotFound/NotFoundPage";
import ProfilePage from "./pages/Profile/Profile";
import NavigationBarLayout from "./components/NavigationBar/NavigationBarLayout";
import ExceptionProvider from "./hooks/ExceptionHandler";
import UserProvider from "./hooks/UserProvider";
import ProjectList from "./pages/ProjectList/ProjectList";

export const Router = (): JSX.Element => {
  return (
    <BrowserRouter>
      <AuthProvider>
        <ExceptionProvider>
          <Routes>
            <Route element={<LoginGuard />}>
              <Route path="/" element={<WelcomePage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
            </Route>
            <Route element={<AuthGuard />}>
              <Route element={<UserProvider />}>
                <Route element={<NavigationBarLayout />}>
                  <Route path="/profile" element={<ProfilePage />} />
                  <Route path="/tasks" element={<ProfilePage />} />
                  <Route path="/projects" element={<ProjectList />} />
                  <Route path="*" element={<NotFoundPage />} />
                </Route>
              </Route>
            </Route>
          </Routes>
        </ExceptionProvider>
      </AuthProvider>
    </BrowserRouter>
  );
};
