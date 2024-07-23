import useSWR from "swr";
import { Status } from "../domain/Status";
import { getFetcher } from "../api/fetchers";
import { displayError } from "../utils/displayError";

const useProjectStatus = () => {
  const { data, error, isLoading, mutate } = useSWR<Status[]>(
    "statuses/project",
    getFetcher
  );

  if (error) displayError(error, undefined, true);

  if (isLoading || !data)
    return {
      projectStatuses: [],
      mutate,
    };

  return {
    projectStatuses: data,
    mutate,
  };
};

export default useProjectStatus;
