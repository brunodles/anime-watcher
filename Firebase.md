# Firebase Structure

```json
{
    "//" : "this is the environment name, all data related to this environement will be kept inside it.",
    "{{environment}}" : {
        "users" : {
            "{{user.id}}" : {
                "//" : "May change in the future to keep player position",
                "history" : {
                    "{{video.id}}" : "{{video.url}}"
                }
            }
        },
        "videos": {
            "{{video.url.toKey}}" : {
                "animeName" : "{{video.animeName}}",
                "description" : "{{video.description}}",
                "//" : "Video Thumbnail",
                "image" : "{{video.image}}",
                "link" : "{{video.link}}",
                "number" : {{video.number}},
                "//" : "the url of video to be used for players",
                "video" : "{{video.video}}",
                "nextEpisodes" : {
                    "//" : "May repeat or be missing",
                    "{{index}}" : {
                        "animeName" : "{{video.animeName}}",
                        "description" : "{{video.description}}",
                        "image" : "{{video.image}}",
                        "link" : "{{video.link}}",
                        "number" : {{video.number}},
                        "//" : "Video will probably be missing",
                        "video" : "{{video.video}}",
                    }
                }
            }
        }
    },
    "//" : "tracks all pages that works with the app, may be used to disable it.",
    "pages" : {
        "//" : "The name in key form (just a valid form for firebase)",
        "{{name.toKey}}" : {
            "enabled" : "{{true|false}}",
            "url" : "{{page.url}}"
        }
    },
    "//" : "servers to redirect, may be used to disable.",
    "servers" : {
        "{{page.url.toKey}}" : {
            "enabled" : "{{true|false}}",
            "url" : "{{page.url}}"
        }
    }
}
```