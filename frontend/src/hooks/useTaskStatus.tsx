import useSWR from "swr";
import { Status } from "../domain/Status";
import { getFetcher } from "../api/fetchers";
import { displayError } from "../utils/displayError";

const useTaskStatus = () => {
  const { data, error, isLoading, mutate } = useSWR<Status[]>(
    "statuses/task",
    getFetcher
  );

  if (error) displayError(error, undefined, true);

  if (isLoading || !data)
    return {
      taskStatuses: [],
      mutate,
    };

  return {
    taskStatuses: data,
    mutate,
  };
};

export default useTaskStatus;
