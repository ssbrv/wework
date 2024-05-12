import { Outlet, Navigate } from "react-router-dom";
import { useAuth } from "./AuthProvider";
import { okNotification } from "./components/Notifications/Notifications";

const AuthGuard = (): JSX.Element => {
  const { token } = useAuth();

  if (token) return <Outlet />;

  okNotification("Please, sign in!", "Transferring you to the login page...");

  return <Navigate to="/login" />;
};

export default AuthGuard;
