/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    colors: {
      primary: "white",
      secondary: "#272727",
      background: "#E7E7E7",

      action: "#23a6fe",
      danger: "#EC5469",
      attract: "#6FD7A3",

      action_light: "#9AD6FE",
      danger_light: "#F7B6BE",
      attract_light: "#BFEDD6",

      hover: "#e4f3fa",
    },
    boxShadow: {
      DEFAULT: "rgba(149, 157, 165, 0.2) 0px 8px 24px",
    },
    borderColor: { DEFAULT: "#f2f2f2", transparent: "transparent" },
    borderRadius: {
      DEFAULT: "0.75rem",
    },
    borderWidth: {
      DEFAULT: "0.125rem",
    },
    extend: {
      spacing: {
        xs: "0.5rem", // between items
        s: "1rem", // between items, ???
        m: "1.5rem", // card -> content
        l: "2rem", // background -> card

        // logos and icons
        sm: "3rem",
        md: "4rem",
        lg: "6rem",
      },
      fontSize: {
        sm: "1rem", // basic
        md: "1.25rem", // ???
        lg: "2rem", // headings
      },
      lineHeight: {
        sm: "1.25rem", // basic
        md: "1.5rem", // ???
        lg: "2.625rem", // headings
      },
    },
  },

  corePlugins: {
    preflight: false, // avoid conflict with mantine theming
  },
  plugins: [],
};
