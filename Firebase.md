# Firebase Structure

```json
{
    "{{environment}}" : { # this is the environment name, all data related to this environement will be kept inside it.
        "users" : {
            "{{user.id}}" : {
                "history" : { # May change in the future to keep player position
                    "{{video.id}}" : "{{video.url}}"
                }
            }
        },
        "videos": {
            "{{video.url.toKey}}" : {
                "animeName" : "{{video.animeName}}",
                "description" : "{{video.description}}", # Video Description
                "image" : "{{video.image}}", # Video Thumbnail
                "link" : "{{video.link}}",
                "number" : {{video.number}},
                "video" : "{{video.video}}", # the link to video url
                "nextEpisodes" : {
                    "{{index}}" : { # May repeat or be missing
                        "animeName" : "{{video.animeName}}",
                        "description" : "{{video.description}}",
                        "image" : "{{video.image}}",
                        "link" : "{{video.link}}",
                        "number" : {{video.number}},
                        "video" : "{{video.video}}", # probably missing
                    }
                }
            }
        }
    },
    "pages" : { # tracks all pages that works with the app, may be used to disable it.
        "{{name.toKey}}" : { # The name in key form (just a valid form for firebase)
            "enabled" : "{{true|false}}",
            "url" : "{{page.url}}"
        }
    },
    "servers" : { # servers to redirect, may be used to disable.
        "{{page.url.toKey}}" : {
            "enabled" : "{{true|false}}",
            "url" : "{{page.url}}"
        }
    }
}
```