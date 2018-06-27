package challengemvvm.fundingcircle.com.fcchallengemvvm.feature.datamodel

import challengemvvm.fundingcircle.com.fcchallengemvvm.model.Auction
import challengemvvm.fundingcircle.com.fcchallengemvvm.model.networkmodel.AuctionsEndPoint
import challengemvvm.fundingcircle.com.fcchallengemvvm.network.FundingCircleClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by david.martinezgarcia on 21/04/2018.
 */
class AuctionsListRepository : AuctionsListRepositoryInterface {

    override fun retrieveAuctions(): Observable<AuctionsEndPoint> {
        return FundingCircleClient.getInstance()
                .getAuctions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }





}