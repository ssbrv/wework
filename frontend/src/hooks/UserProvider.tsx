import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from "react";
import useSWR from "swr";
import { User } from "../domain/User";
import { getFetcher } from "../api/fetchers";
import { useException } from "./ExceptionProvider";
import api from "../api/api";
import { Outlet, useParams } from "react-router-dom";
import { UpdateUserRequest } from "../http/request/UpdateUserRequest";
import { useAuth } from "./AuthProvider";

interface UserContextProps {
  isItMe: boolean;
  user: User | undefined;
  editBasic: (editBasicRequest: UpdateUserRequest) => Promise<void>;
  editUsername: (editUsernameRequest: UpdateUserRequest) => Promise<void>;
}

const UserContext = createContext<UserContextProps | undefined>(undefined);

const UserProvider = (): JSX.Element => {
  const { myId } = useAuth();

  const { userId } = useParams<{ userId: string }>();
  const [isItMe, setIsItMe] = useState<boolean>(userId === myId);
  const { handleException } = useException();

  useEffect(() => {
    setIsItMe(userId === myId);
  }, [userId, myId]);

  const {
    data: user,
    error,
    mutate,
  } = useSWR<User>(`users/${userId}`, getFetcher);

  if (error) {
    console.log("The exception was caught while fetching data from get/me");
    handleException(error, undefined, true);
  }

  const editBasic = useCallback(
    async (editBasicRequest: UpdateUserRequest): Promise<void> => {
      await api.put(`users/${userId}`, editBasicRequest);
      mutate();
    },
    [mutate, userId]
  );

  const editUsername = useCallback(
    async (editUsernameRequest: UpdateUserRequest): Promise<void> => {
      await api.put(`users/${userId}`, editUsernameRequest);
      mutate();
    },
    [mutate, userId]
  );

  const contextValue = useMemo(
    () => ({ user, editBasic, editUsername, isItMe }),
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
