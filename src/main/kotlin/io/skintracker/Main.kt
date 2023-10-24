package io.skintracker;

import io.activej.http.AsyncServlet
import io.activej.http.HttpRequest
import io.activej.http.HttpResponse
import io.activej.inject.annotation.Provides
import io.activej.launchers.http.HttpServerLauncher
import kotlinx.html.*
import kotlinx.html.stream.createHTML

const val appName = "Skintracker"

fun A.appLink(c: String, href: String, block: A.() -> Unit) {
    classes = setOf("hover:cursor-pointer", c)
    attributes["href"] = href
    block()
}

fun HTML.baseLayout(pageName: String, block: MAIN.() -> Unit) {
    head {
        title(String.format("%s | %s", appName, pageName))
        meta(name = "viewport", content = "width=device-width, initial-scale=1.0") {}
        meta {
            attributes["charset"] = "UTF-8"
        }
        script(type = ScriptType.textJavaScript, src = "https://unpkg.com/htmx.org@1.9.6") {
            attributes["integrity"] = "sha384-FhXw7b6AlE/jyjlZH5iHa/tTe9EpJ1Y55RjcgPbjeWMskSxZt1v9qkxLJWNJaGni"
            attributes["crossorigin"] = "anonymous"
        }
        script(type = ScriptType.textJavaScript, src = "https://cdn.tailwindcss.com") {}
    }
    body {
        nav(classes = "bg-slate-800 text-white py-4 px-8") {
            div(classes = "grid grid-cols-2") {
                a {
                    appLink("self-center", "/") {
                        span(classes = "text-xl font-bold") { +appName }
                    }
                }
                ul(classes = "justify-self-end self-center items-center inline-flex") {
                    li(classes = "mr-4") {
                        a(classes = "hover:cursor-pointer") {
                            attributes["hx-trigger"] = "click, keydown[metaKey && key=='k'] from:html"
                            attributes["hx-get"] = "/client/command/bar"
                            attributes["hx-target"] = "body"
                            attributes["hx-swap"] = "beforeend"
                            span(classes = "mr-2") {
                                +"Actions"
                            }
                            span(classes = "text-white/70") {
                                +"⌘K"
                            }
                        }
                    }
                    li {
                        button(classes = "flex border border-solid border-slate-200 rounded p-2") {
                            attributes["type"] = "button"
                            attributes["onclick"] = "location.href=\"/login\""
                            span(classes = "px-1") {
                                +"Login"
                            }
                            svg {
                                attributes["title"] = "Steam Logo"
                                attributes["fill"] = "#ffffff"
                                attributes["width"] = "24px"
                                attributes["height"] = "24px"
                                attributes["viewBox"] = "0 0 32 32"
                                attributes["onload"] =
                                    "this.innerHTML = '<path d=\"M 16 3 C 8.832 3 3 8.832 3 16 C 3 23.168 8.832 29 16 29 C 23.168 29 29 23.168 29 16 C 29 8.832 23.168 3 16 3 z M 16 5 C 22.065 5 27 9.935 27 16 C 27 22.065 22.065 27 16 27 C 10.891494 27 6.5985638 23.494211 5.3671875 18.765625 L 9.0332031 20.335938 C 9.2019466 21.832895 10.457908 23 12 23 C 13.657 23 15 21.657 15 20 C 15 19.968 14.991234 19.93725 14.990234 19.90625 L 19.167969 16.984375 C 21.297969 16.894375 23 15.152 23 13 C 23 10.791 21.209 9 19 9 C 16.848 9 15.106578 10.702031 15.017578 12.832031 L 12.09375 17.009766 C 12.06175 17.008766 12.032 17 12 17 C 11.336696 17 10.729087 17.22153 10.232422 17.585938 L 5.0332031 15.357422 C 5.3688686 9.5919516 10.151903 5 16 5 z M 19 10 C 20.657 10 22 11.343 22 13 C 22 14.657 20.657 16 19 16 C 17.343 16 16 14.657 16 13 C 16 11.343 17.343 10 19 10 z M 19 11 A 2 2 0 0 0 19 15 A 2 2 0 0 0 19 11 z M 12 18 C 13.105 18 14 18.895 14 20 C 14 21.105 13.105 22 12 22 C 11.191213 22 10.498775 21.518477 10.183594 20.828125 L 10.966797 21.164062 C 11.158797 21.247062 11.359641 21.287109 11.556641 21.287109 C 12.138641 21.287109 12.6935 20.945953 12.9375 20.376953 C 13.2635 19.615953 12.910438 18.734203 12.148438 18.408203 L 11.419922 18.095703 C 11.604729 18.039385 11.796712 18 12 18 z\" />'"
                            }
                        }
                    }
                }
            }
        }
        main(classes = "py-2 px-8") {
            block()
        }
    }
}

class Markdown {
    fun home(): String {
        val pageName = "Home"
        return createHTML().html {
            baseLayout(pageName) {
                div {
                    p { +pageName }
                }
            }
        }
    }
}

class Main : HttpServerLauncher() {
    @Provides
    fun index(): AsyncServlet {
        val text = Markdown().home()
        return AsyncServlet { _: HttpRequest -> HttpResponse.ok200().withHtml(text) }
    }
}

fun main(args: Array<String>) {
    val launcher = Main()
    launcher.launch(args)
}