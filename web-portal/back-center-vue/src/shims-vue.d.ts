declare module '*.vue' {
  import Vue from 'vue'
  global {
    namespace KWResponse {
      interface Result<T = undefined> {
        code: number
        msg: string
        data: T
      }
      interface PageResult<T = undefined> {
        pageNo: number
        pageSize: number
        count: number
        code: number
        data: T[]
        totalPage: number
      }
    }
    namespace Model {
      interface Id {
        id: string
      }
      interface BaseFleidCU extends Id {
        createTime: number
        updateTime: number
      }
    }
    namespace KWRequest {
      type OrderDir = 'ASC' | 'DESC'
      interface PageRequest<T = undefined> {
        pageNo: number
        pageSize: number
        sortName?: string
        sortDir?: OrderDir
        t?: T
      }
    }
    namespace KWRule {
      interface Rule {
        required: boolean
        message: string
        trigger: string | Array<string>
      }
    }
  }
  export default Vue
}
declare module 'nprogress'
