package managers

import com.squareup.moshi.Moshi


object Json{
    val moshi = Moshi.Builder().build()

    inline fun <reified T>toJson(obj: T): String {
        val jsonAdapter = moshi.adapter<T>(T::class.java)
        return jsonAdapter.toJson(obj)
    }

    inline fun <reified T>fromJson(json: String): T? {
        val jsonAdapter = moshi.adapter<T>(T::class.java)
        return jsonAdapter.fromJson(json)
    }
}