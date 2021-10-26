declare module '*.vue' {
  import Vue from 'vue'
  global {
    namespace Ajax {
      interface AjaxResult<T = undefined> {
        code: number,
        msg: string,
        data: T
      }
    }
  }
  export default Vue
}
