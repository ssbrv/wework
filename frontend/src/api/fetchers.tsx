import api from "./api";

export const getFetcher = async (
  url: string,
  signal?: AbortSignal
): Promise<any> =>
  await api
    .get(url, { signal: signal })
    .then((response: { data: any }) => response.data);
