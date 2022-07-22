package moe.kurenai.cq

import moe.kurenai.cq.event.Event
import org.apache.logging.log4j.LogManager
import java.util.concurrent.Flow
import java.util.concurrent.Flow.Subscriber

abstract class AbstractEventSubscriber : Subscriber<Event> {

    companion object {
        private val log = LogManager.getLogger()
    }

    protected lateinit var subscription: Flow.Subscription

    override fun onSubscribe(subscription: Flow.Subscription) {
        this.subscription = subscription
        this.subscription.request(1)
        onSubscribe0()
    }

    open fun onSubscribe0() {}

    override fun onNext(event: Event) {
        onNext0(event)
        subscription.request(1)
    }

    abstract fun onNext0(event: Event)

    override fun onError(e: Throwable) {}

    override fun onComplete() {}
}