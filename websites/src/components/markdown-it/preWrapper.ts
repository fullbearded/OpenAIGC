// @ts-ignore
import type MarkdownIt from "markdown-it"

export function extractTitle(info: string) {
  // eslint-disable-next-line @typescript-eslint/no-use-before-define
  return info.match(/\[(.*)\]/)?.[1] || extractLang(info) || "txt"
}

export default function preWrapperPlugin(md: MarkdownIt) {
  const fence = md.renderer.rules.fence!
  md.renderer.rules.fence = (...args: [any, any]) => {
    const [tokens, idx] = args
    const token = tokens[idx]
    // remove title from info
    token.info = token.info.replace(/\[.*\]/, "")

    // eslint-disable-next-line @typescript-eslint/no-use-before-define
    const lang = extractLang(token.info)
    const rawCode = fence(...args)
    return rawCode.replace(
      "<pre>",
      `<pre style="position: relative"><span class="lang">${lang}</span><button title="Copy Code" class="copy"></button>`
    )
  }
}

const extractLang = (info: string) => {
  return info.trim().replace(/:(no-)?line-numbers({| |$).*/, "")
}
