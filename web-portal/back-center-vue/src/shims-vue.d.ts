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
      interface Version extends Id {
        version: number
      }
      interface BaseFleid extends Version {
        createDate: number
        updateDate: number | null
        createUserId: string
        updateUserId: string | null
        enableFlag: boolean
        createUserName: string
        updateUserName: string | null
      }
    }
    namespace KWRequest {
      type OrderDir = 'ASC' | 'DESC'
      type MethodType = 'POST' | 'GET'
      interface PageRequest<T = undefined> {
        pageNo: number
        pageSize: number
        sortName?: string
        sortDir?: OrderDir
        t?: T
      }
    }
    namespace KWRule {
      interface CallbackFunction {
        (error?: Error): void
      }
      interface TriggerRule {
        trigger: string | Array<string>
      }
      interface MessageRule extends TriggerRule {
        message: string
      }
      interface Rule extends MessageRule {
        required: boolean
      }
      interface MixinRule extends BaseRule {
        min: number
        max: number
      }
      interface ValidatorFunction {
        // eslint-disable-next-line no-use-before-define
        (rule: ValidatorRule, value: string, cb: CallbackFunction): void
      }
      interface ValidatorRule extends TriggerRule {
        validator: ValidatorFunction
      }
    }
  }
  export default Vue
}
declare module 'nprogress'
