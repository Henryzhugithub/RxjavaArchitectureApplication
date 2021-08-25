package com.zhl.module_book.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.zhl.lib_common.base.BaseListActivity
import com.zhl.lib_common.constant.ARouterConstant
import com.zhl.lib_common.model.BookModel.BookBean
import com.zhl.lib_common.service.BookService
import com.zhl.lib_core.databinding.ActivityCommonListBinding
import com.zhl.lib_core.service.ServiceGenerator
import com.zhl.lib_core.utils.ToastUtil
import com.zhl.module_book.R
import com.zhl.module_book.adapter.CommonListAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 *    author : zhuhl
 *    date   : 2021/6/24
 *    desc   :
 */

@Route(path = ARouterConstant.BOOK.BOOK_LIST)
class BookListActivity : BaseListActivity<ActivityCommonListBinding, BookBean>() {

    private val TAG = BookListActivity::class.java.simpleName

    override val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        binding.swipeRefreshLayout
    }

    override val recyclerView: RecyclerView
        get() = binding.rvCommonList

    override val adapter: CommonListAdapter by lazy {
        CommonListAdapter()
    }

    override val loadDataList: (page: Int) -> Unit = { page ->
        loadDataList(page)
    }


    override fun initOtherData() {
        adapter.setOnItemClickListener { adapter, view, position ->
            ToastUtil.show("点击了${position}项")
        }
        adapter.addChildClickViewIds(R.id.llDetail, R.id.rivBciCoverImg)
        adapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.llDetail) {
                ToastUtil.show("点击了查看详情")
                return@setOnItemChildClickListener
            }
            if (view.id == R.id.rivBciCoverImg) {
                ToastUtil.show("点击了头像")
                return@setOnItemChildClickListener
            }
        }
    }

    private fun loadDataList(page: Int) {
        var bookService = ServiceGenerator.createService(BookService::class.java)
        val bookListObservable = bookService.getBookListObservable(1, pageNum = page)
        bookListObservable.compose(ObservableTransformer {
            it
        })
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { t ->
                    handleData(page, t.data)
                }, { t1 ->
                    handleError()
                }, {

                }
            )
    }

    override fun enableRefresh(): Boolean {
        return true
    }

    override fun enableLoadMore(): Boolean {
        return false
    }


    override fun getLayoutViewBinding(): ActivityCommonListBinding {
        return ActivityCommonListBinding.inflate(layoutInflater)
    }


    override fun isShowToolBar(): Boolean {
        return true
    }

    override fun getToolBarTitle(): String {
        return "通用标题样式"
    }

}