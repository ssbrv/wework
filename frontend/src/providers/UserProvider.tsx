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
import api from "../api/api";
import { Outlet, useParams } from "react-router-dom";
import { UpdateUserRequest } from "../http/request/UpdateUserRequest";
import { displayError } from "../utils/displayError";
import { useMyId } from "../api/auth/authApi";

interface UserContextProps {
  isItMe: boolean;
  user: User | undefined;
  editBasic: (editBasicRequest: UpdateUserRequest) => Promise<void>;
  editUsername: (editUsernameRequest: UpdateUserRequest) => Promise<void>;
}

const UserContext = createContext<UserContextProps | undefined>(undefined);

const UserProvider = (): JSX.Element => {
  const { myId } = useMyId();
  const { userId: userIdAsString } = useParams<{ userId: string }>();
  const [userId, setUserId] = useState<number | null>();
  const [isItMe, setIsItMe] = useState<boolean>(userId === myId);

  useEffect(() => {
    setUserId(userIdAsString ? parseInt(userIdAsString) : null);
  }, [userIdAsString]);

  useEffect(() => {
    setIsItMe(userId === myId);
  }, [userId, myId]);

  const {
    data: user,
    error,
    mutate,
  } = useSWR<User>(userId ? `users/${userId}` : null, getFetcher);

  if (error) {
    console.log("The exception was caught while fetching data from users/id");
    displayError(error, undefined, true);
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
    [editBasic, editUsername, isItMe, user]
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
