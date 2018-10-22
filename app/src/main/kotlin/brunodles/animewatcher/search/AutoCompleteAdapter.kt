package brunodles.animewatcher.search

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filterable

class AutoCompleteAdapter(context: Context) :
    ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line), Filterable {

}