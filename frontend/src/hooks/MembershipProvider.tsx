import { createContext, useCallback, useContext, useMemo } from "react";
import useSWR, { KeyedMutator } from "swr";
import { getFetcher } from "../api/fetchers";
import { useException } from "./ExceptionProvider";
import { Outlet, useParams } from "react-router-dom";
import { Loader } from "@mantine/core";
import { Membership } from "../domain/Membership";
import { ChangeMembershipStatusRequest } from "../http/request/ChangeMembershipStatusRequest";
import api from "../api/api";

interface MembershipContextProps {
  membership: Membership | undefined;
  mutate: KeyedMutator<Membership>;
  changeMembershipStatus: (
    changeMembershipStatusRequest: ChangeMembershipStatusRequest
  ) => Promise<void>;
}

const MembershipContext = createContext<MembershipContextProps | undefined>(
  undefined
);

const MembershipProvider = (): JSX.Element => {
  const { membershipId } = useParams<{ membershipId: string }>();
  const { handleException } = useException();

  const {
    data: membership,
    error,
    isLoading,
    mutate,
  } = useSWR<Membership>(`memberships/${membershipId}`, getFetcher);

  if (error) {
    console.log(
      "The exception was caught while fetching data from memberships/membershipId"
    );
    handleException(error, undefined, true);
  }

  const changeMembershipStatus = useCallback(
    async (
      changeMembershipStatusRequest: ChangeMembershipStatusRequest
    ): Promise<void> => {
      await api.put(
        `/memberships/${membership?.id}`,
        changeMembershipStatusRequest
      );
    },
    [membership]
  );

  const contextValue = useMemo(
    () => ({ mutate, membership, changeMembershipStatus }),
    [membership, mutate, changeMembershipStatus]
  );
  return (
    <MembershipContext.Provider value={contextValue}>
      {isLoading ? (
        <div className="min-h-screen flex justify-center items-center">
          <Loader />
        </div>
      ) : (
        <Outlet />
      )}
    </MembershipContext.Provider>
  );
};

export const useMembership = (): MembershipContextProps => {
  const context = useContext(MembershipContext);
  if (!context)
    throw new Error("useMembership must be used within MembershipProvider");
  return context;
};

export default MembershipProvider;
