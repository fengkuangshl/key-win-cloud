import translations, { TranslationType } from './translations'

export default function customTranslate(template: string, replacements: TranslationType): string {
  replacements = replacements || {}

  // Translate
  template = translations[template] || template

  // Replace
  return template.replace(/{([^}]+)}/g, function (_, key) {
    return replacements[key] || '{' + key + '}'
  })
}
