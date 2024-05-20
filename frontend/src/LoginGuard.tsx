import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./hooks/AuthProvider";

const LoginGuard = (): JSX.Element => {
  const { token, myId } = useAuth();

  return token ? <Navigate to={`/users/${myId}/profile`} /> : <Outlet />;
};

export default LoginGuard;
