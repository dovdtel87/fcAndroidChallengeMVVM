package challengemvvm.fundingcircle.com.fcchallengemvvm.feature.viewmodel

import android.support.annotation.MainThread
import android.util.Log
import challengemvvm.fundingcircle.com.fcchallengemvvm.model.Auction
import challengemvvm.fundingcircle.com.fcchallengemvvm.feature.datamodel.AuctionsListRepositoryInterface
import challengemvvm.fundingcircle.com.fcchallengemvvm.model.networkmodel.AuctionsEndPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AuctionsListViewModel(val mDataModel : AuctionsListRepositoryInterface) {


   /*fun retrieveAuctions() : Observable<List<Auction>> {
       return Observable.concat {
           mDataModel.retrieveAuctions().map {
               auctionsEndpoint -> return@map auctionsEndpoint.items
           }

        }
    }*/

    fun retrieveAuctions() : Observable<AuctionsEndPoint> {
        return mDataModel.retrieveAuctions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).onErrorReturn {
                    it ->
                    Log.d("Error", it.message)
                    var list : ArrayList<Auction> = ArrayList()
                    list.add(Auction("id","Title",4.0f,"A"))
                    AuctionsEndPoint(list) };//.map {
               // auctionsEndpoint -> return@map auctionsEndpoint.items
           // }

        }


   /* fun retrieveAuctions() : Observable<List<Auction>> {
        mDataModel.retrieveAuctions().map {
            it -> it.items
        }

        var observable : Observable<AuctionsEndPoint> = mDataModel.retrieveAuctions()
        observable.map { it ->
            it.items
            System.out.print("Showing items")
            System.out.print(it.items.get(0).title)
        }



        var list : ArrayList<Auction> = ArrayList()
        list.add(Auction("id","Title",4.0f,"A"))
        return Observable.just(list)
    }*/
}