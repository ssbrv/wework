import { useEffect, useState } from "react";

export class LocalStorageService<T> {
  constructor(protected key: string) {}

  getItem(): T | null {
    const item = localStorage.getItem(this.key);
    return item ? (JSON.parse(item) as T) : null;
  }

  setItem(value: T | null): void {
    value
      ? localStorage.setItem(this.key, JSON.stringify(value))
      : localStorage.removeItem(this.key);
    window.dispatchEvent(new Event("storage"));
  }

  getKey(): string {
    return this.key;
  }
}

export const useLocalStorageItem = <T,>(
  localStorageService: LocalStorageService<T>
) => {
  const [item, setItemState] = useState<T | null>(
    localStorageService.getItem()
  );

  useEffect(() => {
    const handleStorageChange = () =>
      setItemState(localStorageService.getItem());

    window.addEventListener("storage", handleStorageChange);

    return () => {
      window.removeEventListener("storage", handleStorageChange);
    };
  }, []);

  const setItem = (value: T | null) => {
    localStorageService.setItem(value);
    setItemState(value);
  };

  return {
    setItem,
    item,
  };
};
