package cn.yonghui.shop.framework.network

import com.google.gson.Gson
import fi.iki.elonen.NanoHTTPD

/**
 * Created by freedy on 31/08/2017.
 */
abstract class HttpServer(port: Int) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        var parMap: HashMap<String, String?>
        if (Method.POST.equals(session.method)) {
            var fieldMap = HashMap<String,String?>()
            session.parseBody(fieldMap)
            parMap = session.parms as HashMap<String, String?>
        } else {
            parMap = session.parms as HashMap<String, String?>
        }

        var response = handleRequest(parMap)
        return newFixedLengthResponse(response, NanoHTTPD.MIME_PLAINTEXT, response.description)
    }


    abstract fun handleRequest(map: HashMap<String, String?>): NanoHTTPD.Response.IStatus
}