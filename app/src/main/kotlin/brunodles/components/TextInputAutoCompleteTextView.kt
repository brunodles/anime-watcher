package brunodles.components

import android.content.Context
import android.graphics.Rect
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection

// Reference found on: https://stackoverflow.com/questions/39431378/textinputlayout-and-autocompletetextview/41864063#41864063
// Based on code TextInputEditText
/**
 * An implementation of @class AppCompatAutoCompleteTextView
 * With override on @see #onCreateInputConnection
 * Based on official implementation of @class TextInputEditText
 */
class TextInputAutoCompleteTextView : AppCompatAutoCompleteTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /*
    Auto performFilter
    Thanks to CommonsWare on: https://stackoverflow.com/a/2126852/1622925
     */
    override fun enoughToFilter(): Boolean = true

    /*
    When receive focus performFiltering, so the dropdown pops up.
    Thanks to David Vávra on: https://stackoverflow.com/a/5783983/1622925
     */
    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && adapter != null)
            performFiltering(text, 0)
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        val ic = super.onCreateInputConnection(outAttrs)
        if (ic != null && outAttrs.hintText == null) {
            // If we don't have a hint and our parent is a TextInputLayout, use it's hint for the
            // EditorInfo. This allows us to display a hint in 'extract mode'.
            val parent = parent
            if (parent is TextInputLayout) {
                outAttrs.hintText = parent.hint
            }
        }
        return ic
    }
}
