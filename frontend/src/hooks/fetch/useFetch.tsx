import useSWR, { KeyedMutator } from "swr";
import { getFetcher } from "../../api/fetchers";
import { displayError } from "../../utils/displayError";

export interface FetchHookInterface<T> {
  data: T | undefined;
  error: any;
  isLoading: boolean;
  mutate: KeyedMutator<T>;
}

export interface FetchArrayHookInterface<T> extends FetchHookInterface<T[]> {
  data: T[] | [];
}

export function useFetch<T>(
  url: string,
  signal?: AbortSignal
): FetchHookInterface<T> {
  const fetcher = async (url: string): Promise<any> =>
    await getFetcher(url, signal);

  const { data, error, isLoading, mutate } = useSWR<T>(url, fetcher);

  if (error) {
    displayError(error, undefined, true);
  }

  return {
    data,
    error,
    isLoading,
    mutate,
  };
}

export function useFetchArray<T>(
  url: string,
  signal?: AbortSignal
): FetchArrayHookInterface<T> {
  const fetcher = async (url: string): Promise<any> =>
    await getFetcher(url, signal);

  const { data, error, isLoading, mutate } = useSWR<T[]>(url, fetcher);

  if (error) {
    displayError(error, undefined, true);
  }

  if (!data || isLoading)
    return {
      data: [],
      error,
      isLoading,
      mutate,
    };

  return {
    data,
    error,
    isLoading,
    mutate,
  };
}
