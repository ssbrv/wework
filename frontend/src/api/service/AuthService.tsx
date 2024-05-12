import { AuthRequest } from "../../http/request/AuthRequest";
import { RegisterRequest } from "../../http/request/RegisterRequest";
import api from "../api";

const AuthService = {
  auth: async (
    authRequest: AuthRequest,
    setToken: (newToken: string | null) => void
  ) => {
    const authResponse = await api.post("auth/authenticate", authRequest);
    setToken(authResponse.data.jwtToken);
  },

  register: async (
    registerRequest: RegisterRequest,
    setToken: (newToken: string | null) => void
  ) => {
    try {
      const authResponse = await api.post("auth/register", registerRequest);
      setToken(authResponse.data.jwtToken);
    } catch (error) {
      console.error("Login error: ", error);
    }
  },
};

export default AuthService;
