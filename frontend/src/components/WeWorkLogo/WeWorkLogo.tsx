import { Image } from "@mantine/core";
import {
  fontMapper,
  gapMapper,
  sizeMapper,
  SmMdLg,
  Spacing,
} from "../../styles/TypesAndMappers";

interface Props {
  withImage?: boolean;
  withName?: boolean;
  gap?: Spacing;
  newLine?: boolean;
  center?: boolean;
  logoSize?: SmMdLg;
  fontSize?: SmMdLg;
}

export const WeWorkLogo = ({
  withImage = true,
  withName = true,
  gap = "s",
  newLine = true,
  center = false,
  logoSize = "lg",
  fontSize = "lg",
}: Props): JSX.Element => {
  return (
    <div
      className={`flex justify-center ${gapMapper[gap]} flex-shrink-0 items-center`}
    >
      {withImage && (
        <Image
          src="/wework.png"
          className={`flex-shrink-0 ${sizeMapper[logoSize]}`}
        />
      )}
      {withName && (
        <div
          className={`${!center && "mt-auto"} font-bold ${
            fontMapper[fontSize]
          } flex-shrink-0`}
        >
          we {newLine && <br />}
          work.
        </div>
      )}
    </div>
  );
};
