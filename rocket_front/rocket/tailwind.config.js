/** @type {import('tailwindcss').Config} */
const mode = process.env.TAILWIND_MODE ? 'jit' : 'aot';
module.exports = {
  mode: mode,
  content: [
    "./src/**/*.{html,ts}",
    "./node_modules/flowbite/**/*.js"
  ],
  theme: {
    extend: {
      colors: {
        primary : {
          light: "#FEC901",
          medium: "#FBBC02",
          dark: "#E1A901"
        },
        secondary: {
          light: "#0D4F83",
          medium: "#0B4775",
          dark: "#0A3F68"
        },
        tertiary: {
          light: "#F5F6F7",
          medium: "#949EA0",
        }
      }
    },
  },
  plugins: [
    require('flowbite/plugin')
  ],
}
