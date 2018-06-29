package challengemvvm.fundingcircle.com.fcchallengemvvm.feature.viewmodel

import challengemvvm.fundingcircle.com.fcchallengemvvm.feature.datamodel.AuctionsListRepositoryInterface
import challengemvvm.fundingcircle.com.fcchallengemvvm.model.networkmodel.AuctionsEndPoint
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class AuctionsListViewModel(val mDataModel : AuctionsListRepositoryInterface) {

    private val mLoadingIndicatorSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun retrieveAuctions() : Observable<AuctionsEndPoint> {
        return mDataModel.retrieveAuctions()
                .doOnSubscribe { _ -> mLoadingIndicatorSubject.onNext(true) }
                .doOnComplete { mLoadingIndicatorSubject.onNext(false) }
    }

    fun getLoadingIndicatorVisibility(): Observable<Boolean> {
        return mLoadingIndicatorSubject
    }
}