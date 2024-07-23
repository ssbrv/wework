import { createContext, useContext, useMemo } from "react";
import useSWR, { KeyedMutator } from "swr";
import { getFetcher } from "../api/fetchers";
import { Outlet, useParams } from "react-router-dom";
import { Loader } from "@mantine/core";
import { Membership } from "../domain/Membership";
import { displayError } from "../utils/displayError";

interface MembershipContextProps {
  membership: Membership | undefined;
  mutate: KeyedMutator<Membership>;
}

const MembershipContext = createContext<MembershipContextProps | undefined>(
  undefined
);

const MembershipProvider = (): JSX.Element => {
  const { membershipId } = useParams<{ membershipId: string }>();

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
    displayError(error, undefined, true);
  }

  const contextValue = useMemo(
    () => ({ mutate, membership }),
    [membership, mutate]
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
