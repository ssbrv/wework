import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./hooks/AuthProvider";

const LoginGuard = (): JSX.Element => {
  const { token } = useAuth();

  return token ? <Navigate to="profile" /> : <Outlet />;
};

export default LoginGuard;
