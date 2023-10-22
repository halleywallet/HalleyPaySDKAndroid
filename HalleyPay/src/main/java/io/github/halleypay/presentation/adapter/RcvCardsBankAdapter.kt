package io.github.halleypay.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import io.github.halleywallet.R
import io.github.halleypay.domain.entity.CardModel

class RcvCardsBankAdapter(
    val context: Context
) : ListAdapter<CardModel, RcvCardsBankAdapter.CardListViewHolder>(
    object : DiffUtil.ItemCallback<CardModel>() {
        override fun areItemsTheSame(oldItem: CardModel, newItem: CardModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CardModel, newItem: CardModel): Boolean {
            return oldItem == newItem
        }
    }
) {
    var onCardClickListener: OnCardClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListViewHolder {
        return when (viewType) {
            VIEW_NORMAL -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_bank_card, null)
                CardListViewHolder(view)
            }

            else -> throw IllegalStateException("Ops")
        }
    }

    override fun onBindViewHolder(holder: CardListViewHolder, position: Int) {
        val params =
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        holder.clMain.layoutParams = params

        when (position) {
            0 -> {
                holder.imvCard.setImageResource(R.mipmap.card1)
            }

            1 -> {
                holder.imvCard.setImageResource(R.mipmap.card2)
            }

            2 -> {
                holder.imvCard.setImageResource(R.mipmap.card3)
            }
        }
        holder.cvCard.setOnClickListener {
            onCardClickListener?.onClick(getItem(position), position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_NORMAL
    }

    internal fun setOnItemClickListener(onCardClickListener: OnCardClickListener) {
        this.onCardClickListener = onCardClickListener
    }

    inner class CardListViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val imvCard: ImageView
        val cvCard: MaterialCardView
        val clMain: ConstraintLayout

        init {
            imvCard = view.findViewById(R.id.imvCard)
            cvCard = view.findViewById(R.id.cvCard)
            clMain = view.findViewById(R.id.clMain)
        }
    }

    interface OnCardClickListener {
        fun onClick(cardModel: CardModel, position: Int)
    }

    companion object {
        const val VIEW_NORMAL = 0
    }
}

