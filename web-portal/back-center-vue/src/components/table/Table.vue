<template>
  <div>
    <el-table
      :$ready="false"
      :border="true"
      :data="pageResult.data"
      :highlight-current-row="true"
      :row-key="rowKey"
      :default-expand-all="isExpandAll"
      :tree-props="treeProps"
      @cell-click="doTableCellClick"
      @current-change="doChangeTableCurrent"
      @row-click="doTableRowClick"
      @selection-change="doChangeTableSelection"
      @sort-change="doSortChange"
      ref="tableRef"
    >
      <slot></slot>
    </el-table>
    <el-pagination :current-page="pageResult.pageNo" :layout="pageLayout" :page-count="pageResult.count" :page-size="pageResult.pageSize" :page-sizes="pageSizes" :pager-count="5" :total="pageResult.count" @current-change="doChangePageCurrent" @size-change="doChangePageSize"></el-pagination>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Ref, Vue } from 'vue-property-decorator'
import { cellCallbackParams, ElTable, SortOrder } from 'element-ui/types/table'
import request from '@/fetch'
import { session } from '@/store'
import { ElTableColumn } from 'element-ui/types/table-column'

@Component
export default class KWTable<T, RT> extends Vue {
  // 内部变量
  pageRequest: KWRequest.PageRequest<T> = {
    pageSize: 10, // 每页的数据条数
    pageNo: 1 // 默认开始页面
  }

  pageResult: KWResponse.PageResult<RT> = {
    pageNo: 0,
    pageSize: 0,
    count: 0,
    code: 0,
    data: [],
    totalPage: 0
  }

  @Ref('tableRef')
  readonly tableRef!: ElTable

  @Prop({ required: true, type: String })
  private url!: string // url

  @Prop({ default: () => 1, type: Number })
  private pageNo!: number

  @Prop({ default: () => null, type: Object })
  private param!: T

  @Prop({ default: () => 10, type: Number })
  private pageSize!: number

  @Prop({ default: 'id', type: [String, Function] })
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private rowKey!: string | ((row: any) => any)

  @Prop({ default: () => [5, 10, 15, 20], type: [] })
  private pageSizes!: number[]

  @Prop({ default: () => 'total, sizes, prev, pager, next, jumper', type: String })
  private pageLayout!: string

  @Prop({ default: () => false, type: Boolean })
  private isExpandAll!: boolean

  @Prop({
    default: () => ({
      children: 'children',
      hasChildren: 'hasChildren'
    }),
    type: Object
  })
  private treeProps!: { children: string; hasChildren: string }

  @Prop({
    default: (datas: Array<RT>): Array<RT> => {
      return datas
    },
    type: Function
  })
  private renderPreFn!: (datas: Array<RT>) => Array<RT>

  @Prop({
    default: (datas: Array<RT>): void => {
      console.log(datas)
    },
    type: Function
  })
  private callbackFn!: (datas: Array<RT>) => void

  private currentRow!: RT

  private selectionRow: Array<RT> = []

  /** 加载表格数据 */
  public async load(pageNo?: number, pageSize?: number, param?: T): Promise<void> {
    // 如果传入空就使用之前的值
    this.pageRequest.pageNo = pageNo || this.pageNo
    this.pageRequest.pageSize = pageSize || this.pageSize
    this.pageRequest.t = param || this.param
    const res: KWResponse.PageResult<RT> = await request.post(this.url, this.pageRequest)
    // ------------------组装数据开始------------------
    this.pageResult.data = this.renderPreFn(res.data) // 渲染前的回调函数
    this.pageResult.pageNo = res.pageNo
    this.pageResult.pageSize = res.pageSize
    this.pageResult.count = res.count
    this.pageResult.code = res.code
    this.pageResult.totalPage = res.totalPage
    this.callbackFn(res.data) // 渲染之后的回调函数
    // ------------------组装数据结束------------------
  }

  /** 重载数据 */
  public reload(): void {
    this.load(this.pageNo, this.pageSize, this.param)
  }

  public loadByCondition(param?: T): void {
    this.load(this.pageNo, this.pageSize, param)
  }

  /** 获取指定行 */
  public getRow(row: number): RT | undefined {
    if (row >= 0 && row < this.pageResult.count) {
      return this.pageResult.data[row]
    } else {
      return undefined
    }
  }

  /** 获取当前选择行 */
  public getCurrentRow(): RT {
    return this.currentRow
  }

  /** 设置当前选择行 */
  public setCurrentRow(row: number): void {
    if (row >= 0 && row < this.pageResult.count) {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      this.tableRef.setCurrentRow(this.pageResult.data[row] as any)
    }
  }

  /** 清空被选中行 */
  public clearSelection(): void {
    return this.tableRef.clearSelection()
  }

  /** 获取被选中行 */
  public getSelectionRow(): Array<RT> {
    return this.selectionRow || []
  }

  /** 获取被选中行(KEY值) */
  public getSelectionKey(): Array<string> {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    return this.selectionRow.map((item: any) => {
      if (typeof this.rowKey === 'function') {
        return this.rowKey(item)
      } else {
        return item[this.rowKey]
      }
    })
  }

  /** 设置行被选中/取消选中 */
  public setSelectionRow(key: string, check: boolean): void {
    for (const item of this.pageResult.data) {
      // 如果Rowkey是函数->判断值是否一致
      if (typeof this.rowKey === 'function' && this.rowKey(item) === key) {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        this.tableRef.toggleRowSelection(item as any, check)
      }

      // 如果Rowkey是字符串->判断值是否一致
      if (typeof this.rowKey === 'string' /* && item[this.rowKey] === key */) {
        Object.keys(item).forEach(k => {
          if (k === key) {
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            this.tableRef.toggleRowSelection(item as any, check)
            return false
          }
        })
      }
    }
  }

  /** 组件创建时：从缓存中获取数据 & 注入实例到管理器 */
  private mounted() {
    // 加载缓存配置
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    let cacheParam: any = session.getObj(this.key('p'))
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    let cachePageNum: any = session.getAny(this.key('n'))
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    let cachePageSize: any = session.getAny(this.key('s'))

    // 加载配置条件 & 参数过滤
    cacheParam = Object.assign(cacheParam || {}, this.param)
    cachePageSize = cachePageSize || this.pageSize || 10
    cachePageNum = cachePageNum || this.pageNo || 1

    // 默认第一次加载数据
    this.load(cachePageNum, cachePageSize, cacheParam)
    this.$emit('table-page-init', cacheParam)
  }

  /** 组件销毁时：将表单参数、分页信息存入缓存 */
  private destroyed() {
    session.save(this.key('p'), this.pageRequest.t)
    session.save(this.key('n'), this.pageRequest.pageNo)
    session.save(this.key('s'), this.pageResult.pageSize)
  }

  /** 缓存KEY */
  private key(k: string) {
    return `page-cache-${k}-${this.url}`
  }

  /** 每页条数切换 */
  private doChangePageSize(pageSize: number) {
    this.load(1, pageSize)
    this.$emit('table-page-size', pageSize)
  }

  /** 页码切换 */
  private doChangePageCurrent(pageNo: number) {
    this.load(pageNo)
    this.$emit('table-page-num', pageNo)
  }

  /** 切换当前选择行 */
  private doChangeTableCurrent(currentRow: RT, lastRow: RT) {
    this.$emit('table-row-current', (this.currentRow = currentRow), lastRow)
  }

  /** 改变被选中行 */
  private doChangeTableSelection(selectionRows: RT[]) {
    this.$emit('table-row-selection', (this.selectionRow = selectionRows))
  }

  /** 行被点击 */
  private doTableRowClick(row: RT, event: PointerEvent, column: ElTableColumn) {
    this.$emit('table-row-click', row, column, event)
  }

  /** 单元格被点击 */
  private doTableCellClick(row: RT, column: ElTableColumn, cell: cellCallbackParams, event: PointerEvent) {
    this.$emit('table-cell-click', row, column, cell, event)
  }

  /** 排序事件 */
  private doSortChange(column: { column: ElTableColumn; prop: SortOrder; order: string }) {
    this.pageRequest.sortName = column.prop
    this.pageRequest.sortDir = column.order === 'ascending' ? 'ASC' : 'DESC'
    this.reload()
    this.$emit('table-sort-change', column.column, column.prop, column.order)
  }
}
</script>

<style lang="less" scoped>
.el-pagination {
  margin-top: 15px;
  float: right;
}
/* .el-cascader-menu {
  height: 300px;
} */
// .el-cascader-menu__wrap,
// .el-scrollbar__wrap {
//   height: 162px !important;
// }
.el-table {
  margin-top: 15px;
}
</style>
