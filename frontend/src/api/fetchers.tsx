import api from "./api";

export const getFetcher = async (url: string): Promise<any> =>
  await api.get(url).then((response: { data: any }) => response.data);

export const postFetcher = async (url: string, data?: any): Promise<any> =>
  await api.post(url, data).then((response: { data: any }) => response.data);

export const putFetcher = async (url: string, data?: any): Promise<any> =>
  await api.put(url, data).then((response: { data: any }) => response.data);

export const patchFetcher = async (url: string, data?: any): Promise<any> =>
  await api.patch(url, data).then((response: { data: any }) => response.data);

export const deleteFetcher = async (url: string): Promise<any> =>
  await api.delete(url).then((response: { data: any }) => response.data);
