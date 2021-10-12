package com.example.zebrascannerappdemo.presentation.ui.main.fragments.register.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zebrascannerappdemo.R
import com.example.zebrascannerappdemo.databinding.ItemProductBinding
import com.example.zebrascannerappdemo.domain.model.Product

class ProductAdapter(
    private val listener: ProductItemClickListener
) : ListAdapter<Product, ProductViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            holder.onBind(currentList[position])
        }
    }
}

class ProductViewHolder(private val binding: ItemProductBinding, listener: ProductItemClickListener) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var product: Product

    init {
        with(binding) {
            removeItemBtn.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                listener.onRemoveClicked(product)
            }

            removeAllBtn.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                listener.onRemoveAllClicked(product)
            }

            infoBtn.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                listener.onShowInfoClicked(product)
            }
        }
    }

    fun onBind(product: Product) {
        this.product = product
        with(binding) {
            productTitleTv.text =
                "${product.productName}\n${product.barcode}"
            countTv.text = "${product.quantity}×"
            removeAllBtn.text = removeAllBtn.context.getString(
                R.string.product_remove_all_label,
                product.quantity
            )
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.barcode == newItem.barcode
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}

interface ProductItemClickListener {
    fun onRemoveClicked(product: Product)
    fun onRemoveAllClicked(product: Product)
    fun onShowInfoClicked(product: Product)
}