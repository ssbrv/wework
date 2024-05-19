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
import ExceptionProvider from "./hooks/ExceptionProvider";
import UserProvider from "./hooks/UserProvider";
import ProjectList from "./pages/ProjectList/ProjectList";
import ProjectDetails from "./pages/ProjectList/Project/ProjectDetails/ProjectDetails";
import ProjectProvider from "./hooks/ProjectProvider";
import ProjectLayout from "./pages/ProjectList/Project/ProjectLayout";
import ProjectTasks from "./pages/ProjectList/Project/ProjectTasks/ProjectTasks";
import ProjectMembers from "./pages/ProjectList/Project/ProjectMembers/ProjectMembers";
import ProjectSettings from "./pages/ProjectList/Project/ProjectSettings/ProjectSettings";
import ProjectInvitations from "./pages/ProjectList/Project/ProjectInvitations/ProjectInvitations";

export const Router = (): JSX.Element => {
  return (
    <BrowserRouter>
      <AuthProvider>
        <ExceptionProvider>
          <Routes>
            <Route element={<LoginGuard />}>
              <Route path="/" element={<WelcomePage />} />
              <Route path="login" element={<LoginPage />} />
              <Route path="register" element={<RegisterPage />} />
            </Route>
            <Route element={<AuthGuard />}>
              <Route element={<NavigationBarLayout />}>
                <Route path=":userId" element={<UserProvider />}>
                  <Route path="profile" element={<ProfilePage />} />
                </Route>
                <Route path="tasks" element={<NotFoundPage />} />
                <Route path="projects">
                  <Route path="" element={<ProjectList />} />
                  <Route path=":projectId" element={<ProjectProvider />}>
                    <Route path="" element={<ProjectLayout />}>
                      <Route path="details" element={<ProjectDetails />} />
                      <Route path="tasks" element={<ProjectTasks />} />
                      <Route path="members" element={<ProjectMembers />} />
                      <Route
                        path="invitations"
                        element={<ProjectInvitations />}
                      />
                      <Route path="settings" element={<ProjectSettings />} />
                    </Route>
                  </Route>
                </Route>

                <Route path="*" element={<NotFoundPage />} />
              </Route>
            </Route>
          </Routes>
        </ExceptionProvider>
      </AuthProvider>
    </BrowserRouter>
  );
};
