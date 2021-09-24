package events

class UrlMappings {

    static mappings = {
        delete "/api/v1/$controller/$id(.$format)?"(action:"delete", namespace:'v1')
        get "/api/v1/$controller(.$format)?"(action:"index", namespace:'v1')
        get "/api/v1/$controller/$id(.$format)?"(action:"show", namespace:'v1')
        post "/api/v1/$controller(.$format)?"(action:"save", namespace:'v1')
        put "/api/v1/$controller/$id(.$format)?"(action:"update", namespace:'v1')
        patch "/api/v1/$controller/$id(.$format)?"(action:"patch", namespace:'v1')

        get "/api/v1/event/search"(controller:"event", action:"search", namespace:'v1')
        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
