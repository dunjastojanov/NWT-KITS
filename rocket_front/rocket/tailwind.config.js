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
          light: "#f5f4f4",
          mediumLight: "#e0dfdf",
          medium: "#b9b9b9",
        }
      },
      backgroundImage: {
        'statistics': "url('./assets/statistics.jpg')",  
      }
    },
  },
  plugins: [
    require('flowbite/plugin')
  ],
}
