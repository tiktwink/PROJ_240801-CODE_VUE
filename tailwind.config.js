module.exports = {
  content: ['./index.html', './src/**/*.{vue,js,ts,html}'], //index.html看情况
  plugins: [require('daisyui')],
  darkMode: 'class', //深浅模式配置，使用该配置时是基于可控模式，如果将darkMode选项注释掉，则为基于设备模式(自动跟随设备深浅模式变换，不建议)
  
  //TailwindCSS主题
  theme: {
    fontSize: {
      xs: '0.7rem',
      sm: '0.8rem',
      base: '0.9rem',
      lg: '1rem',
      xl: '1.125rem',
      '2xl': '1.25rem',
      '3xl': '1.5rem',
      '4xl': '1.75rem',
      '5xl': '2rem',
      '6xl': '3rem'
    },
    
  },
  
  // daisyUI配置
  daisyui: {
    themes: [
      { //自定义主题
        mytheme: { //主题名
          "primary": "#70e753",
          "primary-content": "#ffffff",
          "secondary": "#bbf8ae",
          "secondary-content": "#080808",
          
          "accent": "#f5f5f5",
          "accent-content": "#eeeeee",
          "neutral": "#0bcc45",
          "neutral-content": "#080808",
          
          "base-100": "#ffffff",
          
          "info": "#05aeec",
          "success": "#0bcc45",
          "warning": "#ff9a00",
          "error": "#ff4444",
          "--animation-btn": "0.3s",
          "--animation-input": "0.3s",
          
        }
      },
      { //自定义主题
        mythemedark: { //主题名
          "primary": "#61c44c",
          "primary-content": "#ffffff",
          "secondary": "#aee79f",
          "secondary-content": "#080808",
          
          "accent": "#05aefc",
          "accent-content": "#1e1f22",
          "neutral": "#1e1f22",
          "neutral-content": '#fafafa',
          
          "base-100": "#121212",
          
          "info": "#05aefc",
          "info-content": "#f5f1e8",
          "success": "#3ed274",
          "success-content": "#2b2d30",
          "warning": "#ff9a00",
          "error": "#ff4444",
          "--animation-btn": "0.3s",
          "--animation-input": "0.5s",
          
        }
      },
    ]
  }
};

