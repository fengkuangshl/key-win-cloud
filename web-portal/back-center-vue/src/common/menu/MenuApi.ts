import request from '../../fetch'
import { MenuResponse } from './interface/MenuResponse'

export const MenuApi = (): Promise<Ajax.AjaxResult<Array<MenuResponse>>> => request.get('api-user/menus/current/menus', {})
