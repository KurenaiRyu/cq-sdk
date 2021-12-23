package moe.kurenai.bot.kurenaibot.response

class Receive<T>(val data: T?, val retcode: Int?, val status: String?) {

    override fun toString(): String {
        return "MessageReceive(data=$data, retcode=$retcode, status='$status')"
    }

    fun isSuccess(): Boolean {
        return status.equals("OK", true)
    }
}

class Data(val messageId: Int) {
    override fun toString(): String {
        return "Data(messageId=$messageId)"
    }
}