import {
  DndContext,
  DragEndEvent,
  useDroppable,
  useDraggable,
  useSensor,
  useSensors,
  DragOverlay,
  DragStartEvent,
  PointerSensor,
} from "@dnd-kit/core";
import React, { forwardRef, useState } from "react";

interface Identifiable {
  id: string | number;
}

interface KanbanBoardProps<
  ITEM extends Identifiable,
  COLUMN extends Identifiable
> {
  columns?: COLUMN[];
  items?: ITEM[];
  renderItemContent?: (item: ITEM) => JSX.Element;
  renderColumnHeader?: (column: COLUMN) => JSX.Element;
  renderColumnFooter?: (column: COLUMN) => JSX.Element;
  filterItem?: (item: ITEM, column: COLUMN) => boolean;
  onDragEnd?: (item: ITEM, newColumn: COLUMN) => void;
}

const KanbanBoard = <ITEM extends Identifiable, COLUMN extends Identifiable>({
  columns = [],
  items = [],
  renderItemContent = () => {
    return <></>;
  },
  renderColumnHeader,
  filterItem = () => {
    return true;
  },
  onDragEnd = () => {},
  renderColumnFooter,
}: KanbanBoardProps<ITEM, COLUMN>): JSX.Element => {
  const [activeItem, setActiveItem] = useState<ITEM | null>(null);
  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        distance: 10,
      },
    })
  );

  interface ItemProps extends React.HTMLAttributes<HTMLDivElement> {
    item: ITEM;
  }

  const Item = forwardRef<HTMLDivElement, ItemProps>(
    ({ item, ...props }, ref) => {
      return (
        <div
          className={`
            transition-all duration-200 ease-out  
            ${!activeItem && "group hover:scale-105 cursor-grab"} 
            ${activeItem?.id === item.id && "cursor-grabbing scale-105"}
          `}
          {...props}
          ref={ref}
        >
          {renderItemContent(item)}
        </div>
      );
    }
  );

  const DraggableItem: React.FC<{ item: ITEM }> = ({ item }) => {
    const { attributes, listeners, setNodeRef } = useDraggable({
      id: item.id,
      data: { item },
    });

    const style = {
      opacity: activeItem?.id === item.id ? 0 : 1,
    };

    return (
      <Item
        item={item}
        ref={setNodeRef}
        {...listeners}
        {...attributes}
        style={style}
      />
    );
  };

  const DroppableColumn: React.FC<{ column: COLUMN; columnItems: ITEM[] }> = ({
    column,
    columnItems,
  }) => {
    const { setNodeRef, isOver } = useDroppable({
      id: column.id,
      data: { column },
    });

    return (
      <div
        className={`
            h-fit transition-all ease-out duration-200 flex flex-col card gap-m bg-opacity-60 p-xs 
            ${isOver && "shadow-background scale-105"}
            `}
        ref={setNodeRef}
      >
        {renderColumnHeader?.(column)}

        {columnItems?.length !== 0 && (
          <div className="flex flex-col gap-s px-s">
            {columnItems.map((item) => (
              <DraggableItem item={item} key={item.id} />
            ))}
          </div>
        )}
        {renderColumnFooter && renderColumnFooter(column)}
      </div>
    );
  };

  function handleDragStart(event: DragStartEvent) {
    setActiveItem(event?.active?.data?.current?.item);
  }

  function handleDragEnd(event: DragEndEvent) {
    const { active, over } = event;

    if (!over) return;

    onDragEnd(active.data?.current?.item, over.data?.current?.column);

    // delay for the animation to end
    setTimeout(() => {
      setActiveItem(null);
    }, 200);
  }

  return (
    <div className="card p-m bg-gradient-to-br from-action_light to-danger_light">
      <DndContext
        sensors={sensors}
        onDragStart={handleDragStart}
        onDragEnd={handleDragEnd}
      >
        <div
          className="grid gap-m"
          style={{
            gridTemplateColumns: `repeat(auto-fit, minmax(350px, 1fr))`,
          }}
        >
          {columns.map((column) => (
            <DroppableColumn
              column={column}
              key={column.id}
              columnItems={items.filter((item) => filterItem(item, column))}
            />
          ))}
        </div>

        <DragOverlay>{activeItem && <Item item={activeItem} />}</DragOverlay>
      </DndContext>
    </div>
  );
};

export default KanbanBoard;
