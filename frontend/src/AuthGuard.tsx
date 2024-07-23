import { Outlet, Navigate } from "react-router-dom";
import { useToken } from "./api/auth/authApi";
import LoadingScreen from "./components/LoadingScreen/LodaingScreen";
import { useState } from "react";

const AuthGuard = (): JSX.Element => {
  const { token, isLoading } = useToken();
  const [delayed, setDelayed] = useState<boolean>(true);

  setTimeout(() => {
    setDelayed(false);
  }, 1000);

  if (isLoading || delayed) {
    return (
      <LoadingScreen>
        <div className="fnt-lg">
          <span className="font-bold italic">We are working</span> on your
          identity
        </div>
      </LoadingScreen>
    );
  }

  return token ? <Outlet /> : <Navigate to="/login" />;
};

export default AuthGuard;
