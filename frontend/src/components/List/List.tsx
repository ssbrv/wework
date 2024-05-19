import React, { useState } from "react";
import {
  ActionIcon,
  CloseButton,
  Divider,
  Pagination,
  TextInput,
  Tooltip,
} from "@mantine/core";
import { Search } from "tabler-icons-react";

interface ItemList {
  id: number;
  name: string;
  [key: string]: any;
}

interface ItemTool {
  index: number;
  function: (item: any) => void;
  icon: React.ReactNode;
  toolTipLabel: string;
}

interface DisplayAttribute {
  index: number;
  attribute: keyof ItemList;
}

interface ListProps {
  list?: ItemList[];
  title?: string;
  itemsPerPage?: number;
  controls?: React.ReactNode[];
  itemToolBar?: ItemTool[];
  displayAttributes?: DisplayAttribute[];
  onItemClick?: (itemList: ItemList) => void;
  wrapInCard?: boolean;
}

export const List = ({
  list = [],
  title = "",
  itemsPerPage = 9,
  controls = [],
  itemToolBar,
  displayAttributes = [],
  onItemClick,
  wrapInCard = true,
}: ListProps): JSX.Element => {
  const [searchTerm, setSearchTerm] = useState("");

  const filteredList = list?.filter((item) =>
    item.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  function chunk<T>(array: T[], size: number): T[][] {
    if (!array.length) {
      return [];
    }
    const head = array.slice(0, size);
    const tail = array.slice(size);
    return [head, ...chunk(tail, size)];
  }

  const data = chunk(filteredList, itemsPerPage);

  const [activePage, setPage] = useState(1);
  const filteredAndChunkedContent =
    data[activePage - 1]?.map((item, index) => (
      <div key={item.id}>
        <div className="h-md flex">
          <div
            onClick={() => {
              if (onItemClick) onItemClick(item);
            }}
            className={`flex items-center h-full w-full ${
              onItemClick && "hover:cursor-pointer hover:bg-hover"
            } p-m transition-all ease-linear duration-200`}
          >
            {item.name}
            <div className="flex items-center h-full ml-auto gap-m">
              {displayAttributes
                .sort((a, b) => a.index - b.index)
                .map((displayAttribute) => (
                  <div key={displayAttribute.attribute}>
                    {item[displayAttribute.attribute]}
                  </div>
                ))}
            </div>
          </div>

          {itemToolBar && (
            <div className="flex justify-center items-center gap-m px-m">
              {itemToolBar
                .sort((a, b) => a.index - b.index)
                .map((ItemTool, index) => (
                  <Tooltip
                    label={ItemTool.toolTipLabel}
                    position="bottom"
                    offset={10}
                    withArrow
                    arrowSize={8}
                    arrowRadius={4}
                  >
                    <ActionIcon
                      key={index}
                      variant="transparent"
                      className="hover:text-danger"
                      color="black"
                      onClick={() => ItemTool.function(item)}
                    >
                      {ItemTool.icon}
                    </ActionIcon>
                  </Tooltip>
                ))}
            </div>
          )}
        </div>
        {index !== list.length - 1 && <Divider />}
      </div>
    )) || [];

  return (
    <div className={`flex flex-col gap-m ${wrapInCard && "p-m card"}`}>
      <div className="flex gap-m justify-between items-center">
        <div className="font-bold fnt-md">{title}</div>
        <div className="flex gap-m items-center">
          <Tooltip
            key="tooltip"
            label="Remove filter"
            position="bottom"
            offset={10}
            withArrow
            arrowSize={8}
            arrowRadius={4}
          >
            <CloseButton
              onClick={() => {
                setSearchTerm("");
              }}
            />
          </Tooltip>
          <TextInput
            key="search"
            size="md"
            radius="md"
            placeholder="Search by name"
            leftSection={<Search />}
            variant="filled"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.currentTarget.value)}
          />
          {controls}
        </div>
      </div>

      <div>
        <Divider />
        {filteredAndChunkedContent}
      </div>

      <Pagination
        total={data.length}
        value={activePage}
        onChange={setPage}
        radius="md"
        withEdges
        className="self-center"
      />
    </div>
  );
};
