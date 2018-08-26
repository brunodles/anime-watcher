# Anime Watcher - Server
An api to decode pages into a simple model.

## Usage
If you're planning to use this server you can follow these steps.

### 1 - Search for a anime on google
You can use this
[Sample Link](http://google.com/search?q=Dragon+Ball+Super+ep+01+site%3Aanimekaionline.com+OR+site%3Aanimeskai.com+OR+site%3Aanimakai.info+OR+site%3Aanimesonlinebr.com.br+OR+site%3Aanimesorion.site+OR+site%3Aanimesorion.tv+OR+site%3Aanimesorion.video+OR+site%3Aanimetubebrasil.com+OR+site%3Aanitubebr.com+OR+site%3Aanitube.site+OR+site%3Aonepiece-ex.com.br+OR+site%3Aone-piece-x.com.br+OR+site%3Atvcurse.com+OR+site%3Aanimacurse.moe+OR+site%3Aanimacurse.tv+OR+site%3Axvideos.com).
It have filters for pages that we can handle.

### 2 - Grab the link of one result
Select one result and copy its link, there is no need navigate to the page,
this is what we're trying to avoid.

Here is a sample:
```
https://www.animesorion.video/60417
```

### 3 - Send the link to the server
The link should be passed as `text/plain` on the request body.
You should use `POST`.
Be careful: do not send the url quoted, it will mess up the server.

You can run a sample with this command bellow:
```bash
curl https://anime-watcher-spark.herokuapp.com/decoder -d "https://www.animesorion.video/60417"
```

## Documentation
We use [Api Blueprint](https://apiblueprint.org/) for documentation.

Our documentation is [here](server_blueprint.md).

You can use one [mock server](https://apiblueprint.org/tools.html#mock%20servers)
to run the documentation as a server.

If you want to see documentation in a better way, use a
[renderer](https://apiblueprint.org/tools.html#renderers).
