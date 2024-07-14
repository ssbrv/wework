import useSWR from "swr";
import { Status } from "../domain/Status";
import { getFetcher } from "../api/fetchers";
import { useException } from "./ExceptionProvider";

const useProjectStatus = () => {
  const { handleException } = useException();

  const { data, error, isLoading, mutate } = useSWR<Status[]>(
    "statuses/project",
    getFetcher
  );

  if (error) handleException(error, undefined, true);

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
