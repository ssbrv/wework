import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./AuthProvider";

const LoginGuard = (): JSX.Element => {
  const { token } = useAuth();

  return token ? <Outlet /> : <Navigate to="profile" />;
};

export default LoginGuard;
