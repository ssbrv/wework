import useSWR from "swr";
import { getFetcher } from "./fetchers";
import { displayError } from "../utils/displayError";

export function useFetch<T>(url: string, signal?: AbortSignal) {
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
