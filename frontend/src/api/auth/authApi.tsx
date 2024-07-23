import { mutate } from "swr";
import { AuthRequest } from "../../http/request/AuthRequest";
import { RegisterRequest } from "../../http/request/RegisterRequest";
import api from "../api";
import {
  LocalStorageService,
  useLocalStorageItem,
} from "../../utils/storageManagment";
import { ChangePasswordRequest } from "../../http/request/ChangePasswordRequest";
import { useEffect, useState } from "react";

const AUTH_API_BASE_URL = "auth";
const TOKEN_LOCAL_STORAGE_ITEM_NAME = "token";
const MY_ID_LOCAL_STORAGE_ITEM_NAME = "myId";

class TokenLocalStorageService extends LocalStorageService<string> {
  constructor() {
    super(TOKEN_LOCAL_STORAGE_ITEM_NAME);
  }

  setItem(token: string | null): void {
    super.setItem(token);
    this.setAuthHeader();
  }

  setAuthHeader(): void {
    const token = this.getItem();
    token
      ? (api.defaults.headers.common.Authorization = `Bearer ${token}`)
      : (api.defaults.headers.common.Authorization = undefined);
  }
}

export const useToken = () => {
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const tokenLocalStorageService = new TokenLocalStorageService();
  const { item: token, setItem: setToken } = useLocalStorageItem(
    tokenLocalStorageService
  );

  useEffect(() => {
    tokenLocalStorageService.setAuthHeader();
    setIsLoading(false);
  }, [token]);

  return {
    token,
    setToken,
    isLoading,
  };
};

class MyIdLocalStorageService extends LocalStorageService<number> {
  constructor() {
    super(MY_ID_LOCAL_STORAGE_ITEM_NAME);
  }
}

export const useMyId = () => {
  const myIdLocalStorageService = new MyIdLocalStorageService();
  const { item: myId, setItem: setMyId } = useLocalStorageItem(
    myIdLocalStorageService
  );

  return {
    myId,
    setMyId,
  };
};

export const login = async (authRequest: AuthRequest) => {
  const authResponse = await api.post(
    `${AUTH_API_BASE_URL}/authenticate`,
    authRequest
  );

  const tokenLocalStorageService = new TokenLocalStorageService();
  tokenLocalStorageService.setItem(authResponse.data.jwtToken);

  const myIdLocalStorageService = new MyIdLocalStorageService();
  myIdLocalStorageService.setItem(authResponse.data.id);
};

export const register = async (registerRequest: RegisterRequest) => {
  const authResponse = await api.post(
    `${AUTH_API_BASE_URL}/register`,
    registerRequest
  );

  const tokenLocalStorageService = new TokenLocalStorageService();
  tokenLocalStorageService.setItem(authResponse.data.jwtToken);

  const myIdLocalStorageService = new MyIdLocalStorageService();
  myIdLocalStorageService.setItem(authResponse.data.id);
};

export const logout = async () => {
  const tokenLocalStorageService = new TokenLocalStorageService();
  await api
    .post("auth/logout", { jwtToken: tokenLocalStorageService.getItem() })
    .catch(function (ignore) {});
  tokenLocalStorageService.setItem(null);

  const myIdLocalStorageService = new MyIdLocalStorageService();
  myIdLocalStorageService.setItem(null);
  mutate(() => true, undefined, { revalidate: false }); // this deletes swr cache
};

export const fullLogout = async () => {
  const authResponse = await api.post(`${AUTH_API_BASE_URL}/full-logout`);
  const tokenLocalStorageService = new TokenLocalStorageService();
  tokenLocalStorageService.setItem(authResponse.data.jwtToken);
};

export const changePassword = async (
  changePasswordRequest: ChangePasswordRequest
) => {
  const authResponse = await api.put(
    `${AUTH_API_BASE_URL}/password`,
    changePasswordRequest
  );
  const tokenLocalStorageService = new TokenLocalStorageService();
  tokenLocalStorageService.setItem(authResponse.data.jwtToken);
};

export const deleteAccount = async () => {
  await api.delete(AUTH_API_BASE_URL);
  const tokenLocalStorageService = new TokenLocalStorageService();
  tokenLocalStorageService.setItem(null);

  const myIdLocalStorageService = new MyIdLocalStorageService();
  myIdLocalStorageService.setItem(null);
};
