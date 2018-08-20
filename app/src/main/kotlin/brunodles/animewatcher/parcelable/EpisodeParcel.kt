package brunodles.animewatcher.parcelable

import android.os.Parcel
import android.os.Parcelable
import brunodles.animewatcher.explorer.Episode

data class EpisodeParcel(
        val description: String,
        val number: Int,
        val animeName: String? = null,
        val image: String? = null,
        val video: String? = null,
        val link: String,
        val nextEpisodes: List<EpisodeParcel> = arrayListOf()) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(CREATOR))

    constructor(episode: Episode) : this(
            episode.description,
            episode.number,
            episode.animeName,
            episode.image,
            episode.video,
            episode.link,
            episode.nextEpisodes.map { EpisodeParcel(it) }.toList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeInt(number)
        parcel.writeString(animeName)
        parcel.writeString(image)
        parcel.writeString(video)
        parcel.writeString(link)
        parcel.writeTypedList(nextEpisodes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EpisodeParcel> {
        override fun createFromParcel(parcel: Parcel): EpisodeParcel {
            return EpisodeParcel(parcel)
        }

        override fun newArray(size: Int): Array<EpisodeParcel?> {
            return arrayOfNulls(size)
        }
    }

    fun isVideoMissing(): Boolean {
        return video == null
    }
}
