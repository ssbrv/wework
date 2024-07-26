import { STATUSES_API_BASE_URL } from "../../../api/statuses";
import { Status } from "../../../domain/Status";
import { useFetchArray } from "../useFetch";

export const useTaskStatuses = (signal?: AbortSignal) => {
  const { statuses, error, isLoading, mutate } = useStatuses(`task`, signal);
  return {
    taskStatuses: statuses,
    error,
    isLoading,
    mutate,
  };
};

export const useProjectStatuses = (signal?: AbortSignal) => {
  const { statuses, error, isLoading, mutate } = useStatuses(`project`, signal);
  return {
    projectStatuses: statuses,
    error,
    isLoading,
    mutate,
  };
};

export const useMembershipStatuses = (signal?: AbortSignal) => {
  const { statuses, error, isLoading, mutate } = useStatuses(
    `membership`,
    signal
  );
  return {
    membershipStatuses: statuses,
    error,
    isLoading,
    mutate,
  };
};

export function useStatuses(urlAddon?: string, signal?: AbortSignal) {
  const url = urlAddon
    ? `${STATUSES_API_BASE_URL}/${urlAddon}`
    : STATUSES_API_BASE_URL;

  const { data, error, isLoading, mutate } = useFetchArray<Status>(url, signal);

  return {
    statuses: data,
    error,
    isLoading,
    mutate,
  };
}
