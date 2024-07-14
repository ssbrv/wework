import useSWR from "swr";
import { Status } from "../domain/Status";
import { getFetcher } from "../api/fetchers";
import { useException } from "./ExceptionProvider";

const useTaskStatus = () => {
  const { handleException } = useException();

  const { data, error, isLoading, mutate } = useSWR<Status[]>(
    "statuses/task",
    getFetcher
  );

  if (error) handleException(error, undefined, true);

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
