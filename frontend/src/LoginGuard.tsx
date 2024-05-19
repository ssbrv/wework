import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./hooks/AuthProvider";

const LoginGuard = (): JSX.Element => {
  const { token, myId } = useAuth();

  return token ? <Navigate to={`/${myId}/profile`} /> : <Outlet />;
};

export default LoginGuard;
