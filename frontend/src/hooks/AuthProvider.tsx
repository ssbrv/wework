import React, {
  useState,
  createContext,
  useMemo,
  useContext,
  useEffect,
  useCallback,
} from "react";
import api from "../api/api";
import { AuthRequest } from "../http/request/AuthRequest";
import { RegisterRequest } from "../http/request/RegisterRequest";
import { mutate } from "swr";
import { useNavigate } from "react-router-dom";
import { ChangePasswordRequest } from "../http/request/ChangePasswordRequest";

interface AuthContextProps {
  myId: string | null;
  setMyId: (newId: string | null) => void;
  token: string | null;
  setToken: (newToken: string | null) => void;
  login: (authRequest: AuthRequest) => Promise<void>;
  register: (registerRequest: RegisterRequest) => Promise<void>;
  logout: () => Promise<void>;
  fullLogout: () => Promise<void>;
  changePassword: (
    changePasswordRequest: ChangePasswordRequest
  ) => Promise<void>;
  deleteAccount: () => Promise<void>;
}

const AuthContext = createContext<AuthContextProps | undefined>(undefined);

const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  console.log("auth provider");

  const [token, setToken_] = useState<string | null>(
    localStorage.getItem("token")
  );
  const [myId, setMyId_] = useState<string | null>(
    localStorage.getItem("myId")
  );

  const [tokenLoaded, setTokenLoaded] = useState<boolean>(false);
  const navigate = useNavigate();

  const setMyId = (newId: string | null): void => {
    setMyId_(newId);
  };

  const setToken = (newToken: string | null): void => {
    setTokenLoaded(false);
    setToken_(newToken);
  };

  useEffect(() => {
    console.log("The local token was triggered");
    if (token) {
      console.log(
        "The local token was set to a new value. Putting it into the local storage",
        token
      );
      api.defaults.headers.common.Authorization = `Bearer ${token}`;
      localStorage.setItem("token", token);
    } else {
      console.log(
        "The local token was set to null. Removing it from the local storage"
      );
      api.defaults.headers.common.Authorization = undefined;
      localStorage.removeItem("token");
    }
    setTokenLoaded(true);
  }, [token]);

  useEffect(() => {
    if (myId) {
      localStorage.setItem("myId", myId);
      return;
    }

    localStorage.removeItem("myId");
  }, [myId]);

  const login = useCallback(async (authRequest: AuthRequest): Promise<void> => {
    const authResponse = await api.post("auth/authenticate", authRequest);
    console.log("Loging in and setting the local token");
    setToken(authResponse.data.jwtToken);
    setMyId(authResponse.data.id.toString());
  }, []);

  const register = useCallback(
    async (registerRequest: RegisterRequest): Promise<void> => {
      const authResponse = await api.post("auth/register", registerRequest);
      setToken(authResponse.data.jwtToken);
      setMyId(authResponse.data.id.toString());
    },
    []
  );

  const logout = useCallback(async (): Promise<void> => {
    // attempt to logout on backend
    await api.post("auth/logout", { jwtToken: token }).catch(function (ignore) {
      // the token got corrupted or expired -> just logout
    });
    // remove token from storage
    console.log("Loging out and removing the local token");
    setToken(null);
    setMyId(null);
    // delete swr cache
    mutate(() => true, undefined, { revalidate: false });
    navigate("/login");
  }, [navigate, token]);

  const fullLogout = useCallback(async (): Promise<void> => {
    const authResponse = await api.post("auth/full-logout");
    // reset token
    console.log("Full log out. Replacing the old token with a new one");
    setToken(authResponse.data.jwtToken);
  }, []);

  const changePassword = useCallback(
    async (changePasswordRequest: ChangePasswordRequest): Promise<void> => {
      const authResponse = await api.put(
        "auth/password",
        changePasswordRequest
      );
      setToken(authResponse.data.jwtToken);
    },
    []
  );

  const deleteAccount = useCallback(async (): Promise<void> => {
    await api.delete("auth");
    setToken(null);
    setMyId(null);
  }, []);

  const contextValue = useMemo(
    () => ({
      myId,
      setMyId,
      token,
      setToken,
      login,
      register,
      logout,
      fullLogout,
      changePassword,
      deleteAccount,
    }),
    [
      myId,
      token,
      login,
      register,
      logout,
      fullLogout,
      changePassword,
      deleteAccount,
    ]
  );

  return (
    <AuthContext.Provider value={contextValue}>
      {tokenLoaded && children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextProps => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within AuthProvider");
  return context;
};

export default AuthProvider;
