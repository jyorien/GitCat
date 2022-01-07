package com.example.gitcat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gitcat.databinding.RepositoryListItemBinding

class RepositoryAdapter(private val callback: (repository: Repository) -> Unit) : ListAdapter<Repository, RepositoryViewHolder>(RepositoryComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repository_list_item, parent, false)
        return RepositoryViewHolder(view, callback)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class RepositoryViewHolder(itemView: View, private val callback: (repository: Repository) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val binding: RepositoryListItemBinding = RepositoryListItemBinding.bind(itemView)

    fun bind(repository: Repository) {
        binding.repositoryName.text = repository.name
        binding.repositoryDescription.text = repository.description
        binding.addButton.setOnClickListener { callback(repository) }
    }
}

class RepositoryComparator : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.name == newItem.name && oldItem.description == newItem.description
    }

}
