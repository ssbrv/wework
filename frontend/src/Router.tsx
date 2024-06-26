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
import InvitationList from "./pages/Invitations/InvitationList";
import MembershipProvider from "./hooks/MembershipProvider";
import ProjectMember from "./pages/ProjectList/Project/ProjectMembers/ProjectMember";
import ProjectInvitation from "./pages/ProjectList/Project/ProjectInvitations/ProjectInvitation";
import TaskList from "./pages/TaskList/TaskList";
import TaskProvider from "./hooks/TaskProvider";
import ProjectTask from "./pages/ProjectList/Project/ProjectTasks/ProjectTask.tsx/ProjectTask";

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
                <Route path="users">
                  <Route path=":userId" element={<UserProvider />}>
                    <Route path="profile" element={<ProfilePage />} />
                  </Route>
                </Route>
                <Route path="projects">
                  <Route path="" element={<ProjectList />} />
                  <Route path=":projectId" element={<ProjectProvider />}>
                    <Route path="" element={<ProjectLayout />}>
                      <Route path="details" element={<ProjectDetails />} />
                      <Route path="tasks">
                        <Route path="" element={<ProjectTasks />} />
                        <Route path=":taskId" element={<TaskProvider />}>
                          <Route path="" element={<ProjectTask />} />
                        </Route>
                      </Route>
                      <Route path="members">
                        <Route path="" element={<ProjectMembers />} />
                        <Route
                          path=":membershipId"
                          element={<MembershipProvider />}
                        >
                          <Route path="" element={<ProjectMember />} />
                        </Route>
                      </Route>
                      <Route path="invitations">
                        <Route path="" element={<ProjectInvitations />} />
                        <Route
                          path=":membershipId"
                          element={<MembershipProvider />}
                        >
                          <Route path="" element={<ProjectInvitation />} />
                        </Route>
                      </Route>
                      <Route path="settings" element={<ProjectSettings />} />
                    </Route>
                  </Route>
                </Route>
                <Route path="/invitations" element={<InvitationList />} />
                <Route path="tasks" element={<TaskList />} />
                <Route path="*" element={<NotFoundPage />} />
              </Route>
            </Route>
          </Routes>
        </ExceptionProvider>
      </AuthProvider>
    </BrowserRouter>
  );
};
