import React, {
  useState,
  createContext,
  useMemo,
  useContext,
  useEffect,
} from "react";
import api from "./api/api";

interface AuthContextProps {
  token: string | null;
  setToken: (newToken: string | null) => void;
}

const AuthContext = createContext<AuthContextProps | undefined>(undefined);

const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [token, setToken_] = useState<string | null>(
    localStorage.getItem("token")
  );

  const setToken = (newToken: string | null): void => {
    setToken_(newToken);
  };

  useEffect(() => {
    if (token) {
      api.defaults.headers.common.Authorization = `Bearer ${token}`;
      localStorage.setItem("token", token);
    } else {
      api.defaults.headers.common.Authorization = undefined;
      localStorage.removeItem("token");
    }
  }, [token]);

  const contextValue = useMemo(() => ({ token, setToken }), [token]);

  return (
    <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextProps => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within AuthProvider");
  return context;
};

export default AuthProvider;
