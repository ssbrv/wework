import { Image } from "@mantine/core";

export const WeWorkLogo = (): JSX.Element => {
  return (
    <div className="flex items-end justify-center gap-2">
      <Image src="wework.png" className="size-24" />
      <div className="font-bold text-2xl">
        we <br />
        work.
      </div>
    </div>
  );
};
