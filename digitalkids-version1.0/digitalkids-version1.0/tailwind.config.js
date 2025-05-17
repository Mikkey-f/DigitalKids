export default {
    content: [
      "./index.html",
      "./src/**/*.{vue,js,ts,jsx,tsx}",
    ],
    theme: {
      extend: {
        colors: {
          primary: {
            light: '#6EE7B7',
            DEFAULT: '#3B82F6',
            dark: '#9333EA',
          },
        },
        animation: {
          'gradient': 'gradient 8s ease infinite',
        },
        keyframes: {
          gradient: {
            '0%': { backgroundPosition: '0% 50%' },
            '50%': { backgroundPosition: '100% 50%' },
            '100%': { backgroundPosition: '0% 50%' },
          },
        },
      },
    },
    plugins: [
      require('@tailwindcss/forms'),
    ],
  }