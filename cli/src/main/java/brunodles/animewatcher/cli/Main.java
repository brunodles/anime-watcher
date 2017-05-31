package brunodles.animewatcher.cli;

import bruno.animewatcher.explorer.AnimeExplorer;
import bruno.animewatcher.explorer.AnimeFactory;
import bruno.animewatcher.explorer.EpisodeLink;
import brunodles.animacurse.AnimaCurseFactory;
import brunodles.animesproject.AnimesProjectFactory;
import brunodles.anitubex.AnitubexFactory;

public class Main {

    private static AnimeFactory[] factories = {
            AnitubexFactory.INSTANCE,
            AnimaCurseFactory.INSTANCE, AnimesProjectFactory.INSTANCE};

    public static void main(String[] args) {
        System.out.print("Find url in: ");
        System.out.println(args[0]);

        System.out.println("\n ");
        AnimeExplorer explorer = findVideoUrl(args[0]);
        System.out.println(explorer.currentEpisode().getVideo());

        System.out.println("\n next episodes: ");
        for (EpisodeLink episodeLink : explorer.nextEpisodes()) {
            System.out.print("* ");
            System.out.println(episodeLink.getDescription());

            System.out.print("   - ");
            System.out.println(episodeLink.getLink());
        }
    }

    private static AnimeExplorer findVideoUrl(final String url) {
        for (AnimeFactory factory : factories) {
            if (factory.isEpisode(url))
                return factory.episode(url);
        }
        return null;
    }
}
