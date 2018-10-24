package com.brunodles.animewatcher.cli

import brunodles.animewatcher.decoders.UrlChecker
import brunodles.urlfetcher.UrlFetcher

fun main(args: Array<String>) {
    val cacheDir = UrlFetcher.cacheDir + "/cli/cache"
    println("CacheDir $cacheDir")
    UrlFetcher.cacheDir = cacheDir
    UrlFetcher.useLog = true
    println("\n\n")
    val videoInfo = UrlChecker.videoInfo(args[0])
    println(videoInfo)
}