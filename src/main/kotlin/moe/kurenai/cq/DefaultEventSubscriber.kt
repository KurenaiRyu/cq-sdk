package moe.kurenai.cq

import moe.kurenai.cq.event.Event
import moe.kurenai.cq.util.DefaultMapper.convertToString
import org.apache.logging.log4j.LogManager

open class DefaultEventSubscriber : AbstractEventSubscriber() {

    companion object {
        private val log = LogManager.getLogger()
    }

    override fun onSubscribe0() {
        log.info("New subscription to UpdateSubscriber ")
    }

    override fun onNext0(event: Event) {
        log.debug("Received ${event::class.simpleName} \n{}", convertToString(event))
    }

    override fun onError(e: Throwable) {
        log.error("Error on processing update ", e)
    }

    override fun onComplete() {
        log.info("Complete processing")
    }
}