package com.example.firstapp.featurechat

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstapp.databinding.*


enum class ChatAction(val original: Int) {
    RECEIVE(RECEIVE_TEXT), SEND(SEND_TEXT), RECEIVE_PHOTO(RECEIVE_PHOTOS), SEND_PHOTO(SEND_PHOTOS), SEND_MULTIPHOTO(
        SEND_MULTIPHOTOS
    )
}

class ChatAdapter(
    private var context: Context, var listMessageChat: ArrayList<Chat>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemChatReceiveVH(
        val bindingReceive: ItemChatReceiveBinding
    ) : RecyclerView.ViewHolder(bindingReceive.root)

    class ItemChatSendVH(
        val bindingSend: ItemChatSendBinding
    ) : RecyclerView.ViewHolder(bindingSend.root)

    class ItemChatOnepictureSendVH(
        val bindingSendPhoto: ItemChatOnepictureSendBinding
    ) : RecyclerView.ViewHolder(bindingSendPhoto.root)

    class ItemChatOnepictureReceiveVH(
        val bindingReceivePhoto: ItemChatOnepictureReceiveBinding
    ) : RecyclerView.ViewHolder(bindingReceivePhoto.root)

    class ItemChatMultiPictureVH(
        val bindingMultiPicture: ItemChatMultipictureSendBinding
    ) : RecyclerView.ViewHolder(bindingMultiPicture.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ChatAction.RECEIVE.original) return ItemChatReceiveVH(
            ItemChatReceiveBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
        if (viewType == ChatAction.SEND.original) return ItemChatSendVH(
            ItemChatSendBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
        if (viewType == ChatAction.RECEIVE_PHOTO.original)
            return ItemChatOnepictureReceiveVH(
                ItemChatOnepictureReceiveBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
        if (viewType == ChatAction.SEND_PHOTO.original)
            return ItemChatOnepictureSendVH(
                ItemChatOnepictureSendBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
        return ItemChatMultiPictureVH(
            ItemChatMultipictureSendBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ChatAction.RECEIVE.original -> {
                val userFeatureViewHolder = holder as ItemChatReceiveVH
                userFeatureViewHolder.bindingReceive.tvItemchatMess.text =
                    listMessageChat[position].message
            }
            ChatAction.SEND.original -> {
                val userFeatureViewHolder = holder as ItemChatSendVH
                userFeatureViewHolder.bindingSend.tvItemSendMess.text =
                    listMessageChat[position].message
            }
            ChatAction.RECEIVE_PHOTO.original -> {
            }
            ChatAction.SEND_PHOTO.original -> {
                val userFeatureViewHolder = holder as ItemChatOnepictureSendVH
                Glide.with(context)
                    .load(listMessageChat[position].uri)
                    .override(
                        (300 * holder.itemView.context.resources.displayMetrics.density).toInt(),
                        (300 * holder.itemView.context.resources.displayMetrics.density).toInt()
                    )
                    .into(userFeatureViewHolder.bindingSendPhoto.ivItemOnepicture)
            }
            ChatAction.SEND_MULTIPHOTO.original -> {
                val userFeatureViewHolder = holder as ItemChatMultiPictureVH
                val pictureAdapter =
                    PhotoAdapterv2(context, listMessageChat[position].picList ?: listOf())
                userFeatureViewHolder.bindingMultiPicture.recycler.apply {
                    adapter = pictureAdapter

                    if (listMessageChat[position].picList!!.size > 2) {
                        layoutManager = GridLayoutManager(context, 3)
                        val divider = GridSpacingItemDecoration(convertDpToPixel(context,3), 3)
                        userFeatureViewHolder.bindingMultiPicture.recycler.addItemDecoration(divider)
                    } else {
                        layoutManager = GridLayoutManager(context, 2)
                        val divider = GridSpacingItemDecoration(convertDpToPixel(context,3), 2)
                        userFeatureViewHolder.bindingMultiPicture.recycler.addItemDecoration(divider)
                    }

                }
                pictureAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return listMessageChat.size
    }

    override fun getItemViewType(position: Int): Int {
        if (listMessageChat[position].isSend == RECEIVE_TEXT) {
            return ChatAction.RECEIVE.original
        }
        if (listMessageChat[position].isSend == SEND_TEXT) {
            return ChatAction.SEND.original
        }
        if (listMessageChat[position].isSendPhoto == RECEIVE_PHOTOS) {
            return ChatAction.RECEIVE_PHOTO.original
        }
        if (listMessageChat[position].isSendPhoto == SEND_PHOTOS) {
            return ChatAction.SEND_PHOTO.original
        }
        return ChatAction.SEND_MULTIPHOTO.original
    }
}