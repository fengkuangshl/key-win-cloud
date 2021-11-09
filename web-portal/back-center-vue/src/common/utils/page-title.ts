import { Settings } from '@/settings'

const title = Settings.title || 'key-win 后台管理'

export default function getPageTitle(pageTitle: string): string {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
