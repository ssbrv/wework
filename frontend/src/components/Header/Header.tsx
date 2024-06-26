interface Props {
  name?: string;
  controls?: JSX.Element[];
}

export const Header = (props: Props): JSX.Element => {
  return (
    <div className="card p-m flex justify-between gap-m items-center">
      <div className="fnt-lg font-bold">{props.name}</div>
      <div className="flex gap-m items-center">{props.controls}</div>
    </div>
  );
};
