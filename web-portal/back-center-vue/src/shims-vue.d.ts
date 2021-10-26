declare module '*.vue' {
  import Vue from 'vue'
  declare global {
    namespace Ajax {
      interface AjaxResult {
        code: number,
        msg: string,
        data: S
      }
    }
  }
  export default Vue
}
