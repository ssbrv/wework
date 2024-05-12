import { Image } from "@mantine/core";

interface Props {
  withImage?: boolean;
  withName?: boolean;
}

export const WeWorkLogo = ({
  withImage = true,
  withName = true,
}: Props): JSX.Element => {
  return (
    <div className="flex items-end justify-center gap-2">
      {withImage && <Image src="wework.png" className="size-24" />}
      {withName && (
        <div className="font-bold text-2xl">
          we <br />
          work.
        </div>
      )}
    </div>
  );
};
