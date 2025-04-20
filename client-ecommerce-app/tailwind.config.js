/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      fontSize: {
        h1:['2.25rem',{lineHeight:"2.5rem"}]
      },
      colors: {
        primary: '#ef4444', // Define your primary color
      },
    },
  },
  plugins: [require("@tailwindcss/typography")],
};