package com.capsule.healthytimes.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


/**
 * [RecyclerView.ViewHolder] that holds a [ViewBinding]
 */
open class ViewBindingHolder<T : ViewBinding>(val binding: T) :
    RecyclerView.ViewHolder(binding.root)


/**
 * Simple [RecyclerView.Adapter] implementation for holding a single view. This can be used with
 * a ConcatAdapter to display e.g. a header or footer.
 * Usage:
 * val adapter = SingleItemAdapter<MyViewBinding, SomeValueType>(MyViewBinding::inflate) { binding, value ->
 *   binding.someView.text = value.displayValue
 * }
 * adapter.value = SomeValueType()
 */
open class SingleItemAdapter<T : ViewBinding, R>(
    private val createBinding: (LayoutInflater, ViewGroup, Boolean) -> T,
    private val viewType: Int = 0,
    val bind: (binding: T, value: R) -> Unit
) : RecyclerView.Adapter<ViewBindingHolder<T>>() {

    var value: R? = null
        set(value) {
            if (field != null && value == null) {
                notifyItemRemoved(0)
            } else if (field == null && value != null) {
                notifyItemInserted(0)
            } else if (field != value) {
                notifyItemChanged(0)
            }
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewBindingHolder(
            createBinding(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = if (value == null) 0 else 1
    override fun getItemViewType(position: Int) = viewType
    override fun onBindViewHolder(holder: ViewBindingHolder<T>, position: Int) =
        bind(holder.binding, value!!)
}