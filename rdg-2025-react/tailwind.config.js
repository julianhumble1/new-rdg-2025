import flowbite from "flowbite-react/tailwind";

/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}", flowbite.content()],
  theme: {
    extend: {
      fontFamily: {
        merriweather: ["Merriweather", "serif"],
          },
              colors: {
                  'rdg-red': '#e9462f',
                  "rdg-blue": "#0347f7",
                  "rdg-yellow": "#f7c938"
      },
    },
  },
  plugins: [flowbite.plugin()],
};
