package io.github.kurenairyu.kurenaibot

import java.util.concurrent.ConcurrentLinkedDeque

class ContextHolder {

    companion object {
        val QQ_MSG_QUEUE = ConcurrentLinkedDeque<String>()
    }

}