import request from '@/fetch'
import { MenuResponse } from './interface/menu-response'

export const MenuApi = (): Promise<KWResponse.Result<Array<MenuResponse>>> => request.get('api-user/menus/current/menus', {})