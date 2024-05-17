interface Props {
  name?: string;
  controls?: JSX.Element[];
}

export const Header = (props: Props): JSX.Element => {
  return (
    <div className="card p-m">
      <div className="fnt-lg font-bold">{props.name}</div>
    </div>
  );
};
