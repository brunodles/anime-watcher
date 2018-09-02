package brunodles.animewatcher.nextepisodes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import brunodles.animewatcher.ImageLoader
import brunodles.animewatcher.R
import brunodles.animewatcher.databinding.ItemEpisodeBinding
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.history.OnItemClick

class EpisodeAdapter : RecyclerView.Adapter<EpisodeAdapter.EpisodeHolder>() {

    private val list = mutableListOf<Episode>()
    private var layoutInflater: LayoutInflater? = null
    private var onEpisodeClickListener: OnItemClick<Episode>? = null
    private var internalEpisodeClickListener: OnItemClick<Episode> = {
        onEpisodeClickListener?.invoke(it)
    }

    fun setEpisodeClickListener(listener: OnItemClick<Episode>?) {
        onEpisodeClickListener = listener
    }

    override fun getItemCount(): Int = list.count()

    override fun onBindViewHolder(holder: EpisodeHolder, position: Int) {
        holder.let {
            it.onBind(list[position])
            it.clickListener = internalEpisodeClickListener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeHolder {
        val context = parent.context
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(context)
        return EpisodeHolder(
            ItemEpisodeBinding.inflate(
                layoutInflater!!,
                parent,
                false
            )
        )
    }

    class EpisodeHolder(val binder: ItemEpisodeBinding) : RecyclerView.ViewHolder(binder.root) {

        var clickListener: OnItemClick<Episode>? = null

        fun onBind(item: Episode) {
            binder.root.setOnClickListener { clickListener?.invoke(item) }
            if (item.number > 0)
                binder.description.text = "${item.number} - ${item.description}"
            else
                binder.description.text = item.description
            binder.title.text = item.animeName
            binder.image.setImageResource(R.drawable.img_loading)
            ImageLoader.loadImageInto(item.image, binder.image)
        }
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun add(it: Episode) {
        val position = list.size
        if (list.add(it))
            notifyItemInserted(position)
    }
}
