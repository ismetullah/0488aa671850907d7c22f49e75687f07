package com.asmatullah.spaceapp.common.core.ui.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created on 4/23/18.
 *
 * @author Minor946 (minor946@yandex.com)
 */
abstract class EndlessScrollListener private constructor(private val linearLayoutManager: LinearLayoutManager?) :
    RecyclerView.OnScrollListener() {
    private val visibleThreshold = 3
    private var currentPage = 0
    private var previousTotalItemCount = 0
    private val startingPageIndex = 0
    var isLoading = true
    var isLastPage = false

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(
        view: RecyclerView,
        dx: Int,
        dy: Int
    ) {
        if (isLoading) {
            return
        }
        if (isLastPage) {
            isLoading = false
            return
        }
        var lastVisibleItemPosition = 0
        var totalItemCount = 0
        if (linearLayoutManager != null) {
            totalItemCount = linearLayoutManager.itemCount
            lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
        }
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                isLoading = true
            }
        }
        if (isLoading && totalItemCount > previousTotalItemCount) {
            isLoading = false
            previousTotalItemCount = totalItemCount
        }
        if (!isLoading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount)
            isLoading = true
        }
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    companion object {
        operator fun invoke(
            linearLayoutManager: LinearLayoutManager,
            loadMore: (page: Int) -> Unit
        ) = object : EndlessScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                loadMore.invoke(page)
            }
        }
    }
}