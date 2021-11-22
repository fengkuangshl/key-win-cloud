module.exports = {
  root: true,
  env: {
    browser: true,
    node: true
  },
  extends: ['plugin:vue/essential', '@vue/standard', '@vue/typescript/recommended'],
  parserOptions: {
    ecmaVersion: 2020
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    // 左括号前没有空格报错
    'space-before-function-paren': 0,
    // 在格式化的时候不加分号
    semi: false,
    // 将双引号转成单引号
    singleQuote: true
  },
  globals: {
    KWResponse: true,
    KWRequest: true,
    Model: true,
    KWRule: true,
    localStorage: true,
    window: true
  }
}
