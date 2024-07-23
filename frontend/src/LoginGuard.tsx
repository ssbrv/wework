import { Navigate, Outlet } from "react-router-dom";
import { useMyId, useToken } from "./api/auth/authApi";

const LoginGuard = (): JSX.Element => {
  const { token } = useToken();
  const { myId } = useMyId();

  return token ? <Navigate to={`/users/${myId}/profile`} /> : <Outlet />;
};

export default LoginGuard;
