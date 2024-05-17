import { createContext, useCallback, useContext, useMemo } from "react";
import useSWR from "swr";
import { User } from "../http/response/User";
import { getFetcher } from "../api/fetchers";
import { useException } from "./ExceptionProvider";
import api from "../api/api";
import { EditBasicRequest } from "../http/request/EditBasicRequest";
import { Outlet } from "react-router-dom";
import { EditUsernameRequest } from "../http/request/EditUsernameRequest";

interface UserContextProps {
  user: User | undefined;
  editBasic: (editBasicRequest: EditBasicRequest) => Promise<void>;
  editUsername: (editUsernameRequest: EditUsernameRequest) => Promise<void>;
}

const UserContext = createContext<UserContextProps | undefined>(undefined);

const UserProvider = (): JSX.Element => {
  const { handleException } = useException();

  console.log("get/me");
  const { data: user, error, mutate } = useSWR<User>("user/me", getFetcher);

  if (error) {
    console.log("The exception was caught while fetching data from get/me");
    handleException(error, undefined, true);
  }

  const editBasic = useCallback(
    async (editBasicRequest: EditBasicRequest): Promise<void> => {
      await api.put("user/me/basic", editBasicRequest);
      mutate();
    },
    [mutate]
  );

  const editUsername = useCallback(
    async (editUsernameRequest: EditUsernameRequest): Promise<void> => {
      await api.put("user/me/username", editUsernameRequest);
      mutate();
    },
    [mutate]
  );

  const contextValue = useMemo(
    () => ({ user, editBasic, editUsername }),
    [editBasic, editUsername, user]
  );
  return (
    <UserContext.Provider value={contextValue}>
      <Outlet />
    </UserContext.Provider>
  );
};

export const useUser = (): UserContextProps => {
  const context = useContext(UserContext);
  if (!context) throw new Error("useUser must be used within UserProvider");
  return context;
};

export default UserProvider;
