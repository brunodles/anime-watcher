package brunodles.animewatcher.cli;

import brunodles.animewatcher.explorer.UrlFetcher;

public class Main {

    public static void main(String[] args) {
        String cacheDir = UrlFetcher.Companion.getCacheDir() + "/cli/cache";
        System.out.println("CacheDir "+cacheDir);
        UrlFetcher.Companion.setCacheDir(cacheDir);
        UrlFetcher.Companion.setUseLog(true);
        System.out.println("\n\n");
        UrlFetcher.Companion.fetchUrl("https://www.google.com.br/url?sa=t&rct=j&q=&esrc=s&source=web&cd=2&ved=0ahUKEwi217zc7LbYAhVDQZAKHV5qBzIQFggwMAE&url=http%3A%2F%2Fwww.animesorion.tv%2F74597&usg=AOvVaw1PqCiwa4IPitNaiRx7luBT");
    }
}
