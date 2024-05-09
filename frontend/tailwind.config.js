/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    colors: {
      gradient: {
        from: "#66b5ff",
        to: "#ffcce6",
      },

      primary: "#8080ff",
      secondary: "white",
      tretiary: "white",

      action: "blue",
      danger: "red",
      attract: "green",
      neutral: "white",

      success: "green",
      warning: "red",
      info: "white",
    },
    boxShadow: {
      sm: "rgba(149, 157, 165, 0.2) 0px 8px 24px",
      md: "rgba(149, 157, 165, 0.2) 0px 8px 24px",
      lg: "rgba(0, 0, 0, 0.2) 0rem 0.25rem 0.75rem",
    },
    borderColor: { DEFAULT: "#f2f2f2" },
    borderRadius: {
      sm: "0.75rem",
      md: "0.75rem",
    },
    borderWidth: {
      DEFAULT: "0.125rem",
    },
  },
  corePlugins: {
    preflight: false, // avoid conflict with mantine theming
  },
  plugins: [],
};
