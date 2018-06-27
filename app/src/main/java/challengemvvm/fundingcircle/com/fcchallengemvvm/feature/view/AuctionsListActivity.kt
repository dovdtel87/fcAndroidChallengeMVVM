package challengemvvm.fundingcircle.com.fcchallengemvvm.feature.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import challengemvvm.fundingcircle.com.fcchallengemvvm.Application
import challengemvvm.fundingcircle.com.fcchallengemvvm.R
import challengemvvm.fundingcircle.com.fcchallengemvvm.feature.adapter.AuctionsListAdapter
import challengemvvm.fundingcircle.com.fcchallengemvvm.feature.datamodel.AuctionsListRepository
import challengemvvm.fundingcircle.com.fcchallengemvvm.feature.viewmodel.AuctionsListViewModel
import challengemvvm.fundingcircle.com.fcchallengemvvm.model.Auction
import challengemvvm.fundingcircle.com.fcchallengemvvm.service.GsonFactory
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_main.*


class AuctionsListActivity : AppCompatActivity(), AuctionsListAdapter.AuctionClickListener /*, AuctionsListContract.View, AuctionsListAdapter.AuctionClickListener*/ {

    companion object {
        const val STATE_AUCTIONS = "state.auctions.list"
    }

    //private lateinit var mPresenter: AuctionsListContract.Presenter
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAuctions: List<Auction>

    private lateinit var mCompositeDisposable: CompositeDisposable
    private lateinit var mViewModel: AuctionsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mRecyclerView = recycler_view
        mProgressBar = progress_bar


        mViewModel = getViewModel()
        //mPresenter = AuctionsListPresenter(this, AuctionsListRepository())

        if (savedInstanceState?.getString(STATE_AUCTIONS) != null) {
            mAuctions = GsonFactory.getInstance().fromJson(savedInstanceState.getString(STATE_AUCTIONS), object : TypeToken<ArrayList<Auction>>() {}.type)
        }

        if (this::mAuctions.isInitialized) {
            showAuctions(mAuctions)
        } else {
            //initNetworkCalls()
        }
    }

    override fun onResume() {
        super.onResume()
        bind()
    }

    override fun onPause() {
        super.onPause()
        unBind()
    }

    private fun bind() {
        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable.add(mViewModel.retrieveAuctions()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    listAuctions -> showAuctions(listAuctions.items)
                })
    }

    private fun unBind() {
        mCompositeDisposable.clear()
    }

    /*private fun initNetworkCalls() {
        mPresenter.retrieveAuctions()
    }*/

    //TODO manage states of showing progress bars
   /* override fun showProgress() {
        mProgressBar.visibility = VISIBLE
    }

    override fun hideProgress() {
        mProgressBar.visibility = GONE
    }*/



    fun showAuctions(auctions: List<Auction>) {
        mAuctions = auctions
        val adapter = AuctionsListAdapter(this, this)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = adapter
        adapter.setAuctions(auctions)
    }

    override fun onAuctionClicked(auction: Auction) {
        val dialogFragment = EraDialogFragment.newInstance(auction.title, auction.rate, auction.riskBand)
        dialogFragment.show(supportFragmentManager, "Dialog")
    }

    //TODO manage API Errors
    /*override fun onApiError(exception: Throwable) {
        hideProgress()
        val errorText = error_text
        errorText.text = getString(R.string.api_error)
        mRecyclerView.visibility = GONE
        errorText.visibility = VISIBLE
    }*/

    private fun getViewModel(): AuctionsListViewModel {
        return (application as Application).getViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (this::mAuctions.isInitialized) {
            outState.putString(STATE_AUCTIONS, GsonFactory.getInstance().toJson(mAuctions))
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        //mPresenter.onViewDestroyed()
        super.onDestroy()
    }
}
