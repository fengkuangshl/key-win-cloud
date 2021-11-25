import { IMenuTree } from '../interface/menu-response'

export class MenuTree implements IMenuTree {
  id!: number
  name!: string
  children!: Array<IMenuTree>
}
