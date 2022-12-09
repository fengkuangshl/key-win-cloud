declare module '*.vue' {
  import Vue from 'vue'
  global {
    namespace Token {
      /* eslint-disable camelcase */
      export interface BaseToken {
        grant_type: string
        client_id: string
        client_secret: string
      }
      export interface Scope {
        scope: string
      }
      export interface TokenRequest extends Scope, BaseToken {
        username: string
        password: string
      }
      export interface RefreshToken {
        refresh_token: string
      }
      export interface RefreshTokenRequest extends BaseToken, RefreshToken {}
      export interface TokenResponse extends Scope, RefreshToken {
        access_token: string
        token_type: string
        expires_in: string
      }
    }
    namespace KWResponse {
      interface BaseResult {
        code: number
        msg: string
      }
      interface Result<T = undefined> extends BaseResult {
        data: T
      }
      interface PageResult<T = undefined> extends BaseResult {
        pageNo: number
        pageSize: number
        count: number
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
      interface BaseField extends Version {
        createDate: number
        updateDate: number | null
        createUserId: string
        updateUserId: string | null
        enableFlag: boolean
        createUserName: string
        updateUserName: string | null
      }
      interface Name {
        name: string
      }
      interface CodeField extends Name {
        code: string
      }
      interface ParentId {
        parentId: string
      }
      interface EnumEntity {
        code: string
        stringValue: string
        text: string
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
      interface DateRule extends MessageRule {
        type: string
      }
      interface ArrayRule extends MessageRule {
        type: string
      }
      interface MixinRule extends MessageRule {
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
      interface TransformFunction {
        (value: string): number
      }
      interface NumberRule extends MessageRule {
        type: string
        transform: TransformFunction
      }
    }
  }
  export default Vue
}
declare module 'nprogress'
declare module 'viewerjs'
declare module 'spark-md5'
declare module 'vue-simple-uploader'
declare module 'bpmn-js/lib/Modeler'
declare module 'bpmn-js-properties-panel'
declare module 'bpmn-js-properties-panel/lib/provider/camunda'
declare module 'camunda-bpmn-moddle/resources/camunda'
