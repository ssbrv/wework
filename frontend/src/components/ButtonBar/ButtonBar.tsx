import React from "react";
import { Group } from "@mantine/core";

interface Props {
  children?: React.ReactNode;
}

export const ButtonBar = ({ children }: Props): JSX.Element => {
  return (
    <div className="card p-m">
      <Group className="justify-end gap-m">{children}</Group>
    </div>
  );
};
