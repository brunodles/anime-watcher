package brunodles.xvideos

import com.google.gson.annotations.SerializedName


data class RelatedVideoDTO(@SerializedName("u") val videoLink: String,
                           @SerializedName("i") val image: String,
                           @SerializedName("tf") val fullTitle: String,
                           @SerializedName("t") val title: String,
                           @SerializedName("d") val duration: String)