import { SegmentedControl, Skeleton, TextInput, Tooltip } from "@mantine/core";
import { Header } from "../../components/Header/Header";
import { Search } from "tabler-icons-react";
import { SrcollUpAffix } from "../../components/Affix/ScrollUpAffix";
import useSWR from "swr";
import { getFetcher } from "../../api/fetchers";
import { useException } from "../../hooks/ExceptionProvider";
import { useState } from "react";
import { CloseButton } from "@mantine/core";
import { Membership } from "../../domain/Membership";
import { InvitationCard } from "./InvitationCard";

const InvitationList = (): JSX.Element => {
  const { handleException } = useException();
  const [searchTerm, setSearchTerm] = useState("");
  const [sortTerm, setSortTerm] = useState("newest");

  const {
    data: invitations,
    error,
    mutate,
  } = useSWR<Membership[]>("memberships/invitations", getFetcher);

  if (error) {
    console.log(
      "The exception was caught while fetching data from invitations"
    );
    handleException(error, undefined, true);
  }

  const filteredInvitations = invitations?.filter((invitation) =>
    invitation.project.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const sortedAndFilteredInvitations = filteredInvitations?.sort((a, b) => {
    const dateA = new Date(a.createdAt);
    const dateB = new Date(b.createdAt);

    if (sortTerm === "newest") {
      return dateB.getTime() - dateA.getTime();
    } else {
      return dateA.getTime() - dateB.getTime();
    }
  });

  return (
    <div className="p-l flex flex-col gap-m">
      <Header
        name="Invitations"
        controls={[
          <Tooltip
            label="Remove filter"
            position="bottom"
            offset={10}
            withArrow
            arrowSize={8}
            arrowRadius={4}
          >
            <CloseButton
              onClick={() => {
                setSearchTerm("");
              }}
            />
          </Tooltip>,
          <TextInput
            size="md"
            radius="md"
            placeholder="Search by project name"
            leftSection={<Search />}
            variant="filled"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.currentTarget.value)}
          />,
          <SegmentedControl
            value={sortTerm}
            size="md"
            radius="md"
            data={[
              { label: "Newest", value: "newest" },
              { label: "Oldest", value: "oldest" },
            ]}
            onChange={setSortTerm}
          />,
        ]}
      />

      {invitations ? (
        sortedAndFilteredInvitations &&
        sortedAndFilteredInvitations.length > 0 ? (
          <div className="grid grid-cols-3 gap-m max-sm:grid-cols-1 max-md:grid-cols-2">
            {sortedAndFilteredInvitations.map((invitation) => (
              <InvitationCard
                key={invitation.id}
                invitation={invitation}
                mutate={mutate}
              />
            ))}
          </div>
        ) : (
          <div className="mt-md text-center fnt-lg">
            All clear. No invitations found!
          </div>
        )
      ) : (
        <div className="grid grid-cols-3 gap-m max-sm:grid-cols-1 max-md:grid-cols-2">
          <Skeleton className="h-[350px] card" />
          <Skeleton className="h-[350px] card" />
          <Skeleton className="h-[350px] card" />
        </div>
      )}

      <SrcollUpAffix />
    </div>
  );
};

export default InvitationList;
