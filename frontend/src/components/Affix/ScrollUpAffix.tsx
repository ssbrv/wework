import { Affix, Button, Transition } from "@mantine/core";
import { useWindowScroll } from "@mantine/hooks";
import { ArrowUp } from "tabler-icons-react";

export const SrcollUpAffix = (): JSX.Element => {
  const [scroll, scrollTo] = useWindowScroll();

  return (
    <Affix position={{ bottom: 20, right: 20 }}>
      <Transition transition="slide-up" mounted={scroll.y > 0}>
        {(transitionStyles) => (
          <Button
            size="md"
            radius="md"
            leftSection={<ArrowUp />}
            style={transitionStyles}
            onClick={() => scrollTo({ y: 0 })}
          >
            Scroll to top
          </Button>
        )}
      </Transition>
    </Affix>
  );
};
